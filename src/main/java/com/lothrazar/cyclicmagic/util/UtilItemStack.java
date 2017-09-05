package com.lothrazar.cyclicmagic.util;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UtilItemStack {
  /**
   * match item, damage, and NBT
   * 
   * @param chestItem
   * @param bagItem
   * @return
   */
  public static boolean canMerge(ItemStack chestItem, ItemStack bagItem) {
    if (chestItem.isEmpty() || bagItem.isEmpty()) { return false; }
    return (bagItem.getItem().equals(chestItem.getItem())
        && bagItem.getItemDamage() == chestItem.getItemDamage()
        && ItemStack.areItemStackTagsEqual(bagItem, chestItem));
  }
  public static int mergeItemsBetweenStacks(ItemStack takeFrom, ItemStack moveTo) {
    int room = moveTo.getMaxStackSize() - moveTo.getCount();
    int moveover = 0;
    if (room > 0) {
      moveover = Math.min(takeFrom.getCount(), room);
      //      moveTo.stackSize += moveover;
      //      takeFrom.stackSize -= moveover;
      moveTo.grow(moveover);
      takeFrom.shrink(moveover);
    }
    return moveover;
  }
  public static boolean isEmpty(ItemStack is) {
    return is == null || is.isEmpty() || is == ItemStack.EMPTY;
  }
  public static int getMaxDmgFraction(Item tool, int d) {
    return tool.getMaxDamage() - (int) MathHelper.floor(tool.getMaxDamage() / d);
  }
  public static void damageItem(EntityLivingBase p, ItemStack s) {
    if (p instanceof EntityPlayer) {
      damageItem((EntityPlayer) p, s);
    }
    else {
      s.damageItem(1, p);
    }
  }
  public static void damageItem(EntityPlayer p, ItemStack s) {
    damageItem(p, s, 1);
  }
  public static void damageItem(EntityPlayer p, ItemStack s, int num) {
    if (p.capabilities.isCreativeMode == false && p.world.isRemote == false) {
      s.damageItem(num, p);
    }
  }
  public static String getRawName(Item item) {
    return item.getUnlocalizedName().replaceAll("item.", "");
  }
  /**
   * Created becuase getStateFromMeta is deprecated, and its used everywhere so
   * tons of warnings, and i have no idea how simple/complex the solution will
   * be
   * 
   * @param b
   * @param meta
   * @return
   */
  public static IBlockState getStateFromMeta(Block b, int meta) {
    return b.getStateFromMeta(meta);
  }
  /**
   * created to wrap up deprecated calls
   * 
   * @param b
   * @param state
   * @param player
   * @param worldIn
   * @param pos
   * @return
   */
  @SuppressWarnings("deprecation")
  public static float getPlayerRelativeBlockHardness(Block b, IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
    return b.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
  }
  @SuppressWarnings("deprecation")
  public static float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
    //no way the forge hooks one has a stupid thing where <0 returns 0
    return state.getBlock().getBlockHardness(state, worldIn, pos);
    //    return b.getPlayerRelativeBlockHardness(state, player, worldIn, pos);
  }
  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Block block) {
    return dropItemStackInWorld(worldObj, pos, new ItemStack(block));
  }
  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, Item item) {
    return dropItemStackInWorld(worldObj, pos, new ItemStack(item));
  }
  public static EntityItem dropItemStackInWorld(World worldObj, BlockPos pos, ItemStack stack) {
    EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
    if (worldObj.isRemote == false) {
      // do not spawn a second 'ghost' one onclient side
      worldObj.spawnEntity(entityItem);
    }
    return entityItem;
  }
  public static void dropItemStacksInWorld(World world, BlockPos pos, List<ItemStack> stacks) {
    for (ItemStack s : stacks) {
      UtilItemStack.dropItemStackInWorld(world, pos, s);
    }
  }
  public static @Nonnull String getStringForItemStack(ItemStack itemStack) {
    Item item = itemStack.getItem();
    return item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath() + "/" + itemStack.getMetadata();
  }
  public static String getStringForItem(Item item) {
    if (item == null || item.getRegistryName() == null) { return ""; }
    return item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
  }
  public static String getStringForBlock(Block b) {
    return b.getRegistryName().getResourceDomain() + ":" + b.getRegistryName().getResourcePath();
  }
  public static void dropBlockState(World world, BlockPos position, IBlockState current) {
    if (world.isRemote == false && current.getBlock() != Blocks.AIR) {
      dropItemStackInWorld(world, position, getSilkTouchDrop(current));
    }
  }
  public static IBlockState getStateFromStack(ItemStack stack) {
    Block stuff = Block.getBlockFromItem(stack.getItem());
    return UtilItemStack.getStateFromMeta(stuff, stack.getMetadata());
  }
  //stupid Block class has this not public
  public static ItemStack getSilkTouchDrop(IBlockState state) {
    Item item = Item.getItemFromBlock(state.getBlock());
    int i = 0;
    if (item.getHasSubtypes()) {
      i = state.getBlock().getMetaFromState(state);
    }
    return new ItemStack(item, 1, i);
  }
  /**
   * STOLEN from ItemGlassBottle which is for osme reason protected
   * 
   * @param is
   * @param player
   * @param stack
   * @return
   */
  //  public static ItemStack turnBottleIntoItem(ItemStack is, EntityPlayer player, ItemStack stack)
  //  {
  //      is.shrink(1);
  //      player.addStat(StatList.getObjectUseStats(is.getItem()));
  //
  //      if (is.isEmpty())
  //      {
  //          return stack;
  //      }
  //      else
  //      {
  //          if (!player.inventory.addItemStackToInventory(stack))
  //          {
  //              player.dropItem(stack, false);
  //          }
  //
  //          return is;
  //      }
  //  }
}
