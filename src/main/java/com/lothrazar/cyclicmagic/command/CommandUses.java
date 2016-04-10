package com.lothrazar.cyclicmagic.command;

import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommandUses extends BaseCommand implements ICommand {
	public CommandUses(String n, boolean op) {

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

			ItemStack[] ingred = CommandRecipe.getRecipeInput(recipe);

			for (ItemStack is : ingred) {
				if (is != null && held.getItem() == is.getItem() && held.getMetadata() == is.getMetadata()) {
					UtilChat.addChatMessage(player, recipeResult.getDisplayName());

					foundSomething = true;
					// break only the inner loop, keep looking for other recipes
					break;
				}
			}
		}// end loop

		if (foundSomething == false) {
			UtilChat.addChatMessage(player, "command.recipes.notfound");
		}
	}
}
