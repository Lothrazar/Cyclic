package com.lothrazar.cyclic.item.equipment;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.render.ShieldBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class ShieldCyclicItem extends ItemBaseCyclic {

  public static final ResourceLocation BLOCKING = new ResourceLocation("minecraft:blocking");

  public static enum ShieldType {
    LEATHER, WOOD, FLINT, OBSIDIAN, BONE;
    // STONE? COPPER? 
  }

  public static IntValue LEATHER_PCT;
  public static IntValue WOOD_PCT;
  public static IntValue FLINT_PCT;
  public static IntValue FLINT_THORNS_PCT;
  private ShieldType type;

  /**
   * See ItemEvents:onShieldBlock and ClientRegistryCyclic:initShields
   *
   * @param properties
   */
  public ShieldCyclicItem(Properties properties, ShieldType type) {
    super(properties);
    this.type = type;
    DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    super.appendHoverText(stack, worldIn, tooltip, flagIn);
    // tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip.line").withStyle(ChatFormatting.GRAY));
  }

  @Override
  public boolean isValidRepairItem(ItemStack stackShield, ItemStack stackIngredient) {
    if (type == ShieldType.WOOD)
      return stackIngredient.is(Items.STICK);
    if (type == ShieldType.LEATHER)
      return stackIngredient.is(Items.LEATHER);
    if (type == ShieldType.BONE)
      return stackIngredient.is(Items.BONE);
    if (type == ShieldType.OBSIDIAN)
      return stackIngredient.is(Blocks.OBSIDIAN.asItem());
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
    return ToolActions.DEFAULT_SHIELD_ACTIONS.contains(toolAction) || toolAction.equals(ToolActions.SHIELD_BLOCK);
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
  public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
    consumer.accept(new IClientItemExtensions() {

      @Override
      public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return ShieldBlockEntityWithoutLevelRenderer.instance;
      }
    });
  }
  //1.18.2 below, changed above 
  //  @Override
  //  public void initializeClient(Consumer<IItemRenderProperties> consumer) {
  //    consumer.accept(new IItemRenderProperties() {
  //
  //      @Override
  //      public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
  //        return ShieldBlockEntityWithoutLevelRenderer.instance;
  //      }
  //    });
  //  }

  public void onShieldBlock(ShieldBlockEvent event, Player playerIn) {
    LivingEntity shieldHolder = event.getEntity();
    ItemStack shield = shieldHolder.getUseItem();
    DamageSource dmgSource = event.getDamageSource();
    int thornsDmg = 0;
    int cooldown = 1;
    float reduceBlockedDamagePct = 1F;
    boolean immuneToDamage = false;
    boolean isDestroyed = false;
    //decide based on type and stuff
    switch (this.type) {
      case LEATHER:
        cooldown = 6;
        reduceBlockedDamagePct = LEATHER_PCT.get() / 100F; // 0.25F means so 25% weaker than normal shield  
        //reduce by 50% so its weaker than vanilla shield // 0.50F means 50% weaker than shield
        if (dmgSource.isExplosion()) {
          immuneToDamage = true;
        }
      break;
      case WOOD:
        cooldown = 10;
        reduceBlockedDamagePct = WOOD_PCT.get() / 100F; //50% so half as effectve as normal 
        if (dmgSource.isExplosion()) {
          isDestroyed = true;
        }
      break;
      case FLINT:
        cooldown = 4;
        reduceBlockedDamagePct = FLINT_PCT.get() / 100F;
        if (dmgSource.isProjectile()) {
          //50% chance to not take durability from arrows 
          immuneToDamage = playerIn.level.random.nextDouble() < 0.5; // 50% chance  TODO config and hardcoded in lang
        }
        if (!dmgSource.isExplosion()
            && dmgSource.isProjectile()
            && playerIn.level.random.nextDouble() < (FLINT_THORNS_PCT.get() / 100F)) {
          //ranged thorns
          thornsDmg = 1;
        }
      break;
      case BONE:
        //        reduceBlockedDamagePct = default;
        cooldown = 2;
        //bone has no damage reduction
        //immune to all arrow/projectile damage damage
        if (!dmgSource.isBypassArmor() && dmgSource.isProjectile()) {
          immuneToDamage = true;
        }
      break;
      case OBSIDIAN:
        reduceBlockedDamagePct = 0; // important!
        cooldown = 0;
        if (!dmgSource.isBypassArmor() && dmgSource.isProjectile()) {
          immuneToDamage = true;
        }
        if (!dmgSource.isBypassArmor() && dmgSource.isExplosion()) {
          immuneToDamage = true;
        }
      break;
    }
    //results
    if (immuneToDamage) {
      event.setShieldTakesDamage(false);
    }
    if (isDestroyed && playerIn != null) {
      shield.hurtAndBreak(shield.getMaxDamage(), playerIn, (p) -> {
        p.broadcastBreakEvent(playerIn.getUsedItemHand());
      });
    }
    event.setBlockedDamage(event.getBlockedDamage() * reduceBlockedDamagePct);
    ModCyclic.LOGGER.info(this + " original damage " + event.getOriginalBlockedDamage() + " :set Blocked Damage " + event.getBlockedDamage());
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

  public void onKnockback(LivingKnockBackEvent event) {
    switch (this.type) {
      case BONE:
      break;
      case OBSIDIAN:
        event.setCanceled(true);
      break;
      case FLINT:
      break;
      case LEATHER:
      break;
      case WOOD:
        event.setStrength(event.getStrength() * 1.5F);
      break;
      default:
      break;
    }
  }
}
