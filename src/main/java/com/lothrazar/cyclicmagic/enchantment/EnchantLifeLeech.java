package com.lothrazar.cyclicmagic.enchantment;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantLifeLeech extends EnchantBase {
  public EnchantLifeLeech() {
    super("lifeleech",Rarity.COMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
  
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (event.getSource().getSourceOfDamage() instanceof EntityPlayer && event.getEntity() instanceof EntityLivingBase) {
      EntityPlayer attacker = (EntityPlayer) event.getSource().getSourceOfDamage();
      EntityLivingBase target = (EntityLivingBase) event.getEntity();
      int level = getCurrentLevelTool(attacker);
      if (level > 0) {
        // we -1  since potion level 1 is  II
        //so that means enchantment I giving poison I means this
        int restore = (int) Math.max(Math.ceil(target.getMaxHealth() / 5), 4);
        int min = 1;//so if restore starts at 4 the rand will be [min,restore]
        restore = attacker.getEntityWorld().rand.nextInt(restore + 1) + min;
        if (restore > 0) {
          attacker.getFoodStats().setFoodLevel(attacker.getFoodStats().getFoodLevel() + level);
          if (attacker.getHealth() < attacker.getMaxHealth()) {
            attacker.heal(restore);
            UtilParticle.spawnParticle(attacker.getEntityWorld(), EnumParticleTypes.HEART, attacker.getPosition().up(2));
          }
        }
      }
    }
  }
  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (event.getTarget() instanceof EntityLivingBase == false) { return; }
    //    EntityLivingBase target = (EntityLivingBase) event.getTarget();
    EntityPlayer attacker = event.getEntityPlayer();
    int level = getCurrentLevelTool(attacker);
    if (level > 0 && attacker.getHealth() < attacker.getMaxHealth()) {
      UtilParticle.spawnParticle(attacker.getEntityWorld(), EnumParticleTypes.HEART, attacker.getPosition().up(2));
      attacker.heal(level);
    }
  }
}
