package com.lothrazar.cyclicmagic.item.crashtestdummy;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCrashSpawner extends BaseTool implements IHasRecipe {

  //
  private static final int COOLDOWN = 20 * 5;
  private static final int TICKS_USING = 53000;//bow has 72000

  public ItemCrashSpawner() {
    super(50);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return TICKS_USING;//bow has 72000
  }

  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;//make it use cooldown
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
    if (hand != EnumHand.MAIN_HAND) {
      return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
    }
    player.setActiveHand(hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItemMainhand());
  }

  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    EntityPlayer player = (EntityPlayer) entity;
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) {
      return;
    }
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    if (!world.isRemote) {
      BlockPos pos = entity.getPosition();//look location???
      EntityRobot robot = new EntityRobot(world);
      robot.setPosition(pos.getX(), pos.getY(), pos.getZ());
      world.spawnEntity(robot);
    }
    UtilChat.sendStatusMessage(player, this.getUnlocalizedName() + ".health" + charge);
    stack.damageItem(1, player);
    //bow konws how to say , how charged up am i, ok heres your power
    //    float power = Math.min(MAX_POWER, ItemBow.getArrowVelocity(charge) * POWER_UPSCALE);
    //    Vec3d vec = player.getLookVec().normalize();
    //    int rev = (ActionType.isForward(stack)) ? 1 : -1;
    //    power *= rev;//flip it the other way if we are going backwards
    //    player.addVelocity(vec.x * power,
    //        vec.y * power / VERTICAL_FACTOR,
    //        vec.z * power);
    //    player.addPotionEffect(new PotionEffect(PotionEffectRegistry.BOUNCE, POTION_TIME, 0));
    //    UtilSound.playSound(player, player.getPosition(), SoundRegistry.machine_launch, SoundCategory.PLAYERS);
    player.getCooldownTracker().setCooldown(stack.getItem(), COOLDOWN);
    super.onUse(stack, player, world, EnumHand.MAIN_HAND);
  }

  @Override
  public IRecipe addRecipe() {
    // TODO Auto-generated method stub
    return null;
  }
}
