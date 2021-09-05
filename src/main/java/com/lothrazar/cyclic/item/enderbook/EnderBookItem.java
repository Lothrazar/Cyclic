package com.lothrazar.cyclic.item.enderbook;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.List;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EnderBookItem extends ItemBase {

  private static final String ENDERSLOT = "enderslot";
  private static final String TELEPORT_COUNTDOWN = "TeleportCountdown";

  public EnderBookItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    if (stack.hasTag()) {
      CompoundNBT stackTag = stack.getOrCreateTag();
      //      if (stackTag.contains("itemTooltip")) {
      //        String itemTooltip = stackTag.getString("itemTooltip");
      //        tooltip.add(new TranslationTextComponent(itemTooltip).mergeStyle(TextFormatting.GRAY));
      //      }
      if (stackTag.contains("itemCount")) {
        int itemCount = stackTag.getInt("itemCount");
        TranslationTextComponent t = new TranslationTextComponent("cyclic.screen.filter.item.count");
        t.appendString("" + itemCount);
        t.mergeStyle(TextFormatting.GRAY);
        tooltip.add(t);
      }
    }
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayerEntity) playerIn, new ContainerProviderEnderBook(), playerIn.getPosition());
    }
    if (!worldIn.isRemote && playerIn.isCrouching()) {
      playerIn.getHeldItem(handIn).getOrCreateTag().putInt(TELEPORT_COUNTDOWN, 60);
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (stack.hasTag() && stack.getTag().contains(TELEPORT_COUNTDOWN) && entityIn instanceof LivingEntity) {
      int ct = stack.getOrCreateTag().getInt(TELEPORT_COUNTDOWN);
      if (ct < 0) {
        cancelTeleport(stack);
        return;
      }
      if (ct == 0) {
        cancelTeleport(stack);
        int enderslot = stack.getTag().getInt(ENDERSLOT);
        BlockPosDim loc = EnderBookItem.getLocation(stack, enderslot);
        if (loc != null &&
            loc.getPos() != null) {
          if (loc.getDimension().equalsIgnoreCase(UtilWorld.dimensionToString(worldIn))) {
            UtilEntity.enderTeleportEvent((LivingEntity) entityIn, worldIn, loc.getPos());
          }
          else {
            //diff dim
          }
          return;
        }
      }
      else if (ct % 20 == 0 && entityIn instanceof PlayerEntity) {
        UtilChat.sendStatusMessage((PlayerEntity) entityIn, new TranslationTextComponent("item.cyclic.ender_book.coutndown").appendString("" + (ct / 20)));
      }
      ct--;
      stack.getOrCreateTag().putInt(TELEPORT_COUNTDOWN, ct);
    }
  }

  public static void cancelTeleport(ItemStack stack) {
    stack.getOrCreateTag().remove(TELEPORT_COUNTDOWN);
  }

  private static BlockPosDim getLocation(ItemStack stack, int enderSlot) {
    IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    if (cap != null) {
      return LocationGpsCard.getPosition(cap.getStackInSlot(enderSlot));
    }
    return null;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
    return new CapabilityProviderEnderBook();
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.ender_book, ScreenEnderBook::new);
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundNBT getShareTag(ItemStack stack) {
    CompoundNBT nbt = stack.getOrCreateTag();
    IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    //on server  this runs . also has correct values.
    //set data for sync to client
    if (cap != null) {
      int count = 0;
      ITextComponent first = null;
      for (int i = 0; i < cap.getSlots(); i++) {
        if (!cap.getStackInSlot(i).isEmpty()) {
          //non empty stack eh
          count++;
          if (first == null) {
            first = cap.getStackInSlot(i).getDisplayName();
          }
        }
      }
      nbt.putInt("itemCount", count);
      if (first != null) {
        nbt.putString("itemTooltip", first.getString());
      }
    }
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack stack, CompoundNBT nbt) {
    if (nbt != null) {
      CompoundNBT stackTag = stack.getOrCreateTag();
      stackTag.putString("itemTooltip", nbt.getString("itemTooltip"));
      stackTag.putInt("itemCount", nbt.getInt("itemCount"));
    }
    super.readShareTag(stack, nbt);
  }

  public static void scroll(ServerPlayerEntity player, int slot, boolean isDown) {
    ItemStack book = player.inventory.getStackInSlot(slot);
    if (book.hasTag()) {
      int enderslot = book.getTag().getInt(ENDERSLOT);
      enderslot += isDown ? -1 : 1;
      if (enderslot < 0) {
        enderslot = CapabilityProviderEnderBook.SLOTS - 1;
      }
      book.getTag().putInt(ENDERSLOT, enderslot % CapabilityProviderEnderBook.SLOTS);
      BlockPosDim loc = EnderBookItem.getLocation(book, enderslot);
      //      if (loc != null &&
      String msg = "---";
      if (loc != null) {
        msg = loc.toString();
      }
      UtilChat.addServerChatMessage(player, new StringTextComponent(book.getTag().getInt(ENDERSLOT) + " : ").appendString(msg));
    }
  }
}
