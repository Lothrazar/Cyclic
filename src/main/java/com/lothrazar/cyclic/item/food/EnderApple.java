package com.lothrazar.cyclic.item.food;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.LevelWorldUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.ModConfigSpec;

public class EnderApple extends ItemBaseCyclic {

  public static ModConfigSpec.ConfigValue<List<? extends String>> STRUCTURE_TAGS;
  public static ModConfigSpec.IntValue PRINTED;
  private static final int COOLDOWN = 60;

  public EnderApple(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }

  //does pos exist in biome
  //    RegistryAccess.BUILTIN.get().registryOrThrow(Registry.BIOME_REGISTRY).getOrCreateTag(yourBiomeTagKey).contains(level.getBiome(pos))
  public Pair<BlockPos, Holder<Structure>> findNearestPair(ServerLevel sl, TagKey<Structure> p_215012_, BlockPos p_215013_, int p_215014_, boolean p_215015_) {
    //getLevelSettings() -> worldGenOptions
    if (!sl.getServer().getWorldData().worldGenOptions().generateStructures()) {
      return null;
    }
    else {
      Optional<HolderSet.Named<Structure>> optional = sl.registryAccess().registryOrThrow(Registries.STRUCTURE).getTag(p_215012_);
      if (optional.isEmpty()) {
        return null;
      }
      else {
        Pair<BlockPos, Holder<Structure>> pair = sl.getChunkSource().getGenerator().findNearestMapStructure(sl, optional.get(), p_215013_, p_215014_, p_215015_);
        return pair;
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof Player == false) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    Player player = (Player) entityLiving;
    if (player.getCooldowns().isOnCooldown(this)) {
      return super.finishUsingItem(stack, worldIn, entityLiving);
    }
    player.getCooldowns().addCooldown(this, COOLDOWN);
    if (worldIn instanceof ServerLevel serverlevel) {
      Map<String, Integer> distanceStructNames = new HashMap<>();
      try {
        final List<String> structList = (List<String>) STRUCTURE_TAGS.get();
        for (String conf : structList) {
          //EXAMPLE    test = StructureTags.EYE_OF_ENDER_LOCATED;
          Pair<BlockPos, Holder<Structure>> blockpos = findNearestPair(serverlevel,
              TagKey.create(Registries.STRUCTURE, Identifier.parse(conf)),
              entityLiving.blockPosition(), 100, false);
          if (blockpos != null) {
            //add to ze frekni map yo
            double distance = LevelWorldUtil.distanceBetweenHorizontal(blockpos.getFirst(), entityLiving.blockPosition());
            Holder<Structure> holder = blockpos.getSecond();
            String name = holder.unwrapKey().map(k -> k.location().toString()).orElse(holder.toString());
            distanceStructNames.put(name, (int) distance);
          }
        }
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Apple structure?", e);
      }
      if (distanceStructNames.isEmpty()) {
        ChatUtil.addServerChatMessage(player, "item.cyclic.apple_ender.empty");
      }
      else {
        //
        //SORT
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        distanceStructNames.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        int count = 0;
        for (Map.Entry<String, Integer> e : sortedMap.entrySet()) {
          ChatUtil.addServerChatMessage(player, e.getValue() + "m | " + e.getKey());
          count++;
          //?? is it sorted
          if (count >= PRINTED.get()) {
            break;
          }
        }
      }
    }
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }
}
