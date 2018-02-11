package com.lothrazar.cyclicmagic.component.pumpfluid;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingOmni;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.ITileEntityProvider;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class BlockFluidPump extends BlockBaseFacingOmni implements ITileEntityProvider, IHasRecipe {
  public BlockFluidPump() {
    super(Material.WOOD);
    this.setHardness(3F);
    this.setResistance(3F);
    this.setHarvestLevel("pickaxe", 1);
    this.setTranslucent();
    this.placeType = PlacementType.SIDE_BLOCK;
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityFluidPump();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "i i",
        " r ",
        "ibi",
        'b', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
        'i', "nuggetGold",
        'r', Blocks.DROPPER);
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    TileEntityBaseMachineFluid te = (TileEntityBaseMachineFluid) world.getTileEntity(pos);
    boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    if (te != null) {
      if (world.isRemote == false) { //server side
        String message = null;
        FluidStack fs = te.getCurrentFluidStack();
        if (fs != null) {
          String amtStr = fs.amount + " / " + te.getCapacity() + " ";
          message = UtilChat.lang("cyclic.fluid.amount") + amtStr + fs.getLocalizedName();
        }
        else {
          message = UtilChat.lang("cyclic.fluid.empty");
        }
        String powered = world.isBlockPowered(pos) ? "cyclic.redstone.on" : "cyclic.redstone.off";
        UtilChat.sendStatusMessage(player, message + "; " + UtilChat.lang(powered));
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
}
