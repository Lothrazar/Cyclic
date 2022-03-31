package com.lothrazar.cyclic.item;

import java.util.function.Consumer;
import com.lothrazar.cyclic.render.ShieldBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
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
    LEATHER, WOOD, FLINT;
    // STONE? COPPER? 
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
    if (type == ShieldType.FLINT)
      return stackIngredient.is(ItemTags.STONE_TOOL_MATERIALS);
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

  public void onShieldBlock(ShieldBlockEvent event, Player playerIn) {
    LivingEntity shieldHolder = event.getEntityLiving();
    ItemStack shield = shieldHolder.getUseItem();
    DamageSource dmgSource = event.getDamageSource();
    System.out.println(" does shield take durability by default??  " + event.shieldTakesDamage());
    int thornsDmg = 0;
    int cooldown = 1;
    float reduceBlockedDamagePct = 1F;
    boolean immuneToDamage = false;
    boolean isDestroyed = false;
    //decide based on type and stuff
    switch (this.type) {
      case LEATHER:
        cooldown = 4;
        reduceBlockedDamagePct = 0.25F; // 0.25F means so 25% weaker than normal shield  TODO config?
        //reduce by 50% so its weaker than vanilla shield // 0.50F means 50% weaker than shield
        if (dmgSource.isExplosion()) {
          immuneToDamage = true;
        }
      break;
      case WOOD:
        cooldown = 10;
        reduceBlockedDamagePct = 0.50F; //50% so half as effectve as normal TODO config?
        if (dmgSource.isExplosion()) {
          isDestroyed = true;
        }
      break;
      case FLINT:
        cooldown = 4;
        reduceBlockedDamagePct = 0.25F;
        if (dmgSource.isProjectile()) {
          //50% chance to not take durability from arrows 
          immuneToDamage = playerIn.level.random.nextDouble() < 0.5; // 50% chance  TODO config and hardcoded in lang
        }
        if (!dmgSource.isExplosion()
            && dmgSource.isProjectile()
            && playerIn.level.random.nextDouble() < 0.25) { // chance on  TODO: config and hardcoded in lang
          //ranged thorns
          thornsDmg = 1;
        }
      break;
      //TODO: thorns on MELEE unused. very OP against zombie, might be neat with a cooldown
    }
    //results
    if (immuneToDamage) {
      event.setShieldTakesDamage(false);
    }
    if (isDestroyed && playerIn != null) {
      //      shield.setDamageValue(shield.getMaxDamage()); 
      //      shield.setCount(0);
      //TODO: extra sound?
      shield.hurtAndBreak(shield.getMaxDamage(), playerIn, (p) -> {
        p.broadcastBreakEvent(playerIn.getUsedItemHand());
      });
    }
    event.setBlockedDamage(event.getBlockedDamage() * reduceBlockedDamagePct);
    if (playerIn != null && cooldown > 0) {
      playerIn.getCooldowns().addCooldown(shield.getItem(), cooldown);
    }
    if (thornsDmg > 0
        && event.getDamageSource() instanceof EntityDamageSource eds) {
      Entity enemy = eds.getEntity();
      if (enemy instanceof LivingEntity liv) {
        enemy.hurt(DamageSource.thorns(shieldHolder), thornsDmg);
      }
    }
    //make some not take damage
  }
}
