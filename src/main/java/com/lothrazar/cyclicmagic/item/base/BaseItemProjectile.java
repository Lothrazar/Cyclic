package com.lothrazar.cyclicmagic.item.base;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseItemProjectile extends BaseItem {
  private static final float VELOCITY_DEFAULT = 1.5F;
  private static final float INACCURACY_DEFAULT = 1.0F;
  private static final float PITCHOFFSET = 0.0F;
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack h = playerIn.getHeldItem(hand);
    onItemThrow(h, worldIn, playerIn, hand);
    playerIn.swingArm(hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, h);
  }
  public abstract void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand);
  public abstract EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z);//, double accelX, double accelY, double accelZ
  public abstract SoundEvent getSound();
  public void doThrow(World world, EntityPlayer player, EnumHand hand, EntityThrowable thing, float velocity) {
    if (!world.isRemote) {
      // func_184538_a
      //zero pitch offset, meaning match the players existing. 1.0 at end ins inn
      thing.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, PITCHOFFSET, velocity, INACCURACY_DEFAULT);
      world.spawnEntity(thing);
    }
    player.swingArm(hand);
    BlockPos pos = player.getPosition();
    UtilSound.playSound(player, pos, getSound(), SoundCategory.PLAYERS, 0.5F);
  }
  public void doThrow(World world, EntityPlayer player, EnumHand hand, EntityThrowable thing) {
    this.doThrow(world, player, hand, thing, VELOCITY_DEFAULT);
  }
}
