package com.lothrazar.cyclic.block.soundplay;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockSoundPlayer extends BlockBase {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public BlockSoundPlayer(Properties properties) {
    super(properties.hardnessAndResistance(1F).sound(SoundType.SCAFFOLDING));
    this.setHasGui();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    boolean flag = worldIn.isBlockPowered(pos);
    if (flag != state.get(POWERED)) {
      if (flag) {
        this.tryPlaySound(worldIn, pos);
      }
      worldIn.setBlockState(pos, state.with(POWERED, Boolean.valueOf(flag)), 3);
    }
  }

  private void tryPlaySound(World worldIn, BlockPos pos) {
    ModCyclic.LOGGER.error("TRY PLAY SOUND" + worldIn.getTileEntity(pos));
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.SOUND_PLAYER, ScreenSoundPlayer::new);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileSoundPlayer();
  }
}
