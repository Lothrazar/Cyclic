package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemTrader extends BaseItem {
  public ItemTrader() {
    super();
    this.setMaxStackSize(1);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    if (entity instanceof EntityVillager) {
      World world = entity.getEntityWorld();
      EntityVillager villager = (EntityVillager) entity;
      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_VILLAGER, world,
          villager.getPosition().getX(),
          villager.getPosition().getY(),
          villager.getPosition().getZ());
    }
    return true;//Returns true if the item can be used on the given entity, e.g. shears on sheep.
  }
}
