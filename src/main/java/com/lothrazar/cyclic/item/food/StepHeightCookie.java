package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.base.ItemBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class StepHeightCookie extends ItemBase {

  public StepHeightCookie(Properties properties) {
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
    //    PlayerEntity player = (PlayerEntity) entityLiving;
    //    if (player.getCooldownTracker().hasCooldown(this)) {
    //      return super.onItemUseFinish(stack, worldIn, entityLiving);
    //    }
    //    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    //    if (worldIn instanceof ServerWorld) {
    //      final List<String> structIgnoreList = Arrays.asList(ignoreMe);
    //      //
    //      ServerWorld serverWorld = (ServerWorld) worldIn;
    //      Map<String, Integer> distanceStructNames = new HashMap<>();
    //      for (Structure<?> structureFeature : net.minecraftforge.registries.ForgeRegistries.STRUCTURE_FEATURES) {
    //        try {
    //          String name = structureFeature.getRegistryName().toString();
    //          if (!structIgnoreList.contains(name)) {
    //            //then we are allowed to look fori t, we are not in ignore list 
    //            BlockPos targetPos = entityLiving.getPosition();
    //            final BlockPos posOfStructure = serverWorld.func_241117_a_(structureFeature, targetPos, 100, false);
    //            if (posOfStructure != null) {
    //              double distance = UtilWorld.distanceBetweenHorizontal(posOfStructure, targetPos);
    //              distanceStructNames.put(name, (int) distance);
    //            }
    //          }
    //        }
    //        catch (Exception e) {
    //          //third party non vanilla mods can crash, or cause ServerWatchdog errors. example:
    //          //          java.lang.Error: ServerHangWatchdog detected that a single server tick took 192.11 seconds (should be max 0.05)
    //          // ...
    //          //          at com.telepathicgrunt.repurposedstructures.world.structures.AbstractBaseStructure.locateStructureFast(AbstractBaseStructure.java:61) ~[repurposed_structures:2.7.5] {re:classloading}
    //          //          at com.telepathicgrunt.repurposedstructures.world.structures.AbstractBaseStructure.func_236388_a_(AbstractBaseStructure.java:41) ~[repurposed_structures:2.7.5] {re:classloading}
    //          //          
    //        }
    //      }
    //      //done loopiong on features
    //      //
    //      //SORT
    //      LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
    //      distanceStructNames.entrySet()
    //          .stream()
    //          .sorted(Map.Entry.comparingByValue())
    //          .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
    //      //
    //      //      ModCyclic.LOGGER.info("Sorted Map   : " + sortedMap); 
    //      int count = 0;
    //      //      UtilChat.addServerChatMessage(player, "STARRT");
    //      for (Map.Entry<String, Integer> e : sortedMap.entrySet()) {
    //        UtilChat.addServerChatMessage(player, e.getValue() + "m | " + e.getKey());
    //        count++;
    //        //?? is it sorted
    //        if (count >= NUM_PRINTED) {
    //          break;
    //        }
    //      }
    //      //
    //      //      String name = regName.replace("minecraft:", "");
    //      //      literalargumentbuilder = literalargumentbuilder.then(Commands.literal(name)
    //      //            .executes(ctx -> func_241053_a_(ctx.getSource(), structureFeature)));
    //      //collections CUSTOM SORT by distance 
    //      //
    //    }
    return super.onItemUseFinish(stack, worldIn, entityLiving);
  }
}
