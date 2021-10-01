package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoundCard extends ItemBase {

  private static final String SOUND_ID = "sound_id";

  public SoundCard(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    //    BlockPos pos = context.getPos();
    //    World world = context.getWorld();
    PlayerEntity player = context.getPlayer();
    if (player.getCooldownTracker().hasCooldown(this)) {
      return ActionResultType.PASS;
    }
    ItemStack stack = context.getItem();
    if (stack.hasTag() && stack.getTag().contains(SOUND_ID)) {
      //assume sound is valid
      player.getCooldownTracker().setCooldown(this, 10);
      player.swingArm(context.getHand());
      //actually play it
      String sid = stack.getTag().getString(SOUND_ID);
      UtilSound.playSoundById(player, sid);
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
