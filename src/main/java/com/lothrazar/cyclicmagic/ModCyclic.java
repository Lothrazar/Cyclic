package com.lothrazar.cyclicmagic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.*;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.config.Configuration;
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

@Mod(modid = Const.MODID, useMetadata = true, 
dependencies = "before:guideapi;after:jei;after:baubles", canBeDeactivated = false, 
updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", 
acceptableRemoteVersions = "*", 
acceptedMinecraftVersions = "[1.11.2,)",
guiFactory = "com.lothrazar." + Const.MODID + ".gui.IngameConfigFactory")
public class ModCyclic {
  @Instance(value = Const.MODID)
  public static ModCyclic instance;
  @SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
  public static CommonProxy proxy;
  public static ModLogger logger;
  public EventRegistry events;
  public static SimpleNetworkWrapper network;
  private Item tabItem = null;
  public void setTabItemIfNull(Item i) {
    if (tabItem == null)
      tabItem = i;
  }
  public final static CreativeTabs TAB = new CreativeTabs(Const.MODID) {
    @Override
    public ItemStack getTabIconItem() {
      return ModCyclic.instance.tabItem == null ? new ItemStack(Items.DIAMOND) : new ItemStack(ModCyclic.instance.tabItem);
    }
  };
  @CapabilityInject(IPlayerExtendedProperties.class)
  public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;
  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    logger = new ModLogger(event.getModLog());
    ConfigRegistry.init(new Configuration(event.getSuggestedConfigurationFile()));
    network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    PacketRegistry.register(network);
    SoundRegistry.register();
    CapabilityRegistry.register();
    ReflectionRegistry.register();
    this.events = new EventRegistry();
    this.events.registerCoreEvents();
    ModuleRegistry.init();
    ModuleRegistry.registerAll();//create new instance of every module
    ConfigRegistry.syncAllConfig();
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onPreInit();
    }
  }
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    PotionEffectRegistry.register();
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onInit();
    }
//    ItemRegistry.register();//now that modules have added their content (items), we can register them
    proxy.register();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    ConfigRegistry.syncAllConfig(); //fixes things , stuff was added to items and content that has config
    this.events.registerAll(); //important: register events AFTER modules onInit, since modules add events in this phase.
    PermissionRegistry.register();
  }
  @EventHandler
  public void onPostInit(FMLPostInitializationEvent event) {
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onPostInit();
    }
    AchievementRegistry.registerPage();
  }
  @EventHandler
  public void onServerStarting(FMLServerStartingEvent event) {
    for (ICyclicModule module : ModuleRegistry.modules) {
      module.onServerStarting(event);
    }
  }
  /*
   * REQUESTED FEATURES:
   * 
   * https://github.com/PrinceOfAmber/Cyclic/milestones
   * 
   * BUGS:
   * 
   * https://github.com/PrinceOfAmber/Cyclic/issues
   * 
   */ 
}
