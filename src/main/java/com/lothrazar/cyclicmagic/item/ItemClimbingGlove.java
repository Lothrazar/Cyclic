package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemClimbingGlove extends BaseCharm implements IHasRecipe {
  private static final double CLIMB_SPEED = 0.288D;
  public ItemClimbingGlove() {
    super(5000);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    //    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    //    if (itemSlot > ITEMSLOT_OFFHANDMAX) { return; }
    //    if (!(entityIn instanceof EntityLivingBase)) { return; }
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
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
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) { return; }
    if (player.isCollidedHorizontally) {
      World world = player.getEntityWorld();
      UtilEntity.tryMakeEntityClimb(world, player, CLIMB_SPEED);
      if (world.rand.nextDouble() < 0.01) {
        super.damageCharm(player, stack);
      }
    }
  }
}
