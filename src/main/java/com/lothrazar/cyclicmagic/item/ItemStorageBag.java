package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.gui.storage.InventoryStorage;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilInventorySort.BagDepositReturn;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStorageBag extends BaseItem implements IHasRecipe {
  public static enum StorageActionType {
    NOTHING, DEPOSIT, MERGE;
    private final static String NBT = "build";
    private final static String NBTTIMEOUT = "timeout";
    public static int getTimeout(ItemStack wand) {
      return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
    }
    public static void setTimeout(ItemStack wand) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
    }
    public static void tickTimeout(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int t = tags.getInteger(NBTTIMEOUT);
      if (t > 0) {
        UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
      }
    }
    public static int get(ItemStack wand) {
      if (wand == null) { return 0; }
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      return tags.getInteger(NBT);
    }
    public static String getName(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return "item.storage_bag." + StorageActionType.values()[tags.getInteger(NBT)].toString().toLowerCase();
      }
      catch (Exception e) {
        return "item.storage_bag." + NOTHING.toString().toLowerCase();
      }
    }
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      type++;
      if (type > MERGE.ordinal()) {
        type = NOTHING.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
  }
  public ItemStorageBag() {
    this.setMaxStackSize(1);
  }
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 1; // Without this method, your inventory will NOT work!!!
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    int size = InventoryStorage.countNonEmpty(stack);
    tooltip.add(UtilChat.lang("item.storage_bag.tooltip") + size);
    tooltip.add(UtilChat.lang("item.storage_bag.tooltip2") + UtilChat.lang(StorageActionType.getName(stack)));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    StorageActionType.tickTimeout(stack);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }
  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held != null && held.getItem() == this) {
      World world = event.getWorld();
      TileEntity tile = event.getWorld().getTileEntity(event.getPos());
      if (tile != null && tile instanceof IInventory) {
        int depositType = StorageActionType.get(held);
        if (depositType == StorageActionType.NOTHING.ordinal()) {
          if (world.isRemote) {
            UtilChat.addChatMessage(player, UtilChat.lang("item.storage_bag.disabled"));
          }
          return;
        }
        else {
          if (world.isRemote == false) {
            ItemStack[] inv = InventoryStorage.readFromNBT(held);
            BagDepositReturn ret = null;
            if (depositType == StorageActionType.DEPOSIT.ordinal()) {
              ret = dumpFromListToIInventory(world, (IInventory) tile, inv, false);
            }
            else if (depositType == StorageActionType.MERGE.ordinal()) {
              ret = dumpFromListToIInventory(world, (IInventory) tile, inv, true);
            }
            if (ret != null && ret.moved > 0) {
              InventoryStorage.writeToNBT(held, ret.stacks);
              UtilChat.addChatMessage(player, UtilChat.lang("item.storage_bag.success") + ret.moved); //   // TODO: fix the count, make sure its accuarte
            }
          }
          UtilSound.playSound(player, SoundRegistry.basey);
        }
      }
      else { //hit something not an invenotry
        if (StorageActionType.getTimeout(held) > 0) {
          //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
          return;
        }
        StorageActionType.setTimeout(held);
        event.setCanceled(true);
        UtilSound.playSound(player, player.getPosition(), SoundRegistry.dcoin, SoundCategory.PLAYERS);
        if (!player.worldObj.isRemote) { // server side
          StorageActionType.toggle(held);
          UtilChat.addChatMessage(player, UtilChat.lang(StorageActionType.getName(held)));
        }
      }
    }
  }
  public static ItemStack getPlayerItemIfHeld(EntityPlayer player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand == null || wand.getItem() instanceof ItemStorageBag == false) {
      wand = player.getHeldItemOffhand();
    }
    if (wand == null || wand.getItem() instanceof ItemStorageBag == false) { return null; }
    return wand;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer player, EnumHand hand) {
    if (!world.isRemote) {
      BlockPos pos = player.getPosition();
      int x = pos.getX(), y = pos.getY(), z = pos.getZ();
      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_STORAGE, world, x, y, z);
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }
  public static ItemStack getPlayerBagIfHeld(EntityPlayer player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand != null && wand.getItem() instanceof ItemStorageBag) { return wand; }
    wand = player.getHeldItemOffhand();
    if (wand != null && wand.getItem() instanceof ItemStorageBag) { return wand; }
    return null;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this), "lsl", "ldl", "lrl",
        'l', Items.LEATHER,
        's', Items.STRING,
        'r', Items.REDSTONE,
        'd', Items.GOLD_INGOT);
  }
 
  public static BagDepositReturn dumpFromListToIInventory(World world, IInventory chest, ItemStack[] stacks, boolean onlyMatchingItems) {
    ItemStack chestItem;
    ItemStack bagItem;
    int room;
    int toDeposit;
    int chestMax;
    int itemsMoved = 0;
    for (int islotStacks = 0; islotStacks < stacks.length; islotStacks++) {
      bagItem = stacks[islotStacks];
      if (bagItem == null || bagItem.stackSize == 0) {
        continue;
      }
      // System.out.println(bagItem.stackSize + "_" + bagItem.getDisplayName());
      for (int islotChest = 0; islotChest < chest.getSizeInventory(); islotChest++) {
        chestItem = chest.getStackInSlot(islotChest);
        //we have a space in the inventory thats empty. are we allowed
        if (chestItem == null && onlyMatchingItems == false) {
          //then yeah we are allowed to use the empty space
          if (chest.isItemValidForSlot(islotStacks, bagItem)) {
            // System.out.println("dump at " + islotChest);
            itemsMoved += bagItem.stackSize;
            chest.setInventorySlotContents(islotChest, bagItem);
            stacks[islotStacks] = null;
            bagItem = null;
            break;//move to next bag item, we're done here
          }
          else {
            //cant dump here. but also cant merge so move to next slot
            continue;
          }
        }
        if (chestItem == null) {
          //chest item is null, and were trying to merge (check is probably redundant here)
          continue;//go to next chest item
        }
        //ok so chestItem is not nulll
        if (bagItem == null || bagItem.stackSize == 0) {
          break;//stop lookin in the chest, get a new bag item
        }
        bagItem = stacks[islotStacks];
        if (canMerge(bagItem,chestItem)) {
          chestMax = chestItem.getItem().getItemStackLimit(chestItem);
          room = chestMax - chestItem.stackSize;
          if (room <= 0) {
            continue;//no room on this chest slot, so move to next slot
          } // no room, check the next spot
          //System.out.println("merge at " + islotChest);
          // so if i have 30 room, and 28 items, i deposit 28.
          // or if i have 30 room and 38 items, i deposit 30
          toDeposit = Math.min(bagItem.stackSize, room);
          chestItem.stackSize += toDeposit;
          chest.setInventorySlotContents(islotChest, chestItem);
          bagItem.stackSize -= toDeposit;
          itemsMoved += toDeposit;
          if (bagItem.stackSize <= 0) {
            // item stacks with zero count do not destroy
            // themselves, they show
            // up and have unexpected behavior in game so set to
            // empty
            stacks[islotStacks] = null;
          }
          else {
            // set to new quantity
            stacks[islotStacks] = bagItem;
          }
        } // end if items match
        if (bagItem == null || bagItem.stackSize == 0) {
          break;//stop lookin in the chest, get a new bag item
        }
      } // close loop on player inventory items
    } // close loop on chest items
    return new BagDepositReturn(itemsMoved, stacks);
  }
  /**
   * match item, damage, and NBT
   * @param chestItem
   * @param bagItem
   * @return
   */
  public static boolean canMerge(ItemStack chestItem, ItemStack bagItem) {
    if (chestItem == null || bagItem == null) { return false; }
    return (bagItem.getItem().equals(chestItem.getItem())
        && bagItem.getItemDamage() == chestItem.getItemDamage()
        && ItemStack.areItemStackTagsEqual(bagItem, chestItem));
  }
}
