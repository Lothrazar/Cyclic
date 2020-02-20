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

  private static final int COOLDOWN = 10;
  private static int DUNGEONRADIUS = 64;

  public ItemProjectileDungeon(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
    ItemStack stack = player.getHeldItem(hand);
    //    if (!world.isRemote) {
    EntityDungeonEye ball = new EntityDungeonEye(player, world);
    ball.shoot(player, player.rotationPitch, player.rotationYaw, 0, 0.5F, 1);
    player.getHeldItem(hand).damageItem(1, player, (p) -> {
      p.sendBreakAnimation(hand);
    });
    world.addEntity(ball);
    Runnable runnable = new Runnable() {

      @Override
      public void run() {
        findTargetLocation(player, ball);
      }
    };
    Thread thread = new Thread(runnable);
    thread.start(); // starts thread in background.
    //    }
    return super.onItemRightClick(world, player, hand);
  }

  private void findTargetLocation(PlayerEntity player, EntityDungeonEye entityendereye) {
    if (entityendereye == null || !entityendereye.isAlive()) {
      return;//something happened!
    }
    BlockPos blockpos = UtilWorld.findClosestBlock(player, Blocks.SPAWNER, DUNGEONRADIUS);
    if (blockpos == null) {
      //      entityendereye.remove();
      UtilChat.sendStatusMessage(player, UtilChat.lang("item.ender_dungeon.notfound") + " " + DUNGEONRADIUS);
    }
    else {
      UtilChat.sendStatusMessage(player, UtilChat.lang("item.ender_dungeon.found") + " " + DUNGEONRADIUS);
      entityendereye.moveTowards(blockpos);
    }
  }
}
