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
    register(new ItemConsumeablesModule());
    register(new ItemCharmModule());
    register(new ItemPotionModule());
    register(new ItemHorseFoodModule());
    register(new ItemstackInfoModule());
    register(new DispenserBehaviorModule());
    register(new GearEmeraldModule());
    register(new EnchantModule());
    register(new PlayerAbilitiesModule());
    register(new F3InfoModule());
    register(new FragileTorchesModule());
    register(new FuelAdditionModule());
    register(new GuiTerrariaButtonsModule());
    register(new KeyInventoryShiftModule());
    register(new LightningTransformModule());
    register(new LootTableModule());
    register(new MagicBeanModule());
    register(new MobDropChangesModule());
    register(new MobSpawnModule());
    register(new MountedTweaksModule());
    register(new ItemProjectileModule());
    register(new RecipeChangerModule());
    register(new GearSandstoneModule());
    register(new EnvironmentTweaksModule());
    register(new StackSizeModule());
    register(new ItemToolsModule());
    register(new VillagerCreateModule());
    register(new WorldGenModule());
  }
}
