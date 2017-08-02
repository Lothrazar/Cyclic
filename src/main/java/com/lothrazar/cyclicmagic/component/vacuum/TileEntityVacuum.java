package com.lothrazar.cyclicmagic.component.vacuum;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.registry.FluidsRegistry;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraft.inventory.InventoryHelper;

public class TileEntityVacuum extends TileEntityBaseMachineInvo implements ITickable {
  private static final int VRADIUS = 2;
  public static final int TIMER_FULL = 18;
  private static final String NBT_TIMER = "Timer";
  public final static int RADIUS = 16;
  private static final int[] SLOTS_EXTRACT = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
  public static enum Fields {
    TIMER;
  }
  private int timer = 0;
  public TileEntityVacuum() {
    super(3 * 9);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    updateCollection();
  }
  private void updateCollection() {
    //expand only goes ONE direction. so expand(3...) goes 3 in + x, but not both ways. for full boc centered at this..!! we go + and -
    AxisAlignedBB region = new AxisAlignedBB(this.getPos().up()).expand(RADIUS, VRADIUS, RADIUS).expand(-1 * RADIUS, -1 * VRADIUS, -1 * RADIUS);//expandXyz
    List<EntityItem> orbs = getWorld().getEntitiesWithinAABB(EntityItem.class, region);
    if (orbs != null) { //no timer just EAT
      for (EntityItem orb : orbs) {
        if (orb.isDead) {
          continue;
        }
        ItemStack contained = orb.getItem();
 
        ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(new ArrayList<ItemStack>() {
          {
            add(contained);
          }
        }, this,0);
        if (toDrop.size() > 0) {//JUST in case it did not fit or only half of it fit
          orb.setItem(toDrop.get(0));
        }
        else{
          orb.setDropItemsWhenDead(false);
          orb.setDead();
        }
      }
    }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return SLOTS_EXTRACT;
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
    }
    return -1;
  }
}
