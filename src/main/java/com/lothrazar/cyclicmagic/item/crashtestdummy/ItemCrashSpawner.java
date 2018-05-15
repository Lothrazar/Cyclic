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
    int health = charge * 10;
    if (!world.isRemote) {
      BlockPos pos = entity.getPosition();//look location???
      EntityRobot robot = new EntityRobot(world);
      robot.setMaxHealth(health);//NO(T WORKIN
      robot.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
      world.spawnEntity(robot);
    }
    UtilChat.addChatMessage(player, this.getUnlocalizedName() + ".health" + health);
    stack.damageItem(1, player);

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
