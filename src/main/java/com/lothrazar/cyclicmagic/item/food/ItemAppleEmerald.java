package com.lothrazar.cyclicmagic.item.food;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAppleEmerald extends BaseItem implements IHasRecipe {
  private static final int CONVTIME = 1200;
  @Override
  public IRecipe addRecipe() {
    return  RecipeRegistry.addShapelessRecipe(new ItemStack(this),
        Items.EMERALD,
        Items.GOLDEN_APPLE);
   
  }
  @Override
  @SideOnly(Side.CLIENT)
  public EnumRarity getRarity(ItemStack par1ItemStack) {
    return EnumRarity.RARE;
  }
  private void startConverting(EntityZombieVillager v, int t) {
    //      v.conversionTime = t;
    ObfuscationReflectionHelper.setPrivateValue(EntityZombieVillager.class, v, t, "conversionTime", "field_82234_d");
    v.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, t, Math.min(v.world.getDifficulty().getDifficultyId() - 1, 0)));
    v.world.setEntityState(v, (byte) 16);
    try {
      //       v.getDataManager().set(CONVERTING, Boolean.valueOf(true));
      DataParameter<Boolean> CONVERTING = ObfuscationReflectionHelper.getPrivateValue(EntityZombieVillager.class, v, "CONVERTING", "field_184739_bx");
      v.getDataManager().set(CONVERTING, Boolean.valueOf(true));
    }
    catch (Exception e) {}
  }
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) event.getEntity();
    //    ItemStack held = player.getHeldItemMainhand();
    ItemStack itemstack = event.getItemStack();
    if (itemstack != null && itemstack.getItem() instanceof ItemAppleEmerald && itemstack.getCount() > 0) {
      if (event.getTarget() instanceof EntityVillager) {
        EntityVillager villager = ((EntityVillager) event.getTarget());
        int count = 0;
        for (MerchantRecipe merchantrecipe : villager.getRecipes(player)) {
          if (merchantrecipe.isRecipeDisabled()) {
            //vanilla code as of 1.9.4 odes this (2d6+2) 
            merchantrecipe.increaseMaxTradeUses(villager.getEntityWorld().rand.nextInt(6) + villager.getEntityWorld().rand.nextInt(6) + 2);
            count++;
          }
        }
        if (count > 0) {
          UtilChat.addChatMessage(player, UtilChat.lang("item.apple_emerald.merchant") + count);
          itemstack.shrink(1);
          if (itemstack.getCount() == 0) {
            itemstack = ItemStack.EMPTY;
          }
        }
        event.setCanceled(true);// stop the GUI inventory opening && horse mounting
      }
    }
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    if (entity instanceof EntityZombieVillager) {
      EntityZombieVillager zombie = ((EntityZombieVillager) entity);
      //this is what we WANT to do, but the method is protected. we have to fake it by faking the interact event
      //      zombie.startConverting(1200);
      startConverting(zombie, CONVTIME);
      return true;
    }
    return super.itemInteractionForEntity(itemstack, player, entity, hand);
  }
}
