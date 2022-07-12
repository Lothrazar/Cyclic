package com.lothrazar.cyclic.block.soundplay;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlockSoundPlayer extends BlockCyclic {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public BlockSoundPlayer(Properties properties) {
    super(properties.strength(1F).sound(SoundType.SCAFFOLDING));
    this.setHasGui();
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }

  @Override
  public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    boolean currentPower = worldIn.hasNeighborSignal(pos);
    if (currentPower != state.getValue(POWERED)) {
      if (currentPower) {
        ((TileSoundPlayer) worldIn.getBlockEntity(pos)).tryPlaySound();
      }
      worldIn.setBlock(pos, state.setValue(POWERED, currentPower), 3);
    }
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.SOUND_PLAYER.get(), ScreenSoundPlayer::new);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileSoundPlayer(pos, state);
  }
  //
  //  @Override
  //  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
  //    return createTickerHelper(type, TileRegistry.SOUND_PLAYER, world.isClientSide ? TileSoundPlayer::clientTick : TileSoundPlayer::serverTick);
  //  }
}
