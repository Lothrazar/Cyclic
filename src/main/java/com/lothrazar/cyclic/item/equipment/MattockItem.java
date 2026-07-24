package com.lothrazar.cyclic.item.equipment;

import java.util.List;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

public class MattockItem extends Item {

  final int radius; //radius 2 is 5x5 area square
  private final ToolMaterial material;

  public MattockItem(ToolMaterial tr, int durability, Properties builder, int radius) {
    // NOTE: .tool(...) sets its own durability from the material, so the caller's
    // intended durability must be (re-)applied after it, not baked into builder beforehand.
    super(builder.tool(tr, DataTags.WITH_MATTOCK, 1.0F, -2.8F, 0.0F).durability(durability));
    this.material = tr;
    this.radius = radius;
  }

  private List<BlockPos> getShape(BlockPos pos, int yoff, Direction sideHit) {
    List<BlockPos> shape;
    if (sideHit == Direction.UP || sideHit == Direction.DOWN) {
      shape = ShapeUtil.squareHorizontalHollow(pos, radius);
      if (radius == 2) {
        shape.addAll(ShapeUtil.squareHorizontalHollow(pos, radius - 1));
      }
    }
    else if (sideHit == Direction.EAST || sideHit == Direction.WEST) {
      int y = 1 + radius - yoff;
      int z = radius;
      shape = ShapeUtil.squareVerticalZ(pos, y, z);
    }
    else { //has to be NORTHSOUTH
      int x = radius;
      int y = 1 + radius - yoff;
      shape = ShapeUtil.squareVerticalX(pos, x, y);
    }
    return shape;
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    if (this.material == ToolMaterial.STONE) {
      return Math.max(Items.STONE_PICKAXE.getDestroySpeed(stack, state), Items.STONE_SHOVEL.getDestroySpeed(stack, state));
    }
    return Math.max(Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state), Items.DIAMOND_SHOVEL.getDestroySpeed(stack, state));
  }

  /**
   * 1.21 port: NeoForge removed Item#onBlockStartBreak. We replicate the AOE
   * by hooking BlockEvent.BreakEvent and triggering when the player's held
   * mattock breaks a block. Registered via EventRegistry.
   */
  public static class BreakHandler {

    // Re-entry guard: destroying a block here can fire another BreakEvent for
    // the same player while we are still iterating.
    private static final ThreadLocal<Boolean> BUSY = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreakEvent(BreakBlockEvent event) {
      if (BUSY.get()) {
        return;
      }
      Player player = event.getPlayer();
      if (player == null) {
        return;
      }
      LevelAccessor world = event.getLevel();
      if (world.isClientSide()) {
        return;
      }
      ItemStack stack = player.getMainHandItem();
      if (!(stack.getItem() instanceof MattockItem mattock)) {
        return;
      }
      Level level = (Level) world;
      HitResult ray = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
      if (ray == null || ray.getType() != HitResult.Type.BLOCK) {
        return;
      }
      Direction sideHit = ((BlockHitResult) ray).getDirection();
      int yoff = 0;
      if (mattock.radius == 2 && player.isCrouching()) {
        yoff = 1;
      }
      BlockPos origin = event.getPos();
      List<BlockPos> shape = mattock.getShape(origin, yoff, sideHit);
      BUSY.set(Boolean.TRUE);
      try {
        for (BlockPos posCurrent : shape) {
          if (posCurrent.equals(origin)) {
            continue; // vanilla handles the original block
          }
          BlockState bsCurrent = level.getBlockState(posCurrent);
          if (bsCurrent.isAir()) {
            continue;
          }
          if (bsCurrent.getDestroySpeed(level, posCurrent) < 0) {
            continue; // unbreakable
          }
          if (!player.mayUseItemAt(posCurrent, sideHit, stack)) {
            continue;
          }
          if (mattock.getDestroySpeed(stack, bsCurrent) <= 1) {
            continue;
          }
          if (!player.hasCorrectToolForDrops(bsCurrent)) {
            continue;
          }
          if (level instanceof ServerLevel sl) {
            Block.dropResources(bsCurrent, level, posCurrent, level.getBlockEntity(posCurrent), player, stack);
            int exp = bsCurrent.getExpDrop(level, posCurrent, level.getBlockEntity(posCurrent), player, stack);
            if (exp > 0) {
              bsCurrent.getBlock().popExperience(sl, posCurrent, exp);
            }
          }
          level.destroyBlock(posCurrent, false);
          stack.mineBlock(level, bsCurrent, posCurrent, player);
          if (stack.isEmpty()) {
            break;
          }
        }
      }
      finally {
        BUSY.set(Boolean.FALSE);
      }
    }
  }
}
