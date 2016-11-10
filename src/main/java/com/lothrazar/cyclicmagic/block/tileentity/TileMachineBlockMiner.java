package com.lothrazar.cyclicmagic.block.tileentity;
import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.BlockMiner;
import com.lothrazar.cyclicmagic.block.BlockMiner.MinerType;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

/**
 * 
 * @author Lothrazar (Sam Bassett)
 *
 *         This entire file basically started as a carbon copy from lumien
 *         https://github.com/lumien231/Random-Things/blob/master/src/main/java/lumien/randomthings/tileentity/TileEntityBlockBreaker.java
 *         Which was released open source under MIT license
 *         https://github.com/lumien231/Random-Things/blob/master/README.md copy
 *         of lumien license
 *
 *
 *         <quote>"
 *
 *         Source Code of the Random Things mod. Feel free to make pull requests
 *         for translation and other stuff.
 * 
 *         License
 * 
 *         The MIT License (MIT)
 * 
 *         Copyright (c) <2015> lumien231 (https://github.com/lumien231)
 * 
 *         Permission is hereby granted, free of charge, to any person obtaining
 *         a copy of this software and associated documentation files (the
 *         "Software"), to deal in the Software without restriction, including
 *         without limitation the rights to use, copy, modify, merge, publish,
 *         distribute, sublicense, and/or sell copies of the Software, and to
 *         permit persons to whom the Software is furnished to do so, subject to
 *         the following conditions:
 * 
 *         The above copyright notice and this permission notice shall be
 *         included in all copies or substantial portions of the Software.
 * 
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *         EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *         MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *         NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *         BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *         ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *         CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *         SOFTWARE. "</quote>
 *
 *         So, i am following the License by including that and the authors name
 *         and links
 *
 *         REASONS for using it: i knew how to break a block instantly, but i
 *         had no idea how to have the mining slow animation speed that a player
 *         yas people on forums said to use a 'fake player' so i researched that
 *         on github and found this
 *
 *         DIFFERENCES FROM ORIGINAL that I added:
 *
 *         - this can only face horizontally, not up and down (defined in block)
 *         - different recipe - different texture/model - can be disabled in the
 *         config system just like other parts of this mod - only works when
 *         redstone powers it on - Slower mining speed - I will be making
 *         multiple versions: for axe/pick/shovel (use axe tool instead of
 *         diamond pick) and ill pass those in as enum flags - possibly also
 *         making a 3x3 version
 * 
 */
public class TileMachineBlockMiner extends TileEntityBaseMachineInvo implements ITileRedstoneToggle  {
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private UUID uuid;
  private boolean isCurrentlyMining;
  private WeakReference<FakePlayer> fakePlayer;
  private float curBlockDamage;
  private int needsRedstone;
  private BlockPos targetPos = null;
  public static enum Fields {
    REDSTONE
  }
  @Override
  public void update() {
    if (!(this.onlyRunIfPowered() && this.isPowered() == false)) {
      this.spawnParticlesAbove();
    }
    if (worldObj.isRemote == false && worldObj instanceof WorldServer && (WorldServer) worldObj != null) {
      if (fakePlayer == null) {
        fakePlayer = UtilFakePlayer.initFakePlayer((WorldServer) worldObj);
        if (fakePlayer == null) {
          ModMain.logger.warn("Warning: Fake player failed to init ");
          return;
        }
        equipItem();
      }
      if (uuid == null) {
        uuid = UUID.randomUUID();
        IBlockState state = worldObj.getBlockState(this.pos);
        worldObj.notifyBlockUpdate(pos, state, state, 3);
      }
      if (fakePlayer.get().getHeldItemMainhand() == null) {
        equipItem();
      }
      BlockMiner.MinerType minerType = ((BlockMiner) worldObj.getBlockState(pos).getBlock()).getMinerType();
      BlockPos start = pos.offset(this.getCurrentFacing());
      if (targetPos == null) {
        targetPos = start; //not sure if this is needed
      }
      if (!(this.onlyRunIfPowered() && this.isPowered() == false)) {
        if (isCurrentlyMining == false) { //we can mine but are not currently
          this.updateTargetPos(start, minerType);
          if (!worldObj.isAirBlock(targetPos)) { //we have a valid target
            isCurrentlyMining = true;
            curBlockDamage = 0;
          }
          else { // no valid target, back out
            isCurrentlyMining = false;
            resetProgress(targetPos);
          }
        }
        // else    we can mine ANd we are already mining, dont change anything eh
      }
      else { // we do not have power
        if (isCurrentlyMining) {
          isCurrentlyMining = false;
          resetProgress(targetPos);
        }
      }
      if (isCurrentlyMining) {
        IBlockState targetState = worldObj.getBlockState(targetPos);
        curBlockDamage += UtilItem.getPlayerRelativeBlockHardness(targetState.getBlock(), targetState, fakePlayer.get(), worldObj, targetPos);
        if (curBlockDamage >= 1.0f) {
          isCurrentlyMining = false;
          resetProgress(targetPos);
          if (fakePlayer != null && fakePlayer.get() != null) {
            fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
          }
        }
        else {
          worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
  }
  private void equipItem() {
    ItemStack unbreakingPickaxe = new ItemStack(Items.DIAMOND_PICKAXE, 1);
    unbreakingPickaxe.addEnchantment(Enchantments.EFFICIENCY, 3);
    unbreakingPickaxe.setTagCompound(new NBTTagCompound());
    unbreakingPickaxe.getTagCompound().setBoolean("Unbreakable", true);
    fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, unbreakingPickaxe);
  }
  private void updateTargetPos(BlockPos start, MinerType minerType) {
    targetPos = start;//always restart here so we dont offset out of bounds
    if (minerType == MinerType.SINGLE) {// stay on target
      return;
    }
    EnumFacing facing = this.getCurrentFacing();
    BlockPos center = start.offset(facing);//move one more over so we are in the exact center of a 3x3x3 area
    //else we do a 3x3 
    int rollFull = worldObj.rand.nextInt(9 * 3);
    int rollHeight = rollFull / 9;
    int rollDice = rollFull % 9;//worldObj.rand.nextInt(9); //TODO: dont have it switch while mining and get this working
    //then do the area
    // 1 2 3
    // 4 - 5
    // 6 7 8
    switch (rollDice) {
    case 0:
      targetPos = center;
      break;
    case 1:
      targetPos = center.offset(EnumFacing.NORTH).offset(EnumFacing.WEST);
      break;
    case 2:
      targetPos = center.offset(EnumFacing.NORTH);
      break;
    case 3:
      targetPos = center.offset(EnumFacing.NORTH).offset(EnumFacing.EAST);
      break;
    case 4:
      targetPos = center.offset(EnumFacing.WEST);
      break;
    case 5:
      targetPos = center.offset(EnumFacing.EAST);
      break;
    case 6:
      targetPos = center.offset(EnumFacing.SOUTH).offset(EnumFacing.WEST);
      break;
    case 7:
      targetPos = center.offset(EnumFacing.SOUTH);
      break;
    case 8:
      targetPos = center.offset(EnumFacing.SOUTH).offset(EnumFacing.EAST);
      break;
    }
    //now do the vertical
    if (rollHeight > 0) {
      targetPos = targetPos.offset(EnumFacing.UP, rollHeight);
    }
    return;
  }
  private static final String NBTMINING = "mining";
  private static final String NBTDAMAGE = "curBlockDamage";
  private static final String NBTPLAYERID = "uuid";
  private static final String NBTTARGET = "target";
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (uuid != null) {
      compound.setString(NBTPLAYERID, uuid.toString());
    }
    if (targetPos != null) {
      compound.setIntArray(NBTTARGET, new int[] { targetPos.getX(), targetPos.getY(), targetPos.getZ() });
    }
    compound.setBoolean(NBTMINING, isCurrentlyMining);
    compound.setFloat(NBTDAMAGE, curBlockDamage);
    return compound;
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    if (compound.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(compound.getString(NBTPLAYERID));
    }
    if (compound.hasKey(NBTTARGET)) {
      int[] coords = compound.getIntArray(NBTTARGET);
      if (coords.length >= 3) {
        targetPos = new BlockPos(coords[0], coords[1], coords[2]);
      }
    }
    isCurrentlyMining = compound.getBoolean(NBTMINING);
    curBlockDamage = compound.getFloat(NBTDAMAGE);
  }
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (isCurrentlyMining && uuid != null) {
      resetProgress(pos);
    }
  }
  private void resetProgress(BlockPos targetPos) {
    if (uuid != null) {
      worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);
      curBlockDamage = 0;
    }
  }
  @Override
  public int getSizeInventory() {
    // TODO Auto-generated method stub
    return 0;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    // TODO Auto-generated method stub
    
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    // TODO Auto-generated method stub
    return null;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount())
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
  private boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
