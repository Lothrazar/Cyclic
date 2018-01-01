package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacing;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockScreen extends BlockBaseFacing implements IBlockHasTESR, IHasRecipe {
  public BlockScreen() {
    super(Material.WOOD);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_SCREEN);
    this.setLightOpacity(0);
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.SOLID;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityScreen();
  }
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityScreen.class, new ScreenTESR<TileEntityScreen>(this));
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "ibi",
        "iii",
        "frf",
        'b', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLACK.getMetadata()),
        'i', "nuggetIron",
        'r', Items.REPEATER,
        'f', Blocks.IRON_BARS);
  }
}
