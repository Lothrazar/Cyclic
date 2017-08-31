package com.lothrazar.cyclicmagic.item.base;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.util.UtilChat;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * TODO: ItemPlayerLauncher should also extend this
 * 
 * @author Sam
 *
 */
public abstract class BaseItemScepter extends BaseTool {
  private static final float VELOCITY_MAX = 1.5F;
  private static final float INACCURACY_DEFAULT = 1.0F;
  private static final float PITCHOFFSET = 0.0F;
  private static final float MAX_CHARGE = 6.7F;
  private static final float CHARGE_MULTISHOT = MAX_CHARGE - 2;
  private static final int TICKS_USING = 53000;
  public BaseItemScepter(int durability) {
    super(durability);
  }
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
    // float power = Math.min(MAX_CHARGE, ItemBow.getArrowVelocity(charge) * POWER_UPSCALE);
    float percentageCharged = ItemBow.getArrowVelocity(charge);//never zero, its from [0.03,1];
    float amountCharged = percentageCharged * MAX_CHARGE;
    float velocityFactor = percentageCharged * 4;//flat upscale
    //between 0.3 and 5.1 roughly
    UtilChat.sendStatusMessage(player, amountCharged + "");
    Vec3d vecCrossRight = player.getLookVec().normalize().crossProduct(new Vec3d(0, 2, 0));
    Vec3d vecCrossLeft = player.getLookVec().normalize().crossProduct(new Vec3d(0, -2, 0));
    float damage = MathHelper.floor(amountCharged) / 2;//so its an even 3 or 2.5
    EntityThrowable proj = createBullet(world, player, damage);
    this.launchProjectile(world, player, proj, velocityFactor * VELOCITY_MAX);
    ModCyclic.logger.log(" damage " + damage);
    if (amountCharged > CHARGE_MULTISHOT) {
      EntityThrowable projRight = createBullet(world, player, damage);
      projRight.posX += vecCrossRight.x;
      projRight.posZ += vecCrossRight.z;
      this.launchProjectile(world, player, projRight, velocityFactor * VELOCITY_MAX);
      EntityThrowable projLeft = createBullet(world, player, damage);
      projLeft.posX += vecCrossLeft.x;
      projLeft.posZ += vecCrossLeft.z;
      this.launchProjectile(world, player, projLeft, velocityFactor * VELOCITY_MAX);
    }
    UtilItemStack.damageItem(player, stack);
    super.onPlayerStoppedUsing(stack, world, entity, chargeTimer);
    //   super.onUse(stack, player, world, EnumHand.MAIN_HAND);
  }
  public abstract EntityThrowable createBullet(World world, EntityPlayer player, float dmg);
  protected void launchProjectile(World world, EntityPlayer player, EntityThrowable thing, float velocity) {
    if (!world.isRemote) {
      //zero pitch offset, meaning match the players existing. 1.0 at end ins inn
      thing.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, PITCHOFFSET, velocity, INACCURACY_DEFAULT);
      world.spawnEntity(thing);
    }
    BlockPos pos = player.getPosition();
    UtilSound.playSound(player, pos, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS);
  }
}
