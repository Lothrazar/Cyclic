/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.item.base;
import java.util.List;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * TODO: extend BaseItemProjectile? then make a third intermediate for the single use ones? maybe
 * 
 * 
 * @author Sam
 *
 */
public abstract class BaseItemChargeScepter extends BaseTool {
  private static final float VELOCITY_MAX = 1.5F;
  private static final float INACCURACY_DEFAULT = 1.0F;
  private static final float PITCHOFFSET = 0.0F;
  private static final float MAX_CHARGE = 9.7F;
  private static final int TICKS_USING = 93000;
  private static final int COOLDOWN = 5;
  public enum ActionType {
    SINGLE, DOUBLE, TRIPLE;
    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT) + 1;
      if (type >= ActionType.values().length) {
        type = 0;
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
    public static ActionType getAction(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return ActionType.values()[tags.getInteger(NBT)];
      }
      catch (Exception e) {
        return SINGLE;
      }
    }
    public static String getName(ItemStack wand) {
      return "wand.action." + ActionType.getAction(wand).toString().toLowerCase();
    }
    public static void setTimeout(ItemStack wand) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
    }
    public static int getTimeout(ItemStack wand) {
      return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
    }
    public static void tickTimeout(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int t = tags.getInteger(NBTTIMEOUT);
      if (t > 0) {
        UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
      }
    }
  }
  public BaseItemChargeScepter(int durability) {
    super(durability);
  }
  public abstract SoundEvent getSound();
  public abstract EntityThrowable createBullet(World world, EntityPlayer player, float dmg);
  //start of toggle fns
  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held.getItem() == this) {
      //did we turn it off? is the visible timer still going?
      if (ActionType.getTimeout(held) > 0) {
        return;
      }
      ActionType.setTimeout(held);
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.tool_mode, SoundCategory.PLAYERS, 0.1F);
      if (!player.getEntityWorld().isRemote) { // server side
        ActionType.toggle(held);
        UtilChat.addChatMessage(player, UtilChat.lang(ActionType.getName(held)));
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
  }
  //end of toggle fns
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return TICKS_USING;//bow has 72000
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
    if (hand != EnumHand.MAIN_HAND || player.getCooldownTracker().hasCooldown(player.getHeldItem(hand).getItem())) {
      //dont let them use it yet
      return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
    }
    player.setActiveHand(hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItemMainhand());
  }
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;//make it use cooldown
  }
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    if (entity instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) entity;
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return;
    }
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return;
    }
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    // float power = Math.min(MAX_CHARGE, ItemBow.getArrowVelocity(charge) * POWER_UPSCALE);
    float percentageCharged = ItemBow.getArrowVelocity(charge);//never zero, its from [0.03,1];
    float amountCharged = percentageCharged * MAX_CHARGE;
    float velocityFactor = percentageCharged * 1.5F;//flat upscale
    //between 0.3 and 5.1 roughly
    //UtilChat.sendStatusMessage(player, amountCharged + "");
    float damage = MathHelper.floor(amountCharged) / 2;//so its an even 3 or 2.5
    int shots = 0;
    switch (ActionType.getAction(stack)) {
      case TRIPLE:
        shootTwins(world, player, velocityFactor, damage);
        shootMain(world, player, velocityFactor, damage);
        shots = 3;
      break;
      case DOUBLE:
        shootTwins(world, player, velocityFactor, damage);
        shots = 2;
      break;
      case SINGLE:
        shootMain(world, player, velocityFactor, damage);
        shots = 1;
      break;
      default:
      break;
    }
    UtilItemStack.damageItem(player, stack, shots);
    player.getCooldownTracker().setCooldown(stack.getItem(), COOLDOWN);
    super.onPlayerStoppedUsing(stack, world, entity, chargeTimer);
    super.onUse(stack, player, world, EnumHand.MAIN_HAND);
  }
  private void shootMain(World world, EntityPlayer player, float velocityFactor, float damage) {
    EntityThrowable proj = createBullet(world, player, damage);
    this.launchProjectile(world, player, proj, velocityFactor * VELOCITY_MAX);
  }
  private void shootTwins(World world, EntityPlayer player, float velocityFactor, float damage) {
    Vec3d vecCrossRight = player.getLookVec().normalize().crossProduct(new Vec3d(0, 2, 0));
    Vec3d vecCrossLeft = player.getLookVec().normalize().crossProduct(new Vec3d(0, -2, 0));
    EntityThrowable projRight = createBullet(world, player, damage);
    projRight.posX += vecCrossRight.x;
    projRight.posZ += vecCrossRight.z;
    this.launchProjectile(world, player, projRight, velocityFactor * VELOCITY_MAX);
    EntityThrowable projLeft = createBullet(world, player, damage);
    projLeft.posX += vecCrossLeft.x;
    projLeft.posZ += vecCrossLeft.z;
    this.launchProjectile(world, player, projLeft, velocityFactor * VELOCITY_MAX);
  }
  protected void launchProjectile(World world, EntityPlayer player, EntityThrowable thing, float velocity) {
    if (!world.isRemote) {
      //zero pitch offset, meaning match the players existing. 1.0 at end ins inn
      thing.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, PITCHOFFSET, velocity, INACCURACY_DEFAULT);
      world.spawnEntity(thing);
    }
    BlockPos pos = player.getPosition();
    UtilSound.playSound(player, pos, getSound(), SoundCategory.PLAYERS);
  }
}
