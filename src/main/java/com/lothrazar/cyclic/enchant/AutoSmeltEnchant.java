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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.MinecraftForge;

public class AutoSmeltEnchant extends EnchantmentCyclic {

  public static final String ID = "auto_smelt";
  public static BooleanValue CFG;

  public AutoSmeltEnchant(Rarity rarityIn, EnchantmentCategory typeIn, EquipmentSlot... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  //
  // config stuff start
  //
  @Override
  public boolean isTradeable() {
    return isEnabled() && super.isTradeable();
  }

  @Override
  public boolean isDiscoverable() {
    return isEnabled() && super.isDiscoverable();
  }

  @Override
  public boolean isAllowedOnBooks() {
    return isEnabled() && super.isAllowedOnBooks();
  }

  @Override
  public boolean canEnchant(ItemStack stack) {
    return isEnabled() && super.canEnchant(stack);
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return isEnabled() && super.canApplyAtEnchantingTable(stack);
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
  //  public static class EnchantAutoSmeltModifier extends LootModifier {
  //
  //    public EnchantAutoSmeltModifier(LootItemCondition[] conditionsIn) {
  //      super(conditionsIn);
  //    }
  //
  //    @Override
  //    public List<ItemStack> doApply(List<ItemStack> originalLoot, LootContext context) {
  //      List<ItemStack> newLoot = new ArrayList<>();
  //      originalLoot.forEach((stack) -> {
  //        Optional<SmeltingRecipe> optional = context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel());
  //        if (optional.isPresent()) {
  //          ItemStack smeltedItemStack = optional.get().getResultItem();
  //          if (!smeltedItemStack.isEmpty()) {
  //            smeltedItemStack = ItemHandlerHelper.copyStackWithSize(smeltedItemStack, stack.getCount() * smeltedItemStack.getCount());
  //            newLoot.add(smeltedItemStack);
  //          }
  //          else {
  //            newLoot.add(stack);
  //          }
  //        }
  //        else {
  //          newLoot.add(stack);
  //        }
  //      });
  //      return newLoot;
  //    }
  //  }
  //
  //  public static class Serializer extends Codec<LootModifier><EnchantAutoSmeltModifier> {
  //
  //    @Override
  //    public EnchantAutoSmeltModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
  //      return new EnchantAutoSmeltModifier(conditionsIn);
  //    }
  //
  //    @Override
  //    public JsonObject write(EnchantAutoSmeltModifier instance) {
  //      return null; //not sure what to do with this
  //    }
  //  }
}
