package com.lothrazar.cyclicmagic.block.tileentity;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

/**
 * 
 * SEE TileMachineMiner
 * 
 */
public class TileMachineMinerSmart extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITileSizeToggle, ITickable {
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private static final String NBT_REDST = "redstone";
  private static final String NBTMINING = "mining";
  private static final String NBTDAMAGE = "curBlockDamage";
  private static final String NBTPLAYERID = "uuid";
  private static final String NBTTARGET = "target";
  private static final String NBTHEIGHT = "h";
  private static final String NBT_SIZE = "size";
  private static final String NBT_LIST = "blacklistIfZero";
  private static final int MAX_SIZE = 7;//7 means 15x15
  private static final int INVENTORY_SIZE = 5;
  private static final int TOOLSLOT_INDEX = INVENTORY_SIZE - 1;
  public static int maxHeight = 10;
  private boolean isCurrentlyMining;
  private float curBlockDamage;
  private BlockPos targetPos = null;
  private int size = 4;//center plus 4 in each direction = 9x9
  private int needsRedstone = 1;
  private int height = 6;
  private int blacklistIfZero = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  public static enum Fields {
    HEIGHT, REDSTONE, SIZE, LISTTYPE;
  }
  public TileMachineMinerSmart() {
    super(INVENTORY_SIZE);
  }
  private void verifyFakePlayer(WorldServer w) {
    if (fakePlayer == null) {
      fakePlayer = UtilFakePlayer.initFakePlayer(w, this.uuid);
      if (fakePlayer == null) {
        ModCyclic.logger.warn("Warning: Fake player failed to init ");
        return;
      }
    }
  }
  @Override
  public void update() {
    if (isRunning()) {
      this.spawnParticlesAbove();
    }
    World world = getWorld();
    if (world instanceof WorldServer) {
      verifyUuid(world);
      verifyFakePlayer((WorldServer) world);
      tryEquipItem();
      if (targetPos == null) {
        targetPos = pos.offset(this.getCurrentFacing()); //not sure if this is needed
      }
      if (isRunning()) {
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
//ModCyclic.logger.info("curBlockDamage"+curBlockDamage);
//ModCyclic.logger.info("targetState"+targetState);
          if (curBlockDamage >= 1.0f) {
            isCurrentlyMining = false;
            resetProgress(targetPos);
            if (fakePlayer.get() != null) {
              fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
            }
          }
          else {
            world.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
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
  private void tryEquipItem() {
    ItemStack equip = this.getStackInSlot(TOOLSLOT_INDEX);
    if (equip.getCount() == 0) {
      this.setInventorySlotContents(TOOLSLOT_INDEX, ItemStack.EMPTY);
    }
    if (equip == ItemStack.EMPTY) {
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
    if (world.isAirBlock(targetPos) || world.getBlockState(targetPos) == null) { return false; }
    IBlockState targetState = world.getBlockState(targetPos);
    Block target = targetState.getBlock();
    //else check blacklist
    ItemStack item;
    if (this.blacklistIfZero == 0) {
      for (int i = 0; i < this.getSizeInventory() - 1; i++) {//minus 1 because of TOOL
        if (inv.get(i) == ItemStack.EMPTY) {
          continue;
        }
        item = inv.get(i);
        if (item.getItem() == Item.getItemFromBlock(target)) { return false; }
      }
      return true;//blacklist means default trie
    }
    else {//check it as a WHITELIST
      for (int i = 0; i < this.getSizeInventory() - 1; i++) {//minus 1 because of TOOL
        if (inv.get(i) == ItemStack.EMPTY) {
          continue;
        }
        item = inv.get(i);
        //its a whitelist, so if its found in the list, its good to go right away
        if (item.getItem() == Item.getItemFromBlock(target)) { return true; }
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
    //    else if(rollHeight < 0){
    //      targetPos = targetPos.offset(EnumFacing.DOWN, -1*rollHeight);
    //    }
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
    tagCompound.setInteger(NBTHEIGHT, height);
    tagCompound.setInteger(NBT_SIZE, size);
    tagCompound.setInteger(NBT_LIST, this.blacklistIfZero);
    //<<<<<<< HEAD
    //=======
    //   
    //    //invo stuff
    //    NBTTagList itemList = new NBTTagList();
    //    for (int i = 0; i < inv.length; i++) {
    //      ItemStack stack = inv[i];
    //      if (stack != null) {
    //        NBTTagCompound tag = new NBTTagCompound();
    //        tag.setByte(NBT_SLOT, (byte) i);
    //        stack.writeToNBT(tag);
    //        itemList.appendTag(tag);
    //      }
    //    }
    //    tagCompound.setTag(NBT_INV, itemList);
    //>>>>>>> 7a4c7b0e8136047828c44111eddd82fd4a4bcf71
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    this.size = tagCompound.getInteger(NBT_SIZE);
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
    height = tagCompound.getInteger(NBTHEIGHT);
    blacklistIfZero = tagCompound.getInteger(NBT_LIST);
    //<<<<<<< HEAD
    //=======
    //    //invo stuff
    //    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    //    for (int i = 0; i < tagList.tagCount(); i++) {
    //      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
    //      byte slot = tag.getByte(NBT_SLOT);
    //      if (slot >= 0 && slot < inv.length) {
    //        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
    //      }
    //    }
    //>>>>>>> 7a4c7b0e8136047828c44111eddd82fd4a4bcf71
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
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { TOOLSLOT_INDEX };// tell it to only use the tool slot
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case HEIGHT:
        return getHeight();
      case REDSTONE:
        return this.needsRedstone;
      case SIZE:
        return this.size;
      case LISTTYPE:
        return blacklistIfZero;
      default:
      break;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case HEIGHT:
        if (value > maxHeight) {
          value = maxHeight;
        }
        setHeight(value);
      case REDSTONE:
        needsRedstone = value;
      break;
      case SIZE:
        size = value;
      break;
      case LISTTYPE:
        blacklistIfZero = value % 2;
      break;
      default:
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
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public void toggleSizeShape() {
    this.size++;
    if (this.size > MAX_SIZE) {
      this.size = 0;//size zero means a 1x1 area
    }
  }
  @Override
  public void displayPreview() {
    for (int i = 0; i < this.getHeight(); i++) {
      List<BlockPos> allPos = UtilShape.squareHorizontalHollow(getTargetCenter().up(i), size);
      for (BlockPos pos : allPos) {
        UtilParticle.spawnParticle(getWorld(), EnumParticleTypes.DRAGON_BREATH, pos);
      }
    }
  }
}
