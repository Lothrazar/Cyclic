package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder;
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

public class BlockStructureBuilder extends BlockBaseFacingInventory implements IHasRecipe {
  // dont use blockContainer !!
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockStructureBuilder() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_BUILDER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
    this.setTooltip("tile.builder_block.tooltip");
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineStructureBuilder();//"tile.builder_block.name"
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
        'o', Blocks.OBSIDIAN,
        'g', Items.GHAST_TEAR,
        's', Blocks.DISPENSER,
        'r', Blocks.REDSTONE_BLOCK,
        'b', Blocks.DIAMOND_BLOCK);
  }
}
