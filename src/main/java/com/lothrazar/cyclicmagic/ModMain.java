package com.lothrazar.cyclicmagic;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.event.EventEditSign;
import com.lothrazar.cyclicmagic.event.FurnaceStardewModule;
import com.lothrazar.cyclicmagic.event.LightningTransformModule;
import com.lothrazar.cyclicmagic.event.MobDropChangesModule;
import com.lothrazar.cyclicmagic.event.EventMounted;
import com.lothrazar.cyclicmagic.event.EventMountedPearl;
import com.lothrazar.cyclicmagic.event.DropNametagDeathModule;
import com.lothrazar.cyclicmagic.event.EventPotionModule;
import com.lothrazar.cyclicmagic.event.SkullNameFromSignModule;
import com.lothrazar.cyclicmagic.event.EventSpells;
import com.lothrazar.cyclicmagic.event.core.*;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.module.*;
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
  // thank you for the examples forge. 
  @CapabilityInject(IPlayerExtendedProperties.class)
  public static final Capability<IPlayerExtendedProperties> CAPABILITYSTORAGE = null;
  @EventHandler
  public void onPreInit(FMLPreInitializationEvent event) {
    //CORE modules
    logger = new ModLogger(event.getModLog());
    config = new Configuration(event.getSuggestedConfigurationFile());
    config.load();
    network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
    SoundRegistry.register();
    CapabilityRegistry.register();
    ReflectionRegistry.register();
    PacketRegistry.register(network);
    events = new EventRegistry();//core events
    events.addEvent(new EventConfigChanged());//  MinecraftForge.EVENT_BUS.register(instance);
    events.addEvent(new EventExtendedInventory());
    events.addEvent(new EventKeyInput());
    events.addEvent(new EventPlayerData());
    //Features modules
    this.createFeatureModules();
    //important: sync config before doing anything else, now that constructors have all ran
    this.syncConfig();
    //when a module registers, if its enabled, it can add itself or other objets to instance.events

    for(ICyclicModule module : modules) {
      module.onPreInit();
    }
    //important: register events after modules.
    //since modules can register events too
    //TODO: FIX THIS< if a mod is type init or later, but the events get added in .register, well too late?
    //now tell all events to register/subscribe
    this.events.registerAll();
  }
  private void createFeatureModules() {
    modules.add(new BuilderBlockModule());
    modules.add(new BucketBlockModule());
    modules.add(new CarbonPaperModule());
    modules.add(new ChestSackModule());
    modules.add(new ConsumeablesModule());
    modules.add(new ConveyorPlateModule());
    modules.add(new EnderBookModule());
    modules.add(new EmeraldArmorModule());
    modules.add(new EnderBombModule());
    modules.add(new FragileBlockModule());
    modules.add(new HorseFoodModule());
    modules.add(new MagicBeanModule());
    modules.add(new MobSpawnModule());
    modules.add(new PotionModule());
    modules.add(new ProjectileModule());
    modules.add(new SlimepadModule());
    modules.add(new StackSizeModule());
    modules.add(new StorageBagModule());
    modules.add(new ToolsModule());
    modules.add(new UnbreakableSpawnerModule());
    modules.add(new UncrafterModule());
  //TODO: world gen / nether ore module
    //event modules TODO: make actual modules.?? maybe

    modules.add(new EnchantModule());
    modules.add(new AchievementExpModule());
    modules.add(new EventEditSign());
    modules.add(new EnderChestClickopenModule());
    modules.add(new EndermanDropCarryModule());
    modules.add(new ItemstackInfoModule());
    modules.add(new FragileTorchesModule());
    modules.add(new FurnaceStardewModule());
    modules.add(new GuiTerrariaButtonsModule());
    modules.add(new LadderClimbSpeedModule());
    modules.add(new LightningTransformModule());
    modules.add(new LootTableModule());
    modules.add(new MobDropChangesModule());
    ModMain.instance.events.addEvent(new EventMounted());
    ModMain.instance.events.addEvent(new EventMountedPearl());
    modules.add(new DropNametagDeathModule());
    modules.add(new VillagerNametagModule());
    ModMain.instance.events.addEvent(new DimensionOreModule());
    modules.add(new PassthroughActionModule());
    ModMain.instance.events.addEvent(new EventPotionModule());
    modules.add(new SaplingMutationModule());
    modules.add(new SkullNameFromSignModule());
    modules.add(new F3InfoModule());
    ModMain.instance.events.addEvent(new EventSpells());//so far only used by cyclic wand...
  }
  @EventHandler
  public void onInit(FMLInitializationEvent event) {
    for(ICyclicModule module : modules) {
      module.onInit();
    }
  
    ItemRegistry.register();
    BlockRegistry.registerDimensionOres();
    WorldGenRegistry.register();
    FuelRegistry.register();
    // StackSizeRegistry.register();
    RecipeAlterRegistry.register();
    RecipeNewRegistry.register();
    VillageTradeRegistry.register();
    proxy.register();
    NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());
    ProjectileRegistry.register(event);
    //finally, some items have extra forge events to hook into.
  }
  @EventHandler
  public void onPostInit(FMLPostInitializationEvent event) {
    for(ICyclicModule module : modules) {
      module.onPostInit();
    }
  
    // registers all plantable crops. 
    DispenserBehaviorRegistry.register();
  }
  @EventHandler
  public void onServerStarting(FMLServerStartingEvent event) {
    for(ICyclicModule module : modules) {
      module.onServerStarting();
    }
  
    CommandRegistry.register(event);
  }
  public void syncConfig() {
    // hit on startup and on change event from
    // we cant make this a list/loop because the order does matter
    Configuration c = getConfig();
    for(ICyclicModule module : modules) {
      module.syncConfig(c);
    }
    WorldGenRegistry.syncConfig(c);
   
    ItemRegistry.syncConfig(c);
    FuelRegistry.syncConfig(c);
    RecipeAlterRegistry.syncConfig(c);
    RecipeNewRegistry.syncConfig(c);
    DispenserBehaviorRegistry.syncConfig(c);
    CommandRegistry.syncConfig(c);
    VillageTradeRegistry.syncConfig(c);
    KeyInventoryShiftRegistry.syncConfig(c);
    c.save();
  }
  
  /*
   * 
   * TODO:
   * 
   * 
   * https://www.reddit.com/r/minecraftsuggestions/comments/4smwb5/if_lightning_strikes_a_skeleton_it_turns_into_a/
   * 
   * -- spawn inspector: SKEL/ZOMBIE VARIANTS: stray==winter
   * 
   *  is there a bug in searchrecipe? or a bug in new repeater rec?
   * 
   * fragile torches config: float oddsWillBreak = 0.01F;//  : in config or something? or make this  
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
   * [ Trading Tool // gui]
   *     Upgrade villager gui: either make my own or add buttons/some way to view all trades at once
   *    --inspired by extrautils trading table that is apparently gone after 1710
   * 
   * exp bottler: item with a gui/inventory
   *      put bottles in, toggle on/off and it slowly drains your exp into the bottles at a given ratio
   * 
   * pets live longer and/or respawn
   * 
   * add some of my items to loot tables ?
   *        https://github.com/MinecraftForge/MinecraftForge/blob/master/src/test/java/net/minecraftforge/debug/LootTablesDebug.java
   * 
   * crafting table hotkeys - numpad?
   *
   * ROTATE: STAIRS: allow switch from top to bottom
   * 
   * Fix sorting : UtilInventorySort.sort(p, openInventory);
   * 
   */
}
