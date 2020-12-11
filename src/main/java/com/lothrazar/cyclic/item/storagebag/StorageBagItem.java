package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class StorageBagItem extends ItemBase {
  public StorageBagItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && !playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayerEntity) playerIn, new StorageBagContainerProvider(), playerIn.getPosition());
    }

    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.storage_bag, StorageBagScreen::new);
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
    int slots = 27;
    return new StorageBagCapabilityProvider(stack, slots);
  }
}
