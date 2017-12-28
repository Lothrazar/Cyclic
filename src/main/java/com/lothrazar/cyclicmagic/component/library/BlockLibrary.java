package com.lothrazar.cyclicmagic.component.library;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.component.miner.TileEntityBlockMiner;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockLibrary extends BlockBaseHasTile implements IBlockHasTESR{
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
          player.getCooldownTracker().setCooldown(Items.BOOK, 50);
          library.markDirty();
          onSuccess(player);
          return true;
        }
      }
    }
    else if (playerHeld.getItem().equals(Items.BOOK)
        && player.getCooldownTracker().hasCooldown(Items.BOOK) == false) {
 
      EnchantStack es = library.getEnchantStack(segment);
      if (es.isEmpty() == false) {
        Map<Enchantment, Integer> enchMap = new HashMap<Enchantment, Integer>();
        enchMap.put(es.getEnch(), es.getLevel());
        this.dropEnchantmentInWorld(es.getEnch(), player, pos);
        playerHeld.shrink(1);
        library.removeEnchantment(segment);
        onSuccess(player);
        return true;
      }
    }
    else if (playerHeld.isEmpty()) {
      //display information about whats inside if sneaking
      EnchantStack es = library.getEnchantStack(segment);
      UtilChat.sendStatusMessage(player, es.toString());
      return true;
      //otherwise withdraw?
    }
    //eventually we are doing a withdraw/deposit of an ench
    // dropEnchantmentInWorld(ench, world, pos);
    return false;
  }
  private void onSuccess(EntityPlayer player) {
    UtilSound.playSound(player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE);
  }
  private void dropEnchantmentInWorld(Enchantment ench, EntityPlayer player, BlockPos pos) {
    ItemStack stack = new ItemStack(Items.ENCHANTED_BOOK);
    Map<Enchantment, Integer> enchMap = new HashMap<Enchantment, Integer>();
    enchMap.put(ench, 1);
    EnchantmentHelper.setEnchantments(enchMap, stack);
    player.dropItem(stack, true);
    //    UtilItemStack.dropItemStackInWorld(world, pos, stack);
  }  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLibrary.class, new LibraryTESR<TileEntityLibrary>(this));
 
  }
}
