package com.lothrazar.cyclicmagic.component.pattern;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.component.disenchanter.DisenchantPylonTESR;
import com.lothrazar.cyclicmagic.component.disenchanter.TileEntityDisenchanter;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPatternBuilder extends BlockBaseHasTile implements IHasRecipe, IBlockHasTESR {
  public BlockPatternBuilder() {
    super(Material.IRON);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(true);
    this.setTranslucent();
    this.setGuiId(ModGuiHandler.GUI_INDEX_PATTERN);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPatternBuilder();//"tile.block_fishing.name"
  }
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity instanceof IInventory) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
      worldIn.updateComparatorOutputLevel(pos, this);
    }
    super.breakBlock(worldIn, pos, state);
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        "rbr",
        "did",
        "rmr",
        'b', Blocks.STONE_BUTTON,
        'd', Blocks.DIAMOND_BLOCK,
        'i', Blocks.ICE,
        'm', Blocks.MAGMA,
        'r', Blocks.REDSTONE_BLOCK);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPatternBuilder.class, new PatternBuilderTESR());
  }
}
