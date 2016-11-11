package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ICyclicModule;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.module.*;

public class ModuleRegistry {
  public static List<ICyclicModule> modules = new ArrayList<ICyclicModule>();
  public static void init() {
    modules = new ArrayList<ICyclicModule>();
  }
  public static void register(ICyclicModule m) {
    modules.add(m);
    if (m instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) m);
    }
  }
  public static void registerAll() {
    // :) http://alphabetizer.flap.tv/
    register(new BlockMachineModule());
    register(new BlockPlateModule());
    register(new BlockUtilityModule());
    register(new CommandModule());
    register(new ConsumeablesModule());
    register(new CharmModule());
    register(new DispenserBehaviorModule());
    register(new EmeraldGearModule());
    register(new EnchantModule());
    register(new PlayerAbilitiesModule());
    register(new F3InfoModule());
    register(new FragileTorchesModule());
    register(new FurnaceStardewModule());
    register(new FuelAdditionModule());
    register(new GuiTerrariaButtonsModule());
    register(new HorseFoodModule());
    register(new ItemstackInfoModule());
    register(new KeyInventoryShiftModule());
    register(new LightningTransformModule());
    register(new LootTableModule());
    register(new MagicBeanModule());
    register(new MobDropChangesModule());
    register(new MobSpawnModule());
    register(new MountedTweaksModule());
    register(new PotionModule());
    register(new ProjectileModule());
    register(new RecipeChangerModule());
    register(new SandstoneToolsModule());
    register(new SaplingPlantDespawnModule());
    register(new StackSizeModule());
    register(new ToolsModule());
    register(new UnbreakableSpawnerModule());
    register(new VillagerCreateModule());
    register(new VillagerNametagModule());
    register(new WorldGenModule());
  }
}
