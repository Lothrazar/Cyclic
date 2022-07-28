package com.lothrazar.cyclic.item.crafting.simple;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CraftingStickItem extends ItemBaseCyclic {

  public CraftingStickItem(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    if (!worldIn.isClientSide && !playerIn.isCrouching()) {
      NetworkHooks.openScreen((ServerPlayer) playerIn, new CraftingStickContainerProvider(handIn), playerIn.blockPosition());
    }
    return super.use(worldIn, playerIn, handIn);
  }

  @Override
  public void registerClient() {
    MenuScreens.register(MenuTypeRegistry.CRAFTING_STICK.get(), CraftingStickScreen::new);
  }
}
