package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCharmBoat extends BaseCharm implements IHasRecipe {
  private static final int durability = 2000;
  public ItemCharmBoat() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      EntityPlayer living = (EntityPlayer) entityIn;
      onTick(stack, living);
    }
  }
  @Override
  public void onTick(ItemStack stack,  EntityPlayer entityIn) {
    if (entityIn.getRidingEntity() instanceof EntityBoat) {
      EntityBoat boat = (EntityBoat) entityIn.getRidingEntity();
      if (entityIn.moveForward > 0) {
        float reduce = 0.08F;
        //pulled from private EntityBoat.controlBoat() fn
        boat.motionX += net.minecraft.util.math.MathHelper.sin(-boat.rotationYaw * 0.017453292F) * reduce;
        boat.motionZ += net.minecraft.util.math.MathHelper.cos(boat.rotationYaw * 0.017453292F) * reduce;
        if (entityIn.getEntityWorld().rand.nextDouble() < 0.1) {
          super.damageCharm(entityIn, stack);
        }
      }
    }
  }
  @Override
  public String getTooltip() {
    return "item.charm_boat.tooltip";
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "r n",
        "ic ",
        "iir",
        'c', Items.ARMOR_STAND,
        'n', Items.GUNPOWDER,
        'r', Items.REDSTONE,
        'i', Items.IRON_INGOT);
  }
}
