package com.lothrazar.cyclic.capabilities.item;

import com.lothrazar.cyclic.util.RegistryHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Item-level inventory capability backed by the host stack's CUSTOM_DATA component.
 * Reads/writes the full ItemStackHandler NBT under the "inventory" key so contents
 * persist across place/break cycles.
 */
public class ItemInventoryCap extends ItemStackHandler {

  private static final String KEY = "inventory";
  private final ItemStack host;

  public ItemInventoryCap(ItemStack host, int slots) {
    super(slots);
    this.host = host;
    var registries = RegistryHolder.get();
    if (registries != null) {
      CompoundTag data = host.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (data.contains(KEY)) {
        deserialize(TagValueInput.create(ProblemReporter.DISCARDING, registries, data.getCompound(KEY)));
      }
    }
  }

  @Override
  protected void onContentsChanged(int slot) {
    var registries = RegistryHolder.get();
    if (registries == null) {
      return;
    }
    CompoundTag data = host.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, registries);
      serialize(output);
      data.put(KEY, output.buildResult());
    host.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
  }
}
