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
package com.lothrazar.cyclicmagic.block.forester;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilOreDictionary;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityForester extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITilePreviewToggle, ITickable {

  private static final String[] validTargetsOreDict = new String[] { "logWood", "treeLeaves" };
  private static final String[] validSaplingsOreDict = new String[] { "treeSapling" };
  private static final String NBT_REDST = "redstone";
  private static final String NBTMINING = "mining";
  private static final String NBTDAMAGE = "curBlockDamage";
  private static final String NBTPLAYERID = "uuid";
  static final int MAX_HEIGHT = 32;
  private int height = MAX_HEIGHT;
  private static final int MAX_SIZE = 9;//radius 7 translates to 15x15 area (center block + 7 each side)
  private int size = MAX_SIZE;
  private boolean isCurrentlyMining;
  private float curBlockDamage;
  private BlockPos targetPos = null;
  private int needsRedstone = 1;
  private int renderParticles = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;

  public static enum Fields {
    REDSTONE, RENDERPARTICLES, TIMER, FUEL, SIZE, HEIGHT;
  }

  public TileEntityForester() {
    super(18);
    this.initEnergy(BlockForester.FUEL_COST);
    this.setSlotsForInsert(0, 18);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  private void verifyFakePlayer(WorldServer w) {
    if (fakePlayer == null) {
      fakePlayer = UtilFakePlayer.initFakePlayer(w, this.uuid, this.getBlockType().getUnlocalizedName());
      if (fakePlayer == null) {
        ModCyclic.logger.error("Fake player failed to init ");
      }
    }
  }

  @Override
  public void update() {
    if (!isRunning()) {
      return;
    }
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    if (world instanceof WorldServer) {
      verifyUuid(world);
      verifyFakePlayer((WorldServer) world);
      tryEquipItem();
      if (targetPos == null) {
        targetPos = this.getTargetCenter(); // start at center for fresh placements
      }
      this.shiftAllUp(1);
      this.updatePlantSaplings();
      this.updateMiningProgress();
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return isSaplingValid(stack);
  }

  private void updatePlantSaplings() {
    ItemStack sapling = this.getStackInSlot(0);
    if (this.isSaplingValid(sapling)
        && targetPos.getY() == this.pos.getY() //only at same level as machine
        && world.isAirBlock(this.targetPos)
        && world.isSideSolid(targetPos.down(), EnumFacing.UP)) {
      if (fakePlayer.get().getHeldItemOffhand().isEmpty()) {
        fakePlayer.get().setHeldItem(EnumHand.OFF_HAND, sapling);
      }
      //player uses the sapling with offhand
      fakePlayer.get().interactionManager.processRightClickBlock(fakePlayer.get(), world, sapling, EnumHand.OFF_HAND,
          this.targetPos.down(), EnumFacing.UP, 0.5F, 0.5F, 0.5F);
    }
  }

  /**
   * return true if block is harvested/broken
   */
  private boolean updateMiningProgress() {
    if (this.isPreviewVisible()) {
      UtilParticle.spawnParticlePacket(EnumParticleTypes.DRAGON_BREATH, this.targetPos, this.getDimension());
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
    if (fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
      ItemStack unbreakingPickaxe = new ItemStack(Items.DIAMOND_AXE, 1);
      unbreakingPickaxe.addEnchantment(Enchantments.LOOTING, 3);
      unbreakingPickaxe.addEnchantment(Enchantments.EFFICIENCY, 5);
      unbreakingPickaxe.setTagCompound(new NBTTagCompound());
      unbreakingPickaxe.getTagCompound().setBoolean("Unbreakable", true);
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, unbreakingPickaxe);
    }
  }

  private void verifyUuid(World world) {
    if (uuid == null) {
      uuid = UUID.randomUUID();
      IBlockState state = world.getBlockState(this.pos);
      world.notifyBlockUpdate(pos, state, state, 3);
    }
  }

  private boolean isSaplingValid(ItemStack sapling) {
    return UtilOreDictionary.doesMatchOreDict(sapling, validSaplingsOreDict);
  }

  private boolean isTargetValid() {
    World world = getWorld();
    if (world.isAirBlock(targetPos) || world.getBlockState(targetPos) == null) {
      return false;
    }
    IBlockState targetState = world.getBlockState(targetPos);
    Block target = targetState.getBlock();
    return UtilOreDictionary.doesMatchOreDict(new ItemStack(target), validTargetsOreDict);
  }

  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    //so the rand range is basically [0,8], then we left shift into [-4,+4]
    return getPos();
  }

  private void updateTargetPos() {
    //spiraling outward from center
    //first are we out of bounds? if so start at center + 1
    int minX = this.pos.getX() - this.size;
    int maxX = this.pos.getX() + this.size;
    int minY = this.pos.getY();
    int maxY = this.pos.getY() + height - 1;
    int minZ = this.pos.getZ() - this.size;
    int maxZ = this.pos.getZ() + this.size;
    //first we see if this column is done by going bottom to top
    this.targetPos = this.targetPos.add(0, 1, 0);
    if (this.targetPos.getY() <= maxY) {
      return;//next position is valid
    }
    //when we are at the top, only THEN we move to a new horizontal x,z coordinate
    //starting from the base. first move X left to right only
    targetPos = new BlockPos(targetPos.getX() + 1, minY, targetPos.getZ());
    if (targetPos.getX() <= maxX) {
      return;
    }
    //end of the line
    //so start over like a typewriter, moving up one Z row
    targetPos = new BlockPos(minX, targetPos.getY(), targetPos.getZ() + 1);
    if (targetPos.getZ() <= maxZ) {
      return;
    }
    //this means we have passed over the threshold of ALL coordinates
    targetPos = new BlockPos(minX, minY, minZ);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_REDST, this.needsRedstone);
    if (uuid != null) {
      tags.setString(NBTPLAYERID, uuid.toString());
    }
    tags.setBoolean(NBTMINING, isCurrentlyMining);
    tags.setFloat(NBTDAMAGE, curBlockDamage);
    tags.setInteger(NBT_SIZE, size);
    tags.setInteger("ht", height);
    tags.setInteger(NBT_RENDER, renderParticles);
    if (targetPos != null) {
      UtilNBT.setTagBlockPos(tags, targetPos);
    }
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.needsRedstone = tags.getInteger(NBT_REDST);
    this.size = tags.getInteger(NBT_SIZE);
    this.height = tags.getInteger("ht");
    if (tags.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(tags.getString(NBTPLAYERID));
    }
    this.targetPos = UtilNBT.getTagBlockPos(tags);
    isCurrentlyMining = tags.getBoolean(NBTMINING);
    curBlockDamage = tags.getFloat(NBTDAMAGE);
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
      case REDSTONE:
        return this.needsRedstone;
      case RENDERPARTICLES:
        return this.renderParticles;
      case TIMER:
        return this.timer;
      case FUEL:
        return this.getEnergyCurrent();
      case SIZE:
        return size;
      case HEIGHT:
        return height;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        needsRedstone = value;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
      case TIMER:
        this.timer = value;
      break;
      case FUEL:
        this.setEnergyCurrent(value);
      break;
      case SIZE:
        if (value > MAX_SIZE) {
          value = 1;
        }
        size = value;
      break;
      case HEIGHT:
        this.height = Math.min(value, MAX_HEIGHT);
      break;
    }
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
    return UtilShape.squareHorizontalHollow(this.pos, size);
  }

  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }
}
