package com.lothrazar.cyclicmagic;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.proxy.CommonProxy;
import com.lothrazar.cyclicmagic.registry.*;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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

@Mod(modid = Const.MODID, useMetadata = true, canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master/update.json", acceptableRemoteVersions = "*", guiFactory = "com.lothrazar." + Const.MODID + ".gui.IngameConfigFactory")
public class ModMain {
  private List<ICyclicModule> modules = new ArrayList<ICyclicModule>();
  @Instance(value = Const.MODID)
  public static ModMain instance;
  @SidedProxy(clientSide = "com.lothrazar." + Const.MODID + ".proxy.ClientProxy", serverSide = "com.lothrazar." + Const.MODID + ".proxy.CommonProxy")
  public static CommonProxy proxy;
  public static ModLogger logger;
  public EventRegistry events;
  public static SimpleNetworkWrapper network;
  private static Configuration config;
  public static Configuration getConfig() {
    return config;
  }
  public final static CreativeTabs TAB = new CreativeTabs(Const.MODID) {
    @Override
    public Item getTabIconItem() {
      return ItemRegistry.cyclic_wand_build == null ? Items.DIAMOND : ItemRegistry.cyclic_wand_build;
    }
  };
  @CapabilityInject(IPlayerExtendedProperties.class)
  public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;
  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    logger = new ModLogger(event.getModLog());
    config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    SoundRegistry.register();
    CapabilityRegistry.register();
    ReflectionRegistry.register();
    PacketRegistry.register(network);
    events = new EventRegistry();
    events.registerCoreEvents();
    //Features modules
    ModuleRegistry.register(modules);
    //important: sync config before doing anything else, now that constructors have all ran
    this.syncConfig();
    //when a module registers, if its enabled, it can add itself or other objets to instance.events
    for (ICyclicModule module : modules) {
      module.onPreInit();
    }
  }
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    for (ICyclicModule module : modules) {
      module.onInit();
    }
    ItemRegistry.register();
    proxy.register();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    this.syncConfig(); //fixes things , stuff was added to items and content that has config
    this.events.registerAll(); //important: register events AFTER modules onInit, since modules add events in this phase.
  }
  @EventHandler
  public void onPostInit(FMLPostInitializationEvent event) {
    for (ICyclicModule module : modules) {
      module.onPostInit();
    }
  }
  @EventHandler
  public void onServerStarting(FMLServerStartingEvent event) {
    for (ICyclicModule module : modules) {
      module.onServerStarting(event);
    }
  }
  public void syncConfig() {
    Configuration c = getConfig();
    // hit on startup and on change event from
    // we cant make this a list/loop because the order does matter
    for (ICyclicModule module : modules) {
      module.syncConfig(c);
    }
    //for any modules that have created an item, those items might have inner configs, so hit it up
    Item item;
    for (String key : ItemRegistry.itemMap.keySet()) {
      item = ItemRegistry.itemMap.get(key);
      if (item instanceof IHasConfig) {
        ((IHasConfig) item).syncConfig(config);
      }
    }
    c.save();
  }
  /*
   * 
   * TODO:
   * 
   * 
   * https://www.reddit.com/r/minecraftsuggestions/comments/4smwb5/
   * if_lightning_strikes_a_skeleton_it_turns_into_a/
   * 
   * -- spawn inspector: SKEL/ZOMBIE VARIANTS: stray==winter
   * 
   * is there a bug in searchrecipe? or a bug in new repeater rec?
   * 
   * fragile torches config: float oddsWillBreak = 0.01F;// : in config or
   * something? or make this
   * 
   * frozen/snow effect
   * 
   * config: command search spawner max distances
   * 
   * circle sometimes gets duplicate positions, slowing it down
   * 
   * Some way to keep certain items after death - runestone? enchantment?
   * 
   * ButtonBuildToggle-store ptr to wand not player??
   * 
   * [ Trading Tool // gui] Upgrade villager gui: either make my own or add
   * buttons/some way to view all trades at once --inspired by extrautils
   * trading table that is apparently gone after 1710
   * 
   * exp bottler: item with a gui/inventory put bottles in, toggle on/off and it
   * slowly drains your exp into the bottles at a given ratio
   * 
   * pets live longer and/or respawn
   * 
   * add some of my items to loot tables ?
   * https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/
   * net/minecraftforge/debug/LootTablesDebug.java
   * 
   * crafting table hotkeys - numpad?
   *
   * ROTATE: STAIRS: allow switch from top to bottom
   * 
   * Fix sorting : UtilInventorySort.sort(p, openInventory);
   * 
   */
}
