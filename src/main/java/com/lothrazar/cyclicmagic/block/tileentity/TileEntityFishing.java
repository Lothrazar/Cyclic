package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.Random;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilInventory;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

public class TileEntityFishing extends TileEntityBaseMachineInvo implements ITickable {
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  final static float SPEED = 0.1F;//0.001F // bigger == faster
  public static final int RODSLOT = 1;
  public static final int FISHSLOTS = 12;
  private int toolSlot = 0;
  public ArrayList<Block> waterBoth = new ArrayList<Block>();
  private ItemStack[] inv;
  public TileEntityFishing() {
    inv = new ItemStack[RODSLOT + FISHSLOTS];
    waterBoth.add(Blocks.FLOWING_WATER);
    waterBoth.add(Blocks.WATER);
  }
  public boolean isValidPosition() {
    return waterBoth.contains(worldObj.getBlockState(pos.down()).getBlock()) &&
        waterBoth.contains(worldObj.getBlockState(pos.down(2)).getBlock()) &&
        //  waterBoth.contains(worldObj.getBlockState(pos.down(3)).getBlock()   ) &&
        waterBoth.contains(worldObj.getBlockState(pos.north()).getBlock()) &&
        waterBoth.contains(worldObj.getBlockState(pos.east()).getBlock()) &&
        waterBoth.contains(worldObj.getBlockState(pos.west()).getBlock()) &&
        waterBoth.contains(worldObj.getBlockState(pos.south()).getBlock());
  }
  public boolean isEquipmentValid() {
    return inv[toolSlot] != null;//fakePlayer != null && fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND) != null;
  }
  @Override
  public void update() {
    Random rand = worldObj.rand;

    //make sure surrounded by water
    if (rand.nextDouble() < SPEED &&
        isValidPosition() && isEquipmentValid() &&
        this.worldObj instanceof WorldServer) {
     
      //      UtilEntity.dropItemStackInWorld(worldObj, pos, fishSpawned);
      // sound of cast
      //   worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      //OOOoo. we can use the fake player method of equipped fishing rods
      //even looking at the LUCK  == luck potion
      //but also from luck of sea effect getMaxEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, player);
      LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) this.worldObj);
      float luck = (float) EnchantmentHelper.getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, this.inv[0]);
      lootcontext$builder.withLuck(luck);
      for (ItemStack itemstack : this.worldObj.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.worldObj.rand, lootcontext$builder.build())) {
        //WATER_BUBBLE
      //  System.out.println(itemstack.getDisplayName());
        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.WATER_WAKE, pos.up());
        inv[toolSlot].attemptDamageItem(1, worldObj.rand);
       // System.out.println("FISH DAAMGE"+ inv[toolSlot].getItemDamage());
        if( inv[toolSlot].getItemDamage() >= inv[toolSlot].getMaxDamage()){
          inv[toolSlot] = null;
        }
         
        for (int i = RODSLOT; i <= FISHSLOTS; i++) {
          if(itemstack != null && itemstack.stackSize != 0){
          itemstack = tryMergeStackIntoSlot(itemstack, i);
          }
        }
        if (itemstack != null && itemstack.stackSize != 0) {
          //FULL
         // System.out.println("DROP IN WORLD"+ itemstack.getDisplayName());
          UtilEntity.dropItemStackInWorld(worldObj, this.pos.down(), itemstack);
          
          
        }
      }
    }
  }
  private ItemStack tryMergeStackIntoSlot(ItemStack held, int furnaceSlot) {
  //  System.out.println("Try merge into slot "+furnaceSlot);
    ItemStack current = this.getStackInSlot(furnaceSlot);
    boolean success = false;
    if (current == null) {
     // System.out.println("current= null so insert  ");
      this.setInventorySlotContents(furnaceSlot, held);
      held = null;
      success = true;
    }
    else if (held.isItemEqual(current)) {
     // System.out.println("current match so MERGE  ");
      success = true;
      UtilInventory.mergeItemsBetweenStacks(held, current);
    }
  //  else
   //   System.out.println("cannot merge, skip "+furnaceSlot);
    if (success) {
      if (held != null && held.stackSize == 0) {// so now we just fix if something is size zero
        held = null;
      }
      this.markDirty();
      //      UtilSound.playSound(entityPlayer, SoundEvents.ENTITY_ITEM_PICKUP);
    }
    return held;
  }
  @Override
  public int getSizeInventory() {
    return inv.length;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv[index];
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.stackSize <= count) {
        setInventorySlotContents(index, null);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.stackSize == 0) {
          setInventorySlotContents(index, null);
        }
      }
    }
    return stack;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      setInventorySlotContents(index, null);
    }
    return stack;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    
    inv[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
//    if(stack != null){
//
//      System.out.println("setInventorySlotContents"+stack.getDisplayName());
//    }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if(side == EnumFacing.UP){
      return new int[]{0};
    }
    return new int[0];
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    //    timer = tagCompound.getInteger(NBT_TIMER);
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    //    tagCompound.setInteger(NBT_TIMER, timer);
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.length; i++) {
      ItemStack stack = inv[i];
      if (stack != null) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    tagCompound.setTag(NBT_INV, itemList);
    return super.writeToNBT(tagCompound);
  }
}
