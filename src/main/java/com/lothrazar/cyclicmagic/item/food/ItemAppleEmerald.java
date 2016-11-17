package com.lothrazar.cyclicmagic.item.food;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAppleEmerald extends ItemFood implements IHasRecipe {
  public ItemAppleEmerald() {
    super(2, false);
    this.setAlwaysEdible();
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this),
        Items.EMERALD,
        Items.GOLDEN_APPLE);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public EnumRarity getRarity(ItemStack par1ItemStack) {
    return EnumRarity.RARE;
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    if (entity instanceof EntityZombie) {
      EntityZombie zombie = ((EntityZombie) entity);
      //this is what we WANT to do, but the method is protected. we have to fake it by faking the interact event
      // ((EntityZombie)entity).startConversion(1200);
      if (zombie.isVillager()) {
        UtilEntityLiving.addOrMergePotionEffect(entity, new PotionEffect(MobEffects.WEAKNESS, 10, 0));
        if (zombie.processInteract(player, hand, new ItemStack(Items.GOLDEN_APPLE))) {
          itemstack.stackSize--;
          if (itemstack.stackSize == 0) {
            itemstack = null;
          }
        }
        //UtilInventory.decrStackSize(player, currentItem);
        return true;
      }
    }
    else if (entity instanceof EntityVillager) {
      EntityVillager villager = ((EntityVillager) entity);
      int count = 0;
      for (MerchantRecipe merchantrecipe : villager.getRecipes(player)) {
        if (merchantrecipe.isRecipeDisabled()) {
          //vanilla code as of 1.9.4 odes this (2d6+2) 
          merchantrecipe.increaseMaxTradeUses(villager.worldObj.rand.nextInt(6) + villager.worldObj.rand.nextInt(6) + 2);
          count++;
        }
      }
      if (count > 0) {
        UtilChat.addChatMessage(player, UtilChat.lang("item.apple_emerald.merchant") + count);
        itemstack.stackSize--;
        if (itemstack.stackSize == 0) {
          itemstack = null;
        }
        //				 else{
        //					 UtilSound.playSound(player, villager.getPosition(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE);
        //				 }
      }
      return true;
    }
    return super.itemInteractionForEntity(itemstack, player, entity, hand);
  }
  public void addInformation(ItemStack held, EntityPlayer player, List<String> list, boolean par4) {
    list.add(UtilChat.lang("item.apple_emerald.text"));
  }
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    UtilEntityLiving.addOrMergePotionEffect(player, new PotionEffect(
        MobEffects.SATURATION,
        20 * Const.TICKS_PER_SEC,
        Const.Potions.I));
  }
}
