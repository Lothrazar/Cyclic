package com.lothrazar.cyclicmagic.component.forester;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacing;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.MachineTESR;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockForester extends BlockBaseFacingInventory implements IHasRecipe{//, IBlockHasTESR 
  public static final PropertyDirection PROPERTYFACING = BlockBaseFacing.PROPERTYFACING;
  public BlockForester() {
    super(Material.IRON, ForgeGuiHandler.GUI_INDEX_FORESTER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityForester();
  }
//  @SideOnly(Side.CLIENT)
//  public void initModel() {
//    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//    // Bind our TESR to our tile entity
//    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityForester.class, new MachineTESR(this, 0));
//  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "rsr",
        "gbx",
        "ooo",
        'o', "obsidian",
        'g', "gemDiamond",
        'x', "gemDiamond",
        's', Blocks.OBSERVER,
        'r', "blockLapis",
        'b', Blocks.BED);
  }
}
