package com.lothrazar.cyclicmagic.registry;
import java.util.List;
import com.lothrazar.cyclicmagic.ICyclicModule;
import com.lothrazar.cyclicmagic.module.*;

public class ModuleRegistry {
  public static void register(List<ICyclicModule> modules) {
    // :) http://alphabetizer.flap.tv/
    modules.add(new BucketBlockModule());
    modules.add(new CarbonPaperModule());
    modules.add(new ChestSackModule());
    modules.add(new CommandModule());
    modules.add(new ConsumeablesModule());
    modules.add(new ConveyorPlateModule());
    modules.add(new DispenserBehaviorModule());
    modules.add(new DropNametagDeathModule());
    modules.add(new EditSignBarehandModule());
    modules.add(new EmeraldArmorModule());
    modules.add(new EnchantModule());
    modules.add(new EnderBookModule());
    modules.add(new EnderChestClickopenModule());
    modules.add(new F3InfoModule());
    modules.add(new FragileBlockModule());
    modules.add(new FragileTorchesModule());
    modules.add(new FurnaceStardewModule());
    modules.add(new FuelAdditionModule());
    modules.add(new GuiTerrariaButtonsModule());
    modules.add(new HorseFoodModule());
    modules.add(new ItemstackInfoModule());
    modules.add(new KeyInventoryShiftModule());
    modules.add(new LadderClimbSpeedModule());
    modules.add(new LightningTransformModule());
    modules.add(new LootTableModule());
    modules.add(new MagicBeanModule());
    modules.add(new MachineBlockModule());
    modules.add(new MobDropChangesModule());
    modules.add(new MobSpawnModule());
    modules.add(new MountedTweaksModule());
    modules.add(new PassthroughActionModule());
    modules.add(new PotionModule());
    modules.add(new ProjectileModule());
    modules.add(new RecipeChangerModule());
    modules.add(new SandstoneToolsModule());
    modules.add(new SaplingPlantDespawnModule());
    modules.add(new SkullNameFromSignModule());
    modules.add(new SlimepadModule());
    modules.add(new StackSizeModule());
    modules.add(new StorageBagModule());
    modules.add(new ToolsModule());
    modules.add(new UnbreakableSpawnerModule());
    modules.add(new VillagerCreateModule());
    modules.add(new VillagerNametagModule());
    modules.add(new WorldGenModule());
  }
}
