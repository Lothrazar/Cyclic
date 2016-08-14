package com.lothrazar.cyclicmagic.potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionSnow extends PotionCustom {
  public PotionSnow(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null) { return; }
    if (entity.isPotionActive(this)) {
      World world = entity.getEntityWorld();
      BlockPos here = entity.getPosition();
      BlockPos below = here.down();
      if (world.isAirBlock(here) && world.isSideSolid(below, EnumFacing.UP)) {
        world.setBlockState(here, Blocks.SNOW_LAYER.getDefaultState());
      }
    }
  }
}
