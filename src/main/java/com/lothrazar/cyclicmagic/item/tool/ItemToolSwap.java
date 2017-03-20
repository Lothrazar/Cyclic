package com.lothrazar.cyclicmagic.item.tool;
import java.util.ConcurrentModificationException;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.module.ItemToolsModule;
import com.lothrazar.cyclicmagic.module.ItemToolsModule.RenderLoc;
import com.lothrazar.cyclicmagic.net.PacketSwapBlock;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolSwap extends BaseTool implements IHasRecipe {
  private static final int durability = 1000;
  private static final int COOLDOWN = 30;
  public static String[] swapBlacklist;
  private WandType wandType;
  public ItemToolSwap(WandType t) {
    super(durability);
    setWandType(t);
  }
  public enum WandType {
    NORMAL, MATCH;
  }
  public enum ActionType {
    SINGLE, X3, X5, X7, X9;
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
        return "tool.action." + SINGLE.toString().toLowerCase();
      }
    }
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      type++;
      if (type > X9.ordinal()) {
        type = SINGLE.ordinal();
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
  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onRender(RenderGameOverlayEvent.Post event) {
    EntityPlayer player = Minecraft.getMinecraft().player;
    ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
    if (event.isCanceled() || event.getType() != ElementType.EXPERIENCE) { return; }
    if (held != null && held.getItem() == this) {
      int slot = UtilPlayer.getFirstSlotWithBlock(player);
      if (slot >= 0) {
        ItemStack stack = player.inventory.getStackInSlot(slot);
        int leftOff = 0, rightOff = -18, topOff = 0, bottOff = 0;
        int xmain = RenderLoc.locToX(ItemToolsModule.renderLocation, leftOff, rightOff);
        int ymain = RenderLoc.locToY(ItemToolsModule.renderLocation, topOff, bottOff);
        if (stack != null)
          ModCyclic.proxy.renderItemOnScreen(stack, xmain, ymain);
      }
    }
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    //if we only run this on server, clients dont get the udpate
    //so run it only on client, let packet run the server
    try {
      if (worldObj.isRemote) {
        ModCyclic.network.sendToServer(new PacketSwapBlock(pos, side, ActionType.values()[ActionType.get(stack)], this.getWandType()));
      }
      player.swingArm(hand);
      player.getCooldownTracker().setCooldown(this, COOLDOWN);
    }
    catch (ConcurrentModificationException e) {
      ModCyclic.logger.error("ConcurrentModificationException");
      ModCyclic.logger.error(e.getMessage());// message is null??
      ModCyclic.logger.error(e.getStackTrace().toString());
    }
    return EnumActionResult.FAIL;//super.onItemUse( player, worldObj, pos, hand, side, hitX, hitY, hitZ);// EnumActionResult.PASS;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
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
  public void addRecipe() {
    ItemStack ingredient = null;
    switch (this.getWandType()) {
      case MATCH:
        ingredient = new ItemStack(Items.EMERALD);
      break;
      case NORMAL:
        ingredient = new ItemStack(Blocks.LAPIS_BLOCK);
      break;
    }
    GameRegistry.addRecipe(new ItemStack(this),
        " gi",
        "oig",
        "oo ",
        'i', Blocks.IRON_BLOCK,
        'g', ingredient,
        'o', Blocks.OBSIDIAN);
  }
  public WandType getWandType() {
    return wandType;
  }
  public void setWandType(WandType wandType) {
    this.wandType = wandType;
  }
}
