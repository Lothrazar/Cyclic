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

@Mod(modid = Const.MODID, useMetadata = true, dependencies = "after:JEI;after:Baubles", canBeDeactivated = false, updateJSON = "https://raw.githubusercontent.com/PrinceOfAmber/CyclicMagic/master-1.11.2/update.json", acceptableRemoteVersions = "*", guiFactory = "com.lothrazar." + Const.MODID + ".gui.IngameConfigFactory")
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
    ItemRegistry.register();//now that modules have added their content (items), we can register them
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
   * SOME sort of thing for stepHeight // player.stepHeight = 1.0F; careful on
   * unequip. //TODO:above doesnt get moved down to norm on unequip. save that
   * idea for another item/charm/potion/enchant/something?
   * 
   * fan gui: change distance, change speed, change push/pull
   * 
   * BIG TAB options for my invo buttons . save as player data, not config. ->
   * have a button in both alt guis off to side, hitting it toggles tab mode on
   * and off.
   * 
   * SPEED UPGRADES for many cyclic machines: MAYBE make compatible with stuff
   * from other mods like capacitors. and/or make my own.
   * 
   * multi use bucket: like a lava jug. fill it from a fluid in world or a tank,
   * but it has multiple bucket-units of liquid using it multiple times places
   * multiple liquid source blocks.
   * 
   * water fountain//particle maker: has gui. pick the particle you want
   * (vanilla only if we have to) and whenver its on it spawns particles above
   * or wherever
   * 
   * MAYBE sound maker too?
   * 
   * obsidian maker : takes liquid and outputs blocks
   * 
   * ubertorch? glowstone torch? lights up larger area maybe 32/64 blocks
   * 
   * block that puts fire above on redstone signal?
   * 
   * water redstone spawner? just like a dispenser with bucket, can place remove
   * water
   * 
   * item for my todo list command: simple notepad
   * 
   * item for my /ping command: gps item, shows coords on item. toggle on/off
   * showing on screen can send to chat. maybe control over more f3 stuff like
   * biome/datetime
   * 
   * LADDER CLIMB: remove the && player.moveForward == 0 restriction?
   * 
   * project table (invo plus 3x3)
   * 
   * ??multi furnace / multi dispenser / multi brewing stand
   * 
   * magnet: a way to preview/show range
   * 
   * directional magnet
   * 
   * tesr: animate machines when active
   * 
   * DISABLE the forward vector component of launch enchat, if we are standing
   * still (if no current horiz)
   * 
   * revive my old idea of the flint tool to chop wood, and the idea to disable
   * punching trees?
   * 
   * Reinforced scaffolding - like wood one, still breaks in one hit. disable
   * auto break. climbable like a ladder
   * 
   * disenchanter-> only on books?
   * 
   * tool to create enchanted books but expensive. try to follow existing
   * conventions EX quartz->sharpness
   * 
   * Add Splash and Lingering potions for all new effects enhancement
   * 
   * Trading: raw fish as well as cooked? other fish types?
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
   * [ Trading Tool // gui ] Upgrade villager gui: either make my own or add
   * buttons/some way to view all trades at once --inspired by extrautils
   * trading table that is apparently gone after 1710
   * 
   * exp bottler: item with a gui/inventory put bottles in, toggle on/off and it
   * drains your exp into the bottles at a given ratio // a really good one
   * exists in SuperCraftingFrame -> so not needed..
   * 
   * CYCLICSCEPTER: - ? toggle to tell how many times to rotate an item after
   * placing (ex: stairs to be put upside down)
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
