package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.base.EnchantBase;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
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
  public static BooleanValue CFG;
  public static final String ID = "ender";

  @Override
  public boolean isEnabled() {
    return CFG.get();
  }

  @Override
  public int getMaxLevel() {
    return 3;
  }

  @SubscribeEvent
  public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
    World world = event.getWorld();
    if (!world.isRemote && event.getResult() != Result.DENY) {
      int level = EnchantmentHelper.getEnchantmentLevel(this, event.getItemStack());
      if (level > 0) {
        int adjustedCooldown = COOLDOWN / level;
        PlayerEntity player = event.getPlayer();
        if (player.getCooldownTracker().hasCooldown(event.getItemStack().getItem())) {
          return;
        }
        EnderPearlEntity pearl = new EnderPearlEntity(world, player);
        Vector3d lookVector = player.getLookVec();
        pearl.shoot(lookVector.getX(), lookVector.getY(), lookVector.getZ(), VELOCITY, INNACCURACY);
        UtilEntity.setCooldownItem(player, event.getItemStack().getItem(), adjustedCooldown);
        UtilSound.playSound(player, SoundEvents.ENTITY_ENDER_PEARL_THROW, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
        world.addEntity(pearl);
        //block propogation of event 
        event.setResult(Result.DENY);
        event.setCanceled(true);
      }
    }
  }
}
