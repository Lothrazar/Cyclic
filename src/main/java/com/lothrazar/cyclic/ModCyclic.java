package com.lothrazar.cyclic;

import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ClientRegistry;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.lothrazar.cyclic.registry.EventRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.RecipeRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;

@Mod(ModCyclic.MODID)
public class ModCyclic {

  public static final String MODID = "cyclic";
  public static final CyclicLogger LOGGER = new CyclicLogger(LogManager.getLogger());

  public ModCyclic() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(EventRegistry::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientRegistry::setupClient);
    DistExecutor.safeRunForDist(() -> ClientRegistry::new, () -> EventRegistry::new);
    ConfigRegistry.setup(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
    FluidRegistry.setup();
    FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RecipeRegistry::registerRecipeSerializers);
    MinecraftForge.EVENT_BUS.register(new CommandRegistry());
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    BlockRegistry.BLOCKS.register(bus);
    ItemRegistry.ITEMS.register(bus);
    TileRegistry.TILES.register(bus);
  }
}
