package com.lothrazar.cyclic.enchant;

import java.util.Optional;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

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
      /// First, know if the enchantment is really applied and enabled
      if (!isEnabled()) return originalLoot;

      ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
      if (tool == null || tool.isEmpty()) return originalLoot;

      Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
      if (!(entity instanceof Player player)) return originalLoot;

      Holder<Enchantment> holder = EnchantUtil.holder(EnchantRegistry.AUTO_SMELT, player);
      int level = EnchantUtil.getCurrentLevelTool(holder, tool);
      if (level <= 0) return originalLoot;

      /// The enchantment is applied and enabled, convert the loot
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
