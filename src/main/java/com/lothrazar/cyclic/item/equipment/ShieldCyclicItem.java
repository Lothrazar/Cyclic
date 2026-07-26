package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.core.dispenser.EquipmentDispenseItemBehavior;
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


public class ShieldCyclicItem extends ItemBaseCyclic {

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
    super(applyRepairMaterial(properties, type));
    this.type = type;
    DispenserBlock.registerBehavior(this, EquipmentDispenseItemBehavior.INSTANCE);
  }

  //isValidRepairItem is gone - repair matching is now configured on Item.Properties via .repairable(...)
  private static Properties applyRepairMaterial(Properties properties, ShieldType type) {
    return switch (type) {
      case WOOD -> properties.repairable(Items.STICK);
      case LEATHER -> properties.repairable(Items.LEATHER);
      case BONE -> properties.repairable(Items.BONE);
      case OBSIDIAN -> properties.repairable(Blocks.OBSIDIAN.asItem());
      case FLINT -> properties.repairable(ItemTags.STONE_TOOL_MATERIALS);
    };
  }

  @Override
  public ItemUseAnimation getUseAnimation(ItemStack stack) {
    return ItemUseAnimation.BLOCK;
  }

  @Override
  public EquipmentSlot getEquipmentSlot(ItemStack stack) {
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
