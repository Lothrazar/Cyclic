package com.lothrazar.cyclicmagic.component.itemtransfer;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.Block;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import com.lothrazar.cyclicmagic.block.base.BlockBaseCable;

public class BlockItemCable extends BlockBaseCable implements IHasRecipe {
  public BlockItemCable() {
    super(Material.CLAY);
  }
  @Override
  public EnumConnectType getConnectTypeForPos(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    BlockPos offset = pos.offset(side);
    Block block = worldIn.getBlockState(offset).getBlock();
    if (block == this) {
      return EnumConnectType.CONNECT;
    }
    if (FluidUtil.getFluidHandler((World) worldIn, offset, side) != null)
      return EnumConnectType.STORAGE;
    return EnumConnectType.NULL;
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityItemCable();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    boolean success = false;
    //    TileEntityItemCable te = (TileEntityItemCable) world.getTileEntity(pos);
    //    boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    //    if (te != null) {
    //      if (world.isRemote == false) { //server side
    //        FluidStack fs = te.getCurrentFluidStack();
    //        String msg = null;
    //        if (fs != null) {
    //          // UtilChat.lang("cyclic.fluid.amount") +
    //          msg = fs.getLocalizedName();
    //          if (te.getIncomingStrings() != "") {
    //            msg += " (" + UtilChat.lang("cyclic.fluid.flowing") + te.getIncomingStrings() + ")";
    //          }
    //        }
    //        else {
    //          msg = UtilChat.lang("cyclic.fluid.empty");
    //        }
    //        UtilChat.sendStatusMessage(player, msg);
    //      }
    //    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 32),
        "sis",
        " x ",
        "sis",
        's', Blocks.STONE_STAIRS, 'i', "ingotIron", 'x', "string");
  }
}
