package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.CyclicFile;
import com.lothrazar.cyclic.event.PlayerDataEvents;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
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

public class CakeInventoryItem extends ItemBase {

  public CakeInventoryItem(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    if (!worldIn.isRemote && playerIn.isCrouching()) {
      NetworkHooks.openGui((ServerPlayerEntity) playerIn, new CakeContainerProvider(), playerIn.getPosition());
    }
    return super.onItemRightClick(worldIn, playerIn, handIn);
  }

  @Override
  public void registerClient() {
    ScreenManager.registerFactory(ContainerScreenRegistry.inventory_cake, CakeScreen::new);
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
    if (entityLiving instanceof PlayerEntity == false) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    PlayerEntity player = (PlayerEntity) entityLiving;
    if (!worldIn.isRemote) {
      CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
      datFile.storageVisible = !datFile.storageVisible;
      ModCyclic.LOGGER.info(" storage toggle " + datFile);
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
