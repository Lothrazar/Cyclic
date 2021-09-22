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

  private static BlockPos getTopSolidOrLiquidBlock(IWorldReader worldIn, EntityType<?> p_208498_1_, int x, int z) {
    int i = worldIn.getHeight(EntitySpawnPlacementRegistry.func_209342_b(p_208498_1_), x, z);
    BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, i, z);
    if (worldIn.getDimensionType().getHasCeiling()) {
      do {
        blockpos$mutable.move(Direction.DOWN);
      }
      while (!worldIn.getBlockState(blockpos$mutable).isAir());
      do {
        blockpos$mutable.move(Direction.DOWN);
      }
      while (worldIn.getBlockState(blockpos$mutable).isAir() && blockpos$mutable.getY() > 0);
    }
    if (EntitySpawnPlacementRegistry.getPlacementType(p_208498_1_) == EntitySpawnPlacementRegistry.PlacementType.ON_GROUND) {
      BlockPos blockpos = blockpos$mutable.down();
      if (worldIn.getBlockState(blockpos).allowsMovement(worldIn, blockpos, PathType.LAND)) {
        return blockpos;
      }
    }
    return blockpos$mutable.toImmutable();
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
      for (MobSpawnInfo.Spawners mobspawninfo$spawners : list) {
        //        int weight = mobspawninfo$spawners.itemWeight;
        StringTextComponent str = new StringTextComponent("[" + classif.getName() + "] ");
        BlockPos top = getTopSolidOrLiquidBlock(world, mobspawninfo$spawners.type, pos.getX(), pos.getZ());
        if (mobspawninfo$spawners.type.isSummonable() && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementType(mobspawninfo$spawners.type), world, top, mobspawninfo$spawners.type)) {
          str.append(new StringTextComponent(mobspawninfo$spawners.type.getName().getString()).mergeStyle(TextFormatting.BLUE));
        }
        else {
          str.append(new StringTextComponent(mobspawninfo$spawners.type.getName().getString()).mergeStyle(TextFormatting.RED));
        }
        UtilChat.addServerChatMessage(context.getPlayer(), str);
      }
    }
    return ActionResultType.PASS;
  }
}
