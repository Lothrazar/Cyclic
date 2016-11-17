package com.lothrazar.cyclicmagic.util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UtilItem {
  /**
   * match item, damage, and NBT
   * 
   * @param chestItem
   * @param bagItem
   * @return
   */
  public static boolean canMerge(ItemStack chestItem, ItemStack bagItem) {
    if (chestItem == null || bagItem == null) { return false; }
    return (bagItem.getItem().equals(chestItem.getItem())
        && bagItem.getItemDamage() == chestItem.getItemDamage()
        && ItemStack.areItemStackTagsEqual(bagItem, chestItem));
  }
  public static int getMaxDmgFraction(Item tool, int d) {
    return tool.getMaxDamage() - (int) MathHelper.floor_double(tool.getMaxDamage() / d);
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
    if (p.capabilities.isCreativeMode == false) {
      s.damageItem(1, p);
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
  @SuppressWarnings("deprecation")
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
}
