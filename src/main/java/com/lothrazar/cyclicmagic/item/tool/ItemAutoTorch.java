package com.lothrazar.cyclicmagic.item.tool;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseItem;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAutoTorch extends BaseItem implements IHasRecipe {
  private static final int durability = 256;
  private static final float lightLimit = 7.0F;
  private static final int cooldown = 60;//ticks not seconds
  public ItemAutoTorch() {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }
  public enum ActionType {
    OFF, ON;
    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      if (type == OFF.ordinal()) {
        type = ON.ordinal();
      }
      else {
        type = OFF.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
    public static String getName(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return "tool.action." + ActionType.values()[tags.getInteger(NBT)].toString().toLowerCase();
      }
      catch (Exception e) {
        return "tool.action." + OFF.toString().toLowerCase();
      }
    }
    public static void setTimeout(ItemStack wand) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
    }
    public static int getTimeout(ItemStack wand) {
      return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
    }
    public static void tickTimeout(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int t = tags.getInteger(NBTTIMEOUT);
      if (t > 0) {
        UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
      }
    }
    public static boolean isOff(ItemStack held) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
      int type = tags.getInteger(NBT);
      return type == OFF.ordinal();
    }
  }
  public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
    if (ActionType.isOff(stack)) { return; }
    if (entityIn instanceof EntityPlayer && !world.isRemote) {
      EntityPlayer living = (EntityPlayer) entityIn;
      if (living.getCooldownTracker().hasCooldown(stack.getItem())) { return; } //cancel if on cooldown
      BlockPos pos = living.getPosition();
      if (world.getLight(pos, true) < lightLimit
          && world.isSideSolid(pos.down(), EnumFacing.UP)
          && world.isAirBlock(pos)) { // dont overwrite liquids 
        if (UtilPlaceBlocks.placeStateSafe(world, living, pos, Blocks.TORCH.getDefaultState())) {
          UtilItemStack.damageItem(living, stack);
          if (stack == null || stack.getItemDamage() == stack.getMaxDamage()) {
            stack = null;
            living.inventory.setInventorySlotContents(itemSlot, null);
            UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ITEM_BREAK, living.getSoundCategory());
          }
          living.getCooldownTracker().setCooldown(this, cooldown);
        }
      }
    }
  }
  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held != null && held.getItem() == this) {
      //did we turn it off? is the visible timer still going?
      if (ActionType.getTimeout(held) > 0) { return; }
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
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "cic",
        " i ",
        "cic",
        'c', Blocks.COAL_BLOCK,
        'i', Blocks.IRON_BARS);
  }
  @Override
  public String getTooltip() {
    return "item.tool_auto_torch.tooltip";
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return !ActionType.isOff(stack);
  }
}
