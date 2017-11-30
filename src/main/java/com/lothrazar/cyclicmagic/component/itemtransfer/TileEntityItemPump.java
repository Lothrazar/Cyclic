package com.lothrazar.cyclicmagic.component.itemtransfer;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityItemPump extends TileEntityBaseMachineInvo implements ITickable {
  public TileEntityItemPump() {
    super(1);
  }
  /**
   * for every side connected to me pull fluid in from it UNLESS its my current facing direction. for THAT side, i push fluid out from me pull first then push
   *
   * TODO: UtilFluid that does a position, a facing, and tries to move fluid across
   *
   *
   */
  @Override
  public void update() {
    if (world.isRemote) {
      return;
    }
    if (this.isPowered() == false) {
      return;
    }
    this.tryExport();
    this.tryImport();
  }
  public void tryExport() {
    if (this.getStackInSlot(0).isEmpty()) {
      return;//im empty nothing to give
    }
    boolean outputSuccess = false;
    ItemStack stackToExport = this.getStackInSlot(0).copy();
    EnumFacing facingTo = this.getCurrentFacing();
    BlockPos posSide = pos.offset(facingTo);
    EnumFacing sideOpp = facingTo.getOpposite();
    TileEntity tileTarget = world.getTileEntity(posSide);
    if (tileTarget == null ||
        tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideOpp) == false) {
      return;
    }
    IItemHandler itemHandlerDeposit = world.getTileEntity(posSide).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideOpp);
    for (int i = 0; i < itemHandlerDeposit.getSlots(); i++) {
      ItemStack pulled = itemHandlerDeposit.insertItem(i, stackToExport, false).copy();
      if (pulled.getCount() != stackToExport.getCount()) {
        this.setInventorySlotContents(0, pulled);
        //one or more was put in
        outputSuccess = true;
        break;
      }
    }
    if (outputSuccess && world.getTileEntity(pos.offset(facingTo)) instanceof TileEntityItemCable) {
      TileEntityItemCable cable = (TileEntityItemCable) world.getTileEntity(pos.offset(facingTo));
      cable.updateIncomingFace(facingTo.getOpposite());
    }
  }
  public void tryImport() {
    if (this.getStackInSlot(0).isEmpty() == false) {
      return;//im full leave me alone
    }
    EnumFacing sideOpp = this.getCurrentFacing().getOpposite();
    //get the block Behind me
    BlockPos posSide = pos.offset(sideOpp);
    TileEntity tileTarget = world.getTileEntity(posSide);
    if (tileTarget == null) {
      return;
    }
    if (tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getCurrentFacing())) {
      IItemHandler itemHandlerFrom = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getCurrentFacing());
      for (int i = 0; i < itemHandlerFrom.getSlots(); i++) {
        ItemStack pulled = itemHandlerFrom.extractItem(i, 1, false);
        if (pulled != null && pulled.isEmpty() == false) {
          this.setInventorySlotContents(0, pulled.copy());
          return;
        }
      }
    }
  }
}
