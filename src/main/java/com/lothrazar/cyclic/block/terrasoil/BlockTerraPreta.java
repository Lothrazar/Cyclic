package com.lothrazar.cyclic.block.terrasoil;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockTerraPreta extends BlockBase {

  public BlockTerraPreta(Properties properties) {
    //https://en.wikipedia.org/wiki/Terra_preta
    super(properties.strength(1.8F));
  }


  @Override
  public BlockEntity newBlockEntity(BlockPos pos,BlockState state ) {
    return new TileTerraPreta(pos,state);
  }
}
