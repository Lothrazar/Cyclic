package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import java.util.List;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class SpawnInspectorTool extends ItemBase {

  public SpawnInspectorTool(Properties properties) {
    super(properties);
  }

  @SuppressWarnings("deprecation")
  private static BlockPos getTopSolidOrLiquidBlock(IWorldReader worldIn, EntityType<?> etype, int x, int z) {
    int i = worldIn.getHeight(EntitySpawnPlacementRegistry.func_209342_b(etype), x, z);
    BlockPos.Mutable mutable = new BlockPos.Mutable(x, i, z);
    if (worldIn.getDimensionType().getHasCeiling()) {
      do {
        mutable.move(Direction.DOWN);
      }
      while (!worldIn.getBlockState(mutable).isAir());
      do {
        mutable.move(Direction.DOWN);
      }
      while (worldIn.getBlockState(mutable).isAir() && mutable.getY() > 0);
    }
    if (EntitySpawnPlacementRegistry.getPlacementType(etype) == EntitySpawnPlacementRegistry.PlacementType.ON_GROUND) {
      BlockPos blockpos = mutable.down();
      if (worldIn.getBlockState(blockpos).allowsMovement(worldIn, blockpos, PathType.LAND)) {
        return blockpos;
      }
    }
    return mutable.toImmutable();
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    BlockPos pos = context.getPos();
    World world = context.getWorld();
    if (context.getPlayer().getCooldownTracker().hasCooldown(this)) {
      return ActionResultType.PASS;
    }
    context.getPlayer().getCooldownTracker().setCooldown(this, 10);
    //    EntityClassification classif = context.getPlayer().isCrouching() ? EntityClassification.CREATURE : EntityClassification.MONSTER;
    for (EntityClassification classif : EntityClassification.values()) {
      //      UtilChat.addChatMessage(context.getPlayer(), new StringTextComponent(classif.getName()).mergeStyle(TextFormatting.DARK_PURPLE));
      List<MobSpawnInfo.Spawners> list = context.getWorld().getBiome(pos).getMobSpawnInfo().getSpawners(classif);
      //lop on abobe 
      for (MobSpawnInfo.Spawners spawnerInfo : list) {
        //        int weight = mobspawninfo$spawners.itemWeight;
        StringTextComponent str = new StringTextComponent("[" + classif.getName() + "] ");
        BlockPos top = getTopSolidOrLiquidBlock(world, spawnerInfo.type, pos.getX(), pos.getZ());
        if (spawnerInfo.type.isSummonable() && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementType(spawnerInfo.type), world, top, spawnerInfo.type)) {
          str.append(new StringTextComponent(spawnerInfo.type.getName().getString()).mergeStyle(TextFormatting.BLUE));
        }
        else {
          str.append(new StringTextComponent(spawnerInfo.type.getName().getString()).mergeStyle(TextFormatting.RED));
        }
        UtilChat.addServerChatMessage(context.getPlayer(), str);
      }
    }
    return ActionResultType.PASS;
  }
}
