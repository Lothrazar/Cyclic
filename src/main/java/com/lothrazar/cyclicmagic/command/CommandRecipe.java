package com.lothrazar.cyclicmagic.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class CommandRecipe extends BaseCommand implements ICommand {
	public CommandRecipe(String n, boolean op) {

		super(n, op);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		if (!(sender instanceof EntityPlayer)) { return; }// does not work from
		                                                  // command blocks and such

		EntityPlayer player = (EntityPlayer) sender;
		ItemStack held = player.inventory.getCurrentItem();

		if (held == null && world.isRemote) {
			UtilChat.addChatMessage(player, "command.recipes.empty");
			return;
		}

		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();

		ItemStack recipeResult;

		boolean foundSomething = false;

		for (IRecipe recipe : recipes) {
			recipeResult = recipe.getRecipeOutput();

			// compare ignoring stack size. not null, and the same item
			if (recipeResult == null || recipeResult.getItem() == null) {
				continue;
			}
			if (held.getItem() != recipeResult.getItem()) {
				continue;
			}
			if (held.getMetadata() != recipeResult.getMetadata()) {
				continue;
			}

			// TODO seperator btw recipes: one item can have multiple.

			// for each recipe, we need an array/list of the input, then we pass it
			// off to get printed
			// recipe is either shaped or shapeless
			// on top of that, some use Forge ore dictionary, and some dont
			// so 4 cases total

			// TODO: refactor and use ItemStack[] ingred =
			// Util.getRecipeInput(recipe);
			// to save reuse

			if (recipe instanceof ShapedRecipes) {
				ShapedRecipes r = ((ShapedRecipes) recipe);
				boolean isInventory = (r.recipeHeight < 3 || r.recipeWidth < 3);

				// System.out.println(isInventory+"isInventory is from :
				// "+r.recipeHeight+" "+r.recipeWidth);

				UtilChat.addChatMessage(player, "command.recipes.found");
				addChatShapedRecipe(player, getRecipeInput(recipe), isInventory);
				foundSomething = true;
			}
			else if (recipe instanceof ShapedOreRecipe) // it uses forge ore
			                                            // dictionary
			{
				ShapedOreRecipe r = (ShapedOreRecipe) recipe;

				ItemStack[] recipeItems = getRecipeInput(recipe);

				// only because r.width//r.height is private
				boolean isInventory = false;
				int sum = 0;
				for (Field f : ShapedOreRecipe.class.getDeclaredFields()) {
					f.setAccessible(true);
					// works since we know that the only integers in the class are the
					// width/height
					if (f.getType() == int.class) {
						try {
							sum += f.getInt(r);
							// if either one is > 2, then its in 3x3 grid so isInventory ==
							// false at end
							// isInventory = isInventory || (f.getInt(r) == 2);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
				sum -= 6;// becuase it has four numbers, the current height/width and
				         // the max height/width
				// max + max is 6, and are both private. so if the remainder is 4 =
				// height + width; then it was inventory crafting

				isInventory = (sum == 4);

				UtilChat.addChatMessage(player, "command.recipes.found");
				addChatShapedRecipe(player, recipeItems, isInventory);
				foundSomething = true;
			}
			else if (recipe instanceof ShapelessRecipes || recipe instanceof ShapelessOreRecipe) {
				UtilChat.addChatMessage(player, "command.recipes.found");
				addChatShapelessRecipe(player, getRecipeInput(recipe));
				foundSomething = true;
			}
			else {
				// TODO: furnace?
				// TODO: brewing stand?

				// for example, if its from some special crafting block/furnace from
				// another mod
				// Util.addChatMessage(player, "Recipe type not supported, class = " +
				// recipe.getClass().getName());

			}
		}// end main recipe loop

		if (foundSomething == false) {
			UtilChat.addChatMessage(player, "command.recipes.notfound");
		}
	}

	@SuppressWarnings("unchecked")
	public static ItemStack[] getRecipeInput(IRecipe recipe) {
		System.out.println("TODO: UtilRecipe");
		ItemStack[] recipeItems = null;
		if (recipe instanceof ShapedRecipes) {
			ShapedRecipes r = ((ShapedRecipes) recipe);
			recipeItems = r.recipeItems;
		}
		else if (recipe instanceof ShapedOreRecipe) {
			ShapedOreRecipe r = (ShapedOreRecipe) recipe;

			recipeItems = new ItemStack[r.getInput().length];

			for (int i = 0; i < r.getInput().length; i++) {
				Object o = r.getInput()[i];
				if (o == null) {
					continue;
				}

				if (o instanceof ItemStack) {
					recipeItems[i] = (ItemStack) o;
					// System.out.println(i+" -- "+recipeItems[i].getDisplayName());
				}
				else {
					List<ItemStack> c = (List<ItemStack>) o;

					if (c != null && c.size() > 0) {
						recipeItems[i] = c.get(0);
						// System.out.println(i+" -- "+recipeItems[i].getDisplayName());
					}
				}
			}

			/// so after 1,3,5 we add skips
			boolean doorShape = r.getInput().length == 6;// is a 2x3, with right hand
			                                             // column missing

			if (doorShape) {
				// System.out.println("doorShape:: length "+recipeItems.length);
				ItemStack[] backup = recipeItems;
				recipeItems = new ItemStack[9];
				int iold;
				for (int inew = 0; inew < recipeItems.length; inew++) {
					if (inew == 2 || inew == 5 || inew == 8)
						recipeItems[inew] = null;
					else {
						iold = inew;
						if (inew > 5)
							iold = inew - 2;
						else if (inew > 2)
							iold = inew - 1;
						recipeItems[inew] = backup[iold];
					}
				}
			}

		}
		else if (recipe instanceof ShapelessRecipes) {
			ShapelessRecipes r = (ShapelessRecipes) recipe;

			recipeItems = new ItemStack[r.recipeItems.size()];

			for (int i = 0; i < r.recipeItems.size(); i++) {
				Object o = r.recipeItems.get(i);
				if (o != null && o instanceof ItemStack) {
					recipeItems[i] = (ItemStack) o;
				}
			}
		}
		else if (recipe instanceof ShapelessOreRecipe) {
			ShapelessOreRecipe r = (ShapelessOreRecipe) recipe;

			recipeItems = new ItemStack[r.getInput().size()];

			for (int i = 0; i < r.getInput().size(); i++) {
				Object o = r.getInput().get(i);
				if (o == null) {
					continue;
				}
				if (o instanceof ItemStack) {
					recipeItems[i] = (ItemStack) o;
				}
				else {
					List<ItemStack> c = (List<ItemStack>) o;

					if (c != null && c.size() > 0) {
						recipeItems[i] = c.get(0);
					}
				}
			}
		}

		return recipeItems;
	}

	public static void addChatShapelessRecipe(EntityPlayer player, ItemStack[] recipeItems) {
		for (int i = 0; i < recipeItems.length; i++) {
			ItemStack is = recipeItems[i];

			// list.add(is.getDisplayName());
			UtilChat.addChatMessage(player, " - " + is.getDisplayName());

		}
		// TODO: cleanup/make ncer,etc
		// Util.addChatMessage(player, "SHAPELESS " +String.join(" + ", list));
	}

	public static void addChatShapedRecipe(EntityPlayer player, ItemStack[] recipeItems, boolean isInventory) {
		// System.out.println("addChatShapedRecipe "+isInventory
		// +":"+recipeItems.length);
		int size;

		// needed only becuase MC forge stores as a flat array not a 2D
		if (isInventory)
			size = 4;
		else
			size = 9;
		if (recipeItems.length > size)
			size = 9;// in case my flag is false
		String[] grid = new String[size];
		for (int i = 0; i < grid.length; i++) {
			grid[i] = "- ";
		}

		// System.out.println("size "+size);
		// System.out.println("recipeItems.len "+recipeItems.length);

		// now
		// todo:
		// 1,2,3 iron
		// 5 boat
		// 4,6 piston
		// 7,8,9 apple
		Map<String, String> namenumbers = new HashMap<String, String>();

		String name;
		// int j = 0;
		for (int i = 0; i < size; i++) {
			if (i < recipeItems.length && recipeItems[i] != null) {
				name = recipeItems[i].getDisplayName();

				if (namenumbers.containsKey(name)) {
					namenumbers.put(name, namenumbers.get(name) + ", " + i);
				}
				else {
					namenumbers.put(name, "" + i);
				}

				// j++;

				// ModCommands.addChatMessage(player, i + " : " +
				// recipeItems[i].getDisplayName());
				if (i < grid.length)
					grid[i] = i + " ";
			}
		}

		if (isInventory) {
			UtilChat.addChatMessage(player, grid[0] + grid[1]);
			UtilChat.addChatMessage(player, grid[2] + grid[3]);
		}
		else {
			UtilChat.addChatMessage(player, grid[0] + grid[1] + grid[2]);
			UtilChat.addChatMessage(player, grid[3] + grid[4] + grid[5]);
			UtilChat.addChatMessage(player, grid[6] + grid[7] + grid[8]);
		}
		for (Map.Entry<String, String> entry : namenumbers.entrySet()) {
			String item = entry.getKey();
			String nums = entry.getValue();

			UtilChat.addChatMessage(player, item + " : " + nums);
		}
	}
}
