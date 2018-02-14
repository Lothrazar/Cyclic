package com.lothrazar.cyclicmagic.component.peat.farm;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile; 
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class BlockPeatFarm extends BlockBaseHasTile {
  public BlockPeatFarm() {
    super(Material.IRON);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_PEATFARM);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPeatFarm();
  }
  
  
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    TileEntityPeatFarm te = (TileEntityPeatFarm) world.getTileEntity(pos);
    //    FluidStack bucket = FluidUtil.getFluidContained(player.getHeldItem(hand));
    boolean success = false;
    success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    if (te != null) {
      if (!world.isRemote) {
        int currentFluid = te.getField(TileEntityPeatFarm.Fields.FLUID.ordinal());
        UtilChat.sendStatusMessage(player, UtilChat.lang("cyclic.fluid.amount") + currentFluid);
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  
}
