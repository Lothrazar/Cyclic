package com.lothrazar.cyclicmagic;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.log.ModLogger;
import com.lothrazar.cyclicmagic.module.ICyclicModule;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.ConfigRegistry;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.registry.EventRegistry;
import com.lothrazar.cyclicmagic.registry.FluidsRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.ModuleRegistry;
import com.lothrazar.cyclicmagic.registry.PacketRegistry;
import com.lothrazar.cyclicmagic.registry.PermissionRegistry;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.PotionTypeRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.registry.VillagerProfRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Const.MODID, useMetadata = true, dependencies = "before:guideapi;after:jei;after:baubles", canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.11.2,)", guiFactory = "com.lothrazar." + Const.MODID + ".config.IngameConfigFactory")
public class ModCyclic {
  @Instance(value = Const.MODID)
  public static ModCyclic instance;
  @SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
  public static CommonProxy proxy;
  public static ModLogger logger;
  public EventRegistry events;
  public static SimpleNetworkWrapper network;
  public final static CreativeTabCyclic TAB = new CreativeTabCyclic();
  @CapabilityInject(IPlayerExtendedProperties.class)
  public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;
  static {
    FluidRegistry.enableUniversalBucket();//https://github.com/BluSunrize/ImmersiveEngineering/blob/c76e51998756a54c22dd40ac1877313bf95e8520/src/main/java/blusunrize/immersiveengineering/ImmersiveEngineering.java
  }
  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    logger = new ModLogger(event.getModLog());
    ConfigRegistry.init(new Configuration(event.getSuggestedConfigurationFile()));
    network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    PacketRegistry.register(network);
    SoundRegistry.register();
    CapabilityRegistry.register();
    ReflectionRegistry.register();
    MaterialRegistry.register();
    this.events = new EventRegistry();
    this.events.registerCoreEvents();
    ModuleRegistry.init();
    ModuleRegistry.registerAll();//create new instance of every module
    ConfigRegistry.syncAllConfig();
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onPreInit();
    }
    proxy.preInit();
    //fluids still does the old way ( FluidRegistry.addBucketForFluid)
    //    MinecraftForge.EVENT_BUS.register(FluidsRegistry.class);
    MinecraftForge.EVENT_BUS.register(ItemRegistry.class);
    MinecraftForge.EVENT_BUS.register(BlockRegistry.class);
    MinecraftForge.EVENT_BUS.register(RecipeRegistry.class);
    MinecraftForge.EVENT_BUS.register(SoundRegistry.class);
    MinecraftForge.EVENT_BUS.register(PotionEffectRegistry.class);
    MinecraftForge.EVENT_BUS.register(PotionTypeRegistry.class);
    MinecraftForge.EVENT_BUS.register(EnchantRegistry.class);
    MinecraftForge.EVENT_BUS.register(VillagerProfRegistry.class);
  }
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    PotionEffectRegistry.register();
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onInit();
    }
    proxy.init();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ForgeGuiHandler());
    ConfigRegistry.syncAllConfig(); //fixes things , stuff was added to items and content that has config
    this.events.registerAll(); //important: register events AFTER modules onInit, since modules add events in this phase.
    PermissionRegistry.register();
  }
  @EventHandler
  public void onPostInit(FMLPostInitializationEvent event) {
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onPostInit();
    }
    //    
  }
  @EventHandler
  public void onServerStarting(FMLServerStartingEvent event) {
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onServerStarting(event);
    }
  }
}
