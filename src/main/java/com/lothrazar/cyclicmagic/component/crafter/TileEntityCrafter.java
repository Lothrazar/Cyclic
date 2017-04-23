package com.lothrazar.cyclicmagic.component.crafter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityCrafter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public static final int TIMER_FULL = 10;

  public static final int ROWS = 5;
  public static final int COLS = 2;
  public static final int SIZE_INPUT = ROWS*COLS; 
  public static final int SIZE_GRID = 3 * 3;
  public static final int SIZE_OUTPUT = ROWS*COLS;
  private Container fakeContainer;
  private IRecipe recipe;
  private int needsRedstone = 1;
  private int timer = 1;
  private InventoryCrafting crafter;
  public TileEntityCrafter() {
    super(SIZE_INPUT + SIZE_GRID + SIZE_OUTPUT);//left and right side both have a tall rectangle. then 3x3 crafting 
    fakeContainer = new Container() {
      public boolean canInteractWith(@Nonnull final EntityPlayer playerIn) {
        return false;
      }
    };
    crafter = new InventoryCrafting(fakeContainer, 3, 3);
  }
  public static enum Fields {
    REDSTONE, TIMER;
  }
  @Override
  public void update() {
    if (!isRunning()) { return; }
    this.spawnParticlesAbove();
    timer--;
    if (timer < 0) {
      timer = 0;
    }
    //TODO: first see i we have mats in inventory, 
    //and need some way to set recipe from GUI
    setRecipeInput();
    findRecipe();
    //does it match
    if (timer == 0 && recipe != null && recipe.matches(crafter, world) &&
        tryPayCost()) {
      timer = TIMER_FULL;
      // pay the cost  
      final ItemStack craftingResult = recipe.getCraftingResult(this.crafter);
      //confirmed this test does actually et the outut: 4x planks 
      sendOutput(craftingResult);
    }
  }
  private boolean tryPayCost() {
    ItemStack fromRecipe;
    ItemStack fromInput;
    boolean thisPaid = false;
    List<Integer> slotsToPay = new ArrayList<Integer>();//can have dupes
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
          //          fromInput.shrink(1);
          slotsToPay.add(j);
          thisPaid = true;
          break;//break only the j loop
        }
      }
      if (thisPaid == false) {
     //   ModCyclic.logger.info(" failed cost at  = " + i);
        return false;
      }
    }
    //ot thru all and all have got it
    for (int k : slotsToPay) {
      this.getStackInSlot(k).shrink(1);
    }
    return true;
  }
  private void sendOutput(ItemStack craftingResult) {
    //bit o a hack since util method assmes takes a list, and we have only one, so just wrap it eh
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(Arrays.asList(craftingResult), this, SIZE_INPUT+SIZE_GRID);
    //if something is given back, it didnt fit so we have to spew
    if (!toDrop.isEmpty()) {
      for (ItemStack s : toDrop) {
        UtilItemStack.dropItemStackInWorld(this.getWorld(), this.getPos().up(), s);
      }
    }
  }
  private void findRecipe() {
    if (this.recipe != null) { return; } //already found
    final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
    for (final IRecipe rec : recipes) {
      try {
        if (rec.getRecipeSize() <= 9 && rec.matches(this.crafter, this.world)) {
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
    return new int[] {};
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
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
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
