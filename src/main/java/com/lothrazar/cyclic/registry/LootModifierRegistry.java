package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.enchant.AutoSmeltEnchant;
import com.lothrazar.cyclic.enchant.AutoSmeltEnchant.EnchantAutoSmeltModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class LootModifierRegistry {

  public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ModCyclic.MODID);
  public static final java.util.function.Supplier<MapCodec<EnchantAutoSmeltModifier>> AUTO_SMELT = LOOT.register(AutoSmeltEnchant.ID, () -> EnchantAutoSmeltModifier.MAP_CODEC.get());
}
