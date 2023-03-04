package com.lothrazar.cyclic.item.food.inventorycake;

import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.net.PacketKeyBind;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class ItemCakeInventory extends ItemBaseCyclic {

  public ItemCakeInventory(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide && playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayer) playerIn, new ContainerProviderCake(), playerIn.blockPosition());
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.INVENTORY_CAKE.get(), ScreenCake::new);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof Player == false) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    Player player = (Player) entityLiving;
    if (!worldIn.isClientSide) {
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      if (!datFile.storageVisible) {
        datFile.storageVisible = true;
        ChatUtil.addServerChatMessage(player, "cyclic.unlocks.extended");
      }
    }
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }

  public static void onKeyInput(Player player) {
    PacketRegistry.INSTANCE.sendToServer(new PacketKeyBind(""));
  }
}
