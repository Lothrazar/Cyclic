package com.lothrazar.cyclicmagic.component.disenchanter;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
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
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDisenchanter extends BlockBaseFacingInventory implements IHasRecipe, IBlockHasTESR {
  public BlockDisenchanter() {
    super(Material.ROCK, ModGuiHandler.GUI_INDEX_DISENCH);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityDisenchanter();
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisenchanter.class, new DisenchantPylonTESR());
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        "vhv",
        "grg",
        "sis",
        'v', Items.EXPERIENCE_BOTTLE,
        'h', Blocks.HOPPER,
        'i', Items.DIAMOND,
        'g', new ItemStack(Blocks.STAINED_GLASS, 1, EnumDyeColor.PURPLE.getMetadata()),
        'r', Items.FIRE_CHARGE,
        's', Items.NETHERBRICK);
  }
}
