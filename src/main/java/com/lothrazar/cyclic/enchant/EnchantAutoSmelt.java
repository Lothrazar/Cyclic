/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.enchant;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.base.EnchantBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

public class EnchantAutoSmelt extends EnchantBase {

  public static final String ID = "auto_smelt";
  public static BooleanValue CFG;

  public EnchantAutoSmelt(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 1;
  }

  @Override
  public boolean checkCompatibility(Enchantment ench) {
    return ench != Enchantments.SILK_TOUCH && ench != Enchantments.BLOCK_FORTUNE && super.checkCompatibility(ench);
  }

  public static class EnchantAutoSmeltModifier extends LootModifier {

    public EnchantAutoSmeltModifier(LootItemCondition[] conditionsIn) {
      super(conditionsIn);
    }

    @Override
    public List<ItemStack> doApply(List<ItemStack> originalLoot, LootContext context) {
      List<ItemStack> newLoot = new ArrayList<>();
      originalLoot.forEach((stack) -> {
        Optional<SmeltingRecipe> optional = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel());
        if (optional.isPresent()) {
          ItemStack smeltedItemStack = optional.get().getResultItem();
          if (!smeltedItemStack.isEmpty()) {
            smeltedItemStack = ItemHandlerHelper.copyStackWithSize(smeltedItemStack, stack.getCount() * smeltedItemStack.getCount());
            newLoot.add(smeltedItemStack);
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
  }

  public static class Serializer extends GlobalLootModifierSerializer<EnchantAutoSmeltModifier> {

    @Override
    public EnchantAutoSmeltModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
      return new EnchantAutoSmeltModifier(conditionsIn);
    }

    @Override
    public JsonObject write(EnchantAutoSmeltModifier instance) {
      return null; //not sure what to do with this
    }
  }
}
