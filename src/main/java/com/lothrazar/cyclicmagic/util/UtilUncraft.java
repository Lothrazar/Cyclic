package com.lothrazar.cyclicmagic.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class UtilUncraft {

	// static boolean blockIfCannotDoit;
	public static boolean					dictionaryFreedom;

	public static List<String>		blacklistInput	= new ArrayList<String>();

	// also, when crafting cake you get the empty bucket back.
	// so dont refund full buckets or else thats free infinite iron
	public static List<String>		blacklistOutput	= new ArrayList<String>();// They
	                                                                        // were
	// null

	public static final int							TIMER_FULL = 200;

	private ArrayList<ItemStack>	drops;
	private ItemStack							toUncraft;
	private int										outsize;

	public UtilUncraft(ItemStack stuff) {
		this.drops = new ArrayList<ItemStack>();
		this.toUncraft = stuff;
		this.outsize = 0;
	}

	public ArrayList<ItemStack> getDrops() {
		return drops;
	}

	public int getOutsize() {
		return outsize;
	}

	private void addDrop(ItemStack s) {
		// this fn is null safe, it gets nulls all the time
		if (s == null || s.getItem() == null) { return; }

		if (blacklistOutput.contains(s.getItem().getUnlocalizedName())) { return; }

		ItemStack stack = s.copy();
		stack.stackSize = 1;
		if (stack.getItemDamage() == 32767) {
			// this STILL HAPPENS!! WHAT
			// THIS probably doesnt ever happen anymore, but leaving it just in
			// case
			// bugged out wooden planks from something like a note block or
			// chest
			// where , there are a whole bunch of wooden plank types it COULD be
			// but no way to know for sure
			// by default (if checking Only number) this blocks all oak/quartz
			/*
			 * if("tile.wood.oak".equals( stack.getUnlocalizedName())) { return;
			 * }
			 */
			if (dictionaryFreedom) {
				stack.setItemDamage(0);// do not make invalid quartz
			}
			else {
				return;
			}
		}

		drops.add(stack);
	}

	@SuppressWarnings("unchecked")
	public boolean doUncraft()// World world, ItemStack toUncraft, BlockPos po
	{
		if (toUncraft == null || toUncraft.getItem() == null) { return false; }

		if (blacklistInput.contains(toUncraft.getItem().getUnlocalizedName())) { return false; }

		int i;
		Object maybeOres;
		outsize = 0;

		// outsize is 3 means the recipe makes three items total. so MINUS three
		// from the toUncraft for EACH LOOP

		for (Object next : CraftingManager.getInstance().getRecipeList()) {
			// check ore dictionary for some

			if (next instanceof ShapedOreRecipe) {
				ShapedOreRecipe r = (ShapedOreRecipe) next;

				if (r.getRecipeOutput().isItemEqual(toUncraft)) {
					outsize = r.getRecipeOutput().stackSize;

					if (toUncraft.stackSize >= outsize) {
						for (i = 0; i < r.getInput().length; i++) {
							maybeOres = r.getInput()[i];
							if (maybeOres == null) {
								continue;
							}
							// thanks
							// http://stackoverflow.com/questions/20462819/java-util-collectionsunmodifiablerandomaccesslist-to-collections-singletonlist

							if (maybeOres instanceof List<?> && (List<ItemStack>) maybeOres != null)// <ItemStack>
							{
								List<ItemStack> ores = (List<ItemStack>) maybeOres;

								if (ores.size() == 1) {
									// sticks,iron,and so on
									addDrop(ores.get(0));
								}
								else if ((ores.size() > 1) && dictionaryFreedom) {
									addDrop(ores.get(0));
								}
								// else size is > 1 , so its something like
								// wooden planks
								// TODO:maybe with a config file or something,
								// but not for now
							}
							else if (maybeOres instanceof ItemStack)// <ItemStack>
							{
								addDrop((ItemStack) maybeOres);
							}
						}
					}
					break;
				}
			}
			else if (next instanceof ShapelessOreRecipe) {
				ShapelessOreRecipe r = (ShapelessOreRecipe) next;

				if (r.getRecipeOutput().isItemEqual(toUncraft)) {
					outsize = r.getRecipeOutput().stackSize;

					if (toUncraft.stackSize >= outsize) {
						for (i = 0; i < r.getInput().size(); i++) {
							maybeOres = r.getInput().get(i);

							if (maybeOres instanceof List<?> && (List<ItemStack>) maybeOres != null)// <ItemStack>
							{
								List<ItemStack> ores = (List<ItemStack>) maybeOres;

								if (ores.size() == 1) {
									addDrop(ores.get(0));
									// sticks,iron,and so on
								}
								else if ((ores.size() > 1) && dictionaryFreedom) {
									addDrop(ores.get(0));
								}
								// else size is > 1 , so its something like
								// wooden planks
								// TODO:maybe with a config file or something,
								// but not for now
							}
							if (maybeOres instanceof ItemStack)// <ItemStack>
							{
								addDrop((ItemStack) maybeOres);
							}
						}
					}
					break;
				}
			}
			else if (next instanceof ShapedRecipes) {
				ShapedRecipes r = (ShapedRecipes) next;

				if (r.getRecipeOutput().isItemEqual(toUncraft)) {
					outsize = r.getRecipeOutput().stackSize;

					if (toUncraft.stackSize >= outsize) {
						for (i = 0; i < r.recipeItems.length; i++) {
							addDrop(r.recipeItems[i]);
						}
					}
					break;
				}
			}
			else if (next instanceof ShapelessRecipes) {
				ShapelessRecipes r = (ShapelessRecipes) next;

				if (r.getRecipeOutput().isItemEqual(toUncraft)) {
					outsize = r.getRecipeOutput().stackSize;

					if (toUncraft.stackSize >= outsize) {
						for (i = 0; i < r.recipeItems.size(); i++) {
							addDrop((ItemStack) r.recipeItems.get(i));
						}
					}
					break;
				}
			}
		}

		return (this.drops.size() > 0);
	}

	public boolean canUncraft() {
		// TODO make this actually different and more efficient?
		// ex we dont need drops and such
		return this.doUncraft();
	}
}
