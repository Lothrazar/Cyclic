package com.lothrazar.cyclic.item.lunchbox;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

// Item colors are fully data-driven now (RegisterColorHandlersEvent has no Item variant anymore);
// this is a custom ItemTintSource type, registered via RegisterColorHandlersEvent.ItemTintSources
// and referenced by id from the "tints" array in assets/cyclic/items/lunchbox.json.
public record LunchboxOverlayTintSource() implements ItemTintSource {

  public static final MapCodec<LunchboxOverlayTintSource> MAP_CODEC = MapCodec.unit(LunchboxOverlayTintSource::new);

  @Override
  public int calculate(ItemStack stack, ClientLevel level, LivingEntity owner) {
    return ScreenLunchbox.getColour(stack);
  }

  @Override
  public MapCodec<LunchboxOverlayTintSource> type() {
    return MAP_CODEC;
  }
}
