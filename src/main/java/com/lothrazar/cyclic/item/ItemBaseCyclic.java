package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBaseCyclic extends Item {

  public static final float INACCURACY_DEFAULT = 1.0F;
  public static final float VELOCITY_MAX = 1.5F;

  public ItemBaseCyclic(Properties properties) {
    super(properties);
    ItemRegistry.ITEMSFIXME.add(this);
  }

  protected void shootMe(Level world, Player shooter, ThrowableItemProjectile ball, float pitch, float velocityFactor) {
    if (world.isClientSide) {
      return;
    }
    Vec3 vector3d1 = shooter.getUpVector(1.0F);
    // pitch is degrees so can be -10, +10, etc
    Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), pitch, true);
    Vec3 vector3d = shooter.getViewVector(1.0F);
    Vector3f vector3f = new Vector3f(vector3d);
    vector3f.transform(quaternion);
    ball.shoot(vector3f.x(), vector3f.y(), vector3f.z(), velocityFactor * VELOCITY_MAX, INACCURACY_DEFAULT);
    //    worldIn.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(),
    //        SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
    world.addFreshEntity(ball);
  }

  protected ItemStack findAmmo(Player player, Item item) {
    for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
      ItemStack itemstack = player.getInventory().getItem(i);
      if (itemstack.getItem() == item) {
        return itemstack;
      }
    }
    return ItemStack.EMPTY;
  }

  public void tryRepairWith(ItemStack stackToRepair, Player player, Item target) {
    if (stackToRepair.isDamaged()) {
      ItemStack torches = this.findAmmo(player, target);
      if (!torches.isEmpty()) {
        torches.shrink(1);
        UtilItemStack.repairItem(stackToRepair);
      }
    }
  }

  public float getChargedPercent(ItemStack stack, int chargeTimer) {
    return BowItem.getPowerForTime(this.getUseDuration(stack) - chargeTimer);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
  }

  @OnlyIn(Dist.CLIENT)
  public void registerClient() {}
}
