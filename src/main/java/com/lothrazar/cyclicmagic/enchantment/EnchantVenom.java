package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantVenom extends EnchantBase {
  final int durationTicksPerLevel = 3 * Const.TICKS_PER_SEC;//3 seconds
  public EnchantVenom() {
    super("venom", Rarity.COMMON, EnumEnchantmentType.WEAPON, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>(Arrays.asList(durationTicksPerLevel + "")));
  }
  @Override
  public int getMaxLevel() {
    return 2;
  }
  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (event.getTarget() instanceof EntityLivingBase == false) { return; }
    EntityLivingBase target = (EntityLivingBase) event.getTarget();
    EntityPlayer attacker = event.getEntityPlayer();
    ItemStack main = attacker.getHeldItemMainhand();
    ItemStack off = attacker.getHeldItemOffhand();
    int mainLevel = -1, offLevel = -1;
    if (main != null && EnchantmentHelper.getEnchantments(main).containsKey(this)) {
      mainLevel = EnchantmentHelper.getEnchantments(main).get(this);
    }
    if (off != null && EnchantmentHelper.getEnchantments(off).containsKey(this)) {
      offLevel = EnchantmentHelper.getEnchantments(off).get(this);
    }
    int level = Math.max(mainLevel, offLevel);
    if (level > 0) {
      // we -1  since potion level 1 is Poison II
      //so that means enchantment I giving poison I means this
      UtilEntity.addOrMergePotionEffect(target, new PotionEffect(MobEffects.POISON, durationTicksPerLevel * level, level - 1));
    }
  }
}
