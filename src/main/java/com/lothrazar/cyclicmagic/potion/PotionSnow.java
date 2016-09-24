package com.lothrazar.cyclicmagic.potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PotionSnow extends PotionBase {
  public PotionSnow(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @Override
  public void tick(EntityLivingBase entity) {
    World world = entity.getEntityWorld();
    BlockPos here = entity.getPosition();
    BlockPos below = here.down();
    if (world.isAirBlock(here) && world.isSideSolid(below, EnumFacing.UP)) {
      world.setBlockState(here, Blocks.SNOW_LAYER.getDefaultState());
    }
  }
}
