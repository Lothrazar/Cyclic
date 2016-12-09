package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCharmSpeed extends BaseCharm implements IHasRecipe {
  private static final int durability = 32;
  public ItemCharmSpeed() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) { return; }
    if (player.moveForward > 0) {
      if (player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityLivingBase) {
        applyToEntity(stack, (EntityLivingBase) player.getRidingEntity());
      }
      else {
        applyToEntity(stack, player);
      }
      if (player.getEntityWorld().rand.nextDouble() < 0.1) {
        super.damageCharm(player, stack);
      }
    }
  }
  private void applyToEntity(ItemStack stack, EntityLivingBase player) {
    float reduce = 0.08F;
    player.motionX += net.minecraft.util.math.MathHelper.sin(-player.rotationYaw * 0.017453292F) * reduce;
    player.motionZ += net.minecraft.util.math.MathHelper.cos(player.rotationYaw * 0.017453292F) * reduce;
  }
  @Override
  public void addRecipe() {
    super.addRecipeAndRepair(Items.EMERALD);
  }
  @Override
  public String getTooltip() {
    return "item.charm_speed.tooltip";
  }
}
