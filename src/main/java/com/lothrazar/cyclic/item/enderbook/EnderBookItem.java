package com.lothrazar.cyclic.item.enderbook;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EnderBookItem extends ItemBase {

  private static final String ITEMCOUNT = "itemCount";
  private static final int TP_COUNTDOWN = 60;
  // current slot
  private static final String ENDERSLOT = "enderslot";
  //ticks counting down, when zero teleport fires off
  private static final String TELEPORT_COUNTDOWN = "TeleportCountdown";

  public EnderBookItem(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    if (stack.hasTag()) {
      CompoundTag stackTag = stack.getOrCreateTag();
      if (stackTag.contains(ITEMCOUNT)) {
        int itemCount = stackTag.getInt(ITEMCOUNT);
        TranslatableComponent t = new TranslatableComponent("cyclic.screen.filter.item.count");
        t.append("" + itemCount);
        t.withStyle(ChatFormatting.GRAY);
        tooltip.add(t);
      }
    }
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    if (stack.hasTag() && stack.getTag().contains(TELEPORT_COUNTDOWN)) {
      return true;
    }
    return super.isFoil(stack);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayer) playerIn, new ContainerProviderEnderBook(), playerIn.blockPosition());
    }
    if (!worldIn.isClientSide && playerIn.isCrouching()) {
      //any damage?
      ItemStack stack = playerIn.getItemInHand(handIn);
      if (stack.getDamageValue() < stack.getMaxDamage() - 1) {
        int enderslot = stack.getTag().getInt(ENDERSLOT);
        BlockPosDim loc = EnderBookItem.getLocation(stack, enderslot);
        if (loc != null) {
          UtilChat.addServerChatMessage(playerIn, new TranslatableComponent("item.cyclic.ender_book.start").append(loc.toString()));
          stack.getOrCreateTag().putInt(TELEPORT_COUNTDOWN, TP_COUNTDOWN);
        }
      }
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (stack.hasTag() && stack.getTag().contains(TELEPORT_COUNTDOWN) && entityIn instanceof LivingEntity) {
      int ct = stack.getOrCreateTag().getInt(TELEPORT_COUNTDOWN);
      if (ct < 0) {
        cancelTeleport(stack);
        return;
      }
      if (ct == 0 && entityIn instanceof Player) {
        Player p = (Player) entityIn;
        cancelTeleport(stack);
        int enderslot = stack.getTag().getInt(ENDERSLOT);
        BlockPosDim loc = EnderBookItem.getLocation(stack, enderslot);
        if (loc != null &&
            loc.getPos() != null) {
          if (loc.getDimension().equalsIgnoreCase(UtilWorld.dimensionToString(worldIn))) {
            UtilEntity.enderTeleportEvent(p, worldIn, loc.getPos());
          }
          else {
            //diff dim 
            UtilEntity.dimensionTeleport(p, worldIn, loc);
          }
          // done
          UtilItemStack.damageItem(stack);
          return;
        }
      }
      else if (ct % 20 == 0 && entityIn instanceof Player) {
        UtilChat.sendStatusMessage((Player) entityIn, new TranslatableComponent("item.cyclic.ender_book.countdown").append("" + (ct / 20)));
      }
      ct--;
      stack.getOrCreateTag().putInt(TELEPORT_COUNTDOWN, ct);
    }
  }

  @Override
  public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
    return repair.getItem() == Items.ENDER_PEARL;
  }

  @Override
  public boolean isRepairable(ItemStack stack) {
    return true;
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
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new CapabilityProviderEnderBook();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.ender_book, ScreenEnderBook::new);
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag nbt = stack.getOrCreateTag();
    IItemHandler cap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
    //on server  this runs . also has correct values.
    //set data for sync to client
    if (cap != null) {
      int count = 0;
      for (int i = 0; i < cap.getSlots(); i++) {
        if (!cap.getStackInSlot(i).isEmpty()) {
          count++;
        }
      }
      nbt.putInt(ITEMCOUNT, count);
    }
    return nbt;
  }

  @Override
  public void readShareTag(ItemStack stack, CompoundTag nbt) {
    if (nbt != null) {
      CompoundTag stackTag = stack.getOrCreateTag();
      stackTag.putInt(ITEMCOUNT, nbt.getInt(ITEMCOUNT));
    }
    super.readShareTag(stack, nbt);
  }

  public static void scroll(ServerPlayer player, int slot, boolean isDown) {
    ItemStack book = player.getInventory().getItem(slot);
    if (book.hasTag()) {
      int enderslot = book.getTag().getInt(ENDERSLOT);
      enderslot = scrollSlot(isDown, enderslot);
      book.getTag().putInt(ENDERSLOT, enderslot % CapabilityProviderEnderBook.SLOTS);
      BlockPosDim loc = EnderBookItem.getLocation(book, enderslot);
      //      if (loc != null &&
      String msg = "---";
      if (loc != null) {
        msg = loc.getDisplayString();
      }
      UtilChat.addServerChatMessage(player, new TextComponent(book.getTag().getInt(ENDERSLOT) + " : ").append(msg));
    }
  }

  private static int scrollSlot(final boolean isDown, int enderslot) {
    enderslot += isDown ? -1 : 1;
    if (enderslot < 0) {
      enderslot = CapabilityProviderEnderBook.SLOTS - 1;
    }
    else if (enderslot >= CapabilityProviderEnderBook.SLOTS) {
      enderslot = 0;
    }
    return enderslot;
  }
}
