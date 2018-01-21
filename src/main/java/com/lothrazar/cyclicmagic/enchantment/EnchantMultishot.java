package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantMultishot extends EnchantBase {
  public EnchantMultishot() {
    super("multishot", Rarity.VERY_RARE, EnumEnchantmentType.BOW, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    GuideRegistry.register(this, new ArrayList<String>());
  }
  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof ItemBow
        || stack.getItem() == Items.BOOK;
  }
  @Override
  public int getMaxLevel() {
    //number of EXRA arrows, so max level V is six arrows total
    return 5;
  }
  @SubscribeEvent
  public void onPlayerUpdate(ArrowLooseEvent event) {
 
    EntityPlayer player = event.getEntityPlayer();
    ItemStack stack = event.getBow();
    World worldIn = player.world;
    int level = this.getCurrentLevelTool(stack);
    if (level <= 0) {
      return;
    }
 
    if (!worldIn.isRemote) {
      float charge = ItemBow.getArrowVelocity(stack.getMaxItemUseDuration() - event.getCharge());
      float offsetPerLevel = 0.3F;
      for (int i = 1; i <= level; i++) {
        //TODO: how to gethorizontal offsets based on player facing?
        spawnArrow(worldIn, player, stack, charge, 0, i * offsetPerLevel, 0);
      }
    }
  }
  public void spawnArrow(World worldIn, EntityPlayer player, ItemStack stackBow, float charge, float offsetX, float offsetY, float offsetZ) {
    //this is from vanilla ItemBow.class
    ItemArrow itemarrow = (ItemArrow) (stackBow.getItem() instanceof ItemArrow ? stackBow.getItem() : Items.ARROW);
    EntityArrow entityarrow = itemarrow.createArrow(worldIn, stackBow, player);
    entityarrow.posX += offsetX;
    entityarrow.posY += offsetY;
    entityarrow.posZ += offsetZ;
    entityarrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F, charge * 3.0F, 1.0F);
    if (charge == 1.0F) {
      entityarrow.setIsCritical(true);
    }
    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stackBow);
    if (j > 0) {
      entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
    }
    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stackBow);
    if (k > 0) {
      entityarrow.setKnockbackStrength(k);
    }
    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stackBow) > 0) {
      entityarrow.setFire(100);
    }
    entityarrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
    worldIn.spawnEntity(entityarrow);
  }
}
