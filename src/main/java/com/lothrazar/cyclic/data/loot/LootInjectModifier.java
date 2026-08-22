package com.lothrazar.cyclic.data.loot;

import com.google.common.base.Suppliers;
import com.lothrazar.cyclic.config.LootConfig;
import com.lothrazar.cyclic.config.LootConfig.ParsedEntry;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.List;
import java.util.function.Supplier;

/**
 * Single GLM that injects items into vanilla loot tables based on cyclic-loot.toml.
 * <p>
 * Registered once with no conditions; runs on every loot roll. We early-return
 * for any table that isn't keyed in {@link LootConfig#ENTRIES}, so the overhead
 * on unrelated rolls (block drops, mob drops, fishing, modded tables) is one
 * map lookup.
 */
public class LootInjectModifier extends LootModifier {

  public static final String ID = "loot_inject";
  public static final Supplier<MapCodec<LootInjectModifier>> MAP_CODEC =
      Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, LootInjectModifier::new)));

  public LootInjectModifier(LootItemCondition[] conditionsIn, int priority) {
    super(conditionsIn, priority);
  }

  @Override
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> originalLoot, LootContext context) {
    // Only react to vanilla loot tables in the minecraft namespace.
    Identifier tableId = context.getQueriedLootTableId();
    if (!"minecraft".equals(tableId.getNamespace())) {
      return originalLoot;
    }
    ConfigValue<List<? extends String>> cv = LootConfig.ENTRIES.get(tableId.getPath());
    if (cv == null) {
      return originalLoot;
    }
    List<? extends String> lines = cv.get();
    if (lines == null || lines.isEmpty()) {
      return originalLoot;
    }
    RandomSource rand = context.getRandom();
    net.minecraft.core.HolderLookup.Provider registries = context.getLevel().registryAccess();
    for (String line : lines) {
      ParsedEntry e = LootConfig.parse(line, registries);
      if (e == null) {
        continue;
      }
      // chance is percent 0-100; rand.nextInt(100) yields 0-99 so use < chance
      if (e.chancePercent <= 0) {
        continue;
      }
      if (e.chancePercent >= 100 || rand.nextInt(100) < e.chancePercent) {
        ItemStack rolled = e.roll(rand);
        if (!rolled.isEmpty()) {
          originalLoot.add(rolled);
        }
      }
    }
    return originalLoot;
  }

  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
    return MAP_CODEC.get();
  }
}
