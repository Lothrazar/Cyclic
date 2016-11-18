package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.item.BaseItem;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseItemProjectile extends BaseItem {
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    onItemThrow(itemStackIn, worldIn, playerIn, hand);
    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
  }
  abstract void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand);
  private static final float VELOCITY_DEFAULT = 1.5F;
  private static final float INACCURACY_DEFAULT = 1.0F;
  private static final float PITCHOFFSET = 0.0F;
  protected void doThrow(World world, EntityPlayer player, EnumHand hand, EntityThrowable thing, float velocity) {
    if (!world.isRemote) {
      // func_184538_a
      //zero pitch offset, meaning match the players existing. 1.0 at end ins inn
      thing.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, PITCHOFFSET, velocity, INACCURACY_DEFAULT);
      world.spawnEntityInWorld(thing);
    }
    player.swingArm(hand);
    BlockPos pos = player.getPosition();
    UtilSound.playSound(player, pos, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS);
    UtilPlayer.decrStackSize(player, hand);
  }
  protected void doThrow(World world, EntityPlayer player, EnumHand hand, EntityThrowable thing) {
    this.doThrow(world, player, hand, thing, VELOCITY_DEFAULT);
  }
  // backup in case function is renamed or removed -> ItemEnderPearl
  /*
   * public void func_184538_a(Entity p_184538_1_, float p_184538_2_, float
   * p_184538_3_, float p_184538_4_, float p_184538_5_, float p_184538_6_) {
   * float f = -MathHelper.sin(p_184538_3_ * 0.017453292F) *
   * MathHelper.cos(p_184538_2_ * 0.017453292F); float f1 =
   * -MathHelper.sin((p_184538_2_ + p_184538_4_) * 0.017453292F); float f2 =
   * MathHelper.cos(p_184538_3_ * 0.017453292F) * MathHelper.cos(p_184538_2_ *
   * 0.017453292F); this.setThrowableHeading((double)f, (double)f1, (double)f2,
   * p_184538_5_, p_184538_6_); this.motionX += p_184538_1_.motionX;
   * this.motionZ += p_184538_1_.motionZ;
   * 
   * if (!p_184538_1_.onGround) { this.motionY += p_184538_1_.motionY; } }
   */
}
