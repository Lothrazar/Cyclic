package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPlacer extends BlockBaseFacingInventory implements IHasRecipe {
  public BlockPlacer() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_PLACER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachinePlacer("tile.placer_block.name");
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
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "rsr",
        "gbg",
        "ooo",
        'o', Blocks.COBBLESTONE,
        'g', Items.IRON_INGOT,
        's', Blocks.DISPENSER,
        'r', Blocks.STONE,
        'b', Items.REDSTONE);
  }
  //  @SideOnly(Side.CLIENT)
  //  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
  //    tooltip.add(UtilChat.lang("tile.placer_block.tooltip"));
  //  }
}
