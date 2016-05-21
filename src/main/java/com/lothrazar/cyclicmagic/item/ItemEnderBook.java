package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.List;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.gui.waypoints.GuiEnderBook;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderBook extends BaseItem implements IHasRecipe, IHasConfig {
	public static String KEY_LOC = "location";
	public static String KEY_LARGEST = "loc_largest";
	public static boolean enabled;
	public static final String name = "book_ender";
	public boolean craftNetherStar;
	public static boolean doesPauseGame;
	public static boolean showCoordTooltips;
	public static int maximumSaved;
	public static int btnsPerColumn;
	public static int expCostPerTeleport;

	public ItemEnderBook() {
		super();
		this.setMaxStackSize(1); 
	}

	public static ArrayList<BookLocation> getLocations(ItemStack itemStack) {
		ArrayList<BookLocation> list = new ArrayList<BookLocation>();

		String KEY;
		int end = getLargestSlot(itemStack);
		for (int i = 0; i <= end; i++) {
			KEY = KEY_LOC + "_" + i;

			String csv = UtilNBT.getTagCompoundNotNull(itemStack).getString(KEY);

			if (csv == null || csv.isEmpty()) {
				continue;
			}

			list.add(new BookLocation(csv));
		}

		return list;
	}

	private static int getLocationsCount(ItemStack itemStack) {
		return getLocations(itemStack).size();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("" + getLocationsCount(stack));
	}

	public static int getLargestSlot(ItemStack itemStack) {

		return UtilNBT.getTagCompoundNotNull(itemStack).getInteger(KEY_LARGEST);
	}

	public static int getEmptySlotAndIncrement(ItemStack itemStack) {
		int empty = UtilNBT.getTagCompoundNotNull(itemStack).getInteger(KEY_LARGEST);

		if (empty == 0) {
			empty = 1;
		} // first index is 1 not zero

		UtilNBT.getTagCompoundNotNull(itemStack).setInteger(KEY_LARGEST, empty + 1);// save
																					// the
		// next empty
		// one
		return empty;
	}

	private static ItemStack getPlayersBook(EntityPlayer player) {

		ItemStack book = player.getHeldItem(EnumHand.MAIN_HAND);
		if (book == null || book.getItem() instanceof ItemEnderBook == false) {
			book = player.getHeldItem(EnumHand.OFF_HAND);
		}

		UtilNBT.getTagCompoundNotNull(book);
		return book;
	}

	public static void deleteWaypoint(EntityPlayer player, int slot) {

		ItemStack book = getPlayersBook(player);
		book.getTagCompound().removeTag(KEY_LOC + "_" + slot);
	}

	public static void saveCurrentLocation(EntityPlayer player, String name) {

		ItemStack book = getPlayersBook(player);

		int id = getEmptySlotAndIncrement(book);// int slot =
												// entityPlayer.inventory.currentItem
												// + 1;

		BookLocation loc = new BookLocation(id, player, name);

		book.getTagCompound().setString(KEY_LOC + "_" + id, loc.toCSV());
	}

	private static BookLocation getLocation(ItemStack stack, int slot) {
		String csv = stack.getTagCompound().getString(ItemEnderBook.KEY_LOC + "_" + slot);

		if (csv == null || csv.isEmpty()) {
			return null;
		}

		return new BookLocation(csv);
	}

	public static void teleport(EntityPlayer player, int slot)// ItemStack
																// enderBookInstance
	{
		ItemStack book = getPlayersBook(player);

		String csv = book.getTagCompound().getString(ItemEnderBook.KEY_LOC + "_" + slot);

		if (csv == null || csv.isEmpty()) {
			return;
		}

		BookLocation loc = getLocation(book, slot);
		if (player.dimension != loc.dimension) {
			return;
		}

		// then drain
		int cost = (int) expCostPerTeleport;
		UtilExperience.drainExp(player, cost);
		// play twice on purpose. at old and new locations

		UtilSound.playSound(player, player.getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);

		if (player instanceof EntityPlayerMP) {
			// thanks so much to
			// http://www.minecraftforge.net/forum/index.php?topic=18308.0
			EntityPlayerMP p = ((EntityPlayerMP) player);
			float f = 0.5F;// center the player on the block. also moving up so
							// not
							// stuck in floor


			//p.playerNetServerHandler
			p.connection.setPlayerLocation(loc.X - f, loc.Y + 0.9, loc.Z - f, p.rotationYaw,p.rotationPitch);
			BlockPos dest = new BlockPos(loc.X, loc.Y, loc.Z);
			// try and force chunk loading

			player.worldObj.getChunkFromBlockCoords(dest).setChunkModified();// .markChunkDirty(dest,
																				// null);
			/*
			 * //player.worldObj.markBlockForUpdate(dest);
			 * if(MinecraftServer.getServer().worldServers.length > 0) {
			 * WorldServer s = MinecraftServer.getServer().worldServers[0]; if(s
			 * != null) { s.theChunkProviderServer.chunkLoadOverride = true;
			 * s.theChunkProviderServer.loadChunk(dest.getX(),dest.getZ()); } }
			 */
		}

		UtilSound.playSound(player, player.getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
	}

	public void addRecipe() {

		if (craftNetherStar)
			GameRegistry.addRecipe(new ItemStack(this), "ene", "ebe", "eee", 'e', Items.ENDER_PEARL, 'b', Items.BOOK,
					'n', Items.NETHER_STAR);
		else
			GameRegistry.addRecipe(new ItemStack(this), "eee", "ebe", "eee", 'e', Items.ENDER_PEARL, 'b', Items.BOOK);

		// if you want to clean out the book and start over
		GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer entityPlayer,
			EnumHand hand) {
		if (stack == null || stack.getItem() == null) {
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
		}

		//Minecraft.getMinecraft().displayGuiScreen(new GuiEnderBook(entityPlayer, stack));
		entityPlayer.openGui(ModMain.instance, ModGuiHandler.GUI_INDEX_WAYPOINT, world, 0, 0, 0);

		return super.onItemRightClick(stack, world, entityPlayer, hand);
	}

	public static class BookLocation {
		public double X;
		public double Y;
		public double Z;
		public int id;
		public int dimension;
		public String display;

		public BookLocation(int idx, EntityPlayer p, String d) {
			X = p.posX;
			Y = p.posY;
			Z = p.posZ;
			id = idx;
			dimension = p.dimension;
			display = d;
		}

		public BookLocation(String csv) {
			String[] pts = csv.split(",");
			id = Integer.parseInt(pts[0]);
			X = Double.parseDouble(pts[1]);
			Y = Double.parseDouble(pts[2]);
			Z = Double.parseDouble(pts[3]);
			dimension = Integer.parseInt(pts[4]);
			if (pts.length > 5)
				display = pts[5];
		}

		public String toCSV() {
			return id + "," + X + "," + Y + "," + Z + "," + dimension + "," + display;
		}

		public String coordsDisplay() {
			// "["+id + "] "+
			return Math.round(X) + ", " + Math.round(Y) + ", " + Math.round(Z); // +
																				// showName
		}
	}

	public void syncConfig(Configuration config) {
		String category;

		category = Const.ConfigCategory.items_enderbook;

		enabled = config.getBoolean("Enabled", category, true, "To disable this ender book item");
		doesPauseGame = config.getBoolean("Gui Pauses Game", category, false,
				"The Ender Book GUI will pause the game (single player)");
		craftNetherStar = config.getBoolean("Recipe Nether Star", category, true,
				"The Ender Book requires a nether star to craft.  REQUIRES RESTART.");
		showCoordTooltips = config.getBoolean("Show Tooltip Coords", category, true,
				"Waypoint buttons will show the exact coordinates in a hover tooltip.");
		maximumSaved = config.getInt("Max Saved", category, 16, 1, 999, "How many waypoints the book can store.");
		btnsPerColumn = config.getInt("Column Size", category, 8, 1, 50,
				"Number of waypoints per column.  Change this if they are going off the screen for your chosen GUI Scale.");
		expCostPerTeleport = config.getInt("Exp Cost", category, 10, 0, 9999,
				"How many experience points are drained from the player on each teleport.  Set to zero for free teleports to your waypoints.");

	}
}
