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
    PacketRegistry.register(network);
    SoundRegistry.register();
    CapabilityRegistry.register();
    ReflectionRegistry.register();
    this.events = new EventRegistry();
    this.events.registerCoreEvents();
    ModuleRegistry.register(this.modules);//all features are in a module
    this.syncConfig();
    for (ICyclicModule module : this.modules) {
      module.onPreInit();
    }
  }
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    PotionEffectRegistry.register();
    for (ICyclicModule module : this.modules) {
      module.onInit();
    }
    ItemRegistry.register();//now that modules have added their content (items), we can register them
    proxy.register();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    this.syncConfig(); //fixes things , stuff was added to items and content that has config
    this.events.registerAll(); //important: register events AFTER modules onInit, since modules add events in this phase.
  }
  @EventHandler
  public void onPostInit(FMLPostInitializationEvent event) {
    for (ICyclicModule module : this.modules) {
      module.onPostInit();
    }
  }
  @EventHandler
  public void onServerStarting(FMLServerStartingEvent event) {
    for (ICyclicModule module : this.modules) {
      module.onServerStarting(event);
    }
  }
  public void syncConfig() {
    Configuration c = getConfig();
    // hit on startup and on change event from
    // we cant make this a list/loop because the order does matter
    for (ICyclicModule module : this.modules) {
      module.syncConfig(c);
    }
    //for any modules that have created an item, those items might have inner configs, so hit it up
    Item item;//its a leftover mapping from before modules
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
   * TODO: ideas/plans/features
   * 
   * ender charm: same effect as ender potion but without the potion effect.
   * 
   * OWN custom texture for waterwalk effect (shouldnt ust just vanilla
   * prismarine item)
   * 
   * 
   * 
   * DISABLE the forward vector component of launch enchat, if we are standing
   * still (if no current horiz)
   * 
   * craft saplings into sticks
   * 
   * revive my old idea of the flint tool to chop wood, and the idea to disable
   * punching trees?
   * 
   * speed charm -> make sur it works on horses to
   * 
   * Reinforced scaffolding - like wood one, still breaks in one hit. disable
   * auto break. climbable like a ladder
   * 
   * disenchanter
   * 
   * swap tool - just use block on right to keep it simple. use size modes
   * 
   * 
   * 
   * randomizer tool- 3 or 5, just mix up block sin whatever area. ignore tile
   * entities
   * 
   * 
   * 
   * Add Splash and Lingering potions for all new effects enhancement
   * 
   * Trading: raw fish as well as cooked? other fish types?
   * 
   * Block user: takes a tool/item and uses fakePlayer to RIGHT CLICK
   *
   * a custom TNT that does not destroy any items or blocks -> for clearing area
   * but not destroying stuff
   * 
   * FARM BLOCK: give it a slot for shears, and a bunch of slots for buckets
   * then on collide with entity, it can do the milk/shear for you my old code
   * that does the sheep part -> no inventory
   * https://github.com/PrinceOfAmber/SamsPowerups/blob/
   * 06142623bb5da81af702e473d20f0da4273d222d/FarmingBlocks/src/main/java/com/
   * lothrazar/samsfarmblocks/BlockShearWool.java
   * 
   * tool that detects light/brightness (same way that the spawn detector checks
   * light) and auto places torch. uses durability instead of real torch right
   * click tool to enable on/off state, and it just runs while in invo (not just
   * hotbar)
   * 
   * Full achievement system: - achieve registry - modules add their own
   * achieves based on their items. for either craft or get item.
   * http://jabelarminecraft.blogspot.ca/p/minecraft-forge-creating-custom.html
   * along with public void onItemCraftedEvent(PlayerEvent.ItemCraftedEvent
   * event) {
   * 
   * [ Trading Tool // gui ] Upgrade villager gui: either make my own or add
   * buttons/some way to view all trades at once --inspired by extrautils
   * trading table that is apparently gone after 1710
   * 
   * exp bottler: item with a gui/inventory put bottles in, toggle on/off and it
   * drains your exp into the bottles at a given ratio // a really good one
   * exists in SuperCraftingFrame -> so not needed..
   * 
   * CYCLIC: - ? toggle to tell how many times to rotate an item after placing
   * (ex: stairs to be put upside down)
   * 
   * Block placers and structure builder: add same rotator
   * 
   * Block placers and structure builder: import the existing random/pattern
   * feature from cyclic wand
   * 
   * piston wand - ROTATE: STAIRS: allow switch from top to bottom
   * 
   * 
   */
}
