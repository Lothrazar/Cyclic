package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.util.ChatUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.pathfinder.PathComputationType;

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
    if (SpawnPlacements.getPlacementType(etype) == SpawnPlacements.Type.ON_GROUND) {
      BlockPos blockpos = mutable.below();
      if (worldIn.getBlockState(blockpos).isPathfindable(worldIn, blockpos, PathComputationType.LAND)) {
        return blockpos;
      }
    }
    return mutable.immutable();
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    BlockPos pos = context.getClickedPos();
    Level world = context.getLevel();
    if (context.getPlayer().getCooldowns().isOnCooldown(this)) {
      return InteractionResult.PASS;
    }
    context.getPlayer().getCooldowns().addCooldown(this, 10);
    //    EntityClassification classif = context.getPlayer().isCrouching() ? EntityClassification.CREATURE : EntityClassification.MONSTER;
    for (MobCategory classif : MobCategory.values()) {
      //      UtilChat.addChatMessage(context.getPlayer(), new StringTextComponent(classif.getName()).mergeStyle(TextFormatting.DARK_PURPLE));
      WeightedRandomList<MobSpawnSettings.SpawnerData> list = context.getLevel().getBiome(pos).value().getMobSettings().getMobs(classif);
      //lop on abobe 
      for (MobSpawnSettings.SpawnerData spawnerInfo : list.unwrap()) {
        //        int weight = mobspawninfo$spawners.itemWeight;
        TextComponent str = new TextComponent("[" + classif.getName() + "] ");
        BlockPos top = getTopSolidOrLiquidBlock(world, spawnerInfo.type, pos.getX(), pos.getZ());
        if (spawnerInfo.type.canSummon() && NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(spawnerInfo.type), world, top, spawnerInfo.type)) {
          str.append(new TextComponent(spawnerInfo.type.getDescription().getString()).withStyle(ChatFormatting.BLUE));
        }
        else {
          str.append(new TextComponent(spawnerInfo.type.getDescription().getString()).withStyle(ChatFormatting.RED));
        }
        ChatUtil.addServerChatMessage(context.getPlayer(), str);
      }
    }
    context.getPlayer().swing(context.getHand());
    return InteractionResult.PASS;
  }
}
