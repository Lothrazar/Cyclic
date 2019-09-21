package com.lothrazar.cyclic.item;

import java.util.List;
import com.google.common.collect.Sets;
import com.lothrazar.cyclic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.AxeItem;
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
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

public class MattockItem extends ToolItem {

  public MattockItem(Properties builder) {
    super(5.0F, -3.0F, ItemTier.DIAMOND, Sets.newHashSet(), builder);
    //    super.onBlockStartBreak(itemstack, pos, player)
    AxeItem x;
  }

  final static int RADIUS = 1;//radius 2 is 5x5 area square

  @Override
  public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
    World world = player.world;
    RayTraceResult ray = rayTrace(world, (PlayerEntity) player, RayTraceContext.FluidMode.NONE);
    if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK) {
      BlockRayTraceResult brt = (BlockRayTraceResult) ray;
      Direction sideHit = brt.getFace();
      List<BlockPos> shape;
      if (sideHit == Direction.UP || sideHit == Direction.DOWN) {
        shape = UtilShape.squareHorizontalHollow(pos, RADIUS);
      }
      else if (sideHit == Direction.EAST || sideHit == Direction.WEST) {
        shape = UtilShape.squareVerticalZ(pos, RADIUS);
      }
      else {//has to be NORTHSOUTH
        shape = UtilShape.squareVerticalX(pos, RADIUS);
      }
      for (BlockPos posCurrent : shape) {
        //
        BlockState bsCurrent = world.getBlockState(posCurrent);
        System.out.println(bsCurrent.getBlock() + " " + this.getDestroySpeed(stack, bsCurrent)
            + " " + ForgeHooks.canHarvestBlock(bsCurrent, player, world, posCurrent)
            + " BUT IS IT " + this.efficiency);
        if (player.canPlayerEdit(posCurrent, sideHit, stack)
            && ForgeHooks.canHarvestBlock(bsCurrent, player, world, posCurrent)) {
          // 
          stack.onBlockDestroyed(world, bsCurrent, posCurrent, player);
          Block blockCurrent = bsCurrent.getBlock();
          if (world.isRemote) {//C
            world.playEvent(2001, posCurrent, Block.getStateId(bsCurrent));
            if (blockCurrent.removedByPlayer(bsCurrent, world, posCurrent, player, true, bsCurrent.getFluidState())) {
              blockCurrent.onPlayerDestroy(world, posCurrent, bsCurrent);
            }
            //            stack.onBlockDestroyed(world, bsCurrent, posCurrent, player);//update tool damage
            if (stack.getCount() == 0 && stack == player.getHeldItemMainhand()) {
              //              ForgeEventFactory.onPlayerDestroyItem(player, stack, EnumHand.MAIN_HAND);
              //              player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
            }
            //            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posCurrent, Minecraft.getMinecraft().objectMouseOver.sideHit));
          }
          else if (player instanceof ServerPlayerEntity) {//Server side, so this works
            ServerPlayerEntity mp = (ServerPlayerEntity) player;
            int xpGivenOnDrop = ForgeHooks.onBlockBreakEvent(world, ((ServerPlayerEntity) player).interactionManager.getGameType(), (ServerPlayerEntity) player, posCurrent);
            if (xpGivenOnDrop >= 0) {
              if (blockCurrent.removedByPlayer(bsCurrent, world, posCurrent, player, true, bsCurrent.getFluidState())) {
                TileEntity tile = world.getTileEntity(posCurrent);
                blockCurrent.onPlayerDestroy(world, posCurrent, bsCurrent);
                blockCurrent.harvestBlock(world, player, posCurrent, bsCurrent, tile, stack);
                blockCurrent.dropXpOnBlockBreak(world, posCurrent, xpGivenOnDrop);
              }
              mp.connection.sendPacket(new SChangeBlockPacket(world, posCurrent));
            }
          }
        }
      }
    }
    return super.onBlockStartBreak(stack, pos, player);
  }

//  @Override
//  public boolean canHarvestBlock(BlockState state) {
//    
//    super.canHarvestBlock(blockIn)
//  
//    Block block = state.getBlock();//    super.canHarvestBlock(blockIn)
//    return block == Blocks.OBSIDIAN ? 
//        this.toolMaterial.getHarvestLevel() == 3 : 
//          (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE ? (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK ? (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE ? (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE ? (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE ? (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE ? (state.getMaterial() == Material.ROCK ? true : (state.getMaterial() == Material.IRON ? true : state.getMaterial() == Material.ANVIL)) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 1) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2) : this.toolMaterial.getHarvestLevel() >= 2);
//  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    
return     Math.max(Items.DIAMOND_PICKAXE.getDestroySpeed(stack, state), 

         Items.DIAMOND_SHOVEL.getDestroySpeed(stack, state)
        
        );
//    return state.getMaterial() != Material.IRON && state.getMaterial() != Material.ANVIL
//        && state.getMaterial() != Material.ROCK ? super.getDestroySpeed(stack, state) : this.efficiency;
  }
  //  @Override
  //  public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity player) {
  //    boolean worked = super.onBlockDestroyed(stack, worldIn, state, pos, player);
  //    if (worked && player instanceof PlayerEntity) {
  //      // 
  //      RayTraceResult ray = rayTrace(worldIn, (PlayerEntity) player, RayTraceContext.FluidMode.NONE);
  //      if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK) {
  //        //
  //      }
  //    }
  //    return worked;
  //  }
}
