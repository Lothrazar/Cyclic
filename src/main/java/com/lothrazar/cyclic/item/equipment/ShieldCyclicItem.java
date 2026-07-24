package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ItemAbility;


public class ShieldCyclicItem extends ItemBaseCyclic implements Equipable {

  public static final Identifier BLOCKING = Identifier.parse("minecraft:blocking");

  public static enum ShieldType {
    LEATHER, WOOD, FLINT, OBSIDIAN, BONE;
    // STONE? COPPER? 
  }

  public static ModConfigSpec.IntValue LEATHER_PCT;
  public static ModConfigSpec.IntValue WOOD_PCT;
  public static ModConfigSpec.IntValue FLINT_PCT;
  public static ModConfigSpec.IntValue FLINT_THORNS_PCT;
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
  public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
    return ItemAbilities.DEFAULT_SHIELD_ACTIONS.contains(itemAbility);
  }

  @Override
  public EquipmentSlot getEquipmentSlot() {
    return EquipmentSlot.OFFHAND;
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 72000;
  }

  @Override
  public InteractionResult use(Level world, Player playerIn, InteractionHand hand) {

    ItemStack itemstack = playerIn.getItemInHand(hand);
    playerIn.startUsingItem(hand); //important for Blocking property
    return InteractionResult.CONSUME.heldItemTransformedTo(itemstack);
  }

}
