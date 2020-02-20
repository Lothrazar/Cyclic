package com.lothrazar.cyclic.item.findspawner;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemProjectileDungeon extends ItemBase {

  private static final int DUNGEONRADIUS = 64;

  public ItemProjectileDungeon(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    //    if (!world.isRemote) {
    EntityDungeonEye ball = new EntityDungeonEye(player, world);
    ball.shoot(player, player.rotationPitch, player.rotationYaw, 0, 0.5F, 1);
    stack.shrink(1);
    world.addEntity(ball);
    Runnable runnable = new Runnable() {

      @Override
      public void run() {
        findTargetLocation(player, ball);
      }
    };
    Thread thread = new Thread(runnable);
    thread.start(); // starts thread in background.
    return super.onItemRightClick(world, player, hand);
  }

  private void findTargetLocation(PlayerEntity player, EntityDungeonEye entityendereye) {
    if (entityendereye == null || !entityendereye.isAlive()) {
      return;//something happened!
    }
    BlockPos blockpos = UtilWorld.findClosestBlock(player, Blocks.SPAWNER, DUNGEONRADIUS);
    if (blockpos == null) {
      UtilChat.sendStatusMessage(player, UtilChat.lang("item.cyclic.ender_dungeon.notfound") + " " + DUNGEONRADIUS);
      entityendereye.remove();
    }
    else {
      UtilChat.sendStatusMessage(player, UtilChat.lang("item.cyclic.ender_dungeon.found"));
      entityendereye.moveTowards(blockpos);
    }
  }
}
