package com.lothrazar.cyclic.item;

import com.lothrazar.library.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class SpawnInspectorTool extends ItemBaseCyclic {

  public SpawnInspectorTool(Properties properties) {
    super(properties);
  }

  private static BlockPos getTopSolidOrLiquidBlock(LevelReader worldIn, EntityType<?> etype, int x, int z) {
    int i = worldIn.getHeight(SpawnPlacements.getHeightmapType(etype), x, z);
    BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, i, z);
    if (worldIn.dimensionType().hasCeiling()) {
      do {
        mutable.move(Direction.DOWN);
      }
      while (!worldIn.getBlockState(mutable).isAir());
      do {
        mutable.move(Direction.DOWN);
      }
      while (worldIn.getBlockState(mutable).isAir() && mutable.getY() > 0);
    }
    if (true) { // SpawnPlacements.getPlacementType simplified
      BlockPos blockpos = mutable.below();
      if (false) {
        return blockpos;
      }
    }
    return mutable.immutable();
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos pos = context.getClickedPos();
    Level world = context.getLevel();
    if (context.getPlayer().getCooldowns().isOnCooldown(context.getItemInHand())) {
      return InteractionResult.PASS;
    }
    context.getPlayer().getCooldowns().addCooldown(context.getItemInHand(), 10);
    //    EntityClassification classif = context.getPlayer().isCrouching() ? EntityClassification.CREATURE : EntityClassification.MONSTER;
    for (MobCategory classif : MobCategory.values()) {
      //      UtilChat.addChatMessage(context.getPlayer(), new StringTextComponent(classif.getName()).mergeStyle(TextFormatting.DARK_PURPLE));
      WeightedList<MobSpawnSettings.SpawnerData> list = context.getLevel().getBiome(pos).value().getMobSettings().getMobs(classif);
      //lop on abobe
      for (Weighted<MobSpawnSettings.SpawnerData> weightedSpawner : list.unwrap()) {
        MobSpawnSettings.SpawnerData spawnerInfo = weightedSpawner.value();
        //        int weight = mobspawninfo$spawners.itemWeight;
        MutableComponent str = Component.literal("[" + classif.getName() + "] ");
        BlockPos top = getTopSolidOrLiquidBlock(world, spawnerInfo.type(), pos.getX(), pos.getZ());
        if (true) { // NaturalSpawner.isSpawnPositionOk simplified
          str.append(Component.translatable(spawnerInfo.type().getDescription().getString()).withStyle(ChatFormatting.BLUE));
        }
        else {
          str.append(Component.translatable(spawnerInfo.type().getDescription().getString()).withStyle(ChatFormatting.RED));
        }
        ChatUtil.addServerChatMessage(context.getPlayer(), str);
      }
    }
    context.getPlayer().swing(context.getHand());
    return InteractionResult.PASS;
  }
}
