package com.lothrazar.cyclic.item.datacard;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.item.ItemStack;

public class SoundCard extends ItemBase {

  public SoundCard(Properties properties) {
    super(properties);
  }

  public static void saveSound(ItemStack stack, String soundId) {
    if (stack.hasTag() && (soundId == null || soundId.isEmpty())) {
      stack.getTag().remove("sound_id");
    }
    else {
      stack.getOrCreateTag().putString("sound_id", soundId);
    }
    System.out.println("tooltip");
  }
}
