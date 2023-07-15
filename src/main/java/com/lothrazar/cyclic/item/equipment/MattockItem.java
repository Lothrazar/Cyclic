package com.lothrazar.cyclic.item.equipment;

import java.util.List;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class MattockItem extends DiggerItem {

  final int radius; //radius 2 is 5x5 area square

  public MattockItem(Tiers tr, Properties builder, int radius) {
    super(5.0F, -3.0F, tr, DataTags.WITH_MATTOCK, builder);
    this.radius = radius;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
    Level level = player.level();
    //    this.getTier()
    HitResult ray = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
    int yoff = 0;
    if (radius == 2 && player.isCrouching()) {
      yoff = 1;
    }
    if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
      BlockHitResult brt = (BlockHitResult) ray;
      Direction sideHit = brt.getDirection();
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
      for (BlockPos posCurrent : shape) {
        BlockState bsCurrent = level.getBlockState(posCurrent);
        if (bsCurrent.isAir()) {
          continue;
        }
        if (bsCurrent.destroySpeed >= 0 // -1 is unbreakable
            && player.mayUseItemAt(posCurrent, sideHit, stack)
            && ForgeEventFactory.doPlayerHarvestCheck(player, bsCurrent, true)
            && this.getDestroySpeed(stack, bsCurrent) > 1
            && (bsCurrent.canHarvestBlock(level, pos, player)
                || bsCurrent.is(this.getTier().getTag())
            //this.getTier().getTag().contains(bsCurrent.getBlock())
            )
        //end of OR
        ) {
          stack.mineBlock(level, bsCurrent, posCurrent, player);
          Block blockCurrent = bsCurrent.getBlock();
          if (level.isClientSide) {
            level.levelEvent(2001, posCurrent, Block.getId(bsCurrent));
            //removedByPlayer
            if (blockCurrent.onDestroyedByPlayer(bsCurrent, level, posCurrent, player, true, bsCurrent.getFluidState())) {
              blockCurrent.destroy(level, posCurrent, bsCurrent);
            }
            //            stack.onBlockDestroyed(world, bsCurrent, posCurrent, player);//update tool damage
          }
          else if (player instanceof ServerPlayer) { //Server side, so this works
            ServerPlayer mp = (ServerPlayer) player;
            int xpGivenOnDrop = ForgeHooks.onBlockBreakEvent(level, ((ServerPlayer) player).gameMode.getGameModeForPlayer(), (ServerPlayer) player, posCurrent);
            if (xpGivenOnDrop >= 0) {
              if (blockCurrent.onDestroyedByPlayer(bsCurrent, level, posCurrent, player, true, bsCurrent.getFluidState())
                  && level instanceof ServerLevel) {
                BlockEntity tile = level.getBlockEntity(posCurrent);
                blockCurrent.destroy(level, posCurrent, bsCurrent);
                blockCurrent.playerDestroy(level, player, posCurrent, bsCurrent, tile, stack);
                blockCurrent.popExperience((ServerLevel) level, posCurrent, xpGivenOnDrop);
              }
              mp.connection.send(new ClientboundBlockUpdatePacket(level, posCurrent));
            }
          }
        }
      }
    }
    return super.onBlockStartBreak(stack, pos, player);
  }
  //  @Override
  //  public int getHarvestLevel(ItemStack stack, net.minecraftforge.common.ToolType tool, Player player, BlockState blockState) {
  //    return Math.max(Items.DIAMOND_PICKAXE.getHarvestLevel(stack, tool, player, blockState),
  //        Items.DIAMOND_SHOVEL.getHarvestLevel(stack, tool, player, blockState));
  //  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    if (this.getTier() == Tiers.STONE) {
      return Math.max(Items.STONE_PICKAXE.getDestroySpeed(stack, state), Items.STONE_SHOVEL.getDestroySpeed(stack, state));
    }
    return Math.max(Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state), Items.DIAMOND_SHOVEL.getDestroySpeed(stack, state));
  }
}
