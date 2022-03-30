package com.lothrazar.cyclic.item;

import java.util.function.Consumer;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.render.ShieldBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class ShieldCyclicItem extends ItemBaseCyclic {

  public static final ResourceLocation BLOCKING = new ResourceLocation("minecraft:blocking");

  public static enum ShieldType {
    LEATHER, WOOD;
  }

  private ShieldType type;

  /**
   * See ItemEvents:onShieldBlock and ClientRegistryCyclic:initShields
   *
   * @param properties
   */
  public ShieldCyclicItem(Properties properties, ShieldType type) {
    super(properties);
    this.type = type;
  }

  @Override
  public boolean isValidRepairItem(ItemStack stackShield, ItemStack stackIngredient) {
    if (type == ShieldType.WOOD)
      return stackIngredient.is(Items.STICK);
    if (type == ShieldType.LEATHER)
      return stackIngredient.is(Items.LEATHER);
    return false;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.BLOCK;
  }

  @Override
  public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
    return toolAction.equals(ToolActions.SHIELD_BLOCK);
  }

  @Override
  public int getUseDuration(ItemStack stack) {
    return 72000;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand hand) {
    ItemStack itemstack = playerIn.getItemInHand(hand);
    playerIn.startUsingItem(hand); //important for Blocking property
    return InteractionResultHolder.consume(itemstack);
  }

  @Override
  public void initializeClient(Consumer<IItemRenderProperties> consumer) {
    consumer.accept(new IItemRenderProperties() {

      @Override
      public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
        return ShieldBlockEntityWithoutLevelRenderer.instance;
      }
    });
  }

  public void onShieldBlock(ShieldBlockEvent event) {
    LivingEntity shieldHolder = event.getEntityLiving();
    ItemStack shield = shieldHolder.getUseItem();
    //not called if event was cancelled from a players cooldown
    if (this.type == ShieldType.LEATHER) {
      System.out.println("Blocked with my LEATHER shield" + event.getBlockedDamage());
      //reduce by 50% so its weaker than vanilla shield
      event.setBlockedDamage(event.getBlockedDamage() * .50F); // TODO Config
      if (shieldHolder instanceof Player playerIn) {
        playerIn.getCooldowns().addCooldown(shield.getItem(), 200); // TOOD config
      }
    }
    if (this == ItemRegistry.SHIELD_LEATHER.get()) {
      //reduce by 25% so its weaker than vanilla shielda
      event.setBlockedDamage(event.getBlockedDamage() * .75F); // TOOD configol
      if (shieldHolder instanceof Player playerIn) {
        playerIn.getCooldowns().addCooldown(shield.getItem(), 4);
        //        event.sour
      }
    }
    DamageSource dmgSource = event.getDamageSource();
    if (dmgSource.isProjectile()) {
      //projectile damage
      // do something custom like kill the projectile
    }
    if (dmgSource.isExplosion() && this.type == ShieldType.WOOD) {
      System.out.println("Blocked explosion");
      //projectile damage
      //cp
      //
      // do something custom like kill the projectile
    }
    //    if (event.getDamageSource() instanceof EntityDamageSource eds) {
    //      Entity enemy = eds.getEntity();
    //      if (enemy instanceof LivingEntity liv) {
    //        enemy.hurt(DamageSource.thorns(shieldHolder), 1);
    //      }
    //    }
    if (dmgSource instanceof IndirectEntityDamageSource eds) {
      Entity enemy = eds.getEntity();
      System.out.println("Blocked ENEMY" + enemy);
      if (enemy instanceof LivingEntity liv) {
        enemy.hurt(DamageSource.thorns(shieldHolder), 1);
        System.out.println("THORNS tO ENEMY" + enemy);
      }
    }
    //make some not take damage
    //    event.setShieldTakesDamage(false);
  }
}
