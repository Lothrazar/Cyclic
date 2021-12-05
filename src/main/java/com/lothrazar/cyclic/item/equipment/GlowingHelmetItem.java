package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.api.IHasClickToggle;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.CharmUtil;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilPlayer;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;

public class GlowingHelmetItem extends ArmorItem implements IHasClickToggle {

  public static final String NBT_STATUS = "onoff";

  public GlowingHelmetItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn) {
    super(materialIn, slot, builderIn);
  }

  @Override
  public void onArmorTick(ItemStack stack, Level world, Player player) {
    boolean isTurnedOn = this.isOn(stack);
    removeNightVision(player, isTurnedOn);
    if (isTurnedOn) {
      addNightVision(player);
    }
  }

  @Override
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(new TranslatableComponent(UtilChat.lang(this.getDescriptionId() + ".tooltip")).withStyle(ChatFormatting.GRAY));
    String onoff = this.isOn(stack) ? "on" : "off";
    TranslatableComponent t = new TranslatableComponent(UtilChat.lang("item.cantoggle.tooltip.info") + " " + UtilChat.lang("item.cantoggle.tooltip." + onoff));
    t.withStyle(ChatFormatting.DARK_GRAY);
    tooltip.add(t);
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  private static void addNightVision(Player player) {
    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * Const.TICKS_PER_SEC, 0));
  }

  public static void removeNightVision(Player player, boolean hidden) {
    //flag it so we know the purple glow was from this item, not something else 
    player.removeEffectNoUpdate(MobEffects.NIGHT_VISION);
  }

  private static void checkIfHelmOff(Player player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player, EquipmentSlot.HEAD);
    if (itemInSlot instanceof GlowingHelmetItem) {
      //turn it off once, from the message
      removeNightVision(player, false);
    }
  }

  @Override
  public void toggle(Player player, ItemStack held) {
    CompoundTag tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.putInt(NBT_STATUS, vnew);
  }

  @Override
  public boolean isOn(ItemStack held) {
    //    CompoundTag tags = UtilNBT.getItemStackNBT(held);
    return isOnStatic(held);
  }

  private static boolean isOnStatic(ItemStack held) {
    CompoundTag tags = UtilNBT.getItemStackNBT(held);
    if (!tags.contains(NBT_STATUS)) {
      return true;
    } //default for newlycrafted//legacy items
    return tags.getInt(NBT_STATUS) == 1;
  }

  //from ItemEvents- curios slot
  public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
    //reduce check to only once per second instead  of per tick
    //<<<<<<< HEAD
    if (event.getEntity().level.getGameTime() % 20 == 0 &&
        event.getEntityLiving() != null) { //some of the items need an off switch
      Player player = (Player) event.getEntityLiving();
      //=======
      //    if (event.getEntity().world.getGameTime() % 20 == 0 &&
      //        event.getEntityLiving() instanceof PlayerEntity) { //some of the items need an off switch
      //      PlayerEntity player = (PlayerEntity) event.getEntityLiving();
      //>>>>>>> 9f4791a4f5c1dbc36e417a790d13312fb60c6528
      checkIfHelmOff(player);
      // get helm
      ItemStack helm = CharmUtil.getCurio(player, ItemRegistry.GLOWING_HELMET.get());
      if (!helm.isEmpty()) {
        if (isOnStatic(helm)) {
          addNightVision(player);
        }
        else {
          removeNightVision(player, false);
        }
      }
    }
  }
}
