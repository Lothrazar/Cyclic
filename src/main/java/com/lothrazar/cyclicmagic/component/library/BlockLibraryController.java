package com.lothrazar.cyclicmagic.component.library;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLibraryController extends BlockBase implements IHasRecipe {
  private static final int RANGE = 4;
  Block libraryInstance;
  public BlockLibraryController(Block lib) {
    super(Material.WOOD);
    libraryInstance = lib;
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    List<BlockPos> connectors = UtilWorld.getMatchingInRange(world, pos, libraryInstance, RANGE);
    TileEntity te;
    TileEntityLibrary lib;
    ItemStack playerHeld = player.getHeldItem(hand);
    if (playerHeld.getItem().equals(Items.ENCHANTED_BOOK) == false) {
      return false;
    }
    for (BlockPos p : connectors) {
      te = world.getTileEntity(p);
      if (te instanceof TileEntityLibrary) {
        lib = (TileEntityLibrary) te;
        QuadrantEnum quad = lib.findMatchingQuadrant(playerHeld);
        if (quad == null) {
          quad = lib.findEmptyQuadrant();
        }
        if (quad != null) {
          //now try insert here 
          if (lib.addEnchantmentFromPlayer(player, hand, quad)) {
            return true;
          }
        }
      }
    }
    // UtilChat.sendStatusMessage(player,UtilChat.lang("enchantment_stack.empty"));
    return false;
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " r ",
        "rgr",
        " r ",
        'g', "chestEnder",
        'r', libraryInstance);
  }
}
