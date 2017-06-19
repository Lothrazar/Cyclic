package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSoulstone extends BaseItem implements IHasRecipe {
  public ItemSoulstone() {
    super();
    this.setMaxStackSize(1);
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  @SubscribeEvent
  public void onPlayerHurt(LivingHurtEvent event) {
    if (event.getEntityLiving().getHealth() - event.getAmount() <= 0 && event.getEntityLiving() instanceof EntityPlayer) {
      EntityPlayer p = (EntityPlayer) event.getEntityLiving();
      for (int i = 0; i < p.inventory.getSizeInventory(); ++i) {
        ItemStack s = p.inventory.getStackInSlot(i);
        if (s.getItem() instanceof ItemSoulstone) {
          UtilChat.addChatMessage(p, event.getEntityLiving().getName() + UtilChat.lang("item.soulstone.used"));
          p.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
          UtilSound.playSound(p, SoundEvents.BLOCK_GLASS_BREAK);
          p.setHealth(6);// 3 hearts
          int time = Const.TICKS_PER_SEC * 10;
          p.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, time));//only few secs here
          time = Const.TICKS_PER_SEC * 60;//a full minute
          p.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, time));
          p.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, time, 4));
          //and bad luck lasts much longer
          time = Const.TICKS_PER_SEC * 60 * 10;
          p.addPotionEffect(new PotionEffect(MobEffects.UNLUCK, time));
          p.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, time, 1));
          event.setCanceled(true);
          break;
        }
      }
    }
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " a ", "bsc", " d ", 's', Items.NETHER_STAR, 'a', Items.GOLDEN_APPLE,
        'b', Items.POISONOUS_POTATO, 'c', Blocks.PURPUR_BLOCK, 'd', Items.EMERALD);
  }
}
