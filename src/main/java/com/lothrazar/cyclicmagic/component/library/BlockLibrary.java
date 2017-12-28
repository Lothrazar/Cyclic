package com.lothrazar.cyclicmagic.component.library;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile; 
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
    QuadrantEnum segment = QuadrantEnum.getForFace(side, hitX, hitY, hitZ);
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
