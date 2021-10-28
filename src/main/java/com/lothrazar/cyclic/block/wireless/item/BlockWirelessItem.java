package com.lothrazar.cyclic.block.wireless.item;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockWirelessItem extends BlockBase {

  private static final double BOUNDS = 4;
  public static final VoxelShape AABB = Block.box(BOUNDS, BOUNDS, BOUNDS, 16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);

  public BlockWirelessItem(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
    this.setHasGui();
    this.registerDefaultState(defaultBlockState().setValue(LIT, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LIT);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.wireless_item, ScreenWirelessItem::new);
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
    //    ClientRegistry.bindTileEntityRenderer(TileRegistry.wireless_transmitter, RenderTransmit::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileWirelessItem(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.WIRELESS_ITEM.get(), world.isClientSide ? TileWirelessItem::clientTick : TileWirelessItem::serverTick);
  }

  @Override
  public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileWirelessItem tileentity = (TileWirelessItem) worldIn.getBlockEntity(pos);
      if (tileentity != null) {
        for (int i = 0; i < tileentity.gpsSlots.getSlots(); ++i) {
          Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.gpsSlots.getStackInSlot(i));
        }
      }
      super.onRemove(state, worldIn, pos, newState, isMoving);
    }
  }
  //  @Override
  //  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
  //    if (state.getBlock() != newState.getBlock()) {
  //      TileWirelessItem tileentity = (TileWirelessItem) worldIn.getTileEntity(pos);
  //      if (tileentity != null) {
  //        for (int i = 0; i < tileentity.gpsSlots.getSlots(); ++i) {
  //          InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), tileentity.gpsSlots.getStackInSlot(i));
  //        }
  //      }
  //      super.onReplaced(state, worldIn, pos, newState, isMoving);
  //    }
  //  }
}
