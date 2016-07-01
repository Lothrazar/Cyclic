package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

public class UtilNBT {
  public static String posToStringCSV(BlockPos position) {
    return position.getX() + "," + position.getY() + "," + position.getZ();
  }
  public static void setItemStackNBTVal(ItemStack item, String prop, int value) {
    if (item.getTagCompound() == null) {
      item.setTagCompound(new NBTTagCompound());
    }
    item.getTagCompound().setInteger(prop, value);
  }
  public static int getItemStackNBTVal(ItemStack held, String prop) {
    NBTTagCompound tags = getItemStackNBT(held);
    if (!tags.hasKey(prop)) { return 0; }
    return tags.getInteger(prop);
  }
  public static NBTTagCompound getItemStackNBT(ItemStack held) {
    if (held.getTagCompound() == null) {
      held.setTagCompound(new NBTTagCompound());
    }
    return held.getTagCompound();
  }
  public static BlockPos stringCSVToBlockPos(String csv) {
    String[] spl = csv.split(",");
    // on server i got java.lang.ClassCastException: java.lang.String cannot
    // be cast to java.lang.Integer
    // ?? is it from this?
    BlockPos p = null;
    try {
      if (spl != null && spl.length == 3 && spl[0] != "")
        p = new BlockPos(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), Integer.parseInt(spl[2]));
//      else {
//        System.out.println("invalid string: " + csv);
//      }
    }
    catch (java.lang.ClassCastException e) {
      System.out.println("exc: bad string: " + csv);
    }
    return p;
  }
  public static void incrementPlayerIntegerNBT(EntityPlayer player, String prop, int inc) {
    int prev = player.getEntityData().getInteger(prop);
    prev += inc;
    player.getEntityData().setInteger(prop, prev);
  }
  public static void writeTagsToInventory(IInventory invo, NBTTagCompound tags, String key) {
    NBTTagList items = tags.getTagList(key, tags.getId());
    ItemStack stack;
    int slot;
    for (int i = 0; i < items.tagCount(); ++i) {
      // tagAt(int) has changed to getCompoundTagAt(int)
      NBTTagCompound item = items.getCompoundTagAt(i);
      stack = ItemStack.loadItemStackFromNBT(item);
      slot = item.getInteger("slot");
      // list.add(ItemStack.loadItemStackFromNBT(item));
      invo.setInventorySlotContents(slot, stack);
    }
  }
  public static NBTTagCompound writeInventoryToTag(IInventory invo, NBTTagCompound returnTag, String key) {
    ItemStack chestItem;
    NBTTagCompound itemTag;
    NBTTagList nbttaglist = new NBTTagList();
    for (int i = 0; i < invo.getSizeInventory(); i++) {
      // zeroes to avoid nulls, and signify nothing goes there
      chestItem = invo.getStackInSlot(i);
      if (chestItem == null || chestItem.stackSize == 0) {
        continue;
      } // not an error; empty chest slot
      itemTag = chestItem.writeToNBT(new NBTTagCompound());
      itemTag.setInteger("slot", i);
      nbttaglist.appendTag(itemTag);
      // its either in the bag, or dropped on the player
      invo.setInventorySlotContents(i, null);
    }
    returnTag.setTag(key, nbttaglist);
    return returnTag;
  }
  public static NBTTagCompound writeInventoryToNewTag(IInventory invo, String key) {
    return writeInventoryToTag(invo, new NBTTagCompound(), key);
  }
  public static int countItemsFromNBT(NBTTagCompound tags, String key) {
    if(tags == null){return 0;}
    NBTTagList items = tags.getTagList(key, tags.getId());
    if(items == null){return 0;}
    return items.tagCount();
  }
  public static ArrayList<ItemStack> readItemsFromNBT(NBTTagCompound tags, String key) {
    ArrayList<ItemStack> list = new ArrayList<ItemStack>();
    NBTTagList items = tags.getTagList(key, tags.getId());
    for (int i = 0; i < items.tagCount(); ++i) {
      // tagAt(int) has changed to getCompoundTagAt(int)
      NBTTagCompound item = items.getCompoundTagAt(i);
      list.add(ItemStack.loadItemStackFromNBT(item));
    }
    return list;
  }
  public static ItemStack enchantItem(Item item, Enchantment ench, short level) {
    ItemStack stack = new ItemStack(item);
    stack.addEnchantment(ench, level);
    return stack;
  }
  public static ItemStack buildEnchantedBook(Enchantment ench, short level) {
    ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
    //NOT THIS: if you are putting it on a normal stack (sword/weapon) yeah then that way
    //		stack.addEnchantment(ench, level);
    Items.ENCHANTED_BOOK.addEnchantment(stack, new EnchantmentData(ench, level));
    //		System.out.println(stack.getEnchantmentTagList());
    //just to test it
    return stack;
  }
  public static ItemStack buildEnchantedNametag(String customNameTag) {
    // build multi-level NBT tag so it matches a freshly enchanted one
    ItemStack nameTag = new ItemStack(Items.NAME_TAG, 1);
    NBTTagCompound nbt = new NBTTagCompound();
    NBTTagCompound display = new NBTTagCompound();
    display.setString("Name", customNameTag);// NOT "CustomName" implied by
    // commandblocks/google
    nbt.setTag("display", display);
    nbt.setInteger("RepairCost", 1);
    nameTag.setTagCompound(nbt);// put the data into the item stack
    return nameTag;
  }
  public static ItemStack buildNamedPlayerSkull(EntityPlayer player) {
    return buildNamedPlayerSkull(player.getDisplayNameString());
  }
  public static ItemStack buildNamedPlayerSkull(String displayNameString) {
    ItemStack skull = new ItemStack(Items.SKULL, 1, Const.skull_player);
    if (skull.getTagCompound() == null) {
      skull.setTagCompound(new NBTTagCompound());
    }
    skull.getTagCompound().setString(Const.SkullOwner, displayNameString);
    return skull;
  }
  //
  //	public static NBTTagCompound buildPotionTag(Potion potionIn, int level, int duration, String name) {
  //		// REF : http://minecraft.gamepedia.com/Player.dat_format#Potion_Effects
  //		NBTTagCompound tags = new NBTTagCompound();
  //		tags.setString("Potion", name);
  //		NBTTagCompound inner = new NBTTagCompound();
  //		inner.setInteger("Id", Potion.getIdFromPotion(potionIn));
  //		inner.setInteger("Amplifier", level);
  //		inner.setInteger("Duration", duration);
  //		inner.setBoolean("ShowParticles", true);
  //		tags.setTag("CustomPotionEffects", inner);
  //		
  //		return tags;
  //	}
}
