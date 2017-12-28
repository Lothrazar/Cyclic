package com.lothrazar.cyclicmagic.component.library;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.component.library.TileEntityLibrary.EnchantStack;
import com.lothrazar.cyclicmagic.component.miner.TileEntityBlockMiner;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLibrary extends BlockBaseHasTile {
  private static final float HALF = 0.5F;
  public static enum Quadrant {
    TR, TL, BR, BL;
    /**
     * using (x,y) in [0,1] determine quadrant of block hit
     * 
     * @param hitHoriz
     * @param hitVertic
     * @return
     */
    public static Quadrant getFor(float hitHoriz, float hitVertic) {
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
    /**
     * based on facing side, convert either hitX or hitZ to hitHorizontal relative to player orientation
     * 
     * @param side
     * @param hitX
     * @param hitY
     * @param hitZ
     * @return
     */
    public static Quadrant getForFace(EnumFacing side, float hitX, float hitY, float hitZ) {
      Quadrant segment = null;
      switch (side) {
        case EAST:
          segment = Quadrant.getFor(hitZ, hitY);
        break;
        case NORTH:
          segment = Quadrant.getFor(hitX, hitY);
        break;
        case SOUTH:
          segment = Quadrant.getFor(1 - hitX, hitY);
        break;
        case WEST:
          segment = Quadrant.getFor(1 - hitZ, hitY);
        break;
        case UP:
        case DOWN:
        break;
      }
      return segment;
    }
  }
  public BlockLibrary() {
    super(Material.WOOD);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityLibrary();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // ModCyclic.logger.log(side.name() + "  ! ::   " + hitX + ",  " + hitY + ",  " + hitZ);
    //hit Y is always vertical. horizontal is either X or Z, and sometimes is inverted
    TileEntityLibrary library = (TileEntityLibrary) world.getTileEntity(pos);
    Quadrant segment = Quadrant.getForFace(side, hitX, hitY, hitZ);
    if (segment == null) {
      return false;//literal edge case
    }
    ModCyclic.logger.log(segment.name() + " fromworld " + world.isRemote);
    ItemStack playerHeld = player.getHeldItem(hand);
    Enchantment enchToRemove = null;
    if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK)) {
      Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(playerHeld);
      for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
        if (library.addEnchantment(segment, entry.getKey(), entry.getValue())) {
          enchToRemove = entry.getKey();
          break;
        }
      }
      if (enchToRemove != null) {
        // success
        enchants.remove(enchToRemove);
        EnchantmentHelper.setEnchantments(enchants, playerHeld);
        if (enchants.size() == 0) {
          //TODO: merge shared with TileENtityDisenchanter
          player.setHeldItem(hand, new ItemStack(Items.BOOK));
          library.markDirty();
        }
      }
    }
    else if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK)) {
      EnchantStack es = library.getEnchantStack(segment);
      if (es.isEmpty() == false) {
        this.dropEnchantmentInWorld(es.getEnch(), world, pos);
      }
    }
    else if (playerHeld.isEmpty()) {
      //display information about whats inside if sneaking
      EnchantStack es = library.getEnchantStack(segment);
      UtilChat.sendStatusMessage(player, es.toString());
      //otherwise withdraw?
    }
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
