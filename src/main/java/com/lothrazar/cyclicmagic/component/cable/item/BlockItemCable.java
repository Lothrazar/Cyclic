package com.lothrazar.cyclicmagic.component.cable.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class BlockItemCable extends BlockBaseCable implements IHasRecipe {
  public BlockItemCable() {
    super(Material.CLAY);
//    this.setItemTransport();
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityItemCable();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 32),
        "sis",
        " x ",
        "sis",
        's', Blocks.SANDSTONE_STAIRS, 'i', "ingotIron", 'x', "string");
  }
}
