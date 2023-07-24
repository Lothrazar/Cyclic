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
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import com.lothrazar.cyclicmagic.data.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import com.lothrazar.cyclicmagic.util.UtilInventoryTransfer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilString;
import com.lothrazar.cyclicmagic.util.UtilWorld;
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

public class TileEntityUser extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITilePreviewToggle, ITickable {

  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private static final String NBT_LR = "lr";
  private static final int MAX_SIZE = 9;//WAS 4 9x9 area 
  //  public final static int TIMER_FULL = 120;
  public static int MAX_SPEED = 200;
  public static int MIN_SPEED = 1;
  private static final int SLOT_TOOL = 0;
  private static final int SLOT_OUTPUT_START = 3;
  private static final int INV_SIZE = 9;
  public static int maxHeight = 10;
  private int rightClickIfZero = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int size;
  private int vRange = 2;
  public int yOffset = 0;
  private int tickDelay;
  private static List<String> blacklistAll;

  public static enum Fields {
    TIMER, SPEED, REDSTONE, LEFTRIGHT, SIZE, RENDERPARTICLES, Y_OFFSET;
  }

  public TileEntityUser() {
    super(INV_SIZE);
    timer = tickDelay;
    this.initEnergy(new EnergyStore(MENERGY), BlockUser.FUEL_COST);
    this.setSlotsForInsert(0, SLOT_OUTPUT_START - 1);
    this.setSlotsForExtract(SLOT_OUTPUT_START, INV_SIZE - 1);
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
    this.shiftAllUp(6);
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    boolean triggered = this.updateTimerIsZero();
    if (world instanceof WorldServer) {
      setupBeforeTrigger();
      if (triggered) {
        timer = tickDelay;
        trigger();
      }
      this.markDirty();
    }
  }

  private void trigger() {
    BlockPos targetPos = this.getTargetPos();
    try {
      if (isRightClick()) {//right click entities and blocks
        if (this.isInBlacklist(targetPos) == false) {
          //todo if fluid
          boolean fluidSuccess = interactFluid(targetPos);
          //now the rightclick 
          if (fluidSuccess == false &&
              world.isAirBlock(targetPos) == false) {
            this.rightClickBlock(targetPos);
          }
        }
      }
    }
    catch (Throwable e) {//exception could come from external third party block/mod/etc 
      ModCyclic.logger.error("Automated User [rightClickBlock] Error '" + e.getMessage() + "'" + e.getClass(), e);
    }
    try {
      interactEntities(targetPos);
    }
    catch (Throwable e) {//exception could come from external third party block/mod/etc 
      ModCyclic.logger.error("Automated User [interactEntities] Error '" + e.getMessage() + "'" + e.getClass(), e);
    }
  }

  private void setupBeforeTrigger() {
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
    if (isRightClick()) {//right click entities and blocks
      List<EntityMinecart> carts = world.getEntitiesWithinAABB(EntityMinecart.class, entityRange);
      List<Entity> all = new ArrayList<Entity>(living);
      all.addAll(carts);//works since  they share a base class but no overlap
      // 
      rightClickEntities(all);
      this.getWorld().markChunkDirty(targetPos, this);
    }
    else {
      leftClickEntities(living);
    }
  }

  private void leftClickEntities(List<EntityLivingBase> living) {
    // left click (attack) entities 
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

  private void rightClickEntities(List<Entity> all) {
    Collections.shuffle(all);
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

  private boolean isRightClick() {
    return rightClickIfZero == 0;
  }

  private boolean interactFluid(BlockPos targetPos) {
    FakePlayer player = fakePlayer.get();
    ItemStack playerHeld = player.getHeldItemMainhand();
    //if both block and itemstack are fluid compatible 
    if (UtilFluid.stackHasFluidHandler(playerHeld) &&
        UtilFluid.hasFluidHandler(world.getTileEntity(targetPos), this.getCurrentFacing().getOpposite())) {//tile has fluid
      boolean success = rightClickFluidTank(targetPos);
      if (success) {
        syncPlayerTool();
        return true;
      }
    }
    else if (UtilFluid.stackHasFluidHandler(playerHeld)) {
      if (rightClickFluidAir(targetPos)) {
        //bucket on fluid-in-world   
        syncPlayerTool();
        return true;
      }
    }
    return false;
  }

  private void rightClickBlock(BlockPos targetPos) throws Exception {
    //if both block and itemstack are fluid compatible 
    ItemStack before = fakePlayer.get().getHeldItemMainhand();
    boolean wasEmpty = fakePlayer.get().getHeldItemMainhand().isEmpty();
    //dont ever place a block. they want to use it on an entity
    EnumActionResult result = fakePlayer.get().interactionManager.processRightClickBlock(fakePlayer.get(), world, fakePlayer.get().getHeldItemMainhand(), EnumHand.MAIN_HAND, targetPos, EnumFacing.UP, .5F, .5F, .5F);
    if (result != EnumActionResult.FAIL) {
      boolean eq = ItemStack.areItemStacksEqual(before, fakePlayer.get().getHeldItemMainhand());
      if (wasEmpty == false && fakePlayer.get().getHeldItemMainhand().isEmpty()) {
        syncPlayerTool();
      }
      else if (!eq) {
        //       
        this.tryDumpFakePlayerInvo(true);
        syncPlayerTool();
      }
    }
    else {
      //if its a throwable item, it happens on this line down below, the process right click
      result = fakePlayer.get().interactionManager.processRightClick(fakePlayer.get(), world, fakePlayer.get().getHeldItemMainhand(), EnumHand.MAIN_HAND);
      if (fakePlayer.get().getHeldItemMainhand().getCount() == 0) {
        //some items from some mods dont handle stack size zero and trigger it to empty, so handle that edge case
        inv.set(SLOT_TOOL, ItemStack.EMPTY);
        fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      }
      //if throw has happened, success is true
      if (result != EnumActionResult.SUCCESS) {
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
        }
      }
      //if my hand was empty before operation, then there might be something in it now so drop that
      //if it wasnt empty before (bonemeal whatever) then dont do that, it might dupe
      this.tryDumpFakePlayerInvo(wasEmpty);
    }
  }

  private void syncPlayerTool() {
    this.setInventorySlotContents(SLOT_TOOL, fakePlayer.get().getHeldItemMainhand());
  }

  private void tryDumpStacks(List<ItemStack> toDump) {
    ArrayList<ItemStack> toDrop = UtilInventoryTransfer.dumpToIInventory(toDump, this, SLOT_OUTPUT_START, INV_SIZE);
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
    int start = (includeMainHand) ? 0 : 1;//main hand is 1
    ArrayList<ItemStack> toDrop = new ArrayList<ItemStack>();
    for (int i = start; i < fakePlayer.get().inventory.mainInventory.size(); i++) {
      ItemStack s = fakePlayer.get().inventory.mainInventory.get(i);
      if (s.isEmpty() == false) {
        toDrop.add(s.copy());
        fakePlayer.get().inventory.mainInventory.set(i, ItemStack.EMPTY);
      }
    }
    tryDumpStacks(toDrop);
  }

  /**
   * Interact with a block that has fluid capabilities
   * 
   * @param targetPos
   * @return
   */
  private boolean rightClickFluidTank(BlockPos targetPos) {
    FakePlayer player = fakePlayer.get();
    ItemStack playerHeld = player.getHeldItemMainhand();
    boolean wasFull = UtilFluid.isEmptyOfFluid(playerHeld);
    boolean success = UtilFluid.interactWithFluidHandler(player, world, targetPos, this.getCurrentFacing().getOpposite());
    playerHeld = player.getHeldItemMainhand();
    if (success) {
      if (UtilFluid.isEmptyOfFluid(playerHeld)) {
        this.tryDumpFakePlayerInvo(!wasFull && playerHeld.getCount() == 1);
      }
      else {//im holding a stack that has fluid, get rid of it
        this.tryDumpFakePlayerInvo(true);
      }
      return success;
    }
    return false;
  }

  /**
   * Pickup or place fluid in the world
   * 
   * @param targetPos
   * @return
   */
  private boolean rightClickFluidAir(BlockPos targetPos) {
    FakePlayer player = fakePlayer.get();
    ItemStack playerHeld = player.getHeldItemMainhand();
    //item stack does not hve fluid handler
    //dispense stack so either pickup or place liquid
    if (UtilFluid.isEmptyOfFluid(playerHeld)) {
      FluidActionResult res = UtilFluid.fillContainer(world, targetPos, playerHeld, this.getCurrentFacing());
      if (res != FluidActionResult.FAILURE) {
        player.setHeldItem(EnumHand.MAIN_HAND, res.getResult());
        this.tryDumpFakePlayerInvo(true);
        return true;
      }
    }
    else if (world.isAirBlock(targetPos)) {
      ItemStack drainedStackOrNull = UtilFluid.dumpContainer(world, targetPos, playerHeld);
      player.setHeldItem(EnumHand.MAIN_HAND, drainedStackOrNull);
      if (UtilFluid.isEmptyOfFluid(drainedStackOrNull)) {
        this.tryDumpFakePlayerInvo(true);
      }
      return true;
    }
    return false;
  }

  /**
   * detect if tool stack is empty or destroyed and reruns equip
   */
  private void validateTool() {
    ItemStack maybeTool = getStackInSlot(SLOT_TOOL);
    if (!maybeTool.isEmpty() && maybeTool.getCount() < 0) {
      maybeTool = ItemStack.EMPTY;
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      inv.set(SLOT_TOOL, ItemStack.EMPTY);
    }
  }

  private ItemStack tryEquipItem() {
    ItemStack maybeTool = getStackInSlot(SLOT_TOOL);
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
      inv.set(SLOT_TOOL, ItemStack.EMPTY);
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
      // uuid = UUID.randomUUID();
      uuid = UUID.fromString("9cebd559-0e72-46b4-8e26-7729cf864315");
      IBlockState state = world.getBlockState(this.pos);
      world.notifyBlockUpdate(pos, state, state, 3);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (uuid != null) {
      compound.setString(NBTPLAYERID, uuid.toString());
    }
    compound.setInteger(NBT_LR, rightClickIfZero);
    compound.setInteger(NBT_SIZE, size);
    compound.setInteger("yoff", yOffset);
    if (tickDelay != 0)
      compound.setInteger("tickDelay", tickDelay);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (compound.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(compound.getString(NBTPLAYERID));
    }
    rightClickIfZero = compound.getInteger(NBT_LR);
    size = compound.getInteger(NBT_SIZE);
    yOffset = compound.getInteger("yoff");
    tickDelay = compound.getInteger("tickDelay");
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case SPEED:
        return this.tickDelay;
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
        if (value < MIN_SPEED) {
          value = MIN_SPEED;
        }
        if (value <= MAX_SPEED && value != 0) {
          tickDelay = value;//progress bar prevent overflow 
          if (timer > tickDelay) {
            timer = tickDelay;
          }
        }
      break;
      case TIMER:
        if (value < 0) {
          value = 0;
        }
        timer = value;
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
        if (value > MAX_SIZE) {
          value = 0;
        }
        if (value < 0) {
          value = MAX_SIZE;
        }
        size = value;
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
  public int getSpeed() {
    return 1;
  }

  private BlockPos getTargetPos() {
    BlockPos targetPos = UtilWorld.getRandomPos(world.rand, getTargetCenter(), this.size);
    return targetPos;
  }

  private BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), this.size + 1).offset(EnumFacing.UP, yOffset);
  }

  @Override
  public List<BlockPos> getShape() {
    return UtilShape.squareHorizontalHollow(getTargetCenter(), this.size);
  }

  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    if (index <= 2 && stack.getCount() > 1) {
      return false;
    }
    return super.isItemValidForSlot(index, stack);
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
