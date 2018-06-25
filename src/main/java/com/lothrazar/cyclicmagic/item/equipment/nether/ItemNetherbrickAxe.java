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
package com.lothrazar.cyclicmagic.item.equipment.nether;

import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemNetherbrickAxe extends ItemAxe implements IHasRecipe {

  public ItemNetherbrickAxe() {
    // protected ItemAxe(Item.ToolMaterial material, int damage, int speed)
    super(MaterialRegistry.netherToolMaterial, 8.0F, -3.1F);
  }

  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this), "ee ", "es ", " s ", 'e', "ingotBrickNether", 's', "stickWood");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), " ee", " se", " s ", 'e', "ingotBrickNether", 's', "stickWood");
  }
}
