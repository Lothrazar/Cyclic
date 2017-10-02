package com.lothrazar.cyclicmagic.component.crafter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
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

public class TileEntityCrafter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public static final int TIMER_FULL = 80;
  public static final int ROWS = 5;
  public static final int COLS = 2;
  public static final int SIZE_INPUT = ROWS * COLS;//10
  public static final int SIZE_GRID = 3 * 3;//19
  public static final int SIZE_OUTPUT = ROWS * COLS;//20 to 30
  public static enum Fields {
    REDSTONE, TIMER, FUEL, FUELMAX;
  }
  private Container fakeContainer;
  private IRecipe recipe;
  private int needsRedstone = 1;
  private InventoryCrafting crafter;
  private int[] hopperInput = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
  private int[] hopperInputfUEL = { SIZE_INPUT + SIZE_GRID + SIZE_OUTPUT };
  private int[] hopperOutput = new int[] { 19, 20, 21, 22, 23, 24, 25, 26, 27, 28 };
  public TileEntityCrafter() {
    super(SIZE_INPUT + SIZE_GRID + SIZE_OUTPUT + 1);//+1 for fuel..left and right side both have a tall rectangle. then 3x3 crafting 
    fakeContainer = new Container() {
      public boolean canInteractWith(@Nonnull final EntityPlayer playerIn) {
        return false;
      }
    };
    crafter = new InventoryCrafting(fakeContainer, 3, 3);
    this.setFuelSlot(this.getSizeInventory() - 1);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (!isRunning()) {
      return;
    }
    this.spawnParticlesAbove();
    setRecipeInput();//make sure the 3x3 inventory is linked o the crater
    findRecipe(); //does it match
    this.updateFuelIsBurning();
    if (this.updateTimerIsZero()) {
      findRecipe();
      if (recipe != null && tryPayCost()) {
        // pay the cost  
        final ItemStack craftingResult = recipe.getCraftingResult(this.crafter);
        //confirmed this test does actually et the outut: 4x planks 
        sendOutput(craftingResult);
        timer = TIMER_FULL;
      }
    }
  }
  private boolean tryPayCost() {
    ItemStack fromRecipe;
    ItemStack fromInput;
    boolean thisPaid = false;
    //map is <slotNumber, amtToPay>
    Map<Integer, Integer> slotsToPay = new HashMap<Integer, Integer>();//can have dupes
    for (int i = 0; i < this.crafter.getSizeInventory(); i++) {
      //for ever i val, we must pay the cost
      thisPaid = false;
      fromRecipe = this.crafter.getStackInSlot(i);
      if (fromRecipe.isEmpty()) {
        continue;
      }
      //try to pay its cost
      for (int j = 0; j < SIZE_INPUT; j++) {
        fromInput = this.getStackInSlot(j);
        if (fromRecipe.isItemEqual(fromInput)) {
          //now set the key 'j' slot to need one more extra item
          if (!slotsToPay.containsKey(j)) {
            slotsToPay.put(j, 0);
          }
          //if what we are going to be pulling from this slot not more than what it contains
          if (slotsToPay.get(j) + 1 <= fromInput.getCount()) {
            //            ModCyclic.logger.info(" founnd slot  = " + j + " so will drain " + (slotsToPay.get(j) + 1));
            slotsToPay.put(j, slotsToPay.get(j) + 1);
            thisPaid = true;
            break;//break only the j loop
          }
        }
      }
      if (thisPaid == false) {//required input not even fond
        return false;
      }
    }
    //TODO: in retroscpect this 2econd pass might be redndant since we already validated in above loop. but keeping it doesnt hurt
    //we need to do 2 passes. one pass to make sure we haven enough and another to cost
    //otherwise we could start spending and halfway thru run out and we would havce to rollback ransacions
    //and couldnt have done it above because of the slot spread
    //EX: stairs need 6 wood planks. This could be 6 all from one stack, or split over a few
    for (Map.Entry<Integer, Integer> entry : slotsToPay.entrySet()) {
      // if there isnt enough, in any one of these spots, stop now
      if (entry.getValue() > this.getStackInSlot(entry.getKey()).getCount()) {
        return false;
      }
    }
    for (Map.Entry<Integer, Integer> entry : slotsToPay.entrySet()) {
      //      ModCyclic.logger.info(" PAY cost at  = " + entry);
      //now we know there is enough everywhere. we validated
      this.getStackInSlot(entry.getKey()).shrink(entry.getValue());
    }
    return true;
  }
  private void sendOutput(ItemStack craftingResult) {
    //bit o a hack since util method assmes takes a list, and we have only one, so just wrap it eh
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(Arrays.asList(craftingResult), this, SIZE_INPUT + SIZE_GRID);
    //if something is given back, it didnt fit so we have to spew
    if (!toDrop.isEmpty()) {
      for (ItemStack s : toDrop) {
        UtilItemStack.dropItemStackInWorld(this.getWorld(), this.getPos().up(), s);
      }
    }
  }
  private void findRecipe() {
    if (this.recipe != null && recipe.matches(crafter, world)) {
      //recipe exists and it matches whats currently in the gui so stop now
      return;
    }
    recipe = null;//doesnt match
    //    final List<IRecipe> recipes = CraftingManager.field_193380_a();//.getInstance().getRecipeList();
    for (final IRecipe rec : CraftingManager.REGISTRY) {
      try {
        // rec.getRecipeSize() <= 9 && 
        if (rec.matches(this.crafter, this.world)) {
          this.recipe = rec;
          return;
        }
      }
      catch (Exception err) {
        throw new RuntimeException("Caught exception while querying recipe ", err);
      }
    }
  }
  private void setRecipeInput() {
    int gridStart = SIZE_INPUT, craftSlot;
    for (int i = gridStart; i < gridStart + SIZE_GRID; i++) {
      craftSlot = i - gridStart;
      //      ModCyclic.logger.info("Crafter set "+craftSlot+"_"+ this.getStackInSlot(i ));
      this.crafter.setInventorySlotContents(craftSlot, this.getStackInSlot(i));
    }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    switch (side) {
      case DOWN:
        return hopperOutput;
      case UP:
        return hopperInput;
      default://FUELso all others
        return hopperInputfUEL;
    }
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case FUEL:
        return this.getFuelCurrent();
      case FUELMAX:
        return this.getFuelMax();
      default:
      break;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case TIMER:
        this.timer = value;
      break;
      case FUEL:
        this.setFuelCurrent(value);
      break;
      case FUELMAX:
        this.setFuelMax(value);
      break;
      default:
      break;
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void toggleNeedsRedstone() {
    this.setField(Fields.REDSTONE.ordinal(), (this.needsRedstone + 1) % 2);
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    needsRedstone = tagCompound.getInteger(NBT_REDST);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, needsRedstone);
    return super.writeToNBT(tagCompound);
  }
}
