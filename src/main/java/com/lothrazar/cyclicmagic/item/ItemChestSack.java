package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChestSack extends BaseItem {
  public static final String name = "chest_sack";
  public static final String KEY_BLOCKID = "block";
  public static final String KEY_BLOCKTILE = "tile";
  public static final String KEY_BLOCKNAME = "blockname";
  public static final String KEY_BLOCKSTATE = "blockstate";
  private Item emptySack;
  public ItemChestSack() {
    super();
    this.setMaxStackSize(1);
    // imported from my old mod
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
  }
  /**
   * Called when a Block is right-clicked with this Item
   */
  @Override
  public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    BlockPos offset = pos.offset(side);
    if (worldIn.isAirBlock(offset) == false) { return EnumActionResult.FAIL; }
    ItemStack stack = playerIn.getHeldItem(hand);
    if (createAndFillChest(playerIn, stack, offset)) {
      playerIn.setHeldItem(hand, ItemStack.EMPTY);
      UtilSound.playSound(playerIn, pos, SoundRegistry.thunk);
      if (playerIn.capabilities.isCreativeMode == false && emptySack != null) {//its never really null tho
        UtilItemStack.dropItemStackInWorld(worldIn, playerIn.getPosition(), emptySack);
      }
    }
    return EnumActionResult.SUCCESS;
  }
  private boolean createAndFillChest(EntityPlayer entityPlayer, ItemStack heldChestSack, BlockPos pos) {
    NBTTagCompound itemData = UtilNBT.getItemStackNBT(heldChestSack);
    Block block = Block.getBlockById(itemData.getInteger(KEY_BLOCKID));
    if (block == null) {
      //      heldChestSack.stackSize = 0;
      heldChestSack = ItemStack.EMPTY;
      UtilChat.addChatMessage(entityPlayer, "Invalid block id " + itemData.getInteger(KEY_BLOCKID));
      return false;
    }
    IBlockState toPlace;
    if (itemData.hasKey(KEY_BLOCKSTATE)) {
      //in builds 1.7.8 prior this data tag did not exist, so make sure we support itemstacks created back then
      toPlace = UtilItemStack.getStateFromMeta(block, itemData.getInteger(KEY_BLOCKSTATE));
    }
    else {
      toPlace = block.getDefaultState();
    }
    World world = entityPlayer.getEntityWorld();
    world.setBlockState(pos, toPlace);
    TileEntity tile = world.getTileEntity(pos);
    if (tile != null) {
      NBTTagCompound tileData = (NBTTagCompound) itemData.getCompoundTag(ItemChestSack.KEY_BLOCKTILE);
      tileData.setInteger("x", pos.getX());
      tileData.setInteger("y", pos.getY());
      tileData.setInteger("z", pos.getZ());
      tile.readFromNBT(tileData);
      tile.markDirty();
      world.markChunkDirty(pos, tile);
    }
    //    heldChestSack.stackSize = 0;
    heldChestSack = ItemStack.EMPTY;
    heldChestSack.setTagCompound(null);
    return true;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean advanced) {
    if (itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey(KEY_BLOCKNAME)) {
      String blockname = itemStack.getTagCompound().getString(KEY_BLOCKNAME);
      if (blockname != null && blockname.length() > 0) {
        list.add(UtilChat.lang(blockname + ".name"));
      }
    }
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  public void setEmptySack(ItemChestSackEmpty item) {
    emptySack = item;
  }
}