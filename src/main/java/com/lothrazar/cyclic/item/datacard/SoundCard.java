package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class SoundCard extends ItemBase {

  private static final String SOUND_ID = "sound_id";

  public SoundCard(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    BlockPos pos = context.getPos();
    World world = context.getWorld();
    if (context.getPlayer().getCooldownTracker().hasCooldown(this)) {
      return ActionResultType.PASS;
    }
    ItemStack stack = context.getItem();
    if (stack.hasTag() && stack.getTag().contains(SOUND_ID)) {
      context.getPlayer().getCooldownTracker().setCooldown(this, 10);
      String sid = stack.getTag().getString(SOUND_ID);
      //do the thing
      SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sid));
      if (sound != null && world.isRemote) {
        UtilSound.playSound(context.getPlayer(), sound);
        context.getPlayer().swingArm(context.getHand());
      }
    }
    return ActionResultType.PASS;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    if (stack.hasTag() && stack.getTag().contains(SOUND_ID)) {
      tooltip.add(new StringTextComponent(stack.getTag().getString(SOUND_ID)).mergeStyle(TextFormatting.GOLD));
    }
  }

  public static void saveSound(ItemStack stack, String soundId) {
    if (stack.hasTag() && (soundId == null || soundId.isEmpty())) {
      stack.getTag().remove(SOUND_ID);
    }
    else {
      stack.getOrCreateTag().putString(SOUND_ID, soundId);
    }
  }
}
