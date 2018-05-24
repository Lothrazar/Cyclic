package com.lothrazar.cyclicmagic.block.imbue;


import com.lothrazar.cyclicmagic.block.imbue.BlockImbue.ImbueFlavor;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class TileEntityImbue extends TileEntityBaseMachineInvo implements ITickable {

  public static final int SLOT_TARGET = 0;

  public TileEntityImbue() {
    super(3);//one slot for bow, 2 for ingredients

  }

  @Override
  public void update() {
    // TODO apply thing to target

    if (this.isPowered()) {
      ItemStack target = this.getStackInSlot(SLOT_TARGET);
      if (target.getItem() instanceof ItemBow) {
        BlockImbue.setImbueInt(target, ImbueFlavor.POTION);
      }
    }

  }

  //  private void addImbue(ItemStack target) {
  //    UtilNBT.getItemStackNBT(target).setInteger(BlockImbue.NBT_IMBUE, ImbueFlavor.LEVITATION.ordinal());
  //  }
}
