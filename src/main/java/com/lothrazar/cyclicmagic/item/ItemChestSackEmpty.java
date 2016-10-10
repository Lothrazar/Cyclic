package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChestSackEmpty extends BaseItem implements IHasRecipe {
  public static final String name = "chest_sack_empty";
  public ItemChestSackEmpty() {
    super();
    this.setMaxStackSize(64);
    // imported from my old mod
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
  }
  /**
   * Called when a Block is right-clicked with this Item
   */
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer entityPlayer, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (pos == null) { return EnumActionResult.FAIL; }
    TileEntity tile = world.getTileEntity(pos);
    IBlockState state = world.getBlockState(pos);
    if (state == null || tile == null || tile instanceof IInventory == false) {
      if (world.isRemote) {
        UtilChat.addChatMessage(entityPlayer, "item.chest_sack_empty.inventory");
      }
      return EnumActionResult.FAIL;
    }
    NBTTagCompound tileData = new NBTTagCompound(); //thanks for the tip on setting tile entity data from nbt tag: https://github.com/romelo333/notenoughwands1.8.8/blob/master/src/main/java/romelo333/notenoughwands/Items/DisplacementWand.java
    tile.writeToNBT(tileData);
    NBTTagCompound itemData = new NBTTagCompound();
    itemData.setString(ItemChestSack.KEY_BLOCKNAME, state.getBlock().getUnlocalizedName());
    itemData.setTag(ItemChestSack.KEY_BLOCKTILE, tileData);
    itemData.setInteger(ItemChestSack.KEY_BLOCKID, Block.getIdFromBlock(state.getBlock()));
    itemData.setInteger(ItemChestSack.KEY_BLOCKSTATE, state.getBlock().getMetaFromState(state));
    ItemStack drop = new ItemStack(ItemRegistry.chest_sack);
    drop.setTagCompound(itemData);
    entityPlayer.dropItem(drop, false);
    //now erase the data so it doesnt drop items/etc
    tile.readFromNBT(new NBTTagCompound());
    world.destroyBlock(pos, false);
    if (entityPlayer.capabilities.isCreativeMode == false) {
      stack.stackSize--;
    }
    UtilSound.playSound(entityPlayer, pos, SoundRegistry.thunk);
    return EnumActionResult.SUCCESS;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "lbl",
        "lll",
        'l', new ItemStack(Items.LEATHER),
        'b', new ItemStack(Items.SLIME_BALL),
        's', new ItemStack(Items.STRING));
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "lbl",
        "lll",
        'l', new ItemStack(Items.LEATHER),
        'b', new ItemStack(Items.APPLE),
        's', new ItemStack(Items.STRING));
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    String s = UtilChat.lang("item.chest_sack_empty.tooltip");
    tooltip.add(s);
  }
}