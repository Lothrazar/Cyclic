package com.lothrazar.cyclicmagic.item.base;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
/**
 * TODO: ItemPlayerLauncher should also extend this
 * @author Sam
 *
 */
public abstract class BaseItemScepter extends BaseTool {
  public BaseItemScepter(int durability) {
    super(durability);
  }
  public static final float VELOCITY_DEFAULT = 1.5F;
  public static final float INACCURACY_DEFAULT = 1.0F;
  public static final float PITCHOFFSET = 0.0F;
  private static final float MAX_POWER = 6.7F;
  private static final float POWER_UPSCALE = 5.18F;
  //  @Override
  //  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
  //    ItemStack h = playerIn.getHeldItem(hand);
  //    onItemThrow(h, worldIn, playerIn, hand);
  //    playerIn.swingArm(hand);
  //    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, h);
  //  }
  private static final int TICKS_USING = 53000;
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return TICKS_USING;//bow has 72000
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    playerIn.setActiveHand(hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
  }
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;//make it use cooldown
  }
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    if (entity instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) entity;
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) { return; }
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    float power = Math.min(MAX_POWER, ItemBow.getArrowVelocity(charge) * POWER_UPSCALE);
    Vec3d vec = player.getLookVec().normalize();
    
    
    //TODO: Refactor the abstract throw and factor in this power for velocit
    //    player.getCooldownTracker().setCooldown(stack.getItem(), COOLDOWN);
    this.onItemThrow(stack, world, player, EnumHand.MAIN_HAND);
    super.onPlayerStoppedUsing(stack, world, entity, chargeTimer);
    //   super.onUse(stack, player, world, EnumHand.MAIN_HAND);
  }
  public abstract void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand);
  protected void doThrow(World world, EntityPlayer player, EnumHand hand, EntityThrowable thing, float velocity) {
    if (!world.isRemote) {
      // func_184538_a
      //zero pitch offset, meaning match the players existing. 1.0 at end ins inn
      thing.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, PITCHOFFSET, velocity, INACCURACY_DEFAULT);
      world.spawnEntity(thing);
    }
    player.swingArm(hand);
    BlockPos pos = player.getPosition();
    UtilSound.playSound(player, pos, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS);
  }
  protected void doThrow(World world, EntityPlayer player, EnumHand hand, EntityThrowable thing) {
    this.doThrow(world, player, hand, thing, VELOCITY_DEFAULT);
  }
}
