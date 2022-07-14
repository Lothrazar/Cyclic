package com.lothrazar.cyclic.item.ender;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.SoundUtil;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class ItemProjectileDungeon extends ItemBaseCyclic {

  public static IntValue RANGE;

  public ItemProjectileDungeon(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    EntityDungeonEye ball = new EntityDungeonEye(player, world);
    shootMe(world, player, ball, 0, ItemBaseCyclic.VELOCITY_MAX);
    stack.shrink(1);
    SoundUtil.playSound(player, SoundRegistry.DUNGEONFINDER.get(), 0.1F, 1.0F);
    findTargetLocation(player, ball);
    return super.use(world, player, hand);
  }

  private void findTargetLocation(Player player, EntityDungeonEye entityendereye) {
    if (entityendereye == null || !entityendereye.isAlive()) {
      return; //something happened! but this never happens
    }
    BlockPos blockpos = LevelWorldUtil.findClosestBlock(player, Blocks.SPAWNER, RANGE.get());
    if (blockpos == null) {
      ChatUtil.sendStatusMessage(player, ChatUtil.lang("item.cyclic.spawner_seeker.notfound") + " " + RANGE.get());
      entityendereye.remove(Entity.RemovalReason.DISCARDED);
    }
    else {
      ChatUtil.sendStatusMessage(player, ChatUtil.lang("item.cyclic.spawner_seeker.found"));
      entityendereye.moveTowards(blockpos);
    }
  }
}
