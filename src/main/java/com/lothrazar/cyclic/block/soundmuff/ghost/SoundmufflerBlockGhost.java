package com.lothrazar.cyclic.block.soundmuff.ghost;

import com.lothrazar.cyclic.block.soundmuff.SoundmufflerBlock;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class SoundmufflerBlockGhost extends SoundmufflerBlock {

  public SoundmufflerBlockGhost(Properties properties) {
    super(properties.notSolid());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ClientRegistry.bindTileEntityRenderer(TileRegistry.soundproofing_ghost, SoundmuffRender::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new SoundmuffTile();
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    ItemStack stack = player.getHeldItem(handIn);
    SoundmuffTile ent = (SoundmuffTile) world.getTileEntity(pos);
    if (stack.isEmpty() && handIn == Hand.MAIN_HAND) {
      //try to pull
      ItemStack extracted = ent.notInventory.extractItem(0, 64, false);
      if (!extracted.isEmpty()) {
        //drop it
        player.dropItem(extracted, true);
      }
    }
    if (Block.getBlockFromItem(stack.getItem()) == null) {
      return super.onBlockActivated(state, world, pos, player, handIn, hit);
    }
    //replace it
    if (!ent.notInventory.getStackInSlot(0).isEmpty()) {
      //noempty so drop it first
      ItemStack pulled = ent.notInventory.extractItem(0, 64, false);
      player.dropItem(pulled, true);
    }
    //is it empty so now just replace every time
    ItemStack copy = new ItemStack(stack.getItem(), 1);
    ItemStack insertRemainder = ent.notInventory.insertItem(0, copy, false);
    //success means no remainder, it took 1
    if (insertRemainder.isEmpty()) {
      stack.shrink(1); //eat it!!!
      return ActionResultType.SUCCESS;
    }
    //else do we nuke it? 
    //   
    return super.onBlockActivated(state, world, pos, player, handIn, hit);
  }
}
