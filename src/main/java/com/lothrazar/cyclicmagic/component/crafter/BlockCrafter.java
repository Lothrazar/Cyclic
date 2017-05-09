package com.lothrazar.cyclicmagic.component.crafter;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockCrafter extends BlockBaseFacingInventory implements IHasRecipe {
  public BlockCrafter() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_CRAFTER);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityCrafter();
  }
  @Override
  public IRecipe addRecipe() {
    IRecipe recipe = GameRegistry.addShapedRecipe(new ItemStack(this),
        "pcp",
        "y x",
        "pkp",
        'k', new ItemStack(Blocks.BONE_BLOCK),
        'x', new ItemStack(Blocks.OBSERVER),
        'y', new ItemStack(Blocks.PISTON),
        'c', new ItemStack(Blocks.CRAFTING_TABLE),
        'p', new ItemStack(Items.DYE, 1, EnumDyeColor.PURPLE.getDyeDamage()));
    return recipe;
  }
}
