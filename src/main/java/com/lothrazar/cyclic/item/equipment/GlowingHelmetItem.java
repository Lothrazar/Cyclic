package com.lothrazar.cyclic.item.equipment;

import java.util.List;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.CharmUtil;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.core.IHasClickToggle;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.TagDataUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.minecraft.core.Holder;

public class GlowingHelmetItem extends ArmorItem implements IHasClickToggle {

  public static final String NBT_STATUS = "onoff";

  public GlowingHelmetItem(Holder<ArmorMaterial> materialIn, ArmorItem.Type slot, Properties builderIn) {
    super(materialIn, slot, builderIn);
  }

  @Override
  public void appendHoverText(ItemStack stack, Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(Component.translatable(ChatUtil.lang(this.getDescriptionId() + ".tooltip")).withStyle(ChatFormatting.GRAY));
    String onoff = this.isOn(stack) ? "on" : "off";
    MutableComponent t = Component.translatable(ChatUtil.lang("item.cantoggle.tooltip.info") + " " + ChatUtil.lang("item.cantoggle.tooltip." + onoff));
    t.withStyle(ChatFormatting.DARK_GRAY);
    tooltip.add(t);
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
  }

  private static void addNightVision(Player player) {
    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20 * Const.TICKS_PER_SEC, 0, false, false, false));
  }

  public static void removeNightVision(Player player, boolean hidden) {
    //flag it so we know the purple glow was from this item, not something else 
    player.removeEffectNoUpdate(MobEffects.NIGHT_VISION);
  }

  @Override
  public void toggle(Player player, ItemStack held) {
    int vnew = isOn(held) ? 0 : 1;
    TagDataUtil.setItemStackNBTVal(held, NBT_STATUS, vnew);
  }

  @Override
  public boolean isOn(ItemStack held) {
    //    CompoundTag tags = UtilNBT.getItemStackNBT(held);
    return isOnStatic(held);
  }

  private static boolean isOnStatic(ItemStack held) {
    CompoundTag tags = TagDataUtil.getItemStackNBT(held);
    if (!tags.contains(NBT_STATUS)) {
      return true;
    } //default for newlycrafted//legacy items
    return tags.getInt(NBT_STATUS) == 1;
  }

  //check the vanilla HEAD armor slot first, then fall back to Curios slots
  public static void onEntityUpdate(EntityTickEvent.Pre event) {
    //reduce check to only once per second instead  of per tick
    if (event.getEntity().level().getGameTime() % Const.TICKS_PER_SEC == 0 &&
        event.getEntity() instanceof Player player) {
      ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
      if (!(helm.getItem() instanceof GlowingHelmetItem)) {
        helm = CharmUtil.getCurio(player, ItemRegistry.GLOWING_HELMET.get());
      }
      if (helm.isEmpty()) {
        return;
      }
      if (isOnStatic(helm)) {
        addNightVision(player);
      }
      else {
        removeNightVision(player, false);
      }
    }
  }
}
