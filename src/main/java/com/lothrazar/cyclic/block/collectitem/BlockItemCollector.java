package com.lothrazar.cyclic.block.collectitem;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilBlockstates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockItemCollector extends BlockBase {

  public BlockItemCollector(Properties properties) {
    super(properties.hardnessAndResistance(1.8F).sound(SoundType.STONE));
    this.setHasGui();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(HorizontalBlock.HORIZONTAL_FACING, UtilBlockstates.getFacingFromEntityHorizontal(pos, entity)), 2);
    }
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(HorizontalBlock.HORIZONTAL_FACING).add(LIT);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileItemCollector();
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ClientRegistry.bindTileEntityRenderer(TileRegistry.collector_item, RenderItemCollect::new);
    ScreenManager.registerFactory(ContainerScreenRegistry.collector, ScreenItemCollector::new);
  }
}
