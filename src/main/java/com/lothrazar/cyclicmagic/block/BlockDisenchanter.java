package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDisenchanter;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockDisenchanter extends BlockBaseHasTile implements IHasRecipe {
  public BlockDisenchanter() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
    this.setGuiId(ModGuiHandler.GUI_INDEX_DISENCH);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityDisenchanter();
  }
//  @SideOnly(Side.CLIENT)
//  public void initModel() {
//    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//    // Bind our TESR to our tile entity
//    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFishing.class, new FishingTESR(0));
//  }

  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
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
