package com.lothrazar.cyclicmagic.component.disenchanter;
import java.util.Arrays;
import java.util.Map;
import com.google.common.collect.Maps;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
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
  public TileEntityDisenchanter() {
    super(5 + 9);//5 for main array, 9 for output
    this.setSlotsForInsert(Arrays.asList(0, 1, 2, 3, 4));
    this.setSlotsForExtract(Arrays.asList(5, 6, 7, 8, 9, 10, 11, 12, 13));
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
    if (!isInputValid()) {
      return;
    }
    this.spawnParticlesAbove();
    timer -= 1;
    if (timer > 0) {
      return;
    } //timer zero so go
    timer = TIMER_FULL;
    //now go my pretty!
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
    if (outEnchants.size() == 0 || keyMoved == null) {
      return;
    } //weird none were found. so anyweay dont pay cost
    enchants.remove(keyMoved);
    EnchantmentHelper.setEnchantments(outEnchants, eBook);//add to book
    dropStack(eBook); // drop the new enchanted book
    //special case if input was book, we dont want an ench book with nothin on it
    if (input.getItem() == Items.ENCHANTED_BOOK && enchants.size() == 0) {
      dropStack(new ItemStack(Items.BOOK));
    }
    else {
      //was a normal item, so ok to set its ench list to empty
      if (input.getItem() == Items.ENCHANTED_BOOK) {//hotfix workaround for book: so it dont try to merge eh
        ItemStack inputCopy = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(enchants, inputCopy);//set as remove
        dropStack(inputCopy);
      }
      else {
        EnchantmentHelper.setEnchantments(enchants, input);//set as removed
        dropStack(input);
      }
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
    for (int i = SLOT_BOOK + 1; i < this.getSizeInventory(); i++) {
      if (this.getStackInSlot(i).isEmpty()) {
        this.setInventorySlotContents(i, stack);
        return;
      }
    }
    //well i guess it was full since we didnt return
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
