package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.waypoints.GuiEnderBook;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilExperience;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemEnderBook extends Item implements IHasRecipe {
	public static String	KEY_LOC			= "location";
	public static String	KEY_LARGEST	= "loc_largest";

	public ItemEnderBook() {
		super();
		this.setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabTransport);
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
	
	private static int getLocationsCount(ItemStack itemStack){
		return getLocations(itemStack).size();
	}

  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
  {
  	tooltip.add(""+ getLocationsCount(stack));
  }

	public static int getLargestSlot(ItemStack itemStack) {
		
		return UtilNBT.getTagCompoundNotNull(itemStack).getInteger(KEY_LARGEST);
	}

	public static int getEmptySlotAndIncrement(ItemStack itemStack) {
		int empty = UtilNBT.getTagCompoundNotNull(itemStack).getInteger(KEY_LARGEST);

		if (empty == 0) {
			empty = 1;
		}// first index is 1 not zero

		UtilNBT.getTagCompoundNotNull(itemStack).setInteger(KEY_LARGEST, empty + 1);// save the
		                                                              // next empty
		                                                              // one
		return empty;
	}

	private static ItemStack getPlayersBook(EntityPlayer player) {

		ItemStack book = player.getHeldItem(EnumHand.MAIN_HAND);
		if (book == null || book.getItem() != ItemRegistry.book_ender) {
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

		if (csv == null || csv.isEmpty()) { return null; }

		return new BookLocation(csv);
	}

	public static void teleport(EntityPlayer player, int slot)// ItemStack
	                                                          // enderBookInstance
	{
		ItemStack book = getPlayersBook(player);

		String csv = book.getTagCompound().getString(ItemEnderBook.KEY_LOC + "_" + slot);

		if (csv == null || csv.isEmpty()) { return; }

		BookLocation loc = getLocation(book, slot);
		if (player.dimension != loc.dimension) { return; }

		// then drain
		int cost = (int) ItemRegistry.expCostPerTeleport;
		UtilExperience.drainExp(player, cost);
		// play twice on purpose. at old and new locations

		UtilSound.playSound(player, SoundEvents.item_chorus_fruit_teleport, SoundCategory.PLAYERS);

		if (player instanceof EntityPlayerMP) {
			// thanks so much to
			// http://www.minecraftforge.net/forum/index.php?topic=18308.0
			EntityPlayerMP p = ((EntityPlayerMP) player);
			float f = 0.5F;// center the player on the block. also moving up so not
			               // stuck in floor
			p.playerNetServerHandler.setPlayerLocation(loc.X - f, loc.Y + 0.9, loc.Z - f, p.rotationYaw, p.rotationPitch);
			BlockPos dest = new BlockPos(loc.X, loc.Y, loc.Z);
			// try and force chunk loading

			player.worldObj.getChunkFromBlockCoords(dest).setChunkModified();// .markChunkDirty(dest,
			                                                                 // null);
			/*
			 * //player.worldObj.markBlockForUpdate(dest);
			 * if(MinecraftServer.getServer().worldServers.length > 0)
			 * {
			 * WorldServer s = MinecraftServer.getServer().worldServers[0];
			 * if(s != null)
			 * {
			 * s.theChunkProviderServer.chunkLoadOverride = true;
			 * s.theChunkProviderServer.loadChunk(dest.getX(),dest.getZ());
			 * }
			 * }
			 */
		}

		UtilSound.playSound(player, SoundEvents.item_chorus_fruit_teleport, SoundCategory.PLAYERS);
	}

	public void addRecipe() {

		if (ItemRegistry.craftNetherStar)
			GameRegistry.addRecipe(new ItemStack(this), "ene", "ebe", "eee", 'e', Items.ender_pearl, 'b', Items.book, 'n', Items.nether_star);
		else
			GameRegistry.addRecipe(new ItemStack(this), "eee", "ebe", "eee", 'e', Items.ender_pearl, 'b', Items.book);

		// if you want to clean out the book and start over
		GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer entityPlayer, EnumHand hand) {
		if (stack == null || stack.getItem() == null) { return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack); }

		Minecraft.getMinecraft().displayGuiScreen(new GuiEnderBook(entityPlayer, stack));

		return super.onItemRightClick(stack, world, entityPlayer, hand);
	}

	public static class BookLocation {
		public double	X;
		public double	Y;
		public double	Z;
		public int		id;
		public int		dimension;
		public String	display;

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
			return Math.round(X) + ", " + Math.round(Y) + ", " + Math.round(Z);	// +
			                                                                   	// showName
		}
	}
}
