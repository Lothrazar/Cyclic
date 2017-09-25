package com.lothrazar.cyclicmagic.item.food;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemAppleLapis extends BaseItem implements IHasRecipe {
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapelessRecipe(new ItemStack(this),
        Items.APPLE,
        "blockLapis");
    return null;
  }
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) event.getEntity();
    //    ItemStack held = player.getHeldItemMainhand();
    ItemStack itemstack = event.getItemStack();
    if (itemstack != null && itemstack.getItem() instanceof ItemAppleLapis && itemstack.getCount() > 0) {
      if (event.getTarget() instanceof EntityZombieHorse || event.getTarget() instanceof EntitySkeletonHorse) {
        AbstractHorse h = (AbstractHorse) event.getTarget();
        if (h.isTame() == false) {
          h.setTamedBy(player);
          h.setEatingHaystack(true);
          //        UtilChat.addChatMessage(player, UtilChat.lang("item.apple_emerald.merchant"));
          itemstack.shrink(1);
          if (itemstack.getCount() == 0) {
            itemstack = ItemStack.EMPTY;
          }
          event.setCanceled(true);// stop the GUI inventory opening && horse mounting
        }
      }
    }
  }
}
