package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCharmVoid extends BaseCharm implements IHasRecipe {
  private static final int durability = 16;
  private static final int yLowest = -30;
  private static final int yDest = 255;
  public ItemCharmVoid() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      this.onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void addRecipe() {
    super.addRecipeAndRepair(Items.ENDER_EYE);
  }
  @Override
  public String getTooltip() {
    return "item.charm_void.tooltip";
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) { return; }
    World worldIn = living.getEntityWorld();
    if (living.getPosition().getY() < yLowest) {
      UtilEntity.teleportWallSafe(living, worldIn, new BlockPos(living.getPosition().getX(), yDest, living.getPosition().getZ()));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, living.getSoundCategory());
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.PORTAL, living.getPosition());
    }
  }
}
