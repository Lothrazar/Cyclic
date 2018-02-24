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
package com.lothrazar.cyclicmagic.component.miner;
import java.lang.ref.WeakReference;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

/**
 * 
 * @author Lothrazar (Sam Bassett)
 *
 *         This entire file basically started as a carbon copy from lumien
 *         https://github.com/lumien231/Random-Things/blob/master/src/main/java/lumien/randomthings/tileentity/TileEntityBlockBreaker.java Which was released open source under MIT license
 *         https://github.com/lumien231/Random-Things/blob/master/README.md copy of lumien license
 *
 *
 *         <quote>"
 *
 *         Source Code of the Random Things mod. Feel free to make pull requests for translation and other stuff.
 * 
 *         License
 * 
 *         The MIT License (MIT)
 * 
 *         Copyright (c) <2015> lumien231 (https://github.com/lumien231)
 * 
 *         Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without
 *         restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 *         Software is furnished to do so, subject to the following conditions:
 * 
 *         The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *         NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *         OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. "</quote>
 *
 *         So, i am following the License by including that and the authors name and links
 *
 *         REASONS for using it: i knew how to break a block instantly, but i had no idea how to have the mining slow animation speed that a player yas people on forums said to use a 'fake player' so
 *         i researched that on github and found this
 *
 *         DIFFERENCES FROM ORIGINAL that I added:
 *
 *         - this can only face horizontally, not up and down (defined in block) - different recipe - different texture/model - can be disabled in the config system just like other parts of this mod -
 *         only works when redstone powers it on - Slower mining speed - I will be making multiple versions: for axe/pick/shovel (use axe tool instead of diamond pick) and ill pass those in as enum
 *         flags - possibly also making a 3x3 version
 * 
 */
public class TileEntityBlockMiner extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  public TileEntityBlockMiner() {
    super(0);
  }
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private static final String NBT_REDST = "redstone";
  private UUID uuid;
  private boolean isCurrentlyMining;
  private WeakReference<FakePlayer> fakePlayer;
  private float curBlockDamage;
  private int needsRedstone = 1;
  private BlockPos targetPos = null;
  public static enum Fields {
    REDSTONE
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (isRunning()) {
      this.spawnParticlesAbove();
    }
    World world = this.getWorld();
    if (world instanceof WorldServer) {
      verifyUuid(world);
      if (fakePlayer == null) {
        fakePlayer = UtilFakePlayer.initFakePlayer((WorldServer) world, this.uuid, "block_miner");
        if (fakePlayer == null) {
          ModCyclic.logger.error("Fake player failed to init ");
          return;
        }
      }
      tryEquipItem();
      //      BlockMiner.MinerType minerType = ((BlockMiner) world.getBlockState(pos).getBlock()).getMinerType();
      BlockPos start = pos.offset(this.getCurrentFacing());
      if (targetPos == null) {
        targetPos = start; //not sure if this is needed
      }
      if (isRunning()) {
        if (isCurrentlyMining == false) { //we can mine but are not currently
          if (!world.isAirBlock(targetPos)) { //we have a valid target
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
        IBlockState targetState = world.getBlockState(targetPos);
        curBlockDamage += UtilItemStack.getPlayerRelativeBlockHardness(targetState.getBlock(), targetState, fakePlayer.get(), world, targetPos);
        if (curBlockDamage >= 1.0f) {
          isCurrentlyMining = false;
          resetProgress(targetPos);
          if (fakePlayer != null && fakePlayer.get() != null) {
            fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
          }
        }
        else {
          world.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
  }
  private void verifyUuid(World world) {
    if (uuid == null) {
      uuid = UUID.randomUUID();
      IBlockState state = world.getBlockState(this.pos);
      world.notifyBlockUpdate(pos, state, state, 3);
    }
  }
  private void tryEquipItem() {
    //only equip if empty handed, dont spam
    if (fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
      ItemStack unbreakingPickaxe = new ItemStack(Items.DIAMOND_PICKAXE, 1);
      unbreakingPickaxe.addEnchantment(Enchantments.EFFICIENCY, 3);
      unbreakingPickaxe.setTagCompound(new NBTTagCompound());
      unbreakingPickaxe.getTagCompound().setBoolean("Unbreakable", true);
      fakePlayer.get().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, unbreakingPickaxe);
    }
  }
  private static final String NBTMINING = "mining";
  private static final String NBTDAMAGE = "curBlockDamage";
  private static final String NBTPLAYERID = "uuid";
  private static final String NBTTARGET = "target";
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    if (uuid != null) {
      tagCompound.setString(NBTPLAYERID, uuid.toString());
    }
    if (targetPos != null) {
      tagCompound.setIntArray(NBTTARGET, new int[] { targetPos.getX(), targetPos.getY(), targetPos.getZ() });
    }
    tagCompound.setBoolean(NBTMINING, isCurrentlyMining);
    tagCompound.setFloat(NBTDAMAGE, curBlockDamage);
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    if (tagCompound.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(tagCompound.getString(NBTPLAYERID));
    }
    if (tagCompound.hasKey(NBTTARGET)) {
      int[] coords = tagCompound.getIntArray(NBTTARGET);
      if (coords.length >= 3) {
        targetPos = new BlockPos(coords[0], coords[1], coords[2]);
      }
    }
    isCurrentlyMining = tagCompound.getBoolean(NBTMINING);
    curBlockDamage = tagCompound.getFloat(NBTDAMAGE);
  }
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (isCurrentlyMining && uuid != null) {
      resetProgress(pos);
    }
  }
  private void resetProgress(BlockPos targetPos) {
    if (uuid != null) {
      getWorld().sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);
      curBlockDamage = 0;
    }
  }
  //  @Override
  //  public EnumFacing getCurrentFacing() {
  //    return BlockBaseFacingOmni.getCurrentFacing(this.getWorld(), this.getPos());
  //  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {}
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
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
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
