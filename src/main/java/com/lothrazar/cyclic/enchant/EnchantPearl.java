package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantPearl extends EnchantBase {

  public EnchantPearl(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType... slots) {
    super(rarityIn, typeIn, slots);
    MinecraftForge.EVENT_BUS.register(this);
  }

  private static final int COOLDOWN = 6 * 20;
  private static final float VELOCITY = 1.5F; //Same as EnderPearlItem
  private static final float INNACCURACY = 1F; //Same as EnderPearlItem
  private static final int DURABILITY_DAMAGE = 3;

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @Override
  public boolean canApply(ItemStack stack) {
    return stack.getItem() instanceof SwordItem;
  }

  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
    if (!event.getWorld().isRemote && event.getResult() != Result.DENY) {
      int level = EnchantmentHelper.getEnchantmentLevel(this, event.getItemStack());
      if (level > 0) {
        int adjustedCooldown = COOLDOWN / level;
        PlayerEntity player = event.getPlayer();
        if (player.getCooldownTracker().hasCooldown(event.getItemStack().getItem())) {
          return;
        }
        EnderPearlEntity pearl = new EnderPearlEntity(event.getWorld(), player);
        Vector3d lookVector = player.getLookVec();
        pearl.shoot(lookVector.getX(), lookVector.getY(), lookVector.getZ(), VELOCITY, INNACCURACY);
        UtilEntity.setCooldownItem(player, event.getItemStack().getItem(), adjustedCooldown);
        event.getWorld().playSound((PlayerEntity) null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (event.getWorld().rand.nextFloat() * 0.4F + 0.8F));
        event.getWorld().addEntity(pearl);
        event.getItemStack().damageItem(DURABILITY_DAMAGE, player, (e) -> {});
        //block propogation of event 
        event.setResult(Result.DENY);
        event.setCanceled(true);
      }
    }
  }
}
