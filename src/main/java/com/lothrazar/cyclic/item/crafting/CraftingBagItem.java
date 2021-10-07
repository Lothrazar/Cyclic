package com.lothrazar.cyclic.item.crafting;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;

import net.minecraft.world.item.Item.Properties;

public class CraftingBagItem extends ItemBase {

  public CraftingBagItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayer) playerIn, new CraftingBagContainerProvider(), playerIn.blockPosition());
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public void registerClient() {
    MenuScreens.register(ContainerScreenRegistry.CRAFTING_BAG, CraftingBagScreen::new);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag nbt) {
    return new CraftingBagCapabilityProvider();
  }
}
