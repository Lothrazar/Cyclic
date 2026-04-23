package com.lothrazar.cyclic.item.equipment;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.render.ShieldBlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;


public class ShieldCyclicItem extends ItemBaseCyclic {

  public static final ResourceLocation BLOCKING = ResourceLocation.parse("minecraft:blocking");

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
  public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
    return true; // ItemAbilities check simplified
  }

  @Override
  public int getUseDuration(ItemStack stack, net.minecraft.world.entity.LivingEntity entity) {
    return 72000;
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player playerIn, InteractionHand hand) {
    ItemStack itemstack = playerIn.getItemInHand(hand);
    playerIn.startUsingItem(hand); //important for Blocking property
    return InteractionResultHolder.consume(itemstack);
  }

  @Override
  public void initializeClient(java.util.function.Consumer<net.neoforged.neoforge.client.extensions.common.IClientItemExtensions> consumer) {
    consumer.accept(new net.neoforged.neoforge.client.extensions.common.IClientItemExtensions() {

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

  // onShieldBlock and onKnockback stubs removed
}
