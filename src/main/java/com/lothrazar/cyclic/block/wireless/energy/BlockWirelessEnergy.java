package com.lothrazar.cyclic.block.wireless.energy;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.FluidState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockWirelessEnergy extends BlockBase {

  public BlockWirelessEnergy(Properties properties) {
    super(properties.hardnessAndResistance(1.2F).notSolid());
    this.setHasGui();
  }
  //TODO: lit property based on if its in use or not-anim

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return BlockTrash.AABB;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.wireless_energy, ScreenWirelessEnergy::new);
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
    //    ClientRegistry.bindTileEntityRenderer(TileRegistry.wireless_transmitter, RenderTransmit::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileWirelessEnergy();
  }
}
