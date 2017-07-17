package com.lothrazar.cyclicmagic.component.hydrator;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityHydrator extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public FluidTank tank = new FluidTank(4000);
  public final static int TIMER_FULL = 120;
  public static enum Fields {
    REDSTONE
  }
  private InventoryCrafting crafting = new InventoryCrafting(new ContainerDummy(), 1, 1);
  public TileEntityHydrator() {
    super(9);
    timer = TIMER_FULL;
  }
  private int needsRedstone = 1;
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (!isRunning()) { return; }
    FluidStack fs = this.tank.getFluid();
    //WAT https://github.com/BluSunrize/ImmersiveEngineering/search?utf8=%E2%9C%93&q=FluidUtil&type=
    ModCyclic.logger.info("fluidstack " +fs);
    this.spawnParticlesAbove();
    this.shiftAllUp();
    if (this.updateTimerIsZero()) { // time to burn!
      ItemStack s = this.getStackInSlot(0);
      this.crafting.setInventorySlotContents(0, s);
      IRecipe rec = CraftingManager.findMatchingRecipe(crafting, this.world);
      if (rec != null) {
        this.sendOutput(rec.getRecipeOutput());
        s.shrink(1);
      }
    }
  }
  public void sendOutput(ItemStack out) {
    UtilItemStack.dropItemStackInWorld(this.world, this.getPos(), out);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    tank.readFromNBT(tagCompound.getCompoundTag("tank"));
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
      return true;
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
      return (T) tank;
    return super.getCapability(capability, facing);
  }
  public static class ContainerDummy extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
  }
}
