package com.lothrazar.cyclic.item.findspawner;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilSound;
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
    EntityDungeonEye ball = new EntityDungeonEye(player, world);
    shootMe(world, player, ball);
    stack.shrink(1);
    UtilSound.playSound(player, player.getPosition(), SoundRegistry.dungeonfinder, 0.1F, 1.0F);
    findTargetLocation(player, ball);
    return super.onItemRightClick(world, player, hand);
  }

  private void findTargetLocation(PlayerEntity player, EntityDungeonEye entityendereye) {
    if (entityendereye == null || !entityendereye.isAlive()) {
      return; //something happened! but this never happens
    }
    BlockPos blockpos = UtilWorld.findClosestBlock(player, Blocks.SPAWNER, DUNGEONRADIUS);
    if (blockpos == null) {
      UtilChat.sendStatusMessage(player, UtilChat.lang("item.cyclic.spawner_seeker.notfound") + " " + DUNGEONRADIUS);
      entityendereye.remove();
    }
    else {
      UtilChat.sendStatusMessage(player, UtilChat.lang("item.cyclic.spawner_seeker.found"));
      entityendereye.moveTowards(blockpos);
    }
  }
}
