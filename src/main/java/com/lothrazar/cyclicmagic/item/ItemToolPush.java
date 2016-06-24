package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolPush extends BaseTool implements IHasRecipe {
  private static final int durability = 5000;
  public ItemToolPush() {
    super(durability);
  }
  public enum ActionType {
    PUSH, PULL, ROTATE;
    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";
    public static int getTimeout(ItemStack wand) {
      return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
    }
    public static void setTimeout(ItemStack wand) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
    }
    public static void tickTimeout(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int t = tags.getInteger(NBTTIMEOUT);
      if (t > 0) {
        UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
      }
    }
    public static int get(ItemStack wand) {
      if (wand == null) { return 0; }
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      return tags.getInteger(NBT);
    }
    public static String getName(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return "tool.action." + ActionType.values()[tags.getInteger(NBT)].toString().toLowerCase();
      }
      catch (Exception e) {
        return "tool.action." + PUSH.toString().toLowerCase();
      }
    }
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      type++;
      if (type > ROTATE.ordinal()) {
        type = PUSH.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
  }
  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer entityPlayer = event.getEntityPlayer();
    ItemStack held = entityPlayer.getHeldItem(event.getHand());
    if (held != null && held.getItem() == this) {
      if (ActionType.getTimeout(held) > 0) {
        //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
        return;
      }
      ActionType.setTimeout(held);
      event.setCanceled(true);
      UtilSound.playSound(entityPlayer, SoundRegistry.dcoin);
      if (!entityPlayer.worldObj.isRemote) { // server side
        ActionType.toggle(held);
        UtilChat.addChatMessage(entityPlayer, UtilChat.lang(ActionType.getName(held)));
      }
    }
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    BlockPos resultPosition = null;
    boolean success = false;
    switch (ActionType.values()[ActionType.get(stack)]) {
    case PULL:
      resultPosition = UtilPlaceBlocks.pullBlock(worldObj, player, pos, side);
      success = resultPosition != null;
      break;
    case PUSH:
      resultPosition = UtilPlaceBlocks.pushBlock(worldObj, player, pos, side);
      success = resultPosition != null;
      break;
    case ROTATE:
      success = UtilPlaceBlocks.rotateBlockValidState(pos, worldObj, side, player);
      resultPosition = pos;
      break;
    default:
      break;
    }
    if (success)
      onUse(stack, player, worldObj, hand);
    return super.onItemUse(stack, player, worldObj, resultPosition, hand, side, hitX, hitY, hitZ);// EnumActionResult.PASS;
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
  }
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " gp",
        " bg",
        "b  ",
        'b', Items.BLAZE_ROD,
        'g', Items.GHAST_TEAR,
        'p', Blocks.PISTON);
  }
}
