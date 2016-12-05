package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineBlockMiner;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMiner extends BlockBaseFacingInventory implements IHasRecipe {
  public static final PropertyDirection PROPERTYFACING = BlockBaseFacing.PROPERTYFACING;
  public static enum MinerType {
    SINGLE, TUNNEL
  }
  private MinerType minerType;
  public BlockMiner(MinerType t) {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_BLOCKMINER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    minerType = t;
  }
  public MinerType getMinerType() {
    return minerType;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineBlockMiner();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    ((TileMachineBlockMiner) worldIn.getTileEntity(pos)).breakBlock(worldIn, pos, state);
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public void addRecipe() {
    switch (minerType) {
    case SINGLE:
      GameRegistry.addRecipe(new ItemStack(this),
          "rsr",
          "gbg",
          "ooo",
          'o', Blocks.MOSSY_COBBLESTONE,
          'g', Items.IRON_PICKAXE, // new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE),
          's', Blocks.DISPENSER,
          'r', Items.QUARTZ,
          'b', Items.BLAZE_POWDER);
      break;
    case TUNNEL:
      GameRegistry.addRecipe(new ItemStack(this),
          "rsr",
          "gbg",
          "ooo",
          'o', Blocks.OBSIDIAN,
          'g', Items.IRON_PICKAXE, // new ItemStack(Items.DIAMOND_PICKAXE,1,OreDictionary.WILDCARD_VALUE),
          's', Blocks.DISPENSER,
          'r', Items.QUARTZ,
          'b', Blocks.field_189877_df);// MAGMA BLOCK is field_189877_df in 1.10.2 apparently
      break;
    default:
      break;
    }
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    if (this.minerType == MinerType.SINGLE)
      tooltip.add(UtilChat.lang("tile.block_miner.tooltip"));
    else if (this.minerType == MinerType.TUNNEL)
      tooltip.add(UtilChat.lang("tile.block_miner_tunnel.tooltip"));
  }
}
