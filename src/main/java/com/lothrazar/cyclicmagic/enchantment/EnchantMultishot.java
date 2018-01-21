package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilReflection;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
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
    return 1;
  }
 
  @SubscribeEvent
  public void onPlayerUpdate(ArrowLooseEvent event) {
//    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer player = event.getEntityPlayer();
    ItemStack stack=  event.getBow();
   
      World worldIn = player.world;
      int level = this.getCurrentLevelTool(stack);
      if(level <= 0){
        return;
      }
      
//      i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
//      if (i < 0) return;
      //this is from vanilla ItemBow.class
      if (!worldIn.isRemote)
      {
        float f = ItemBow.getArrowVelocity(stack.getMaxItemUseDuration() -  event.getCharge());
        
          ItemArrow itemarrow = (ItemArrow)(stack.getItem() instanceof ItemArrow ? stack.getItem() : Items.ARROW);
          EntityArrow entityarrow = itemarrow.createArrow(worldIn, stack, player);
          entityarrow.posY += 1.5;//is the dup
          entityarrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);

          
          if (f == 1.0F)
          {
              entityarrow.setIsCritical(true);
          }

          int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

          if (j > 0)
          {
              entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
          }

          int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

          if (k > 0)
          {
              entityarrow.setKnockbackStrength(k);
          }

          if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
          {
              entityarrow.setFire(100);
          }
     
              entityarrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
              

              worldIn.spawnEntity(entityarrow);

          
      }
//      EntityPlayer player = (EntityPlayer) event.getEntity();
//      if (getCurrentLevelTool(player) < 0) {
//        return;
//      }
//      ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
//      if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemBow
//          && player.isHandActive()) {
//        this.tickHeldBow(player);
//        this.tickHeldBow(player);
//      }
//    }
  }
 
}
