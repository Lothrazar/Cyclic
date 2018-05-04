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
package com.lothrazar.cyclicmagic.block.autouser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.core.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.core.util.UtilFluid;
import com.lothrazar.cyclicmagic.core.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilShape;
import com.lothrazar.cyclicmagic.core.util.UtilString;
import com.lothrazar.cyclicmagic.core.util.UtilWorld;
import com.lothrazar.cyclicmagic.gui.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.gui.ITileSizeToggle;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidActionResult;

public class TileEntityUser extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITileSizeToggle, ITilePreviewToggle, ITickable {

  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private static final String NBT_LR = "lr";
  private static final int MAX_SIZE = 4;//9x9 area 
  public final static int TIMER_FULL = 120;
  public static final int MAX_SPEED = 200;
  public static int maxHeight = 10;
  private int rightClickIfZero = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int needsRedstone = 1;
  private int renderParticles = 0;
  private int toolSlot = 0;
  private int size;
  private int vRange = 2;
  public int yOffset = 0;
  private static List<String> blacklistAll;

  public static enum Fields {
    TIMER, SPEED, REDSTONE, LEFTRIGHT, SIZE, RENDERPARTICLES, Y_OFFSET;
  }

  public TileEntityUser() {
    super(9);
    timer = TIMER_FULL;
    speed = SPEED_FUELED;
    this.initEnergy(BlockUser.FUEL_COST);
    this.setSlotsForInsert(Arrays.asList(0, 1, 2));
    this.setSlotsForExtract(Arrays.asList(3, 4, 5, 6, 7, 8));
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    this.shiftAllUp(7);
    this.spawnParticlesAbove();
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    boolean triggered = this.updateTimerIsZero();
    if (world instanceof WorldServer) {
      verifyUuid(world);
      if (fakePlayer == null) {
        fakePlayer = UtilFakePlayer.initFakePlayer((WorldServer) world, this.uuid, "block_user");
        if (fakePlayer == null) {
          ModCyclic.logger.error("Fake player failed to init ");
          return;
        }
      }
      //fake player facing the same direction as tile. for throwables
      fakePlayer.get().rotationYaw = UtilEntity.getYawFromFacing(this.getCurrentFacing());
      tryEquipItem();
      if (triggered) {
        timer = TIMER_FULL;
        try {
          BlockPos targetPos = this.getTargetPos();
          if (rightClickIfZero == 0) {//right click entities and blocks
            if (this.isInBlacklist(targetPos) == false) {
              this.rightClickBlock(targetPos);
            }
            else {
              ModCyclic.logger.log("IN BLACKLIST OOOO" + targetPos);
            }
          }
          interactEntities(targetPos);
        }
        catch (Exception e) {//exception could come from external third party block/mod/etc
          ModCyclic.logger.error("Automated User Error");
          ModCyclic.logger.error(e.getLocalizedMessage());
          e.printStackTrace();
        }
      }
    }
  }

  private boolean isInBlacklist(BlockPos targetPos) {
    if (world.getBlockState(targetPos) == null
        || world.getBlockState(targetPos).getBlock() == null) {
      return false;
    }
    return UtilString.isInList(blacklistAll, world.getBlockState(targetPos).getBlock().getRegistryName());
  }

  private void interactEntities(BlockPos targetPos) {
    BlockPos entityCenter = getTargetCenter();
    AxisAlignedBB entityRange = UtilEntity.makeBoundingBox(entityCenter, size, vRange);
    List<EntityLivingBase> living = world.getEntitiesWithinAABB(EntityLivingBase.class, entityRange);
    List<EntityMinecart> carts = world.getEntitiesWithinAABB(EntityMinecart.class, entityRange);
    List<Entity> all = new ArrayList<Entity>(living);
    all.addAll(carts);//works since  they share a base class but no overlap
    if (rightClickIfZero == 0) {//right click entities and blocks
      Collections.shuffle(all);
      this.getWorld().markChunkDirty(targetPos, this);
      for (Entity ent : all) {//both living and minecarts
        // on the line below: NullPointerException  at com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser.func_73660_a(TileMachineUser.java:101)
        if (world.isRemote == false &&
            ent != null && ent.isDead == false
            && fakePlayer != null && fakePlayer.get() != null) {
          validateTool(); //recheck this at every step so we dont go negative
          if (EnumActionResult.FAIL != fakePlayer.get().interactOn(ent, EnumHand.MAIN_HAND)) {
            this.tryDumpFakePlayerInvo(false);
            break;//dont do every entity in teh whole batch
          }
        }
      }
    }
    else { // left click (attack) entities
      ItemStack held = fakePlayer.get().getHeldItemMainhand();
      fakePlayer.get().onGround = true;
      int countDamaged = 0;
      for (EntityLivingBase ent : living) {// only living, not minecarts
        if (ent == null || ent.isDead) {
          continue;
        } //wont happen eh
        fakePlayer.get().attackTargetEntityWithCurrentItem(ent);
        //THANKS TO FORUMS http://www.minecraftforge.net/forum/index.php?topic=43152.0
        IAttributeInstance damage = new AttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        if (held.isEmpty() == false) {
          for (AttributeModifier modifier : held.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
            damage.applyModifier(modifier);
          }
        }
        float dmgVal = (float) damage.getAttributeValue();
        float f1 = EnchantmentHelper.getModifierForCreature(held, (ent).getCreatureAttribute());
        if (ent.attackEntityFrom(DamageSource.causePlayerDamage(fakePlayer.get()), dmgVal + f1)) {
          // count if attack did not fail
          countDamaged++;
          if (BlockUser.maxAttackPer > 0 && countDamaged >= BlockUser.maxAttackPer) {
            // config is enabled, and it say stop
            break;
          }
        }
      }
    }
  }

  private void rightClickBlock(BlockPos targetPos) {
    if (rightClickFluidAttempt(targetPos)) {
      return;
    }
    if (world.isAirBlock(targetPos)) {
      return;
    }
    //  ItemStack previousHeldCopy = fakePlayer.get().getHeldItemMainhand().copy();
    //dont ever place a block. they want to use it on an entity
    EnumActionResult r = fakePlayer.get().interactionManager.processRightClickBlock(fakePlayer.get(), world, fakePlayer.get().getHeldItemMainhand(), EnumHand.MAIN_HAND, targetPos, EnumFacing.UP, .5F, .5F, .5F);
    if (r != EnumActionResult.SUCCESS) {
      //if its a throwable item, it happens on this line down below, the process right click
      r = fakePlayer.get().interactionManager.processRightClick(fakePlayer.get(), world, fakePlayer.get().getHeldItemMainhand(), EnumHand.MAIN_HAND);
      if (fakePlayer.get().getHeldItemMainhand().getCount() == 0) {
        //some items from some mods dont handle stack size zero and trigger it to empty, so handle that edge case
        inv.set(toolSlot, ItemStack.EMPTY);
        fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      }
      //if throw has happened, success is true
      if (r != EnumActionResult.SUCCESS) {
        ActionResult<ItemStack> res = fakePlayer.get().getHeldItemMainhand().getItem().onItemRightClick(world, fakePlayer.get(), EnumHand.MAIN_HAND);
        if (res == null || res.getType() != EnumActionResult.SUCCESS) {
          //this item onrightclick would/should/could work for GLASS_BOTTLE...except
          //it uses player Ray Trace to get target. which is null for fakes
          //TODO: maybe one solution is to extend FakePlayer to run a rayrace somehow
          //but how to set/manage current lookpos
          //so hakcy time
          if (fakePlayer.get().getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE && world.getBlockState(targetPos).getMaterial() == Material.WATER) {
            ItemStack itemstack = fakePlayer.get().getHeldItemMainhand();
            EntityPlayer p = fakePlayer.get();
            world.playSound(p, p.posX, p.posY, p.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            itemstack.shrink(1);
            ItemStack is = new ItemStack(Items.POTIONITEM);
            PotionUtils.addPotionToItemStack(is, PotionTypes.WATER);
            this.tryDumpStacks(Arrays.asList(is));
          }
          //          else {
          //            ItemStack currentHeldCopy = fakePlayer.get().getHeldItemMainhand().copy();
          //            boolean equalsPrevious = ItemStack.areItemStacksEqual(previousHeldCopy, currentHeldCopy);
          //            //last chance. EX: Pixelmon trees
          //            // https://github.com/PrinceOfAmber/Cyclic/issues/736
          //            fakePlayer.get().interactionManager.onBlockClicked(targetPos, EnumFacing.UP);
          //            //spit out my main hand item ONLY IF i wasnt holding it before ( new aquisition )
          //            this.tryDumpFakePlayerInvo(!equalsPrevious);
          //          }
        }
      }
    }
  }

  private void tryDumpStacks(List<ItemStack> toDump) {
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(toDump, this, 3, 9);
    ///only drop now that its full
    BlockPos dropHere = getTargetPos();
    for (ItemStack s : toDrop) {
      if (!s.isEmpty()) {
        EntityItem entityItem = UtilItemStack.dropItemStackInWorld(world, dropHere, s.copy());
        if (entityItem != null && world.isRemote) {
          entityItem.setVelocity(0, 0, 0);
        }
        s.setCount(0);
      }
    }
  }

  private void tryDumpFakePlayerInvo(boolean includeMainHand) {
    ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
    for (int i = 0; i < fakePlayer.get().inventory.mainInventory.size(); i++) {
      ItemStack s = fakePlayer.get().inventory.mainInventory.get(i);
      if (includeMainHand == false &&
          fakePlayer.get().inventory.currentItem == i) {
        //example: dont push over tools or weapons in certain cases
        continue;
      }
      if (s.isEmpty() == false) {
        ModCyclic.logger.log("fake Player giving out item stack" + s.getCount() + s.getDisplayName() + "_tryDumpFakePlayerInvo " + includeMainHand);//leaving in release
        toDrop.add(s.copy());
        fakePlayer.get().inventory.mainInventory.set(i, ItemStack.EMPTY);
      }
    }
    tryDumpStacks(toDrop);
  }

  private boolean rightClickFluidAttempt(BlockPos targetPos) {
    ItemStack maybeTool = fakePlayer.get().getHeldItemMainhand();
    if (maybeTool != null && !maybeTool.isEmpty() && UtilFluid.stackHasFluidHandler(maybeTool)) {
      if (UtilFluid.hasFluidHandler(world.getTileEntity(targetPos), this.getCurrentFacing().getOpposite())) {//tile has fluid
        ItemStack originalRef = maybeTool.copy();
        int hack = (maybeTool.getCount() == 1) ? 1 : 0;//HAX: if bucket stack size is 1, it somehow doesnt work so yeah. good enough EH?
        maybeTool.grow(hack);
        boolean success = UtilFluid.interactWithFluidHandler(fakePlayer.get(), this.world, targetPos, this.getCurrentFacing().getOpposite());
        if (success) {
          if (UtilFluid.isEmptyOfFluid(originalRef)) { //original was empty.. maybe its full now IDK
            maybeTool.shrink(1 + hack);
          }
          else {//original had fluid in it. so make sure we drain it now hey
            UtilFluid.drainOneBucket(maybeTool.splitStack(1));
            // drained.setCount(1);
            // UtilItemStack.dropItemStackInWorld(this.world, getCurrentFacingPos(), drained);
            maybeTool.shrink(1 + hack);
          }
          this.tryDumpFakePlayerInvo(false);
        }
      }
      else {//no tank, just open world
        //dispense stack so either pickup or place liquid
        if (UtilFluid.isEmptyOfFluid(maybeTool)) {
          FluidActionResult res = UtilFluid.fillContainer(world, targetPos, maybeTool, this.getCurrentFacing());
          if (res != FluidActionResult.FAILURE) {
            maybeTool.shrink(1);
            UtilItemStack.dropItemStackInWorld(this.world, getCurrentFacingPos(), res.getResult());
            return true;
          }
        }
        else {
          ItemStack drainedStackOrNull = UtilFluid.dumpContainer(world, targetPos, maybeTool);
          if (drainedStackOrNull != null) {
            maybeTool.shrink(1);
            UtilItemStack.dropItemStackInWorld(this.world, getCurrentFacingPos(), drainedStackOrNull);
          }
        }
        return true;
      }
    }
    return false;
  }

  /**
   * detect if tool stack is empty or destroyed and reruns equip
   */
  private void validateTool() {
    ItemStack maybeTool = getStackInSlot(toolSlot);
    if (!maybeTool.isEmpty() && maybeTool.getCount() < 0) {
      maybeTool = ItemStack.EMPTY;
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      inv.set(toolSlot, ItemStack.EMPTY);
    }
  }

  private ItemStack tryEquipItem() {
    ItemStack maybeTool = getStackInSlot(toolSlot);
    if (!maybeTool.isEmpty()) {
      //do we need to make it null
      if (maybeTool.getCount() <= 0) {
        maybeTool = ItemStack.EMPTY;
      }
    }
    fakePlayer.get().setPosition(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());//seems to help interact() mob drops like milk
    fakePlayer.get().onUpdate();//trigger   ++this.ticksSinceLastSwing; among other things
    if (maybeTool.isEmpty()) {//null for any reason
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      inv.set(toolSlot, ItemStack.EMPTY);
    }
    else {
      //so its not null
      if (!maybeTool.equals(fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND))) {
        fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, maybeTool);
      }
    }
    return maybeTool;
  }

  private void verifyUuid(World world) {
    if (uuid == null) {
      uuid = UUID.randomUUID();
      IBlockState state = world.getBlockState(this.pos);
      world.notifyBlockUpdate(pos, state, state, 3);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (uuid != null) {
      compound.setString(NBTPLAYERID, uuid.toString());
    }
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setInteger(NBT_LR, rightClickIfZero);
    compound.setInteger(NBT_SIZE, size);
    compound.setInteger(NBT_RENDER, renderParticles);
    compound.setInteger("yoff", yOffset);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (compound.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(compound.getString(NBTPLAYERID));
    }
    needsRedstone = compound.getInteger(NBT_REDST);
    rightClickIfZero = compound.getInteger(NBT_LR);
    size = compound.getInteger(NBT_SIZE);
    renderParticles = compound.getInteger(NBT_RENDER);
    yOffset = compound.getInteger("yoff");
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case SPEED:
        return getSpeed();
      case TIMER:
        return getTimer();
      case REDSTONE:
        return this.needsRedstone;
      case SIZE:
        return this.size;
      case LEFTRIGHT:
        return this.rightClickIfZero;
      case RENDERPARTICLES:
        return this.renderParticles;
      case Y_OFFSET:
        return this.yOffset;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case Y_OFFSET:
        if (value > 1) {
          value = -1;
        }
        this.yOffset = value;
      break;
      case SPEED:
        this.setSpeed(value);
      break;
      case TIMER:
        if (value < 0) {
          value = 0;
        }
        timer = Math.min(value, TIMER_FULL);
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case LEFTRIGHT:
        if (value > 1) {
          value = 0;
        }
        this.rightClickIfZero = value;
      break;
      case SIZE:
        this.size = value;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
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

  public int getTimer() {
    return timer;
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone == 1) ? 0 : 1;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public void toggleSizeShape() {
    this.size++;
    if (this.size > MAX_SIZE) {
      this.size = 0;
    }
  }

  @Override
  public int getSpeed() {
    return speed;
  }

  @Override
  public void setSpeed(int value) {
    if (value < 1) {
      value = 1;
    }
    speed = Math.min(value, MAX_SPEED);
  }

  private BlockPos getTargetPos() {
    BlockPos targetPos = UtilWorld.getRandomPos(getWorld().rand, getTargetCenter(), this.size);
    return targetPos;
  }

  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), this.size + 1).offset(EnumFacing.UP, yOffset);
  }

  @Override
  public void togglePreview() {
    this.renderParticles = (renderParticles + 1) % 2;
  }

  @Override
  public List<BlockPos> getShape() {
    return UtilShape.squareHorizontalHollow(getTargetCenter(), this.size);
  }

  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }

  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    String[] deflist = new String[0];
    String[] blacklist = config.getStringList("AutoUserTargetBlacklist",
        category, deflist, "Blocks in-world that cannot be targeted by the auto user.  Use block id; for example minecraft:chest");
    blacklistAll = NonNullList.from("",
        blacklist);
  }
}
