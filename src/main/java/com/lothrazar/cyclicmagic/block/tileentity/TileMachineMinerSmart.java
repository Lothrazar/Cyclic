package com.lothrazar.cyclicmagic.block.tileentity;
import java.lang.ref.WeakReference;
import java.util.UUID;
import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * 
 * SEE TileMachineMiner
 * 
 */
public class TileMachineMinerSmart extends TileEntityBaseMachineInvo {
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  public static final GameProfile breakerProfile = new GameProfile(UUID.nameUUIDFromBytes("CyclicFakePlayer2".getBytes(Charsets.UTF_8)), "CyclicFakePlayer2");
  private UUID uuid;
  private boolean isCurrentlyMining;
  private WeakReference<FakePlayer> fakePlayer;
  private float curBlockDamage;
  private boolean firstTick = true;
  private BlockPos targetPos = null;
  private ItemStack[] inv;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots for all faces
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  public TileMachineMinerSmart(){
    inv = new ItemStack[9];
  }
  @Override
  public void update() {
    int toolSlot = inv.length - 1;
    if (this.isPowered()) {
      this.spawnParticlesAbove();
    }
    if (!worldObj.isRemote) {
      if (firstTick || fakePlayer == null) {
        firstTick = false;
        initFakePlayer();
      }
      ItemStack maybeTool = inv[toolSlot];
      
      if(maybeTool == null){
        fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, null);
      }
      else{
         if(maybeTool.equals(fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND))){
           //already equipped
         }
         else{
           fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, maybeTool);
         }
      }
      BlockPos start = pos.offset(this.getCurrentFacing());
      if (targetPos == null) {
        targetPos = start; //not sure if this is needed
      }

      if (this.isPowered()) {
        if (isCurrentlyMining == false) { //we can mine but are not currently
          this.updateTargetPos(start);
          
          if (isTargetValid()) { //we have a valid target
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
        curBlockDamage += targetState.getBlock().getPlayerRelativeBlockHardness(targetState, fakePlayer.get(), worldObj, targetPos);
        if (curBlockDamage >= 1.0f) {
          isCurrentlyMining = false;
          resetProgress(targetPos);
          if (fakePlayer.get() != null) {
            fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
            //            if (didBreak == false)
            //              System.out.println("tried to break but failed " + UtilChat.blockPosToString(targetPos) + "_" + targetState.getBlock().getUnlocalizedName());
          }
        }
        else { 
          
          worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
  }
  private boolean isTargetValid(){
    if(worldObj.isAirBlock(targetPos) || worldObj.getBlockState(targetPos) == null) {
      return true;
    }
 
    IBlockState targetState = worldObj.getBlockState(targetPos);
    
    Block target = targetState.getBlock();
     
    //else check blacklist
    ItemStack item;
    for(int i = 0; i < inv.length - 1; i++){//minus 1 because of TOOL
      if(inv[i] == null){
        continue;
      }
      item = inv[i];
 
      if(item.getItem() == Item.getItemFromBlock(target)){
        return false;
      }
    }
    
    return true;
  }
  private void updateTargetPos(BlockPos start) {
    targetPos = start;//always restart here so we dont offset out of bounds

    EnumFacing facing = this.getCurrentFacing();
    BlockPos center = start.offset(facing);//move one more over so we are in the exact center of a 3x3x3 area
    //else we do a 3x3 
    int rollFull = worldObj.rand.nextInt(9 * 3);
    int rollUpOrDown = rollFull / 9;
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
    if (rollUpOrDown == 1) {
      targetPos = targetPos.offset(EnumFacing.UP);
    }
    else if (rollUpOrDown == 2) {
      targetPos = targetPos.offset(EnumFacing.DOWN);
    }
    //0 is center
    return;
  }
  private void initFakePlayer() {
    if (uuid == null) {
      uuid = UUID.randomUUID();
      IBlockState state = worldObj.getBlockState(this.pos);
      worldObj.notifyBlockUpdate(pos, state, state, 3);
    }
    fakePlayer = new WeakReference<FakePlayer>(FakePlayerFactory.get((WorldServer) worldObj, breakerProfile));
//    ItemStack unbreakingIronPickaxe = new ItemStack(Items.DIAMOND_PICKAXE, 1);
//    unbreakingIronPickaxe.setTagCompound(new NBTTagCompound());
//    unbreakingIronPickaxe.getTagCompound().setBoolean("Unbreakable", true);
    fakePlayer.get().onGround = true;
    fakePlayer.get().connection = new NetHandlerPlayServer(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManager(EnumPacketDirection.SERVERBOUND), fakePlayer.get()) {
      @SuppressWarnings("rawtypes")
      @Override
      public void sendPacket(Packet packetIn) {
      }
    };
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
    //invo stuff
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
    compound.setTag(NBT_INV, itemList);
    return compound;// super.writeToNBT(compound);
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
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
    //invo stuff
    NBTTagList tagList = compound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (isCurrentlyMining && uuid != null) {
      resetProgress(pos);
    }
  }
  private void resetProgress(BlockPos targetPos) {
    if (uuid != null) {
      //BlockPos targetPos = pos.offset(state.getValue(BlockMiner.PROPERTYFACING));
      worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);
      curBlockDamage = 0;
    }
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
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return hopperInput;
  }
}
