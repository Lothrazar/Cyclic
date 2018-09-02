/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.controlledminer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.ITileStackWrapper;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.gui.core.StackWrapper;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityControlledMiner extends TileEntityBaseMachineInvo implements ITileStackWrapper, ITileRedstoneToggle, ITilePreviewToggle, ITickable {

  private static final String NBTMINING = "mining";
  private static final String NBTDAMAGE = "curBlockDamage";
  private static final String NBTPLAYERID = "uuid";
  private static final String NBTTARGET = "target";
  private static final String NBTHEIGHT = "h";
  private static final String NBT_LIST = "blacklistIfZero";
  private static final int MAX_SIZE = 7;//7 means 15x15
  public static final int TOOLSLOT_INDEX = 0;
  public static int TIMER_FULL = 100;
  public static int maxHeight = 10;
  private boolean isCurrentlyMining;
  private float curBlockDamage;
  private BlockPos targetPos = null;
  private int size = 4;//center plus 4 in each direction = 9x9
  private int needsRedstone = 1;
  private int height = 6;
  private int blacklistIfZero = 0;
  private int renderParticles = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private NonNullList<StackWrapper> stacksWrapped = NonNullList.withSize(4, new StackWrapper());

  public static enum Fields {
    HEIGHT, REDSTONE, SIZE, LISTTYPE, RENDERPARTICLES, TIMER, FUEL;
  }

  public TileEntityControlledMiner() {
    super(1);
    this.initEnergy(BlockMinerSmart.FUEL_COST);
    this.setSlotsForInsert(Arrays.asList(TOOLSLOT_INDEX));
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  private void verifyFakePlayer(WorldServer w) {
    if (fakePlayer == null) {
      fakePlayer = UtilFakePlayer.initFakePlayer(w, this.uuid, "block_miner_smart");
      if (fakePlayer == null) {
        ModCyclic.logger.error("Fake player failed to init ");
      }
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public void update() {
    if (world instanceof WorldServer) {
      verifyUuid(world);
      verifyFakePlayer((WorldServer) world);
      tryEquipItem();
      if (targetPos == null) {
        targetPos = pos.offset(this.getCurrentFacing()); //not sure if this is needed
      }
      if (isRunning()) {
        //now drain energy. and halt if not enough
        if (this.updateEnergyIsBurning() == false) {
          return;
        }
        if (this.updateTimerIsZero()) {
          if (updateMiningProgress()) {
            this.timer = TIMER_FULL;
          }
        }
      }
      else { // we do not have power
        if (isCurrentlyMining) {
          isCurrentlyMining = false;
          resetProgress(targetPos);
        }
      }
    }
  }

  /**
   * return true if block is harvested/broken
   */
  private boolean updateMiningProgress() {
    if (isCurrentlyMining == false) { //we can mine but are not currently. so try moving to a new position
      updateTargetPos();
    }
    if (isTargetValid()) { //if target is valid, allow mining (no air, no blacklist, etc)
      isCurrentlyMining = true;
    }
    else { // no valid target, back out
      isCurrentlyMining = false;
      updateTargetPos();
      resetProgress(targetPos);
    }
    //currentlyMining may have changed, and we are still turned on:
    if (isCurrentlyMining) {
      IBlockState targetState = world.getBlockState(targetPos);
      curBlockDamage += UtilItemStack.getPlayerRelativeBlockHardness(targetState.getBlock(), targetState, fakePlayer.get(), world, targetPos);
      if (curBlockDamage >= 1.0f) {
        isCurrentlyMining = false;
        resetProgress(targetPos);
        if (fakePlayer.get() != null) {
          return fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
        }
      }
      else {
        world.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
      }
    }
    return false;
  }

  private void tryEquipItem() {
    ItemStack equip = this.getStackInSlot(TOOLSLOT_INDEX);
    if (equip.getCount() == 0) {
      this.setInventorySlotContents(TOOLSLOT_INDEX, ItemStack.EMPTY);
    }
    if (equip.isEmpty()) {
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
    }
    else if (!equip.equals(fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND))) {
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, equip);
    }
  }

  private void verifyUuid(World world) {
    if (uuid == null) {
      uuid = UUID.randomUUID();
      IBlockState state = world.getBlockState(this.pos);
      world.notifyBlockUpdate(pos, state, state, 3);
    }
  }

  private boolean isTargetValid() {
    World world = getWorld();
    if (world.isAirBlock(targetPos) || world.getBlockState(targetPos) == null) {
      return false;
    }
    IBlockState targetState = world.getBlockState(targetPos);
    Block target = targetState.getBlock();
    //else check blacklist
    ItemStack itemStack;
    if (this.blacklistIfZero == 0) {
      for (StackWrapper wrap : this.stacksWrapped) {
        if (wrap.isEmpty() == false) {
          itemStack = wrap.getStack().copy();
          if (itemStack.getItem() == Item.getItemFromBlock(target)) {
            return false;
          }
        }
      }
      //was not found in blacklist -> valid
      return true;//blacklist means default trie
    }
    else {//check it as a WHITELIST
      int countEmpty = 0;
      for (StackWrapper wrap : this.stacksWrapped) {
        if (wrap.isEmpty()) {
          countEmpty++;
          continue;
        }
        itemStack = wrap.getStack().copy();
        //its a whitelist, so if its found in the list, its good to go right away
        if (itemStack.getItem() == Item.getItemFromBlock(target)
            || Block.getBlockFromItem(itemStack.getItem()) == target
            //TODO: util class for registry checking
            || Item.REGISTRY.getNameForObject(itemStack.getItem()).toString().equals(Block.REGISTRY.getNameForObject(target).toString())) {
          //also compare registry names (for SPECIAL itemblocks like reeds)
          //so for reeds, the getBlockFromItem is returning Blocks.AIR
          //BUT the registry name for both the block and the item is "minecraft:reeds",, in other words, identical.
          //and linked through the ItemBlockSpecial() class
          return true;
        }
      }
      //wait, if whitelist is empty then it doesnt matter
      if (this.getWrapperCount() == countEmpty) {
        return true;
      }
      return false;//check as blacklist
    }
  }

  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    //so the rand range is basically [0,8], then we left shift into [-4,+4]
    return getPos().offset(this.getCurrentFacing(), size + 1);
  }

  private void updateTargetPos() {
    //lets make it a AxA?
    //always restart here so we dont offset out of bounds
    BlockPos center = getTargetCenter();
    targetPos = center;
    //HEIGHT
    if (height == 0) {
      height = 6;
    } //should be in first time init. it needs a default
    World world = getWorld();
    int rollHeight = world.rand.nextInt(height);
    //now do the vertical
    if (rollHeight > 0) {
      targetPos = targetPos.offset(EnumFacing.UP, rollHeight);
    }
    //negative doesnt work ,but lets not. quarries already exist 
    //HORIZONTAL
    int randNS = world.rand.nextInt(size * 2 + 1) - size;
    int randEW = world.rand.nextInt(size * 2 + 1) - size;
    //(" H, NS, EW : "+ rollHeight +":"+ randNS +":"+ randEW);
    //both can be zero
    if (randNS > 0) {
      targetPos = targetPos.offset(EnumFacing.NORTH, randNS);
    }
    else if (randNS < 0) {
      targetPos = targetPos.offset(EnumFacing.SOUTH, -1 * randNS);
    }
    if (randEW > 0) {
      targetPos = targetPos.offset(EnumFacing.EAST, randEW);
    }
    else if (randEW < 0) {
      targetPos = targetPos.offset(EnumFacing.WEST, -1 * randEW);
    }
    curBlockDamage = 0;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    writeStackWrappers(stacksWrapped, tags);
    tags.setInteger(NBT_REDST, this.needsRedstone);
    if (uuid != null) {
      tags.setString(NBTPLAYERID, uuid.toString());
    }
    if (targetPos != null) {
      tags.setIntArray(NBTTARGET, new int[] { targetPos.getX(), targetPos.getY(), targetPos.getZ() });
    }
    tags.setBoolean(NBTMINING, isCurrentlyMining);
    tags.setFloat(NBTDAMAGE, curBlockDamage);
    tags.setInteger(NBTHEIGHT, height);
    tags.setInteger(NBT_SIZE, size);
    tags.setInteger(NBT_LIST, this.blacklistIfZero);
    tags.setInteger(NBT_RENDER, renderParticles);
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    readStackWrappers(stacksWrapped, tags);
    this.needsRedstone = tags.getInteger(NBT_REDST);
    this.size = tags.getInteger(NBT_SIZE);
    if (tags.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(tags.getString(NBTPLAYERID));
    }
    if (tags.hasKey(NBTTARGET)) {
      int[] coords = tags.getIntArray(NBTTARGET);
      if (coords.length >= 3) {
        targetPos = new BlockPos(coords[0], coords[1], coords[2]);
      }
    }
    isCurrentlyMining = tags.getBoolean(NBTMINING);
    curBlockDamage = tags.getFloat(NBTDAMAGE);
    height = tags.getInteger(NBTHEIGHT);
    blacklistIfZero = tags.getInteger(NBT_LIST);
    this.renderParticles = tags.getInteger(NBT_RENDER);
  }

  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (isCurrentlyMining && uuid != null) {
      resetProgress(pos);
    }
  }

  private void resetProgress(BlockPos targetPos) {
    if (uuid != null) {
      //BlockPos targetPos = pos.offset(state.getValue(BlockMiner.PROPERTYFACING));
      getWorld().sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);
      curBlockDamage = 0;
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case FUEL:
        return this.getEnergyCurrent();
      case HEIGHT:
        return getHeight();
      case REDSTONE:
        return this.needsRedstone;
      case SIZE:
        return this.size;
      case LISTTYPE:
        return blacklistIfZero;
      case RENDERPARTICLES:
        return this.renderParticles;
      case TIMER:
        return this.timer;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case FUEL:
        this.setEnergyCurrent(value);
      break;
      case HEIGHT:
        if (value > maxHeight) {
          value = maxHeight;
        }
        setHeight(value);
      break;
      case REDSTONE:
        needsRedstone = value;
      break;
      case SIZE:
        if (value > MAX_SIZE) {
          value = 1;
        }
        size = value;
      break;
      case LISTTYPE:
        blacklistIfZero = value % 2;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
      case TIMER:
        this.timer = value;
      break;
    }
  }

  public void toggleListType() {
    blacklistIfZero = (blacklistIfZero + 1) % 2;
  }

  public int getHeight() {
    return this.height;//this.getField(Fields.HEIGHT.ordinal());
  }

  public void setHeight(int val) {
    this.height = val;
  }

  @Override
  public boolean receiveClientEvent(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      this.setField(id, value);
      return true;
    }
    else {
      return super.receiveClientEvent(id, value);
    }
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    // Extracts data from a packet (S35PacketUpdateTileEntity) that was sent
    // from the server. Called on client only.
    this.readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public List<BlockPos> getShape() {
    List<BlockPos> allPos = new ArrayList<BlockPos>();
    for (int i = 0; i < this.getHeight(); i++) {
      allPos.addAll(UtilShape.squareHorizontalHollow(getTargetCenter().up(i), size));
    }
    return allPos;
  }

  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }

  @Override
  public StackWrapper getStackWrapper(int i) {
    return stacksWrapped.get(i);
  }

  @Override
  public void setStackWrapper(int i, StackWrapper stack) {
    stacksWrapped.set(i, stack);
  }

  @Override
  public int getWrapperCount() {
    return stacksWrapped.size();
  }
}
