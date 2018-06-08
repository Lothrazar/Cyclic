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
package com.lothrazar.cyclicmagic.block.builderpattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilParticle;
import com.lothrazar.cyclicmagic.core.util.UtilShape;
import com.lothrazar.cyclicmagic.core.util.UtilSound;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityPatternBuilder extends TileEntityBaseMachineInvo implements ITickable, ITilePreviewToggle, ITileRedstoneToggle {

  private final static int MAXIMUM = 32;
  private static final String NBT_REDST = "redstone";
  private static final int TIMER_FULL = 20;
  private static final int TIMER_SKIP = 1;
  private int height = 5;
  private int offsetTargetX = -5;
  private int offsetTargetY = 0;
  private int offsetTargetZ = 1;
  private int offsetSourceX = 5;
  private int offsetSourceY = 0;
  private int offsetSourceZ = 1;
  private int sizeRadius = 4;
  private int timer = 1;
  private int needsRedstone = 1;
  private int renderParticles = 1;
  private int flipX = 0;
  private int flipY = 0;
  private int flipZ = 0;
  private int rotation = 0;//enum value of Rotation
  private Map<String, String> blockToItemOverrides = new HashMap<String, String>();

  enum RenderType {
    OFF, OUTLINE, PHANTOM, SOLID;
  }
  public static enum Fields {
    OFFTARGX, OFFTARGY, OFFTARGZ, SIZER, OFFSRCX, OFFSRCY, OFFSRCZ, HEIGHT, TIMER, REDSTONE, RENDERPARTICLES, ROTATION, FLIPX, FLIPY, FLIPZ;
  }

  public TileEntityPatternBuilder() {
    super(9 + 9);
    this.initEnergy(BlockPatternBuilder.FUEL_COST);
    this.setSlotsForBoth();
    syncBlockItemMap();
  }

  private void syncBlockItemMap() {
    //maybe in config one day!?!??! good enough for now
    blockToItemOverrides.put("minecraft:redstone_wire", "minecraft:redstone");
    blockToItemOverrides.put("minecraft:powered_repeater", "minecraft:repeater");
    blockToItemOverrides.put("minecraft:unpowered_repeater", "minecraft:repeater");
    blockToItemOverrides.put("minecraft:powered_comparator", "minecraft:comparator");
    blockToItemOverrides.put("minecraft:unpowered_comparator", "minecraft:comparator");
    blockToItemOverrides.put("minecraft:lit_redstone_ore", "minecraft:redstone_ore");
    blockToItemOverrides.put("minecraft:tripwire", "minecraft:string");
    blockToItemOverrides.put("minecraft:wall_sign", "minecraft:sign");
    blockToItemOverrides.put("minecraft:standing_sign", "minecraft:sign");
    blockToItemOverrides.put("minecraft:lit_furnace", "minecraft:furnace");
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

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  private BlockPos getCenterTarget() {
    return this.getPos().add(offsetTargetX, offsetTargetY, offsetTargetZ);
  }

  private BlockPos getCenterSrc() {
    return this.getPos().add(offsetSourceX, offsetSourceY, offsetSourceZ);
  }

  private int findSlotForMatch(IBlockState stateToMatch) {
    int slot = -1;
    if (stateToMatch == null || stateToMatch.getBlock() == null) {
      return slot;
    }
    String blockKey, itemKey, itemInSlot;
    ItemStack is;
    Item itemFromState;
    for (int i = 0; i < this.getSizeInventory(); i++) {
      is = this.getStackInSlot(i);
      if (UtilItemStack.isEmpty(is)) {
        continue;
      }
      itemFromState = Item.getItemFromBlock(stateToMatch.getBlock());
      if (itemFromState == is.getItem()) {
        //        ModCyclic.logger.log("normal match without map "+stateToMatch.getBlock().getLocalizedName());
        slot = i;//yep it matches
        break;
      }
      //TODO: util class for registry checking
      blockKey = Block.REGISTRY.getNameForObject(stateToMatch.getBlock()).toString();
      //   ModCyclic.logger.log("blockKey   " + blockKey);
      if (blockToItemOverrides.containsKey(blockKey)) {
        itemKey = blockToItemOverrides.get(blockKey);
        itemInSlot = Item.REGISTRY.getNameForObject(is.getItem()).toString();
        //  ModCyclic.logger.log("!stateToMatch. KEY?" + blockKey + " VS " + itemInSlot);
        if (itemKey.equalsIgnoreCase(itemInSlot)) {
          slot = i;
          //   ModCyclic.logger.log(blockKey + "->stateToMatch mapped to an item ->" + itemKey);
          break;
        }
      }
    }
    return slot;
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public boolean shouldRenderInPass(int pass) {
    return pass < 2;
  }

  @Override
  public void update() {
    // OR maybe projector upgrade
    //and/or new projector block
    if (isRunning() == false) { // it works ONLY if its powered
      return;
    }
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    timer -= 1;
    if (timer <= 0) { //try build one block
      timer = 0;
      List<BlockPos> shapeSrc = this.getSourceShape();
      List<BlockPos> shapeTarget = this.getTargetShape();
      if (shapeSrc.size() <= 0) {
        return;
      }
      int pTarget = world.rand.nextInt(shapeSrc.size());
      BlockPos posSrc = shapeSrc.get(pTarget);
      BlockPos posTarget = shapeTarget.get(pTarget);
      if (this.renderParticles == 1) {
        UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, posSrc);
        UtilParticle.spawnParticle(this.getWorld(), EnumParticleTypes.CRIT_MAGIC, posTarget);
      }
      IBlockState stateToMatch;
      int slot;
      if (world.isAirBlock(posSrc) == false && world.isAirBlock(posTarget)) {
        stateToMatch = world.getBlockState(posSrc);
        slot = this.findSlotForMatch(stateToMatch);
        if (slot < 0) {
          return;
        } //EMPTY
        timer = TIMER_FULL;//now start over
        world.setBlockState(posTarget, stateToMatch);
        this.decrStackSize(slot, 1);
        SoundType type = UtilSound.getSoundFromBlockstate(stateToMatch, world, posTarget);
        if (type != null && type.getPlaceSound() != null) {
          int dim = this.getDimension();
          int range = 18;
          UtilSound.playSoundFromServer(type.getPlaceSound(), SoundCategory.BLOCKS, posTarget, dim, range);
        }
      }
      else { //src IS air, so skip ahead
        timer = TIMER_SKIP;
      }
    }
  }

  public BlockPos getSourceCenter() {
    return this.getPos().add(offsetSourceX, offsetSourceY, offsetSourceZ);
  }

  public BlockPos getTargetCenter() {
    return this.getPos().add(offsetTargetX, offsetTargetY, offsetTargetZ);
  }

  public List<BlockPos> getSourceFrameOutline() {
    BlockPos centerSrc = getSourceCenter();
    List<BlockPos> shapeSrc = UtilShape.cubeFrame(centerSrc, this.sizeRadius, this.height);
    return shapeSrc;
  }

  public List<BlockPos> getTargetFrameOutline() {
    return UtilShape.cubeFrame(getTargetCenter(), this.sizeRadius, this.height);
  }

  private BlockPos convertPosSrcToTarget(BlockPos posSrc) {
    BlockPos centerSrc = this.getCenterSrc();
    int xOffset = posSrc.getX() - centerSrc.getX();
    int yOffset = posSrc.getY() - centerSrc.getY();
    int zOffset = posSrc.getZ() - centerSrc.getZ();
    BlockPos centerTarget = this.getCenterTarget();
    return centerTarget.add(xOffset, yOffset, zOffset);
  }

  public List<BlockPos> getSourceShape() {
    BlockPos centerSrc = this.getSourceCenter();
    return UtilShape.readAllSolid(world, centerSrc, this.sizeRadius, this.height);
  }

  public Map<BlockPos, IBlockState> getShapeFancy(List<BlockPos> sourceShape, List<BlockPos> targetShape) {
    Map<BlockPos, IBlockState> map = new HashMap<BlockPos, IBlockState>();
    for (int i = 0; i < targetShape.size(); i++) {
      BlockPos src = sourceShape.get(i);
      BlockPos targ = targetShape.get(i);
      if (world.isAirBlock(targ))//dont render on top of thing
        map.put(targ, world.getBlockState(src));
    }

    return map;
  }

  public List<BlockPos> getTargetShape() {
    List<BlockPos> shapeSrc = getSourceShape();
    List<BlockPos> shapeTarget = new ArrayList<BlockPos>();
    for (BlockPos p : shapeSrc) {
      shapeTarget.add(this.convertPosSrcToTarget(new BlockPos(p)));
    }
    //rotate 
    shapeTarget = UtilShape.rotateShape(this.getCenterTarget(), shapeTarget, this.getRotation());
    //flip
    BlockPos trueCenter = this.getCenterTarget().up(getHeight() / 2);
    if (getField(Fields.FLIPX) == 1) {
      shapeTarget = UtilShape.flipShape(trueCenter, shapeTarget, EnumFacing.Axis.X);
    }
    if (getField(Fields.FLIPY) == 1) {
      shapeTarget = UtilShape.flipShape(trueCenter, shapeTarget, EnumFacing.Axis.Y);
    }
    if (getField(Fields.FLIPZ) == 1) {
      shapeTarget = UtilShape.flipShape(trueCenter, shapeTarget, EnumFacing.Axis.Z);
    }
    return shapeTarget;
  }

  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.offsetTargetX = compound.getInteger("ox");
    this.offsetTargetY = compound.getInteger("oy");
    this.offsetTargetZ = compound.getInteger("oz");
    this.offsetSourceX = compound.getInteger("sx");
    this.offsetSourceY = compound.getInteger("sy");
    this.offsetSourceZ = compound.getInteger("sz");
    this.sizeRadius = compound.getInteger("r");
    this.height = compound.getInteger("height");
    this.timer = compound.getInteger("timer");
    this.renderParticles = compound.getInteger("render");
    this.needsRedstone = compound.getInteger(NBT_REDST);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("ox", offsetTargetX);
    compound.setInteger("oy", offsetTargetY);
    compound.setInteger("oz", offsetTargetZ);
    compound.setInteger("sx", offsetSourceX);
    compound.setInteger("sy", offsetSourceY);
    compound.setInteger("sz", offsetSourceZ);
    compound.setInteger("r", sizeRadius);
    compound.setInteger("height", height);
    compound.setInteger("timer", timer);
    compound.setInteger("render", renderParticles);
    compound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(compound);
  }

  public int getHeight() {
    return height;
  }

  public Rotation getRotation() {
    return Rotation.values()[this.rotation];
  }

  public String getRotationName() {
    switch (this.getRotation()) {
      case CLOCKWISE_90:
        return "90";
      case CLOCKWISE_180:
        return "180";
      case COUNTERCLOCKWISE_90:
        return "270";
      case NONE:
      break;
    }
    return "None";
  }

  public int getField(Fields f) {
    switch (f) {
      case OFFTARGX:
        return this.offsetTargetX;
      case OFFTARGY:
        return this.offsetTargetY;
      case OFFTARGZ:
        return this.offsetTargetZ;
      case SIZER:
        return this.sizeRadius;
      case OFFSRCX:
        return this.offsetSourceX;
      case OFFSRCY:
        return this.offsetSourceY;
      case OFFSRCZ:
        return this.offsetSourceZ;
      case HEIGHT:
        return this.getHeight();
      case TIMER:
        return this.timer;
      case REDSTONE:
        return this.needsRedstone;
      case RENDERPARTICLES:
        return this.renderParticles;
      case ROTATION:
        return this.rotation;
      case FLIPX:
        return flipX;
      case FLIPY:
        return flipY;
      case FLIPZ:
        return flipZ;
    }
    return 0;
  }

  public void setField(Fields f, int value) {
    //max applies to all fields
    if (value > MAXIMUM && f.ordinal() < Fields.ROTATION.ordinal()) {
      value = MAXIMUM;
    }
    switch (f) {
      case OFFTARGX:
        this.offsetTargetX = value;
      break;
      case OFFTARGY:
        this.offsetTargetY = value;
      break;
      case OFFTARGZ:
        this.offsetTargetZ = value;
      break;
      case SIZER:
        this.sizeRadius = value;
      break;
      case OFFSRCX:
        this.offsetSourceX = value;
      break;
      case OFFSRCY:
        this.offsetSourceY = value;
      break;
      case OFFSRCZ:
        this.offsetSourceZ = value;
      break;
      case HEIGHT:
        this.height = value;
      break;
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % RenderType.values().length;
      break;
      case ROTATION:
        this.rotation = value % Rotation.values().length;
      break;
      case FLIPX:
        flipX = value % 2;
      break;
      case FLIPY:
        flipY = value % 2;
      break;
      case FLIPZ:
        flipZ = value % 2;
      break;
    }
  }

  public RenderType getRenderType() {
    return RenderType.values()[this.renderParticles];
  }
  @Override
  public int getField(int id) {
    return getField(Fields.values()[id]);
  }

  @Override
  public void setField(int id, int value) {
    setField(Fields.values()[id], value);
  }

  public void swapTargetSource() {
    int srcX = this.offsetSourceX;
    int srcY = this.offsetSourceY;
    int srcZ = this.offsetSourceZ;
    this.offsetSourceX = this.offsetTargetX;
    this.offsetSourceY = this.offsetTargetY;
    this.offsetSourceZ = this.offsetTargetZ;
    this.offsetTargetX = srcX;
    this.offsetTargetY = srcY;
    this.offsetTargetZ = srcZ;
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public void togglePreview() {
    int val = (this.renderParticles + 1) % 2;
    this.setField(Fields.RENDERPARTICLES.ordinal(), val);
  }

  @Override
  public boolean isPreviewVisible() {
    return renderParticles == 1;
  }

  @Override
  public List<BlockPos> getShape() {
    return getTargetShape();//special case for this block, not used here
  }


}
