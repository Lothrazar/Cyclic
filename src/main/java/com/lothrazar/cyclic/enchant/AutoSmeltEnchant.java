package com.lothrazar.cyclic.enchant;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.Optional;
import java.util.function.Supplier;

public class AutoSmeltEnchant {

  public static final String ID = "auto_smelt";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  public static class EnchantAutoSmeltModifier extends LootModifier {

    public static final Supplier<MapCodec<EnchantAutoSmeltModifier>> MAP_CODEC =
        Suppliers.memoize(() -> RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, EnchantAutoSmeltModifier::new)));

    public EnchantAutoSmeltModifier(LootItemCondition[] conditionsIn, int priority) {
      super(conditionsIn, priority);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> originalLoot, LootContext context) {
      ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
      originalLoot.forEach((stack) -> {
        SingleRecipeInput recipeInput = new SingleRecipeInput(stack);
        Optional<RecipeHolder<SmeltingRecipe>> optional = context.getLevel().getServer().getRecipeManager()
            .getRecipeFor(RecipeType.SMELTING, recipeInput, context.getLevel());
        if (optional.isPresent()) {
          ItemStack smeltedItemStack = optional.get().value().assemble(recipeInput);
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
