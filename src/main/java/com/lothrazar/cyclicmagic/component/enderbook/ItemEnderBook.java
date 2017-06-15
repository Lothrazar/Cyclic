package com.lothrazar.cyclicmagic.component.enderbook;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderBook extends BaseItem implements IHasRecipe, IHasConfig {
  public static String KEY_LOC = "location";
  public static String KEY_LARGEST = "loc_largest";
  public static int maximumSaved = 16;
  public static int expDistRatio = 10;
  public static final int BTNS_PER_COLUMN = 8;
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
      String csv = UtilNBT.getItemStackNBT(itemStack).getString(KEY);
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
    tooltip.add(UtilChat.lang(getTooltip()) + getLocationsCount(stack));
  }
  public static int getLargestSlot(ItemStack itemStack) {
    return UtilNBT.getItemStackNBT(itemStack).getInteger(KEY_LARGEST);
  }
  public static int getEmptySlotAndIncrement(ItemStack itemStack) {
    int empty = UtilNBT.getItemStackNBT(itemStack).getInteger(KEY_LARGEST);
    if (empty == 0) {
      empty = 1;
    } // first index is 1 not zero
    UtilNBT.getItemStackNBT(itemStack).setInteger(KEY_LARGEST, empty + 1);
    return empty;
  }
  public static ItemStack getPlayersBook(EntityPlayer player) {
    ItemStack book = player.getHeldItem(EnumHand.MAIN_HAND);
    if (book == null || book.getItem() instanceof ItemEnderBook == false) {
      book = player.getHeldItem(EnumHand.OFF_HAND);
    }
    UtilNBT.getItemStackNBT(book);
    return book;
  }
  public static void deleteWaypoint(EntityPlayer player, int slot) {
    ItemStack book = getPlayersBook(player);
    book.getTagCompound().removeTag(KEY_LOC + "_" + slot);
  }
  public static void saveCurrentLocation(EntityPlayer player, String name) {
    ItemStack book = getPlayersBook(player);
    int id = getEmptySlotAndIncrement(book);
    BookLocation loc = new BookLocation(id, player, name);
    book.getTagCompound().setString(KEY_LOC + "_" + id, loc.toCSV());
  }
  private static BookLocation getLocation(ItemStack stack, int slot) {
    String csv = stack.getTagCompound().getString(ItemEnderBook.KEY_LOC + "_" + slot);
    if (csv == null || csv.isEmpty()) { return null; }
    return new BookLocation(csv);
  }
  public static BlockPos getLocationPos(ItemStack stack, int slot) {
    BookLocation loc = getLocation(stack, slot);
    if (loc == null) { return null; }
    return new BlockPos(loc.X, loc.Y, loc.Z);
  }
  public static boolean teleport(EntityPlayer player, int slot) {
    ItemStack book = getPlayersBook(player);
    String csv = book.getTagCompound().getString(ItemEnderBook.KEY_LOC + "_" + slot);
    if (csv == null || csv.isEmpty()) { return false; }
    BookLocation loc = getLocation(book, slot);
    if (player.dimension != loc.dimension) { return false; }
    //		UtilSound.playSound(player, player.getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
    if (player instanceof EntityPlayerMP) {
      // thanks so much to
      // http://www.minecraftforge.net/forum/index.php?topic=18308.0
      EntityPlayerMP p = ((EntityPlayerMP) player);
      float f = 0.5F;// center the player on the block. 
      //also moving up so  not stuck in floor
      p.connection.setPlayerLocation(loc.X - f, loc.Y + 0.9, loc.Z - f, p.rotationYaw, p.rotationPitch);
      BlockPos dest = new BlockPos(loc.X, loc.Y, loc.Z);
      // try and force chunk loading
      player.getEntityWorld().getChunkFromBlockCoords(dest).setChunkModified();
      UtilSound.playSound(player, dest, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
    }
    return true;
  }
  public IRecipe addRecipe() {
    RecipeRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this));
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), "ene", "ebe", "eee",
        'e', Items.ENDER_PEARL,
        'b', Items.BOOK,
        'n', Blocks.EMERALD_BLOCK);
    // if you want to clean out the book and start over
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand) {
    ItemStack stack = entityPlayer.getHeldItem(hand);
    if (stack == null || stack.getItem() == null) { return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack); }
    //Minecraft.getMinecraft().displayGuiScreen(new GuiEnderBook(entityPlayer, stack));
    entityPlayer.openGui(ModCyclic.instance, ForgeGuiHandler.GUI_INDEX_WAYPOINT, world, 0, 0, 0);
    return super.onItemRightClick(world, entityPlayer, hand);
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
    public BlockPos toBlockPos() {
      return new BlockPos(X, Y, Z);
    }
    public String coordsDisplay() {
      // "["+id + "] "+
      return Math.round(X) + ", " + Math.round(Y) + ", " + Math.round(Z); // +
      // showName
    }
  }
  public static int getExpCostPerTeleport(EntityPlayer player, ItemStack book, int slot) {
    if (expDistRatio <= 0) { return 0; }
    BlockPos toPos = getLocationPos(book, slot);
    int distance = (int) UtilWorld.distanceBetweenHorizontal(toPos, player.getPosition());
    return (int) Math.round(distance / expDistRatio);
  }
  @Override
  public void syncConfig(Configuration config) {
    maximumSaved = config.getInt("EnderBookMaxSaved", Const.ConfigCategory.modpackMisc,
        16, 1, 64, "Maximum number of saved waypoints in the ender book.  It still uses " + BTNS_PER_COLUMN +
            " per column, and putting too many may send it offscreen");
    expDistRatio = config.getInt("EnderBookExpCostRatio", Const.ConfigCategory.modpackMisc,
        10, 0, 100, "The exp cost of teleporting is [the horizontal distance] divided by [this number] rounded to the nearest integer.  For example, if this number is 10, then teleporting 20 blocks costs 2 exp");
  }
}
