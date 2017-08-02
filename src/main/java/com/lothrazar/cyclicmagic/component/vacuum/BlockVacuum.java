package com.lothrazar.cyclicmagic.component.vacuum;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockVacuum extends BlockBaseFacingInventory implements IHasRecipe {
  //block rotation in json http://www.minecraftforge.net/forum/index.php?topic=32753.0
  public BlockVacuum() {
    super(Material.ROCK, ForgeGuiHandler.GUI_INDEX_VACUUM);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.GLASS);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityVacuum();
  }
 

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "vhv",
        "grg",
        "sis",
        'v', "vine",
        'h', Blocks.DROPPER,
        'i', "ingotGold",
        'g', "dyeLime",
        'r', Items.FIRE_CHARGE,
        's', "ingotBrickNether");
  }
 
}
