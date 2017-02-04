package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPatternBuilder extends BlockBaseHasTile implements IHasRecipe {
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
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "rbr",
        "did",
        "rmr",
        'b', Blocks.STONE_BUTTON,
        'd', Blocks.DIAMOND_BLOCK,
        'i', Blocks.ICE,
        'm', Blocks.MAGMA,
        'r', Blocks.REDSTONE_BLOCK);
  }
}
