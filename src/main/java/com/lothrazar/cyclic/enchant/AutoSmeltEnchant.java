package com.lothrazar.cyclic.enchant;

import java.util.Optional;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import static com.lothrazar.cyclic.ModCyclic.MODID;

public class AutoSmeltEnchant {

  public static final String ID = "auto_smelt";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  public static class EnchantAutoSmeltModifier extends LootModifier {

    public static final Supplier<MapCodec<EnchantAutoSmeltModifier>> MAP_CODEC =
        Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, EnchantAutoSmeltModifier::new)));

    public EnchantAutoSmeltModifier(LootItemCondition[] conditionsIn) {
      super(conditionsIn);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> originalLoot, LootContext context) {
      /// First, know if the enchantment is really applied
      ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
      if (tool == null || tool.isEmpty()) return originalLoot;

      ItemEnchantments enchantments = tool.get(DataComponents.ENCHANTMENTS);
      if (enchantments == null) return originalLoot;

      Registry<Enchantment> registry = context.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
      ResourceKey<Enchantment> autoSmeltKey = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MODID, ID));

      Holder<Enchantment> holder = registry.getHolder(autoSmeltKey).orElse(null);
      if (holder == null) return originalLoot;

      int level = enchantments.getLevel(holder);
      if (level <= 0) return originalLoot;

      /// The enchantment is applied, convert the loot
      ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
      originalLoot.forEach((stack) -> {
        Optional<RecipeHolder<SmeltingRecipe>> optional = context.getLevel().getRecipeManager()
            .getRecipeFor(RecipeType.SMELTING, new SingleRecipeInput(stack), context.getLevel());
        if (optional.isPresent()) {
          ItemStack smeltedItemStack = optional.get().value().getResultItem(context.getLevel().registryAccess());
          if (!smeltedItemStack.isEmpty()) {
            newLoot.add(smeltedItemStack.copyWithCount(stack.getCount() * smeltedItemStack.getCount()));
          }
          else {
            newLoot.add(stack);
          }
        }
        else {
          newLoot.add(stack);
        }
      });
      return newLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
      return MAP_CODEC.get();
    }
  }
}
