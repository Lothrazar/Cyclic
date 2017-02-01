package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.MachineTESR;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStructureBuilder extends BlockBaseFacingInventory implements IHasRecipe, IBlockHasTESR {
  // dont use blockContainer !!
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockStructureBuilder() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_BUILDER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
    this.setTooltip("tile.builder_block.tooltip");
  }
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileMachineStructureBuilder.class, new MachineTESR(this.getUnlocalizedName(), 0));
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
