package com.lothrazar.cyclic.item.apple;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilWorld;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class EnderApple extends ItemBaseCyclic {

  final String[] ignoreMe = new String[] {
      "minecraft:shipwreck",
      "minecraft:mineshaft",
      "minecraft:stronghold",
      "minecraft:buried_treasure",
      "minecraft:pillager_outpost",
      "minecraft:village",
      "minecraft:nether_fossil"
  };
  private static final int NUM_PRINTED = 5;
  private static final int COOLDOWN = 60;

  public EnderApple(Properties properties) {
    super(properties); // .food(new Food.Builder().hunger(h).saturation(0)
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }

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
    if (worldIn instanceof ServerLevel) {
      final List<String> structIgnoreList = Arrays.asList(ignoreMe);
      //
      ServerLevel serverWorld = (ServerLevel) worldIn;
      Map<String, Integer> distanceStructNames = new HashMap<>();
      Registry<ConfiguredStructureFeature<?, ?>> registry = worldIn.registryAccess().registryOrThrow(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY);
      //      registry.getho
      IdMap<Holder<ConfiguredStructureFeature<?, ?>>> idmap = registry.asHolderIdMap();
      idmap.forEach(structureFeature -> { // is of type  Holder<ConfiguredStructureFeature<?, ?>>
        try {
          String name = structureFeature.value().feature.getRegistryName().toString();
          if (!structIgnoreList.contains(name)) {
            //then we are allowed to look fori t, we are not in ignore list
            BlockPos targetPos = entityLiving.blockPosition();
            //            LocateCommand y;
            HolderSet<ConfiguredStructureFeature<?, ?>> holderSetOfFeature = HolderSet.direct(structureFeature);
            Pair<BlockPos, Holder<ConfiguredStructureFeature<?, ?>>> searchResult = serverWorld.getChunkSource().getGenerator().findNearestMapFeature(serverWorld,
                holderSetOfFeature, targetPos, 100, false);
            if (searchResult != null && searchResult.getFirst() != null) {
              double distance = UtilWorld.distanceBetweenHorizontal(searchResult.getFirst(), targetPos);
              distanceStructNames.put(name, (int) distance);
            }
          }
        }
        catch (Exception e) {
          ModCyclic.LOGGER.error("", e);
        }
      });
      //      for(int i=0;i<registry.asHolderIdMap().size();i++){
      //        Holder<ConfiguredStructureFeature<?, ?>> thing = registry.asHolderIdMap().byId(i);
      //      }
      //      for (ConfiguredStructureFeature<?,?> structureFeature : registry){  //net.minecraftforge.registries.ForgeRegistries.STRUCTURE_FEATURES) {
      //
      //      }
      //done loopiong on features
      //
      //SORT
      LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
      distanceStructNames.entrySet()
          .stream()
          .sorted(Map.Entry.comparingByValue())
          .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
      //
      //      ModCyclic.LOGGER.info("Sorted Map   : " + sortedMap); 
      int count = 0;
      //      UtilChat.addServerChatMessage(player, "STARRT");
      for (Map.Entry<String, Integer> e : sortedMap.entrySet()) {
        UtilChat.addServerChatMessage(player, e.getValue() + "m | " + e.getKey());
        count++;
        //?? is it sorted
        if (count >= NUM_PRINTED) {
          break;
        }
      }
      //
      //      String name = regName.replace("minecraft:", "");
      //      literalargumentbuilder = literalargumentbuilder.then(Commands.literal(name)
      //            .executes(ctx -> locate(ctx.getSource(), structureFeature)));
      //collections CUSTOM SORT by distance 
      //
    }
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }
}
