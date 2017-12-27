package com.lothrazar.cyclicmagic.component.library;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLibrary extends BlockBase {
  private static final float HALF = 0.5F;
  public static enum Segments {
    TR, TL, BR, BL;
    public static Segments getFor(float hitHoriz, float hitVertic) {
      if (hitHoriz > HALF && hitVertic > HALF) {
        return TL;
      }
      else if (hitHoriz <= HALF && hitVertic > HALF) {
        return TR;
      }
      else if (hitHoriz > HALF && hitVertic < HALF) {
        return BL;
      }
      else {
        return BR;
      }
    }
  }
  public BlockLibrary() {
    super(Material.WOOD);
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ModCyclic.logger.log(side.name() + "  ! ::   " + hitX + ",  " + hitY + ",  " + hitZ);
    //hit Y is always vertical. horizontal is either X or Z, and sometimes is inverted
    Segments segment = null;
    switch (side) {
      case EAST:
        segment = Segments.getFor(hitZ, hitY);
      break;
      case NORTH:// perfect
        segment = Segments.getFor(hitX, hitY);
      break;
      case SOUTH:
        segment = Segments.getFor(1 - hitX, hitY);
      break;
      case WEST:
        segment = Segments.getFor(1 - hitZ, hitY);
      break;
      case UP:
      case DOWN:
        return false;
    }
    ModCyclic.logger.log(segment.name());
    //eventually we are doing a withdraw/deposit of an ench
    // dropEnchantmentInWorld(ench, world, pos);
    return true;
  }
  private void dropEnchantmentInWorld(Enchantment ench, World world, BlockPos pos) {
    ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
    Map<Enchantment, Integer> enchMap = new HashMap<Enchantment, Integer>();
    enchMap.put(ench, 1);
    EnchantmentHelper.setEnchantments(enchMap, stack);
    UtilItemStack.dropItemStackInWorld(world, pos, stack);
  }
}
