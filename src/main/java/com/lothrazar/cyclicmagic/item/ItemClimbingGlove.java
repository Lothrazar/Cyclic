package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemClimbingGlove extends BaseItem implements IHasRecipe {
  private static final double CLIMB_SPEED = 0.288D;
  private static final int ITEMSLOT_OFFHANDMAX = 8;//offhand is 0 , and hotbar is 0-8 (diff arrays)
  public ItemClimbingGlove() {
    this.setMaxStackSize(1);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    if (itemSlot > ITEMSLOT_OFFHANDMAX) { return; }
    if (!(entityIn instanceof EntityLivingBase)) { return; }
    EntityLivingBase entity = (EntityLivingBase) entityIn;
    if (!entityIn.isCollidedHorizontally) { return; }
    UtilEntity.tryMakeEntityClimb(worldIn, entity, CLIMB_SPEED);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 1),
        "ssl",
        "skl",
        "lli",
        's', Items.SLIME_BALL,
        'i', Items.IRON_INGOT,
        'k', new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()),
        'l', Items.LEATHER);
  }
}
