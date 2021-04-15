package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilChat;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;

public class EnderCookie extends ItemBase {

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

  public EnderCookie(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasEffect(ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity == false) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    PlayerEntity player = (PlayerEntity) entityLiving;
    if (player.getCooldownTracker().hasCooldown(this)) {
      return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    //    LocateCommand yy;
    if (worldIn instanceof ServerWorld) {
      //      final String[] stuff = new String[] {
      //          "minecraft:mansion",
      //          "minecraft:jungle_pyramid",
      //          "minecraft:desert_pyramid",
      //          "minecraft:igloo",
      //          "minecraft:ruined_portal",
      //          "minecraft:swamp_hut",
      //          "minecraft:monument",
      //          "minecraft:ocean_ruin",
      //          "minecraft:fortress",
      //          "minecraft:bastion_remnant",
      //          "minecraft:endcity",
      //      };
      final List<String> structIgnoreList = Arrays.asList(ignoreMe);
      //
      //
      ServerWorld serverWorld = (ServerWorld) worldIn;
      //      List<BlockPos> foundStructs = new ArrayList<BlockPos>();
      Map<String, Integer> distanceStructNames = new HashMap<>();
      for (Structure<?> structureFeature : net.minecraftforge.registries.ForgeRegistries.STRUCTURE_FEATURES) {
        String name = structureFeature.getRegistryName().toString();
        if (!structIgnoreList.contains(name)) {
          //then we are allowed to look fori t, we are not in ignore list 
          BlockPos targetPos = entityLiving.getPosition();
          final BlockPos posOfStructure = serverWorld.func_241117_a_(structureFeature, targetPos, 100, false);
          if (posOfStructure != null) {
            double distance = UtilWorld.distanceBetweenHorizontal(posOfStructure, targetPos);
            distanceStructNames.put(name, (int) distance);
          }
        }
      }
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
      //            .executes(ctx -> func_241053_a_(ctx.getSource(), structureFeature)));
      //collections CUSTOM SORT by distance 
      //
    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
