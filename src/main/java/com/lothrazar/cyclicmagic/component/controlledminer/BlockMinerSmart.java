package com.lothrazar.cyclicmagic.component.controlledminer;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.BlockBaseFacing;
import com.lothrazar.cyclicmagic.block.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.tileentity.MachineTESR;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMinerSmart extends BlockBaseFacingInventory implements IHasRecipe, IBlockHasTESR {
  public static final PropertyDirection PROPERTYFACING = BlockBaseFacing.PROPERTYFACING;
  public BlockMinerSmart() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_SMARTMINER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityControlledMiner();//"tile.block_miner_smart.name"
  }
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityControlledMiner.class, new MachineTESR(this.getUnlocalizedName(), 4));
  }
  @Override
  public IRecipe addRecipe() {
   return GameRegistry.addShapedRecipe(new ItemStack(this),
        "rsr",
        "gbx",
        "ooo",
        'o', Blocks.OBSIDIAN,
        'g', Items.DIAMOND_PICKAXE,
        'x', Items.DIAMOND_AXE,
        's', Blocks.DISPENSER,
        'r', Blocks.LAPIS_BLOCK,
        'b', Blocks.MAGMA);// MAGMA BLOCK is field_189877_df in 1.10.2 apparently
  }
}
