package com.lothrazar.cyclic.block.fishing;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
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

public class BlockFisher extends BlockBase {

  public BlockFisher(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).notSolid());
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    ClientRegistry.bindTileEntityRenderer(BlockRegistry.TileRegistry.fisher, RenderFisher::new);
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
    TileFisher tile = (TileFisher) world.getTileEntity(pos);
    tile.inventory.ifPresent(handler -> {
      //
      if (hand == Hand.MAIN_HAND) {//!world.isRemote &&
        ItemStack stack = player.getHeldItem(hand);
        if (stack.isEmpty()) {
          //withdraw
          ItemStack takeout = handler.extractItem(0, 64, false);
          player.dropItem(takeout, true);
        }
        else if (stack.getItem().isIn(TileFisher.RODS)) {
          ItemStack maybeInside = handler.extractItem(0, 64, true);
          if (maybeInside.isEmpty()) {
            handler.insertItem(0, stack, false);
            player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
          }
          //deposit
        }
      }
    });
    return ActionResultType.PASS;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileFisher();
  }
}
