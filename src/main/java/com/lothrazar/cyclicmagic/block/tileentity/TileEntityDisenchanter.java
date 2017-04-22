package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

public class TileEntityDisenchanter extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public static enum Fields {
    REDSTONE, TIMER
  }
  public static final int TIMER_FULL = 100;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_BOTTLE = 1;
  public static final int SLOT_REDSTONE = 2;
  public static final int SLOT_GLOWSTONE = 3;
  public static final int SLOT_BOOK = 4;
  private int needsRedstone = 1;
  private int timer = TIMER_FULL;
  public TileEntityDisenchanter() {
    super(5 + 9);//5 for main array, 9 for output
  }
  @Override
  public void update() {
    if (!isRunning()) { return; }
    if (!isInputValid()) { return; }
    this.spawnParticlesAbove();
    //odo; stop here depending on item state?
    ModCyclic.logger.info("!"+timer);
    System.out.println(""+timer);
    timer -= 1;
    if (timer > 0) { return; }//timer zero so go
    timer = TIMER_FULL;
    //now go my pretty!
    //the good stuff goes here  
    ItemStack input = this.getStackInSlot(SLOT_INPUT);
    ItemStack eBook = new ItemStack(Items.ENCHANTED_BOOK);
    Map<Enchantment, Integer> outEnchants = Maps.<Enchantment, Integer> newLinkedHashMap();
    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(input);
    //since its a map, there is no concept of 'get(0)', its unordered. so just do one
    Enchantment keyMoved = null;
    for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
      keyMoved = entry.getKey();
      outEnchants.put(keyMoved, entry.getValue());
      break;
    }
    if (outEnchants.size() == 0 || keyMoved == null) { return;  }//weird none were found. so anyweay dont pay cost
  
    enchants.remove(keyMoved);
    EnchantmentHelper.setEnchantments(outEnchants, eBook);//add to book
    dropStack(eBook); // drop the new enchanted book
    //special case if input was book, we dont want an ench book with nothin on it
    if (input.getItem() == Items.ENCHANTED_BOOK && enchants.size() == 0) {
      dropStack(new ItemStack(Items.BOOK));
    }
    else {
      //was a normal item, so ok to set its ench list to empty
      EnchantmentHelper.setEnchantments(enchants, input);//set as removed
      dropStack(input);
    }
    //currently it always drops after one enchant is removed
    this.setInventorySlotContents(SLOT_INPUT, ItemStack.EMPTY);
    //pay cost
    this.decrStackSize(SLOT_GLOWSTONE);
    this.decrStackSize(SLOT_REDSTONE);
    this.decrStackSize(SLOT_BOTTLE);
    this.decrStackSize(SLOT_BOOK);
    UtilSound.playSound(world, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS);
  }
  private void dropStack(ItemStack stack) {
    //TODO: spew above/below configurable
    EntityItem ei = UtilItemStack.dropItemStackInWorld(world, this.pos.up(), stack);
    ei.addVelocity(0, 1, 0);
  }
  private boolean isInputValid() {
    return this.getStackInSlot(SLOT_BOOK).getItem() == Items.BOOK
        && this.getStackInSlot(SLOT_REDSTONE).getItem() == Items.REDSTONE
        && this.getStackInSlot(SLOT_GLOWSTONE).getItem() == Items.GLOWSTONE_DUST
        && this.getStackInSlot(SLOT_BOTTLE).getItem() == Items.EXPERIENCE_BOTTLE
        && this.getStackInSlot(SLOT_INPUT).isEmpty() == false;
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP) {
      return new int[] { SLOT_INPUT };
    }
//    else if (side == EnumFacing.DOWN) { return new int[] { SLOT_OUTPUT }; }
    return new int[] { SLOT_BOTTLE, SLOT_REDSTONE, SLOT_GLOWSTONE, SLOT_BOOK };//for outputting stuff
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
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case REDSTONE:
        return this.needsRedstone;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      default:
      break;
    }
  }
}
