package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.block.apple.AppleBuffs;
import com.lothrazar.cyclic.block.apple.AppleChocolate;
import com.lothrazar.cyclic.block.battery.ItemBlockBattery;
import com.lothrazar.cyclic.block.cable.CableWrench;
import com.lothrazar.cyclic.block.expcollect.ExpItemGain;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.block.tank.ItemBlockTank;
import com.lothrazar.cyclic.item.AntimatterEvaporatorWandItem;
import com.lothrazar.cyclic.item.CarbonPaperItem;
import com.lothrazar.cyclic.item.EdibleFlightItem;
import com.lothrazar.cyclic.item.EdibleSpecItem;
import com.lothrazar.cyclic.item.ElevationWandItem;
import com.lothrazar.cyclic.item.EnderBagItem;
import com.lothrazar.cyclic.item.EvokerFangItem;
import com.lothrazar.cyclic.item.GemstoneItem;
import com.lothrazar.cyclic.item.LeverRemote;
import com.lothrazar.cyclic.item.OreProspector;
import com.lothrazar.cyclic.item.PeatItem;
import com.lothrazar.cyclic.item.PeatItem.PeatItemType;
import com.lothrazar.cyclic.item.SleepingMatItem;
import com.lothrazar.cyclic.item.SpawnInspectorTool;
import com.lothrazar.cyclic.item.SpelunkerCaveFinder;
import com.lothrazar.cyclic.item.StirrupsItem;
import com.lothrazar.cyclic.item.StirrupsReverseItem;
import com.lothrazar.cyclic.item.TeleporterWandItem;
import com.lothrazar.cyclic.item.bauble.AirAntiGravity;
import com.lothrazar.cyclic.item.bauble.AutoCaveTorchItem;
import com.lothrazar.cyclic.item.bauble.AutoTorchItem;
import com.lothrazar.cyclic.item.bauble.CharmAntidote;
import com.lothrazar.cyclic.item.bauble.CharmFire;
import com.lothrazar.cyclic.item.bauble.CharmInvisible;
import com.lothrazar.cyclic.item.bauble.CharmOverpowered;
import com.lothrazar.cyclic.item.bauble.CharmVoid;
import com.lothrazar.cyclic.item.bauble.CharmWing;
import com.lothrazar.cyclic.item.bauble.CharmWither;
import com.lothrazar.cyclic.item.bauble.FlippersItem;
import com.lothrazar.cyclic.item.bauble.GloveItem;
import com.lothrazar.cyclic.item.bauble.ItemBaseToggle;
import com.lothrazar.cyclic.item.boomerang.BoomerangItem;
import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.item.builder.BuildStyle;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.carrot.ItemHorseEmeraldJump;
import com.lothrazar.cyclic.item.carrot.ItemHorseEnder;
import com.lothrazar.cyclic.item.carrot.ItemHorseHealthDiamondCarrot;
import com.lothrazar.cyclic.item.carrot.ItemHorseLapisVariant;
import com.lothrazar.cyclic.item.carrot.ItemHorseRedstoneSpeed;
import com.lothrazar.cyclic.item.carrot.ItemHorseToxic;
import com.lothrazar.cyclic.item.crafting.CraftingBagItem;
import com.lothrazar.cyclic.item.craftingsimple.CraftingStickItem;
import com.lothrazar.cyclic.item.datacard.BlockstateCard;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.SettingsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.item.elemental.FireScepter;
import com.lothrazar.cyclic.item.elemental.IceWand;
import com.lothrazar.cyclic.item.elemental.LightningScepter;
import com.lothrazar.cyclic.item.elemental.SnowScepter;
import com.lothrazar.cyclic.item.elemental.WaterSpreaderItem;
import com.lothrazar.cyclic.item.enderbook.EnderBookItem;
import com.lothrazar.cyclic.item.endereye.ItemEnderEyeReuse;
import com.lothrazar.cyclic.item.enderpearl.EnderPearlMount;
import com.lothrazar.cyclic.item.enderpearl.EnderPearlReuse;
import com.lothrazar.cyclic.item.equipment.GlowingHelmetItem;
import com.lothrazar.cyclic.item.equipment.MattockItem;
import com.lothrazar.cyclic.item.equipment.RotatorItem;
import com.lothrazar.cyclic.item.equipment.ShearsMaterial;
import com.lothrazar.cyclic.item.findspawner.ItemProjectileDungeon;
import com.lothrazar.cyclic.item.food.EnderCookie;
import com.lothrazar.cyclic.item.food.StepHeightCookie;
import com.lothrazar.cyclic.item.heart.HeartItem;
import com.lothrazar.cyclic.item.heart.HeartToxicItem;
import com.lothrazar.cyclic.item.magicnet.ItemMagicNet;
import com.lothrazar.cyclic.item.magicnet.ItemMobContainer;
import com.lothrazar.cyclic.item.random.RandomizerItem;
import com.lothrazar.cyclic.item.scythe.ScytheBrush;
import com.lothrazar.cyclic.item.scythe.ScytheForage;
import com.lothrazar.cyclic.item.scythe.ScytheHarvest;
import com.lothrazar.cyclic.item.scythe.ScytheLeaves;
import com.lothrazar.cyclic.item.slingshot.SlingshotItem;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.lothrazar.cyclic.item.torchthrow.ItemTorchThrower;
import com.lothrazar.cyclic.item.transporter.TileTransporterEmptyItem;
import com.lothrazar.cyclic.item.transporter.TileTransporterItem;
import com.lothrazar.cyclic.item.wing.EnderWingItem;
import com.lothrazar.cyclic.item.wing.EnderWingSp;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemRegistry {

  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModCyclic.MODID);
  public static final RegistryObject<Item> FLUIDHOPPER = ITEMS.register("hopper_fluid", () -> new BlockItem(BlockRegistry.FLUIDHOPPER.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> HOPPER = ITEMS.register("hopper", () -> new BlockItem(BlockRegistry.HOPPER.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> HOPPERGOLD = ITEMS.register("hopper_gold", () -> new BlockItem(BlockRegistry.HOPPERGOLD.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> ANVILVOID = ITEMS.register("anvil_void", () -> new BlockItem(BlockRegistry.ANVILVOID.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> FANSLAB = ITEMS.register("fan_slab", () -> new BlockItem(BlockRegistry.FANSLAB.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> ROTATOR = ITEMS.register("rotator", () -> new BlockItem(BlockRegistry.ROTATOR.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> DETECTORMOON = ITEMS.register("detector_moon", () -> new BlockItem(BlockRegistry.DETECTORMOON.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> DETECTORWEATHER = ITEMS.register("detector_weather", () -> new BlockItem(BlockRegistry.DETECTORWEATHER.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> TERRAGLASS = ITEMS.register("terra_glass", () -> new BlockItem(BlockRegistry.TERRAGLASS.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> STATECARD = ITEMS.register("blockstate_data", () -> new BlockstateCard(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> SPRINKLER = ITEMS.register("sprinkler", () -> new BlockItem(BlockRegistry.SPRINKLER.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> SHEARING = ITEMS.register("shearing", () -> new BlockItem(BlockRegistry.SHEARING.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> CHORUS_FLIGHT = ITEMS.register("chorus_flight", () -> new EdibleFlightItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHORUS_SPECTRAL = ITEMS.register("chorus_spectral", () -> new EdibleSpecItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_LONGFALL = ITEMS.register("charm_longfall", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_CREEPER = ITEMS.register("charm_creeper", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_STONE = ITEMS.register("charm_stone", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_ANTIPOTION = ITEMS.register("charm_antipotion", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_STEALTHPOTION = ITEMS.register("charm_stealthpotion", () -> new ItemBaseToggle(new Item.Properties().maxStackSize(1).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_BOOSTPOTION = ITEMS.register("charm_boostpotion", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_CRIT = ITEMS.register("charm_crit", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> QUIVER_DMG = ITEMS.register("quiver_damage", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> QUIVER_LIT = ITEMS.register("quiver_lightning", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CLOAK_INVISIBLE = ITEMS.register("charm_invisible", () -> new CharmInvisible(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_MAGICDEF = ITEMS.register("charm_magicdefense", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_STARVATION = ITEMS.register("charm_starvation", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_VENOM = ITEMS.register("charm_venom", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_WATER = ITEMS.register("charm_water", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_SPEED = ITEMS.register("charm_speed", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_KNOCKBACK_RESIST = ITEMS.register("charm_knockback_resistance", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_LUCK = ITEMS.register("charm_luck", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_ATTACKSPEED = ITEMS.register("charm_attack_speed", () -> new ItemBaseToggle(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_XPSPEED = ITEMS.register("charm_xp_speed", () -> new ItemBaseToggle(new Item.Properties().maxStackSize(1).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_XPSTOPPER = ITEMS.register("charm_xp_blocker", () -> new ItemBaseToggle(new Item.Properties().maxStackSize(1).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> PROSPECTOR = ITEMS.register("prospector", () -> new OreProspector(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> ENDER_BOOK = ITEMS.register("ender_book", () -> new EnderBookItem(new Item.Properties().maxDamage(8).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> DARK_GLASS_CONNECTED = ITEMS.register("dark_glass_connected", () -> new BlockItem(BlockRegistry.DARK_GLASS_CONNECTED.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> ENDER_ITEM_SHELF = ITEMS.register("ender_item_shelf", () -> new BlockItem(BlockRegistry.ENDER_ITEM_SHELF.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> SPIKES_DIAMOND = ITEMS.register("spikes_diamond", () -> new BlockItem(BlockRegistry.spikes_diamond, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> DOORBELL = ITEMS.register("doorbell", () -> new BlockItem(BlockRegistry.DOORBELL.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> WIRELESS_ENERGY = ITEMS.register("wireless_energy", () -> new BlockItem(BlockRegistry.WIRELESS_ENERGY.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> WIRELESS_ITEM = ITEMS.register("wireless_item", () -> new BlockItem(BlockRegistry.WIRELESS_ITEM.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  public static final RegistryObject<Item> BUILD_SCEPTER = ITEMS.register("build_scepter", () -> new BuilderItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP), BuildStyle.NORMAL));
  public static final RegistryObject<Item> REPLACE_SCEPTER = ITEMS.register("replace_scepter", () -> new BuilderItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP), BuildStyle.REPLACE));
  public static final RegistryObject<Item> OFFSET_SCEPTER = ITEMS.register("offset_scepter", () -> new BuilderItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP), BuildStyle.REPLACE));
  public static final RegistryObject<Item> RANDOMIZE_SCEPTER = ITEMS.register("randomize_scepter", () -> new RandomizerItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> SPAWNINSPECTOR = ITEMS.register("spawn_inspector", () -> new SpawnInspectorTool(new Item.Properties().maxDamage(256).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> CHARM_WING = ITEMS.register("charm_wing", () -> new CharmWing(new Item.Properties().maxDamage(64).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> SLINGSHOT = ITEMS.register("slingshot", () -> new SlingshotItem(new Item.Properties().maxDamage(64).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> SOULSTONE = ITEMS.register("soulstone", () -> new ItemBase(new Item.Properties().maxDamage(8).group(MaterialRegistry.ITEM_GROUP)));
  public static final RegistryObject<Item> WIRELESS_FLUID = ITEMS.register("wireless_fluid", () -> new BlockItem(BlockRegistry.WIRELESS_ENERGY.get(), new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)));
  //  //
  public static List<ItemBase> items = new ArrayList<>();
  @ObjectHolder(ModCyclic.MODID + ":charm_fire")
  public static Item charm_fire;
  @ObjectHolder(ModCyclic.MODID + ":gem_amber")
  public static Item gem_amber;
  @ObjectHolder(ModCyclic.MODID + ":biomass")
  public static Item biomass;
  @ObjectHolder(ModCyclic.MODID + ":peat_fuel")
  public static Item peat_fuel;
  @ObjectHolder(ModCyclic.MODID + ":peat_fuel_enriched")
  public static Item peat_fuel_enriched;
  @ObjectHolder(ModCyclic.MODID + ":wrench")
  public static Item wrench;
  @ObjectHolder(ModCyclic.MODID + ":cable_wrench")
  public static CableWrench cable_wrench;
  @ObjectHolder(ModCyclic.MODID + ":spawner_seeker")
  public static Item spawner_seeker;
  @ObjectHolder(ModCyclic.MODID + ":gem_obsidian")
  public static Item gem_obsidian;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_damage")
  public static Item boomerang_damage;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_carry")
  public static Item boomerang_carry;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_stun")
  public static Item boomerang_stun;
  @ObjectHolder(ModCyclic.MODID + ":mob_container")
  public static ItemMobContainer mob_container;
  @ObjectHolder(ModCyclic.MODID + ":experience_food")
  public static Item experience_food;
  @ObjectHolder(ModCyclic.MODID + ":magic_net")
  public static Item magic_net;
  @ObjectHolder(ModCyclic.MODID + ":tile_transporter")
  public static Item tile_transporter;
  @ObjectHolder(ModCyclic.MODID + ":tile_transporter_empty")
  public static Item tile_transporterempty;
  @ObjectHolder(ModCyclic.MODID + ":glowing_helmet")
  public static Item glowing_helmet;
  @ObjectHolder(ModCyclic.MODID + ":elevation_wand")
  public static Item elevation_wand;
  @ObjectHolder(ModCyclic.MODID + ":storage_bag")
  public static Item storage_bag;
  @ObjectHolder(ModCyclic.MODID + ":crafting_bag")
  public static Item crafting_bag;
  @ObjectHolder(ModCyclic.MODID + ":crafting_stick")
  public static Item crafting_stick;
  @ObjectHolder(ModCyclic.MODID + ":antimatter_wand")
  public static Item antimatter_wand;
  @ObjectHolder(ModCyclic.MODID + ":filter_data")
  public static Item filter_data;
  @ObjectHolder(ModCyclic.MODID + ":location")
  public static Item location;
  @ObjectHolder(ModCyclic.MODID + ":shape_data")
  public static Item shape_data;

  @SuppressWarnings("deprecation")
  @SubscribeEvent
  public static void onItemsRegistry(RegistryEvent.Register<Item> event) {
    IForgeRegistry<Item> r = event.getRegistry();
    r.register(new BlockItem(BlockRegistry.ghost_phantom, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("ghost_phantom"));
    r.register(new BlockItem(BlockRegistry.ghost, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("ghost"));
    r.register(new BlockItem(BlockRegistry.laser, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("laser"));
    r.register(new BlockItem(BlockRegistry.apple_sprout, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("apple_sprout"));
    r.register(new BlockItem(BlockRegistry.apple_sprout_diamond, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("apple_sprout_diamond"));
    r.register(new BlockItem(BlockRegistry.apple_sprout_emerald, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("apple_sprout_emerald"));
    r.register(new BlockItem(BlockRegistry.computer_shape, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("computer_shape"));
    r.register(new BlockItem(BlockRegistry.flower_cyan, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("flower_cyan"));
    r.register(new BlockItem(BlockRegistry.mason_cobble, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("mason_cobble"));
    r.register(new BlockItem(BlockRegistry.mason_stone, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("mason_stone"));
    r.register(new BlockItem(BlockRegistry.mason_steel, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("mason_steel"));
    r.register(new BlockItem(BlockRegistry.mason_iron, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("mason_iron"));
    r.register(new BlockItem(BlockRegistry.mason_plate, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("mason_plate"));
    r.register(new BlockItem(BlockRegistry.eye_redstone, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("eye_redstone"));
    r.register(new BlockItem(BlockRegistry.eye_teleport, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("eye_teleport"));
    //
    r.register(new ItemBlockBattery(BlockRegistry.battery, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("battery"));
    r.register(new BlockItem(BlockRegistry.peat_generator, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("peat_generator"));
    r.register(new BlockItem(BlockRegistry.peat_unbaked, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("peat_unbaked"));
    r.register(new BlockItem(BlockRegistry.peat_baked, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("peat_baked"));
    r.register(new BlockItem(BlockRegistry.solidifier, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("solidifier"));
    r.register(new BlockItem(BlockRegistry.peat_farm, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("peat_farm"));
    r.register(new BlockItem(BlockRegistry.melter, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("melter"));
    r.register(new BlockItem(BlockRegistry.placer, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("placer"));
    r.register(new BlockItem(BlockRegistry.breaker, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("breaker"));
    r.register(new BlockItem(BlockRegistry.user, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("user"));
    r.register(new BlockItem(BlockRegistry.dropper, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("dropper"));
    r.register(new BlockItem(BlockRegistry.forester, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("forester"));
    r.register(new BlockItem(BlockRegistry.miner, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("miner"));
    r.register(new BlockItem(BlockRegistry.structure, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("structure"));
    r.register(new BlockItem(BlockRegistry.harvester, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("harvester"));
    r.register(new BlockItem(BlockRegistry.collector, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("collector"));
    r.register(new BlockItem(BlockRegistry.collector_fluid, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("collector_fluid"));
    r.register(new BlockItem(BlockRegistry.placer_fluid, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("placer_fluid"));
    //redstone
    r.register(new BlockItem(BlockRegistry.cask, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("cask"));
    r.register(new BlockItem(BlockRegistry.crate, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("crate"));
    r.register(new BlockItem(BlockRegistry.clock, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("clock"));
    r.register(new BlockItem(BlockRegistry.wireless_transmitter, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("wireless_transmitter"));
    r.register(new BlockItem(BlockRegistry.wireless_receiver, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("wireless_receiver"));
    //fun
    r.register(new BlockItem(BlockRegistry.plate_launch_redstone, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("plate_launch_redstone"));
    r.register(new BlockItem(BlockRegistry.plate_launch, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("plate_launch"));
    // machine blocks   
    r.register(new BlockItem(BlockRegistry.detector_item, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("detector_item"));
    r.register(new BlockItem(BlockRegistry.detector_entity, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("detector_entity"));
    r.register(new BlockItem(BlockRegistry.screen, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("screen"));
    r.register(new BlockItem(BlockRegistry.uncrafter, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("uncrafter"));
    r.register(new BlockItem(BlockRegistry.fisher, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("fisher"));
    r.register(new BlockItem(BlockRegistry.disenchanter, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("disenchanter"));
    r.register(new BlockItem(BlockRegistry.fan, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("fan"));
    r.register(new BlockItem(BlockRegistry.light_camo, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("light_camo"));
    r.register(new BlockItem(BlockRegistry.soundproofing_ghost, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("soundproofing_ghost"));
    r.register(new BlockItem(BlockRegistry.soundproofing, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("soundproofing"));
    r.register(new BlockItem(BlockRegistry.anvil, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("anvil"));
    r.register(new BlockItem(BlockRegistry.anvil_magma, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("anvil_magma"));
    r.register(new BlockItem(BlockRegistry.beacon, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("beacon"));
    r.register(new ItemBlockTank(BlockRegistry.tank, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("tank"));
    r.register(new BlockItem(BlockRegistry.dark_glass, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("dark_glass"));
    r.register(new BlockItem(BlockRegistry.trash, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("trash"));
    r.register(new BlockItem(BlockRegistry.battery_infinite, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("battery_infinite"));
    r.register(new BlockItem(BlockRegistry.item_infinite, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("item_infinite"));
    r.register(new BlockItem(BlockRegistry.dice, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("dice"));
    r.register(new BlockItem(BlockRegistry.terra_preta, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("terra_preta"));
    r.register(new BlockItem(BlockRegistry.water_candle, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("water_candle"));
    r.register(new BlockItem(BlockRegistry.fireplace, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("fireplace"));
    r.register(new BlockItem(BlockRegistry.crafter, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("crafter"));
    r.register(new BlockItem(BlockRegistry.unbreakable_block, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("unbreakable_block"));
    r.register(new BlockItem(BlockRegistry.unbreakable_reactive, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("unbreakable_reactive"));
    r.register(new BlockItem(BlockRegistry.conveyor, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("conveyor"));
    r.register(new BlockItem(BlockRegistry.ender_shelf, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("ender_shelf"));
    r.register(new BlockItem(BlockRegistry.ender_controller, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("ender_controller"));
    r.register(new BlockItem(BlockRegistry.workbench, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("workbench"));
    // exp machines
    r.register(new BlockItem(BlockRegistry.experience_pylon, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("experience_pylon"));
    r.register(new ExpItemGain(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("experience_food"));
    // resources
    r.register(new GemstoneItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("gem_obsidian"));
    r.register(new GemstoneItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("gem_amber"));
    //energy 
    r.register(new PeatItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP), PeatItemType.NORM).setRegistryName("peat_fuel"));
    r.register(new PeatItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP), PeatItemType.ENRICHED).setRegistryName("peat_fuel_enriched"));
    r.register(new PeatItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP), PeatItemType.BIOMASS).setRegistryName("biomass"));
    // basic tools
    r.register(new LocationGpsCard(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("location"));
    r.register(new MattockItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(9000), 1).setRegistryName("mattock"));
    r.register(new MattockItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(9001), 2).setRegistryName("mattock_nether"));
    r.register(new SleepingMatItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("sleeping_mat"));
    r.register(new ShearsMaterial(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(1024 * 1024)).setRegistryName("shears_obsidian"));
    //weak flint n steel 
    r.register(new ShearsMaterial(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(64)).setRegistryName("shears_flint"));
    r.register(new RotatorItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("wrench"));
    r.register(new ScytheBrush(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("scythe_brush"));
    r.register(new ScytheForage(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("scythe_forage"));
    r.register(new ScytheLeaves(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("scythe_leaves"));
    r.register(new StirrupsItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("stirrups"));
    r.register(new StirrupsReverseItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("stirrups_reverse"));
    r.register(new LeverRemote(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxStackSize(1)).setRegistryName("lever_remote"));
    // magic tools
    r.register(new CarbonPaperItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("carbon_paper"));
    r.register(new SnowScepter(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("ice_scepter"));
    r.register(new FireScepter(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("fire_scepter"));
    r.register(new LightningScepter(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("lightning_scepter"));
    r.register(new EnderBagItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("ender_bag"));
    r.register(new WaterSpreaderItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("spell_water"));
    r.register(new IceWand(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("spell_ice"));
    r.register(new ItemTorchThrower(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("torch_launcher"));
    r.register(new AutoTorchItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256 * 4)).setRegistryName("charm_torch"));
    r.register(new AutoCaveTorchItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256 * 4)).setRegistryName("charm_torch_cave"));
    r.register(new EnderWingItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("charm_home"));
    r.register(new EnderWingSp(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("charm_world"));
    r.register(new EvokerFangItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("evoker_fang"));
    r.register(new ItemEnderEyeReuse(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("ender_eye_reuse"));
    r.register(new EnderPearlReuse(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("ender_pearl_reuse"));
    r.register(new EnderPearlMount(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("ender_pearl_mounted"));
    r.register(new ItemProjectileDungeon(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("spawner_seeker"));
    r.register(new SpelunkerCaveFinder(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("spelunker"));
    r.register(new ItemMagicNet(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("magic_net"));
    r.register(new ItemMobContainer(new Item.Properties().maxStackSize(1)).setRegistryName("mob_container"));
    r.register(new TileTransporterEmptyItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("tile_transporter_empty"));
    r.register(new TileTransporterItem(new Item.Properties()).setRegistryName("tile_transporter"));
    r.register(new ElevationWandItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("elevation_wand"));
    r.register(new TeleporterWandItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(64)).setRegistryName("teleport_wand"));
    r.register(new SettingsCard(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("settings_data"));
    r.register(new ShapeCard(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("shape_data"));
    r.register(new FilterCardItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("filter_data"));
    r.register(new ScytheHarvest(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(1024)).setRegistryName("scythe_harvest"));
    r.register(new ItemStorageBag(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxStackSize(1).setNoRepair()).setRegistryName("storage_bag"));
    r.register(new CraftingBagItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxStackSize(1).setNoRepair()).setRegistryName("crafting_bag"));
    r.register(new CraftingStickItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxStackSize(1).setNoRepair()).setRegistryName("crafting_stick"));
    r.register(new AntimatterEvaporatorWandItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(1024)).setRegistryName("antimatter_wand"));
    ///////////////////////// apples
    final int smallPotionDur = 20 * 90; // 1:30
    final int largePotionDur = 3 * 20 * 60; // 3:00
    int h = Foods.APPLE.getHealing();
    float s = Foods.APPLE.getSaturation();
    //honey is basic. fast to eat, gives lots of food but no potion effects 
    r.register(new EnderCookie(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(0).setAlwaysEdible()
        .build())).setRegistryName("apple_ender"));
    //
    r.register(new StepHeightCookie(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(0).setAlwaysEdible()
        .build())).setRegistryName("apple_lofty_stature"));
    //
    r.register(new ItemBase(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h * 4).saturation(s * 4)
        .build())).setRegistryName("apple_honey"));
    //
    r.register(new AppleBuffs(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(s)
        .effect(new EffectInstance(Effects.LEVITATION, largePotionDur, 1), 1)
        .effect(new EffectInstance(Effects.RESISTANCE, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.UNLUCK, largePotionDur, 1), 1)
        .effect(new EffectInstance(Effects.SLOW_FALLING, smallPotionDur, 1), 1)
        .setAlwaysEdible()
        .build())).setRegistryName("apple_chorus"));
    //
    r.register(new AppleBuffs(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(s)
        .effect(new EffectInstance(Effects.JUMP_BOOST, largePotionDur, 4 + 5), 1)
        .effect(new EffectInstance(Effects.INVISIBILITY, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.WEAKNESS, largePotionDur, 2), 1)
        .effect(new EffectInstance(Effects.UNLUCK, largePotionDur, 0), 1)
        .setAlwaysEdible()
        .build())).setRegistryName("apple_bone"));
    //
    r.register(new AppleBuffs(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(s)
        .effect(new EffectInstance(Effects.HASTE, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.GLOWING, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.WATER_BREATHING, largePotionDur, 0), 1)
        .setAlwaysEdible()
        .build())).setRegistryName("apple_prismarine"));
    //
    //iron and lapis are basic ones
    r.register(new AppleBuffs(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(s * 4)
        .effect(new EffectInstance(Effects.NIGHT_VISION, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.WATER_BREATHING, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.CONDUIT_POWER, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.SLOW_FALLING, largePotionDur, 0), 1)
        .effect(new EffectInstance(Effects.SPEED, largePotionDur, 0), 1)
        //        .effect(new EffectInstance(PotionRegistry.PotionEffects.swimspeed, FIVEMIN, 0), 1)
        .fastToEat().setAlwaysEdible()
        .build())).setRegistryName("apple_lapis"));
    r.register(new AppleBuffs(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(s)
        .effect(new EffectInstance(Effects.HEALTH_BOOST, largePotionDur, 2), 1)
        .effect(new EffectInstance(Effects.RESISTANCE, largePotionDur, 2), 1)
        .fastToEat().setAlwaysEdible()
        .build())).setRegistryName("apple_iron"));
    //stronger ones 
    r.register(new AppleBuffs(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(1).saturation(1)
        .effect(new EffectInstance(Effects.HEALTH_BOOST, smallPotionDur, 4), 1)
        .effect(new EffectInstance(Effects.RESISTANCE, smallPotionDur, 4), 1)
        .fastToEat().setAlwaysEdible()
        .build())).setRegistryName("apple_diamond"));
    r.register(new AppleBuffs(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h * 3).saturation(s)
        .effect(new EffectInstance(Effects.HASTE, smallPotionDur, 2), 1)
        .effect(new EffectInstance(Effects.LUCK, smallPotionDur, 1), 1)
        .effect(new EffectInstance(Effects.STRENGTH, smallPotionDur, 1), 1)
        .effect(new EffectInstance(Effects.SLOW_FALLING, smallPotionDur, 1), 1)
        .setAlwaysEdible().build())).setRegistryName("apple_emerald"));
    r.register(new AppleChocolate(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).food(new Food.Builder().hunger(h).saturation(s * 4)
        .setAlwaysEdible().build())).setRegistryName("apple_chocolate"));
    ////////////////////////////////////////
    r.register(new BoomerangItem(Boomer.STUN, new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("boomerang_stun"));
    r.register(new BoomerangItem(Boomer.CARRY, new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("boomerang_carry"));
    r.register(new BoomerangItem(Boomer.DAMAGE, new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("boomerang_damage"));
    r.register(new ItemScaffolding(BlockRegistry.scaffold_replace, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("scaffold_replace"));
    r.register(new ItemScaffolding(BlockRegistry.scaffold_fragile, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("scaffold_fragile"));
    r.register(new ItemScaffolding(BlockRegistry.scaffold_responsive, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("scaffold_responsive"));
    r.register(new BlockItem(BlockRegistry.spikes_iron, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("spikes_iron"));
    r.register(new BlockItem(BlockRegistry.spikes_curse, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("spikes_curse"));
    r.register(new BlockItem(BlockRegistry.spikes_fire, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("spikes_fire"));
    r.register(new BlockItem(BlockRegistry.energy_pipe, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("energy_pipe"));
    r.register(new BlockItem(BlockRegistry.item_pipe, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("item_pipe"));
    r.register(new BlockItem(BlockRegistry.fluid_pipe, new Item.Properties().group(MaterialRegistry.BLOCK_GROUP)).setRegistryName("fluid_pipe"));
    r.register(new CableWrench(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("cable_wrench"));
    r.register(new GloveItem(new Item.Properties().maxDamage(256 * 8).group(MaterialRegistry.ITEM_GROUP)).setRegistryName("glove_climb"));
    r.register(new FlippersItem(new Item.Properties().maxDamage(256 * 4).group(MaterialRegistry.ITEM_GROUP)).setRegistryName("flippers"));
    r.register(new AirAntiGravity(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(1024 * 4)).setRegistryName("antigravity"));
    r.register(new CharmVoid(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(64)).setRegistryName("charm_void"));
    r.register(new CharmAntidote(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(64)).setRegistryName("charm_antidote"));
    r.register(new CharmFire(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(64)).setRegistryName("charm_fire"));
    r.register(new CharmWither(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(64)).setRegistryName("charm_wither"));
    r.register(new CharmOverpowered(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxDamage(256)).setRegistryName("charm_ultimate"));
    r.register(new ItemHorseEnder(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("carrot_ender"));
    r.register(new ItemHorseHealthDiamondCarrot(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("diamond_carrot_health"));
    r.register(new ItemHorseRedstoneSpeed(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("redstone_carrot_speed"));
    r.register(new ItemHorseEmeraldJump(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_carrot_jump"));
    r.register(new ItemHorseLapisVariant(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("lapis_carrot_variant"));
    r.register(new ItemHorseToxic(new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("toxic_carrot"));
    r.register(new SwordItem(MaterialRegistry.ToolMats.GEMOBSIDIAN, 3, -2.4F, (new Item.Properties()).group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_sword"));
    r.register(new PickaxeItem(MaterialRegistry.ToolMats.GEMOBSIDIAN, 1, -2.8F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_pickaxe"));
    r.register(new AxeItem(MaterialRegistry.ToolMats.GEMOBSIDIAN, 5.0F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_axe"));
    r.register(new HoeItem(ItemTier.NETHERITE, -4, 0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_hoe"));
    r.register(new ShovelItem(MaterialRegistry.ToolMats.GEMOBSIDIAN, 1.5F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_shovel"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.GEMOBSIDIAN, EquipmentSlotType.FEET, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_boots"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.GEMOBSIDIAN, EquipmentSlotType.HEAD, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_helmet"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.GEMOBSIDIAN, EquipmentSlotType.CHEST, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_chestplate"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.GEMOBSIDIAN, EquipmentSlotType.LEGS, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("crystal_leggings"));
    r.register(new GlowingHelmetItem(MaterialRegistry.ArmorMats.GLOWING, EquipmentSlotType.HEAD, (new Item.Properties()).group(MaterialRegistry.ITEM_GROUP)).setRegistryName("glowing_helmet"));
    r.register(new SwordItem(MaterialRegistry.ToolMats.EMERALD, 3, -2.4F, (new Item.Properties()).group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_sword"));
    r.register(new PickaxeItem(MaterialRegistry.ToolMats.EMERALD, 1, -2.8F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_pickaxe"));
    r.register(new AxeItem(MaterialRegistry.ToolMats.EMERALD, 5.0F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_axe"));
    r.register(new HoeItem(ItemTier.NETHERITE, -4, 0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_hoe"));
    r.register(new ShovelItem(MaterialRegistry.ToolMats.EMERALD, 1.5F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_shovel"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.EMERALD, EquipmentSlotType.FEET, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_boots"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.EMERALD, EquipmentSlotType.HEAD, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_helmet"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.EMERALD, EquipmentSlotType.CHEST, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_chestplate"));
    r.register(new ArmorItem(MaterialRegistry.ArmorMats.EMERALD, EquipmentSlotType.LEGS, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("emerald_leggings"));
    r.register(new SwordItem(MaterialRegistry.ToolMats.SANDSTONE, 3, -2.4F, (new Item.Properties()).group(MaterialRegistry.ITEM_GROUP)).setRegistryName("sandstone_sword"));
    r.register(new PickaxeItem(MaterialRegistry.ToolMats.SANDSTONE, 1, -2.8F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("sandstone_pickaxe"));
    r.register(new AxeItem(MaterialRegistry.ToolMats.SANDSTONE, 5.0F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("sandstone_axe"));
    r.register(new HoeItem(ItemTier.NETHERITE, -4, 0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("sandstone_hoe"));
    r.register(new ShovelItem(MaterialRegistry.ToolMats.SANDSTONE, 1.5F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("sandstone_shovel"));
    r.register(new SwordItem(MaterialRegistry.ToolMats.NETHERBRICK, 3, -2.4F, (new Item.Properties()).group(MaterialRegistry.ITEM_GROUP)).setRegistryName("netherbrick_sword"));
    r.register(new PickaxeItem(MaterialRegistry.ToolMats.NETHERBRICK, 1, -2.8F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("netherbrick_pickaxe"));
    r.register(new AxeItem(MaterialRegistry.ToolMats.NETHERBRICK, 5.0F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("netherbrick_axe"));
    r.register(new HoeItem(ItemTier.NETHERITE, -4, 0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("netherbrick_hoe"));
    r.register(new ShovelItem(MaterialRegistry.ToolMats.NETHERBRICK, 1.5F, -3.0F, new Item.Properties().group(MaterialRegistry.ITEM_GROUP)).setRegistryName("netherbrick_shovel"));
    r.register(new HeartItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxStackSize(16)).setRegistryName("heart"));
    r.register(new HeartToxicItem(new Item.Properties().group(MaterialRegistry.ITEM_GROUP).maxStackSize(16)).setRegistryName("heart_empty"));
  }
}
