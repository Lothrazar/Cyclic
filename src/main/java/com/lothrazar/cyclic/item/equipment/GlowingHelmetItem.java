package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.base.IHasClickToggle;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilPlayer;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GlowingHelmetItem extends ArmorItem implements IHasClickToggle {

  public static final String NBT_GLOW = Const.MODID + "_glow";
  public static final String NBT_STATUS = "onoff";

  public GlowingHelmetItem(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
    super(materialIn, slot, builderIn);
  }

  @Override
  public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
    boolean isTurnedOn = this.isOn(stack);
    removeNightVision(player, isTurnedOn);
    if (isTurnedOn) {
      addNightVision(player);
    }
  }

  @Override
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    tooltip.add(new TranslationTextComponent(UtilChat.lang(this.getTranslationKey() + ".tooltip")));
    String onoff = this.isOn(stack) ? "on" : "off";
    TranslationTextComponent t = new TranslationTextComponent(UtilChat.lang("item.cantoggle.tooltip.info") + " " + UtilChat.lang("item.cantoggle.tooltip." + onoff));
    t.mergeStyle(TextFormatting.DARK_GRAY);
    tooltip.add(t);
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  private void addNightVision(PlayerEntity player) {
    player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 20 * Const.TICKS_PER_SEC, 0));
  }

  public static void removeNightVision(PlayerEntity player, boolean hidden) {
    //flag it so we know the purple glow was from this item, not something else
    player.getPersistentData().putBoolean(NBT_GLOW, hidden);
    player.removeActivePotionEffect(Effects.NIGHT_VISION);
  }

  private void checkIfHelmOff(PlayerEntity player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player, EquipmentSlotType.HEAD);
    if (itemInSlot == this) {
      //turn it off once, from the message
      removeNightVision(player, false);
    }
  }

  @Override
  public void toggle(PlayerEntity player, ItemStack held) {
    CompoundNBT tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.putInt(NBT_STATUS, vnew);
  }

  @Override
  public boolean isOn(ItemStack held) {
    CompoundNBT tags = UtilNBT.getItemStackNBT(held);
    if (!tags.contains(NBT_STATUS)) {
      return true;
    } //default for newlycrafted//legacy items
    return tags.getInt(NBT_STATUS) == 1;
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
    //reduce check to only once per second instead  of per tick
    if (event.getEntity().world.getGameTime() % 20 == 0 &&
        event.getEntityLiving() != null) { //some of the items need an off switch
      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      checkIfHelmOff(player);
    }
  }
}
