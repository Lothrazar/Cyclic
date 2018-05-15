package com.lothrazar.cyclicmagic.item.crashtestdummy;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    if (charge > 12 && !world.isRemote) {
      BlockPos pos = entity.getPosition();//look location???
      EntityRobot robot = new EntityRobot(world);
      robot.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
      world.spawnEntity(robot);
      stack.damageItem(1, player);
      player.getCooldownTracker().setCooldown(stack.getItem(), COOLDOWN);
    }
    super.onUse(stack, player, world, EnumHand.MAIN_HAND);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "gze",
        " bz",
        "b g",
        'b', Items.BONE,
        'z', Items.ROTTEN_FLESH,
        'e', Items.SPIDER_EYE,
        'g', Items.GUNPOWDER);
  }

  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (event.getEntity() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();
    if (event.getTarget() instanceof EntityRobot) {
      EntityRobot bot = ((EntityRobot) event.getTarget());
      ItemStack held = event.getItemStack().copy();
      if (held.getItem() == this) {
        bot.setDead();
      }
      else {
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
          if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR &&
              held.getItem().isValidArmor(held, slot, bot)) {
            UtilItemStack.dropItemStackInWorld(entityPlayer.world, bot.getPosition(), bot.getItemStackFromSlot(slot).copy());
            bot.setItemStackToSlot(slot, held);
            entityPlayer.setHeldItem(event.getHand(), ItemStack.EMPTY);
            bot.setDropChance(slot, 100);
            break;
          }
        }
      }
    }
  }
}
