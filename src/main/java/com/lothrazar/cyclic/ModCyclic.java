package com.lothrazar.cyclic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.lothrazar.cyclic.event.ClientInputEvents;
import com.lothrazar.cyclic.event.EventRender;
import com.lothrazar.cyclic.event.ItemEvents;
import com.lothrazar.cyclic.event.PotionEvents;
import com.lothrazar.cyclic.registry.CommandRegistry;
import com.lothrazar.cyclic.registry.CuriosRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.PotionRegistry;
import com.lothrazar.cyclic.registry.RecipeRegistry;
import com.lothrazar.cyclic.setup.ClientProxy;
import com.lothrazar.cyclic.setup.IProxy;
import com.lothrazar.cyclic.setup.ServerProxy;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModCyclic.MODID)
public class ModCyclic {

  public static final String MODID = "cyclic";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

  public ModCyclic() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    ConfigManager.setup(FMLPaths.CONFIGDIR.get().resolve(MODID + ".toml"));
    FluidRegistry.setup();
    //why is this event so freakin weird 
    //https://github.com/Minecraft-Forge-Tutorials/Custom-Json-Recipes/blob/master/src/main/java/net/darkhax/customrecipeexample/CustomRecipesMod.java
    FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, RecipeRegistry::registerRecipeSerializers);
  }

  private void setup(final FMLCommonSetupEvent event) {
    //now all blocks/items exist
    CuriosRegistry.setup(event);
    PotionRegistry.setup(event);
    PacketRegistry.setup();
    RecipeRegistry.setup();
    proxy.setup();
    MinecraftForge.EVENT_BUS.register(new CommandRegistry());
    MinecraftForge.EVENT_BUS.register(new ClientInputEvents());
    MinecraftForge.EVENT_BUS.register(new PotionEvents());
    MinecraftForge.EVENT_BUS.register(new ItemEvents());
    MinecraftForge.EVENT_BUS.register(new EventRender());
  }
}//scythe_leaves.json: 
 //"#minecraft:leaves", error?
