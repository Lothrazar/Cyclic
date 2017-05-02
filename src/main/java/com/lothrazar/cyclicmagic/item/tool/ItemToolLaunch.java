package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.item.BaseTool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemToolLaunch extends BaseTool {
  private static final float POWER_UPSCALE = 3.88F;
  private static final float MAX_POWER = 6.5F;
  private static final float VERTICAL_FACTOR = 2.88F;
  private static final int TICKS_USING = 53000;
  private boolean isReverse = false;
  public ItemToolLaunch(int durability) {
    super(durability);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    playerIn.setActiveHand(hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
  }
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    if (entity.onGround == false) { return; }
    if (entity instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) entity;
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    //bow konws how to say , how charged up am i, ok heres your power
    float power = Math.min(MAX_POWER, ItemBow.getArrowVelocity(charge) * POWER_UPSCALE);
    Vec3d vec = player.getLookVec().normalize();
    int rev = (isReverse) ? -1 : 1;
    power *= rev;//flip it the other way if we are going backwards
    player.addVelocity(vec.xCoord * power,
        vec.yCoord * power / VERTICAL_FACTOR,
        vec.zCoord * power);
    super.onUse(stack, player, world, EnumHand.MAIN_HAND);
  }
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return TICKS_USING;
  }
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;//make it use cooldown
  }
}
