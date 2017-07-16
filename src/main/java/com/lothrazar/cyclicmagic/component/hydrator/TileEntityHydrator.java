package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityHydrator extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public TileEntityHydrator() {
    super(9);
  }
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private static final String NBT_REDST = "redstone";
  private int needsRedstone = 1;
  public static enum Fields {
    REDSTONE
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (isRunning()) {
      this.spawnParticlesAbove();
    }
    this.shiftAllUp();
    ItemStack s = this.getStackInSlot(0);
    if (OreDictionary.itemMatches(s, new ItemStack(Blocks.DIRT), false)) {
      sendOutput(new ItemStack(Blocks.FARMLAND));
      s.shrink(1);
    }
    else if (OreDictionary.itemMatches(s, new ItemStack(Blocks.HARDENED_CLAY), false)) {
      sendOutput(new ItemStack(Blocks.CLAY));
      s.shrink(1);
    }
    else if (s.isItemEqual(new ItemStack(Blocks.CONCRETE_POWDER, 1, EnumDyeColor.BLACK.getMetadata())   )) {
      sendOutput(new ItemStack(Blocks.CONCRETE, 1, EnumDyeColor.BLACK.getMetadata()));
      s.shrink(1);
    }
  }
  public void sendOutput(ItemStack out) {
    UtilItemStack.dropItemStackInWorld(this.world, this.getPos(), out);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
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
}
