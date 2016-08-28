package com.lothrazar.cyclicmagic.potion;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionWaterwalk extends PotionBase {
  public PotionWaterwalk(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    EntityLivingBase entity = event.getEntityLiving();
    if (entity == null) { return; }
    if (entity.isPotionActive(this)) {
      this.tick(entity);
    }
  }
  public void tick(EntityLivingBase entityLiving) {
    tickLiquidWalk(entityLiving, Blocks.WATER);
  }
  private void tickLiquidWalk(EntityLivingBase entityLiving, Block liquid) {
    World world = entityLiving.worldObj;
    if (world.getBlockState(entityLiving.getPosition().down()).getBlock() == liquid && world.isAirBlock(entityLiving.getPosition()) && entityLiving.motionY < 0) {
      if (entityLiving instanceof EntityPlayer) {
        EntityPlayer p = (EntityPlayer) entityLiving;
        if (p.isSneaking())
          return;// let them slip down into it
      }
      entityLiving.motionY = 0;// stop falling
      entityLiving.onGround = true; // act as if on solid ground
      entityLiving.setAIMoveSpeed(0.1F);// walking and not sprinting is this
      // speed
    }
  }
}
