package com.lothrazar.cyclic.item.ender;

import java.util.List;
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.EntityUtil;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class EnderBookItem extends ItemBaseCyclic {

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
        MutableComponent t = Component.translatable("cyclic.screen.filter.item.count");
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
      NetworkHooks.openScreen((ServerPlayer) playerIn, new EnderBookContainerProvider(), playerIn.blockPosition());
    }
    if (!worldIn.isClientSide && playerIn.isCrouching()) {
      //any damage?
      ItemStack stack = playerIn.getItemInHand(handIn);
      if (stack.getDamageValue() < stack.getMaxDamage() - 1) {
        int enderslot = stack.getTag().getInt(ENDERSLOT);
        BlockPosDim loc = EnderBookItem.getLocation(stack, enderslot);
        if (loc != null) {
          ChatUtil.addServerChatMessage(playerIn, Component.translatable("item.cyclic.ender_book.start").append(loc.toString()));
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
          if (loc.getDimension().equalsIgnoreCase(LevelWorldUtil.dimensionToString(worldIn))) {
            EntityUtil.enderTeleportEvent(p, worldIn, loc.getPos());
          }
          else {
            //diff dim 
            EntityUtil.dimensionTeleport((ServerPlayer) p, (ServerLevel) worldIn, loc);
          }
          // done
          ItemStackUtil.damageItem(p, stack);
          return;
        }
      }
      else if (ct % 20 == 0 && entityIn instanceof Player) {
        ChatUtil.sendStatusMessage((Player) entityIn, Component.translatable("item.cyclic.ender_book.countdown").append("" + (ct / 20)));
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
    IItemHandler cap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
    if (cap != null) {
      return LocationGpsCard.getPosition(cap.getStackInSlot(enderSlot));
    }
    return null;
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new EnderBookCapabilityProvider();
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.ENDER_BOOK.get(), EnderBookScreen::new);
  }

  // ShareTag for server->client capability data sync
  @Override
  public CompoundTag getShareTag(ItemStack stack) {
    CompoundTag nbt = stack.getOrCreateTag();
    IItemHandler cap = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
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
      book.getTag().putInt(ENDERSLOT, enderslot % EnderBookCapabilityProvider.SLOTS);
      BlockPosDim loc = EnderBookItem.getLocation(book, enderslot);
      //      if (loc != null &&
      String msg = "---";
      if (loc != null) {
        msg = loc.getDisplayString();
      }
      ChatUtil.addServerChatMessage(player, Component.translatable(book.getTag().getInt(ENDERSLOT) + " : ").append(msg));
    }
  }

  private static int scrollSlot(final boolean isDown, int enderslot) {
    enderslot += isDown ? -1 : 1;
    if (enderslot < 0) {
      enderslot = EnderBookCapabilityProvider.SLOTS - 1;
    }
    else if (enderslot >= EnderBookCapabilityProvider.SLOTS) {
      enderslot = 0;
    }
    return enderslot;
  }
}
