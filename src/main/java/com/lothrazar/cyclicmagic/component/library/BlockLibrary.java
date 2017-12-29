package com.lothrazar.cyclicmagic.component.library;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class BlockLibrary extends BlockBaseHasTile implements IBlockHasTESR, IHasRecipe {
  public BlockLibrary() {
    super(Material.WOOD);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityLibrary();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    //hit Y is always vertical. horizontal is either X or Z, and sometimes is inverted
    TileEntityLibrary library = (TileEntityLibrary) world.getTileEntity(pos);
    QuadrantEnum segment = QuadrantEnum.getForFace(side, hitX, hitY, hitZ);
    if (segment == null) {
      return false;//literal edge case
    }
    library.setLastClicked(segment);
    ItemStack playerHeld = player.getHeldItem(hand);
   // Enchantment enchToRemove = null;
    if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK)) {
      if (library.addEnchantmentFromPlayer(player, hand, segment)) {
        onSuccess(player);
        return true;
      }
    }
    else if (playerHeld.getItem().equals(Items.BOOK)
        && player.getCooldownTracker().hasCooldown(Items.BOOK) == false) {
      EnchantStack es = library.getEnchantStack(segment);
      if (es.isEmpty() == false) {
        this.dropEnchantedBookOnPlayer(es, player, pos);
        playerHeld.shrink(1);
        library.removeEnchantment(segment);
        onSuccess(player);
        return true;
      }
    }
    //display information about whats inside ??maybe?? if sneaking
    else if (player.isSneaking() == false) {
      EnchantStack es = library.getEnchantStack(segment);
      UtilChat.sendStatusMessage(player, es.toString());
      return true;
    }
    return false;//so you can still sneak with books or whatever
  }
  private void onSuccess(EntityPlayer player) {
    UtilSound.playSound(player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE);
  }
  private void dropEnchantedBookOnPlayer(EnchantStack ench, EntityPlayer player, BlockPos pos) {
    ItemStack stack = ench.makeEnchantedBook();
    //    new ItemStack(Items.ENCHANTED_BOOK);
    //    Map<Enchantment, Integer> enchMap = new HashMap<Enchantment, Integer>();
    //    enchMap.put(ench.getEnch(), ench.getLevel());
    //    EnchantmentHelper.setEnchantments(enchMap, stack);
    if (player.addItemStackToInventory(stack) == false) {
      //drop if player is full
      player.dropItem(stack, true);
    }
  }
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLibrary.class, new LibraryTESR<TileEntityLibrary>(this));
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 2),
        " r ",
        "sgs",
        " r ",
        'g', "chestEnder",
        's', Blocks.PURPUR_BLOCK,
        'r', Blocks.BOOKSHELF);
  }
}
