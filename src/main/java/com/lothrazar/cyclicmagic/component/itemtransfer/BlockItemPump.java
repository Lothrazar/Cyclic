package com.lothrazar.cyclicmagic.component.itemtransfer;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class BlockItemPump extends BlockBaseFacingOmni implements ITileEntityProvider, IHasRecipe {
  public BlockItemPump() {
    super(Material.WOOD);
    this.setHardness(3F);
    this.setResistance(3F);
    this.setHarvestLevel("pickaxe", 1);
    this.setTranslucent();
  }
  @Override //Use IBlockState.getBlockFaceShape
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if (side == EnumFacing.UP || side == EnumFacing.DOWN)
      return false;//allows, for example, you to open chest that is directly below
    return true;
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityItemPump();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    boolean success = true;
    String powered = world.isBlockPowered(pos) ? "cyclic.redstone.on" : "cyclic.redstone.off";
    UtilChat.sendStatusMessage(player, UtilChat.lang(powered));
    return   super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "i i",
        "frd",
        "i i",
        'r', "nuggetIron",
        'i', "ingotIron",
        'f', Blocks.DROPPER,
        'd', Blocks.HOPPER);
  }
}
