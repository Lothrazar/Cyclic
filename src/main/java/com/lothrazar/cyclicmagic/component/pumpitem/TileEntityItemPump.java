package com.lothrazar.cyclicmagic.component.pumpitem;
import java.util.List;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityItemPump extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
  private static final int SLOT_TRANSFER = 0;
  public static enum Fields {
    REDSTONE, FILTERTYPE;
  }
  static final int FILTER_SIZE = 9;
  private int needsRedstone = 1;
  private int filterType = 0;
  public TileEntityItemPump() {
    super(1 + FILTER_SIZE);
    this.setSlotsForExtract(0);
    this.setSlotsForInsert(0);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case FILTERTYPE:
        return this.filterType;
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case FILTERTYPE:
        this.filterType = value % 2;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }
  private boolean isWhitelist() {
    //default is zero, and default blacklist makes sense -> it is empty, so everythings allowed
    return this.filterType == 1;
  }
  private boolean isTargetItemValid(ItemStack stackToTest) {
    List<ItemStack> inventoryContents = getFilter();
    //edge case: if list is empty ?? should be covered already
    if (OreDictionary.containsMatch(true,
        NonNullList.<ItemStack> from(ItemStack.EMPTY, inventoryContents.toArray(new ItemStack[0])),
        stackToTest)) {
      //the item that I target matches something in my filter
      //meaning, if i am in whitelist mode, it is valid, it matched the allowed list
      //if I am in blacklist mode, nope not valid
      return !this.isWhitelist();
    }
    //here is the opposite: i did NOT match the list
    return this.isWhitelist();
  }
  private List<ItemStack> getFilter() {
    List<ItemStack> validForSide = this.inv.subList(1, FILTER_SIZE + 1);
    return NonNullList.<ItemStack> from(ItemStack.EMPTY, validForSide.toArray(new ItemStack[0]));
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
    if (world.isRemote || this.isValid() == false) {
      return;
    }
    if (this.isPowered() == false && this.onlyRunIfPowered()) {
      return;//i am not powered, and i require it
    }
    //    if (isRunning() == false) {
    //      return;
    //    }
    //    if (this.isPowered() == false ) {
    //      return;
    //    }
    this.tryExport();
    this.tryImport();
  }
  public void tryExport() {
    if (this.getStackInSlot(SLOT_TRANSFER).isEmpty()) {
      return;//im empty nothing to give
    }
    boolean outputSuccess = false;
    ItemStack stackToExport = this.getStackInSlot(SLOT_TRANSFER).copy();
    EnumFacing facingTo = this.getCurrentFacing().getOpposite();
    BlockPos posTarget = pos.offset(facingTo);
    EnumFacing sideOpp = facingTo.getOpposite();
    TileEntity tileTarget = world.getTileEntity(posTarget);
    if (tileTarget == null ||
        tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, sideOpp) == false) {
      return;
    }
    ItemStack pulled = UtilItemStack.tryDepositToHandler(world, posTarget, sideOpp, stackToExport);
    if (pulled.getCount() != stackToExport.getCount()) {
      this.setInventorySlotContents(SLOT_TRANSFER, pulled);
      //one or more was put in
      outputSuccess = true;
    }
    if (outputSuccess && world.getTileEntity(pos.offset(facingTo)) instanceof TileEntityBaseCable) {
      TileEntityBaseCable cable = (TileEntityBaseCable) world.getTileEntity(pos.offset(facingTo));
      if (cable.isItemPipe())
        cable.updateIncomingItemFace(facingTo.getOpposite());
    }
  }
  public void tryImport() {
    if (this.getStackInSlot(SLOT_TRANSFER).isEmpty() == false) {
      return;//im full leave me alone
    }
    EnumFacing sideOpp = this.getCurrentFacing();
    //get the block Behind me
    BlockPos posTarget = pos.offset(sideOpp);
    TileEntity tileTarget = world.getTileEntity(posTarget);
    if (tileTarget == null) {
      return;
    }
    ItemStack itemTarget;
    if (tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getCurrentFacing())) {
      IItemHandler itemHandlerFrom = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, this.getCurrentFacing());
      for (int i = 0; i < itemHandlerFrom.getSlots(); i++) {
        itemTarget = itemHandlerFrom.getStackInSlot(i);
        //check against whitelist/blacklist system
        if (this.isTargetItemValid(itemTarget)) {
          //          ModCyclic.logger.log("not valid " + itemTarget.getDisplayName());
          continue;
        }
        //passed filter check, so do the transaction
        ItemStack pulled = itemHandlerFrom.extractItem(i, 1, false);
        if (pulled != null && pulled.isEmpty() == false) {
          this.setInventorySlotContents(SLOT_TRANSFER, pulled.copy());
          return;
        }
      }
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    needsRedstone = compound.getInteger(NBT_REDST);
    filterType = compound.getInteger("wbtype");
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setInteger("wbtype", this.filterType);
    return super.writeToNBT(compound);
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
