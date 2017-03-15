package com.lothrazar.cyclicmagic.item.food;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAppleLapis extends ItemFood implements IHasRecipe {
  public ItemAppleLapis() {
    super(2, false);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this),
        Items.APPLE,
        Blocks.LAPIS_BLOCK);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public EnumRarity getRarity(ItemStack par1ItemStack) {
    return EnumRarity.RARE;
  }
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) { return; }
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
  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    UtilEntity.addOrMergePotionEffect(player, new PotionEffect(
        MobEffects.SATURATION,
        20 * Const.TICKS_PER_SEC,
        Const.Potions.I));
  }
}
