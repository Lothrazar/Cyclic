package com.lothrazar.cyclicmagic.potion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionEnder extends PotionBase {
  public PotionEnder(String name, boolean b, int potionColor) {
    super(name, b, potionColor);
  }
  @SubscribeEvent
  public void onEnderTeleportEvent(EnderTeleportEvent event) {
    Entity ent = event.getEntity();
    if (ent instanceof EntityLivingBase == false) { return; }
    EntityLivingBase living = (EntityLivingBase) event.getEntity();
    if (living.isPotionActive(this)) {
      event.setAttackDamage(0);
    }
  }
  @SubscribeEvent
  public void onHurt(LivingHurtEvent event) {
    if (event.getEntityLiving().isPotionActive(this) && event.getSource() == DamageSource.IN_WALL) {
      event.setAmount(0);
    }
  }
  @SubscribeEvent
  public void onLivingKill(LivingDeathEvent event) {
    if (event.getSource().getSourceOfDamage() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer) event.getSource().getSourceOfDamage();
      Entity target = event.getEntity();
      if (player.isPotionActive(this) && target instanceof EntityEnderman) {
        World world = player.getEntityWorld();
        int randMore = world.rand.nextInt(5) + 1;// range[1,5]
        world.spawnEntity(new EntityXPOrb(world, target.posX, target.posY, target.posZ, randMore));
      }
    }
  }
  @Override
  public void tick(EntityLivingBase entity) {}
}
