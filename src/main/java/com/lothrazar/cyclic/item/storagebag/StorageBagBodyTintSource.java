package com.lothrazar.cyclic.item.storagebag;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

// Item colors are fully data-driven now (RegisterColorHandlersEvent has no Item variant anymore);
// this is a custom ItemTintSource type, registered via RegisterColorHandlersEvent.ItemTintSources
// and referenced by id from the "tints" array in assets/cyclic/items/storage_bag.json.
public record StorageBagBodyTintSource() implements ItemTintSource {

  public static final MapCodec<StorageBagBodyTintSource> MAP_CODEC = MapCodec.unit(StorageBagBodyTintSource::new);

  @Override
  public int calculate(ItemStack stack, ClientLevel level, LivingEntity owner) {
    return ItemStorageBag.getColour(stack);
  }

  @Override
  public MapCodec<StorageBagBodyTintSource> type() {
    return MAP_CODEC;
  }
}
