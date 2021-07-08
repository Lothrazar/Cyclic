package com.lothrazar.cyclic.item.equipment;

import com.google.common.collect.Sets;
import com.lothrazar.cyclic.util.UtilShape;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.ToolItem;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;

public class MattockItem extends ToolItem {

  final int radius; //radius 2 is 5x5 area square

  public MattockItem(Properties builder, int radius) {
    super(5.0F, -3.0F, ItemTier.DIAMOND, Sets.newHashSet(), builder.addToolType(ToolType.SHOVEL, 4).addToolType(ToolType.PICKAXE, 4));
    this.radius = radius;
  }

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
    World world = player.world;
    RayTraceResult ray = rayTrace(world, player, RayTraceContext.FluidMode.NONE);
    if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK) {
      BlockRayTraceResult brt = (BlockRayTraceResult) ray;
      Direction sideHit = brt.getFace();
      List<BlockPos> shape;
      if (sideHit == Direction.UP || sideHit == Direction.DOWN) {
        shape = UtilShape.squareHorizontalHollow(pos, radius);
        if (radius == 2) {
          shape.addAll(UtilShape.squareHorizontalHollow(pos, radius - 1));
        }
      }
      else if (sideHit == Direction.EAST || sideHit == Direction.WEST) {
        shape = UtilShape.squareVerticalZ(pos, radius, radius);
      }
      else { //has to be NORTHSOUTH
        shape = UtilShape.squareVerticalX(pos, radius, radius);
      }
      for (BlockPos posCurrent : shape) {
        BlockState bsCurrent = world.getBlockState(posCurrent);
        if (bsCurrent.hardness >= 0 // -1 is unbreakable
            && player.canPlayerEdit(posCurrent, sideHit, stack)
            && ForgeHooks.canHarvestBlock(bsCurrent, player, world, posCurrent)
            && this.getDestroySpeed(stack, bsCurrent) > 1) {
          stack.onBlockDestroyed(world, bsCurrent, posCurrent, player);
          Block blockCurrent = bsCurrent.getBlock();
          if (world.isRemote) {
            world.playEvent(2001, posCurrent, Block.getStateId(bsCurrent));
            if (blockCurrent.removedByPlayer(bsCurrent, world, posCurrent, player, true, bsCurrent.getFluidState())) {
              blockCurrent.onPlayerDestroy(world, posCurrent, bsCurrent);
            }
            //            stack.onBlockDestroyed(world, bsCurrent, posCurrent, player);//update tool damage
          }
          else if (player instanceof ServerPlayerEntity) { //Server side, so this works
            ServerPlayerEntity mp = (ServerPlayerEntity) player;
            int xpGivenOnDrop = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, posCurrent);
            if (xpGivenOnDrop >= 0) {
              if (blockCurrent.removedByPlayer(bsCurrent, world, posCurrent, player, true, bsCurrent.getFluidState())
                  && world instanceof ServerWorld) {
                TileEntity tile = world.getTileEntity(posCurrent);
                blockCurrent.onPlayerDestroy(world, posCurrent, bsCurrent);
                blockCurrent.harvestBlock(world, player, posCurrent, bsCurrent, tile, stack);
                blockCurrent.dropXpOnBlockBreak((ServerWorld) world, posCurrent, xpGivenOnDrop);
              }
              mp.connection.sendPacket(new SChangeBlockPacket(world, posCurrent));
            }
          }
        }
      }
    }
    return super.onBlockStartBreak(stack, pos, player);
  }

  @Override
  public int getHarvestLevel(ItemStack stack, net.minecraftforge.common.ToolType tool, PlayerEntity player, BlockState blockState) {
    return Math.max(Items.DIAMOND_PICKAXE.getHarvestLevel(stack, tool, player, blockState),
        Items.DIAMOND_SHOVEL.getHarvestLevel(stack, tool, player, blockState));
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    return Math.max(Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state),
        Items.DIAMOND_SHOVEL.getDestroySpeed(stack, state));
  }
}
