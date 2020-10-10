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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.base.EnchantBase;
import net.minecraft.enchantment.Enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;


public class EnchantAutoSmelt extends EnchantBase {

    public EnchantAutoSmelt(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
        super(rarityIn, typeIn, slots);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        return ench != Enchantments.SILK_TOUCH && ench != Enchantments.FORTUNE && super.canApplyTogether(ench);
    }

    private static class EnchantAutoSmeltModifier extends LootModifier {
        public EnchantAutoSmeltModifier(ILootCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        public List<ItemStack> doApply(List<ItemStack> originalLoot, LootContext context) {
            List<ItemStack> newLoot = new ArrayList<>(); 
            originalLoot.forEach((stack) -> {
                Optional<FurnaceRecipe> optional = context.getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), context.getWorld());
                if (optional.isPresent()) {
                    ItemStack smeltedItemStack = optional.get().getRecipeOutput();
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
        public EnchantAutoSmeltModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new EnchantAutoSmeltModifier(conditionsIn);
        }

        @Override
        public JsonObject write(EnchantAutoSmeltModifier instance) {
            return null; //not sure what to do with this
        }
    }
}
