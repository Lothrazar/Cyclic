package com.lothrazar.cyclic.item.inventorycake;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.net.PacketKeyBind;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ItemCakeInventory extends ItemBase {

  public ItemCakeInventory(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayerEntity) playerIn, new ContainerProviderCake(), playerIn.getPosition());
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.inventory_cake, ScreenCake::new);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (!(entityLiving instanceof PlayerEntity)) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    PlayerEntity player = (PlayerEntity) entityLiving;
    if (!worldIn.isRemote) {
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      datFile.storageVisible = !datFile.storageVisible;
      UtilChat.addServerChatMessage(player, "cyclic.unlocks.extended");
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }

  public static void onKeyInput(PlayerEntity player) {
    PacketRegistry.INSTANCE.sendToServer(new PacketKeyBind(""));
  }
}
