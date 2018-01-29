package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantReach extends EnchantBase {
  private static final String NBT_REACH_ON = "reachon";
  private static final int REACH_VANILLA = 5;
  private static final int REACH_BOOST = 16;
  private static final Rarity RARITY = Rarity.VERY_RARE;
  public EnchantReach() {
    super("reach", RARITY, EnumEnchantmentType.ARMOR_CHEST, new EntityEquipmentSlot[] { EntityEquipmentSlot.CHEST });
    GuideRegistry.register(this, new ArrayList<String>(Arrays.asList(REACH_BOOST + "")));
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet
    boolean yes = stack.getItem() == Items.BOOK ||
        stack.getItem() == Items.ELYTRA ||
        (stack.getItem() instanceof ItemArmor)
            && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.CHEST;
    return yes;
  }
  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }
  private void turnReachOff(EntityPlayer player) {
    player.getEntityData().setBoolean(NBT_REACH_ON, false);
    ModCyclic.proxy.setPlayerReach(player, REACH_VANILLA);
  }
  private void turnReachOn(EntityPlayer player) {
    player.getEntityData().setBoolean(NBT_REACH_ON, true);//.setInteger(NBT_REACH_ON, 1);
    ModCyclic.proxy.setPlayerReach(player, REACH_BOOST);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    //check if NOT holding this harm
    if (event.getEntityLiving() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    //Ticking
    ItemStack armor = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
    int level = 0;
    if (armor.isEmpty() == false && EnchantmentHelper.getEnchantments(armor) != null
        && EnchantmentHelper.getEnchantments(armor).containsKey(this)) {
      //todo: maybe any armor?
      level = EnchantmentHelper.getEnchantments(armor).get(this);
    }
    if (level > 0) {
      turnReachOn(player);
    }
    else {
      //was it on before, do we need to do an off hit
      if (player.getEntityData().hasKey(NBT_REACH_ON) && player.getEntityData().getBoolean(NBT_REACH_ON)) {
        turnReachOff(player);
      }
    }
  }
}
