package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.net.PacketRandomize;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRandomizer extends BaseTool implements IHasRecipe {
  private static final int durability = 5000;
  private static final int cooldown = 15;
  public ItemRandomizer() {
    super(durability);
  }
  public enum ActionType {
    X3, X5, X7, X9;
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
        return "tool.action." + X3.toString().toLowerCase();
      }
    }
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      type++;
      if (type > X9.ordinal()) {
        type = X3.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
  }
  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held != null && held.getItem() == this) {
      if (ActionType.getTimeout(held) > 0) {
        //without a timeout, this fires every tick. so you 'hit once' and get this happening 6 times
        return;
      }
      ActionType.setTimeout(held);
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.dcoin, SoundCategory.PLAYERS);
      if (!player.getEntityWorld().isRemote) { // server side
        ActionType.toggle(held);
        UtilChat.addChatMessage(player, UtilChat.lang(ActionType.getName(held)));
      }
    }
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) { return super.onItemUse(player, worldObj, pos, hand, side, hitX, hitY, hitZ); }
    //if we only run this on server, clients dont get the udpate
    //so run it only on client, let packet run the server
    if (worldObj.isRemote) {
      ModCyclic.network.sendToServer(new PacketRandomize(pos, side, ActionType.values()[ActionType.get(stack)]));
    }
    player.getCooldownTracker().setCooldown(this, cooldown);
    this.onUse(stack, player, worldObj, hand);
    return super.onItemUse(player, worldObj, pos, hand, side, hitX, hitY, hitZ);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " gi",
        " ig",
        "o  ",
        'i', "ingotIron",
        'g', "dustRedstone",
        'o', "obsidian");
    return null;
  }
}
