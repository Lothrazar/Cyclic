package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.battery.ItemBlockBattery;
import com.lothrazar.cyclic.block.batteryclay.ItemBlockClayBattery;
import com.lothrazar.cyclic.block.cable.CableWrench;
import com.lothrazar.cyclic.block.expcollect.ExpItemGain;
import com.lothrazar.cyclic.block.expcollect.ItemBlockPylon;
import com.lothrazar.cyclic.block.scaffolding.ItemScaffolding;
import com.lothrazar.cyclic.block.tank.ItemBlockTank;
import com.lothrazar.cyclic.block.tankcask.ItemBlockCask;
import com.lothrazar.cyclic.item.CarbonPaperItem;
import com.lothrazar.cyclic.item.ElevationWandItem;
import com.lothrazar.cyclic.item.FluteItem;
import com.lothrazar.cyclic.item.GemstoneItem;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.item.LaserItem;
import com.lothrazar.cyclic.item.OreProspector;
import com.lothrazar.cyclic.item.SleepingMatItem;
import com.lothrazar.cyclic.item.SpawnInspectorTool;
import com.lothrazar.cyclic.item.SpelunkerCaveFinder;
import com.lothrazar.cyclic.item.TeleporterWandItem;
import com.lothrazar.cyclic.item.WandHypnoItem;
import com.lothrazar.cyclic.item.animal.ItemHorseCopperRadar;
import com.lothrazar.cyclic.item.animal.ItemHorseEmeraldJump;
import com.lothrazar.cyclic.item.animal.ItemHorseEnder;
import com.lothrazar.cyclic.item.animal.ItemHorseHealthDiamondCarrot;
import com.lothrazar.cyclic.item.animal.ItemHorseLapisVariant;
import com.lothrazar.cyclic.item.animal.ItemHorseNetheriteFire;
import com.lothrazar.cyclic.item.animal.ItemHorsePrismarineWater;
import com.lothrazar.cyclic.item.animal.ItemHorseQuartzStep;
import com.lothrazar.cyclic.item.animal.ItemHorseRedstoneSpeed;
import com.lothrazar.cyclic.item.animal.ItemHorseToxic;
import com.lothrazar.cyclic.item.animal.StirrupsItem;
import com.lothrazar.cyclic.item.animal.StirrupsReverseItem;
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
import com.lothrazar.cyclic.item.bauble.GloveItem;
import com.lothrazar.cyclic.item.bauble.ItemBaseToggle;
import com.lothrazar.cyclic.item.bauble.SoulstoneCharm;
import com.lothrazar.cyclic.item.boomerang.BoomerangItem;
import com.lothrazar.cyclic.item.boomerang.BoomerangItem.Boomer;
import com.lothrazar.cyclic.item.builder.BuildStyle;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.compass.GpsCompassItem;
import com.lothrazar.cyclic.item.crafting.CraftingBagItem;
import com.lothrazar.cyclic.item.crafting.simple.CraftingStickItem;
import com.lothrazar.cyclic.item.datacard.BlockstateCard;
import com.lothrazar.cyclic.item.datacard.EntityDataCard;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.SettingsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.datacard.SoundCard;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.item.datacard.fluid.FluidFilterCardItem;
import com.lothrazar.cyclic.item.elemental.AntimatterEvaporatorWandItem;
import com.lothrazar.cyclic.item.elemental.EvokerFangItem;
import com.lothrazar.cyclic.item.elemental.FireExtinguishItem;
import com.lothrazar.cyclic.item.elemental.FireScepter;
import com.lothrazar.cyclic.item.elemental.FireballItem;
import com.lothrazar.cyclic.item.elemental.FishingMagicItem;
import com.lothrazar.cyclic.item.elemental.GlowingSpark;
import com.lothrazar.cyclic.item.elemental.IceWand;
import com.lothrazar.cyclic.item.elemental.LightningScepter;
import com.lothrazar.cyclic.item.elemental.SnowScepter;
import com.lothrazar.cyclic.item.elemental.TorchThrowingItem;
import com.lothrazar.cyclic.item.elemental.WaterSpreaderItem;
import com.lothrazar.cyclic.item.ender.EnderBagItem;
import com.lothrazar.cyclic.item.ender.EnderEyeReuseItem;
import com.lothrazar.cyclic.item.ender.EnderPearlMount;
import com.lothrazar.cyclic.item.ender.EnderPearlReuse;
import com.lothrazar.cyclic.item.ender.EnderWingItem;
import com.lothrazar.cyclic.item.ender.EnderWingSp;
import com.lothrazar.cyclic.item.ender.ItemProjectileDungeon;
import com.lothrazar.cyclic.item.enderbook.EnderBookItem;
import com.lothrazar.cyclic.item.equipment.AmethystAxeItem;
import com.lothrazar.cyclic.item.equipment.AmethystHoeItem;
import com.lothrazar.cyclic.item.equipment.AmethystPickaxeItem;
import com.lothrazar.cyclic.item.equipment.AmethystShovelItem;
import com.lothrazar.cyclic.item.equipment.GlowingHelmetItem;
import com.lothrazar.cyclic.item.equipment.MattockItem;
import com.lothrazar.cyclic.item.equipment.RotatorItem;
import com.lothrazar.cyclic.item.equipment.ShearsMaterial;
import com.lothrazar.cyclic.item.equipment.ShieldCyclicItem;
import com.lothrazar.cyclic.item.equipment.ShieldCyclicItem.ShieldType;
import com.lothrazar.cyclic.item.food.AppleChocolate;
import com.lothrazar.cyclic.item.food.ChocolateMilk;
import com.lothrazar.cyclic.item.food.EdibleFlightItem;
import com.lothrazar.cyclic.item.food.EdibleSpecItem;
import com.lothrazar.cyclic.item.food.EnderApple;
import com.lothrazar.cyclic.item.food.FoodItemWithEffects;
import com.lothrazar.cyclic.item.food.HeartItem;
import com.lothrazar.cyclic.item.food.HeartToxicItem;
import com.lothrazar.cyclic.item.food.LoftyStatureApple;
import com.lothrazar.cyclic.item.food.MilkBottle;
import com.lothrazar.cyclic.item.lunchbox.ItemLunchbox;
import com.lothrazar.cyclic.item.magicnet.ItemMagicNet;
import com.lothrazar.cyclic.item.magicnet.ItemMobContainer;
import com.lothrazar.cyclic.item.missile.WandMissileItem;
import com.lothrazar.cyclic.item.random.RandomizerItem;
import com.lothrazar.cyclic.item.redstone.LeverRemote;
import com.lothrazar.cyclic.item.scythe.ScytheBrush;
import com.lothrazar.cyclic.item.scythe.ScytheForage;
import com.lothrazar.cyclic.item.scythe.ScytheHarvest;
import com.lothrazar.cyclic.item.scythe.ScytheLeaves;
import com.lothrazar.cyclic.item.slingshot.SlingshotItem;
import com.lothrazar.cyclic.item.storagebag.ItemStorageBag;
import com.lothrazar.cyclic.item.torchthrow.ItemTorchThrower;
import com.lothrazar.cyclic.item.transporter.TileTransporterEmptyItem;
import com.lothrazar.cyclic.item.transporter.TileTransporterItem;
import com.lothrazar.cyclic.registry.MaterialRegistry.ToolMats;
import com.lothrazar.library.core.Const;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {

  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModCyclic.MODID);
  static final int SMALLPOTIONDUR = Const.TICKS_PER_SEC * 90; // 1:30
  static final int LARGEPOTIONDUR = 3 * Const.TICKS_PER_SEC * 60; // 3:00
  static final float APPLESATUR = Foods.APPLE.saturation();
  static final int APPLENUT = Foods.APPLE.nutrition();
  public static final DeferredItem<Item> GEM_OBSIDIAN = ITEMS.registerItem("gem_obsidian", props -> new GemstoneItem(props));
  public static final DeferredItem<Item> GEM_AMBER = ITEMS.registerItem("gem_amber", props -> new GemstoneItem(props));
  public static final DeferredItem<Item> APPLE_HONEY = ITEMS.registerItem("apple_honey", props -> new ItemBaseCyclic(props.food(new FoodProperties.Builder().nutrition(APPLENUT * 4).saturationModifier(APPLESATUR * 4).build())));
  public static final DeferredItem<Item> APPLE_LOFTY_STATURE = ITEMS.registerItem("apple_lofty_stature", props -> new LoftyStatureApple(props.food(new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(0).alwaysEdible()
      .build(), Consumable.builder().sound(SoundRegistry.STEP_HEIGHT_UP).build())));
  // 26.1: FoodProperties.Builder#effect(...) removed - on-consume effects moved onto the Consumable
  // component (Consumable.Builder#onConsume(ApplyStatusEffectsConsumeEffect)) which is now a separate
  // second arg to Item.Properties#food(FoodProperties, Consumable), see vanilla's own
  // net.minecraft.world.item.component.Consumables for the reference shape.
  public static final DeferredItem<Item> APPLE_CHORUS = ITEMS.registerItem("apple_chorus", props -> new FoodItemWithEffects(props.food(
      new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(APPLESATUR).alwaysEdible().build(),
      Consumable.builder()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.LEVITATION, LARGEPOTIONDUR, 1), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.RESISTANCE, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.UNLUCK, LARGEPOTIONDUR, 1), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.SLOW_FALLING, SMALLPOTIONDUR, 1), 1))
          .build())));
  public static final DeferredItem<Item> APPLE_BONE = ITEMS.registerItem("apple_bone", props -> new FoodItemWithEffects(props.food(
      new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(APPLESATUR).alwaysEdible().build(),
      Consumable.builder()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.JUMP_BOOST, LARGEPOTIONDUR, 4 + 5), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.INVISIBILITY, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.WEAKNESS, LARGEPOTIONDUR, 2), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.UNLUCK, LARGEPOTIONDUR, 0), 1))
          .build())));
  public static final DeferredItem<Item> APPLE_PRISMARINE = ITEMS.registerItem("apple_prismarine", props -> new FoodItemWithEffects(props.food(
      new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(APPLESATUR).alwaysEdible().build(),
      Consumable.builder()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.HASTE, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.GLOWING, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.WATER_BREATHING, LARGEPOTIONDUR, 0), 1))
          .build())));
  public static final DeferredItem<Item> APPLE_LAPIS = ITEMS.registerItem("apple_lapis", props -> new FoodItemWithEffects(props.food(
      new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(APPLESATUR * 4).alwaysEdible().build(),
      Consumable.builder().consumeSeconds(0.8F) // was .fast()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.NIGHT_VISION, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.WATER_BREATHING, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.CONDUIT_POWER, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.SLOW_FALLING, LARGEPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.SPEED, LARGEPOTIONDUR, 0), 1))
          .build())));
  public static final DeferredItem<Item> APPLE_IRON = ITEMS.registerItem("apple_iron", props -> new FoodItemWithEffects(props.food(
      new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(APPLESATUR).alwaysEdible().build(),
      Consumable.builder().consumeSeconds(0.8F) // was .fast()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.HEALTH_BOOST, LARGEPOTIONDUR, 2), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.RESISTANCE, LARGEPOTIONDUR, 2), 1))
          .build())));
  public static final DeferredItem<Item> APPLE_DIAMOND = ITEMS.registerItem("apple_diamond", props -> new FoodItemWithEffects(props.food(
      new FoodProperties.Builder().nutrition(1).saturationModifier(1).alwaysEdible().build(),
      Consumable.builder().consumeSeconds(0.8F) // was .fast()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.HEALTH_BOOST, SMALLPOTIONDUR, 4), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.RESISTANCE, SMALLPOTIONDUR, 4), 1))
          .build())));
  public static final DeferredItem<Item> APPLE_EMERALD = ITEMS.registerItem("apple_emerald", props -> new FoodItemWithEffects(props.food(
      new FoodProperties.Builder().nutrition(APPLENUT * 3).saturationModifier(APPLESATUR).alwaysEdible().build(),
      Consumable.builder()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.HASTE, SMALLPOTIONDUR, 2), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.LUCK, SMALLPOTIONDUR, 1), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.STRENGTH, SMALLPOTIONDUR, 1), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.SLOW_FALLING, SMALLPOTIONDUR, 1), 1))
          .build())));
  public static final DeferredItem<Item> APPLE_CHOCOLATE = ITEMS.registerItem("apple_chocolate", props -> new AppleChocolate(props.food(new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(APPLESATUR * 4)
      .alwaysEdible().build())));
  public static final DeferredItem<Item> APPLE_ENDER = ITEMS.registerItem("apple_ender", props -> new EnderApple(props.food(new FoodProperties.Builder().nutrition(APPLENUT).saturationModifier(0).alwaysEdible().build())));
  public static final DeferredItem<Item> APPLE_SPROUT = ITEMS.registerItem("apple_sprout", props -> new BlockItem(BlockRegistry.APPLE_SPROUT.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> APPLE_SPROUT_DIAMOND = ITEMS.registerItem("apple_sprout_diamond", props -> new BlockItem(BlockRegistry.APPLE_SPROUT_DIAMOND.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> APPLE_SPROUT_EMERALD = ITEMS.registerItem("apple_sprout_emerald", props -> new BlockItem(BlockRegistry.APPLE_SPROUT_EMERALD.get(), props.useBlockDescriptionPrefix()));

  // ---- more food stuff
  public static final DeferredItem<Item> CHORUS_FLIGHT = ITEMS.registerItem("chorus_flight", props -> new EdibleFlightItem(props));
  public static final DeferredItem<Item> CHORUS_SPECTRAL = ITEMS.registerItem("chorus_spectral", props -> new EdibleSpecItem(props));
  public static final DeferredItem<Item> MILK_BOTTLE = ITEMS.registerItem("milk_bottle", props -> new MilkBottle(props.food(new FoodProperties.Builder().alwaysEdible().build())));
  public static final DeferredItem<Item> CHOCOLATE_MILK = ITEMS.registerItem("chocolate_milk", props -> new ChocolateMilk(props.food(
      new FoodProperties.Builder().nutrition(1).saturationModifier(0).alwaysEdible().build(),
      Consumable.builder()
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.SPEED, SMALLPOTIONDUR, 0), 1))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.HUNGER, 120, 0), 0.5F))
          .onConsume(new ApplyStatusEffectsConsumeEffect(FoodItemWithEffects.silent(MobEffects.SATURATION, 60, 0), 1))
          .build())));
  public static final DeferredItem<Item> HEART = ITEMS.registerItem("heart", props -> new HeartItem(props.stacksTo(16)));
  public static final DeferredItem<Item> HEART_EMPTY = ITEMS.registerItem("heart_empty", props -> new HeartToxicItem(props.stacksTo(16)));
  //
  public static final DeferredItem<Item> CARROT_ENDER = ITEMS.registerItem("carrot_ender", props -> new ItemHorseEnder(props));
  public static final DeferredItem<Item> CARROT_DIAMOND = ITEMS.registerItem("carrot_diamond", props -> new ItemHorseHealthDiamondCarrot(props));
  public static final DeferredItem<Item> CARROT_REDSTONE = ITEMS.registerItem("carrot_redstone", props -> new ItemHorseRedstoneSpeed(props));
  public static final DeferredItem<Item> CARROT_EMERALD = ITEMS.registerItem("carrot_emerald", props -> new ItemHorseEmeraldJump(props));
  public static final DeferredItem<Item> CARROT_LAPIS = ITEMS.registerItem("carrot_lapis", props -> new ItemHorseLapisVariant(props));
  public static final DeferredItem<Item> CARROT_TOXIC = ITEMS.registerItem("carrot_toxic", props -> new ItemHorseToxic(props));
  public static final DeferredItem<Item> CARROT_QUARTZ = ITEMS.registerItem("carrot_quartz", props -> new ItemHorseQuartzStep(props));
  public static final DeferredItem<Item> CARROT_COPPER = ITEMS.registerItem("carrot_copper", props -> new ItemHorseCopperRadar(props));
  public static final DeferredItem<Item> CARROT_NETHERITE = ITEMS.registerItem("carrot_netherite", props -> new ItemHorseNetheriteFire(props));
  public static final DeferredItem<Item> CARROT_PRISMARINE = ITEMS.registerItem("carrot_prismarine", props -> new ItemHorsePrismarineWater(props));
  // ---- charms
  public static final DeferredItem<Item> CHARM_LONGFALL = ITEMS.registerItem("charm_longfall", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_CREEPER = ITEMS.registerItem("charm_creeper", props -> new ItemBaseToggle(props.durability(256)));
  public static final DeferredItem<Item> CHARM_STONE = ITEMS.registerItem("charm_stone", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_ANTIPOTION = ITEMS.registerItem("charm_antipotion", props -> new ItemBaseToggle(props.durability(256)));
  public static final DeferredItem<Item> CHARM_STEALTHPOTION = ITEMS.registerItem("charm_stealthpotion", props -> new ItemBaseToggle(props.stacksTo(1)));
  public static final DeferredItem<Item> CHARM_BOOSTPOTION = ITEMS.registerItem("charm_boostpotion", props -> new ItemBaseToggle(props.durability(256)));
  public static final DeferredItem<Item> CHARM_CRIT = ITEMS.registerItem("charm_crit", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_ANTIGRAVITY = ITEMS.registerItem("charm_antigravity", props -> new AirAntiGravity(props.durability(1024 * 4)));
  public static final DeferredItem<Item> CHARM_VOID = ITEMS.registerItem("charm_void", props -> new CharmVoid(props.durability(64)));
  public static final DeferredItem<Item> CHARM_ANTIDOTE = ITEMS.registerItem("charm_antidote", props -> new CharmAntidote(props.durability(64)));
  public static final DeferredItem<Item> CHARM_FIRE = ITEMS.registerItem("charm_fire", props -> new CharmFire(props.durability(64)));
  public static final DeferredItem<Item> CHARM_WITHER = ITEMS.registerItem("charm_wither", props -> new CharmWither(props.durability(64)));
  public static final DeferredItem<Item> CHARM_ULTIMATE = ITEMS.registerItem("charm_ultimate", props -> new CharmOverpowered(props.durability(256)));
  public static final DeferredItem<Item> CHARM_MAGICDEFENSE = ITEMS.registerItem("charm_magicdefense", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_STARVATION = ITEMS.registerItem("charm_starvation", props -> new ItemBaseToggle(props.durability(256 * 256)));
  public static final DeferredItem<Item> CHARM_VENOM = ITEMS.registerItem("charm_venom", props -> new ItemBaseToggle(props.durability(256)));
  public static final DeferredItem<Item> CHARM_WATER = ITEMS.registerItem("charm_water", props -> new ItemBaseToggle(props.durability(256)));
  public static final DeferredItem<Item> CHARM_SPEED = ITEMS.registerItem("charm_speed", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_KNOCKBACK_RESISTANCE = ITEMS.registerItem("charm_knockback_resistance", props -> new ItemBaseToggle(props.durability(256)));
  public static final DeferredItem<Item> CHARM_LUCK = ITEMS.registerItem("charm_luck", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_ATTACK_SPEED = ITEMS.registerItem("charm_attack_speed", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_XP_SPEED = ITEMS.registerItem("charm_xp_speed", props -> new ItemBaseToggle(props.stacksTo(1)));
  public static final DeferredItem<Item> CHARM_XP_BLOCKER = ITEMS.registerItem("charm_xp_blocker", props -> new ItemBaseToggle(props.stacksTo(1)));
  public static final DeferredItem<Item> CHARM_TORCH = ITEMS.registerItem("charm_torch", props -> new AutoTorchItem(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_TORCH_CAVE = ITEMS.registerItem("charm_torch_cave", props -> new AutoCaveTorchItem(props.durability(256 * 4)));
  public static final DeferredItem<Item> CHARM_HOME = ITEMS.registerItem("charm_home", props -> new EnderWingItem(props.durability(256)));
  public static final DeferredItem<Item> CHARM_WORLD = ITEMS.registerItem("charm_world", props -> new EnderWingSp(props.durability(256)));
  public static final DeferredItem<Item> CHARM_WING = ITEMS.registerItem("charm_wing", props -> new CharmWing(props.durability(64)));
  public static final DeferredItem<Item> CHARM_INVISIBLE = ITEMS.registerItem("charm_invisible", props -> new CharmInvisible(props.durability(256 * 4)));
  public static final DeferredItem<Item> QUIVER_DMG = ITEMS.registerItem("quiver_damage", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> QUIVER_LIT = ITEMS.registerItem("quiver_lightning", props -> new ItemBaseToggle(props.durability(256 * 4)));
  public static final DeferredItem<Item> SOULSTONE = ITEMS.registerItem("soulstone", props -> new SoulstoneCharm(props.durability(8)));
  public static final DeferredItem<Item> GLOVE_CLIMB = ITEMS.registerItem("glove_climb", props -> new GloveItem(props.durability(256 * 8)));
  public static final DeferredItem<Item> FLIPPERS = ITEMS.registerItem("flippers", props -> new ItemBaseToggle(props.durability(256 * 4)));
  // ---- throwable
  public static final DeferredItem<Item> SPARK = ITEMS.registerItem("spark", props -> new GlowingSpark(props));
  public static final DeferredItem<Item> FIREBALL_ORANGE = ITEMS.registerItem("fireball", props -> new FireballItem(props));
  public static final DeferredItem<Item> ENDER_FISHING = ITEMS.registerItem("ender_fishing", props -> new FishingMagicItem(props));
  public static final DeferredItem<Item> ENDER_TORCH = ITEMS.registerItem("ender_torch", props -> new TorchThrowingItem(props));
  public static final DeferredItem<Item> SPAWNER_SEEKER = ITEMS.registerItem("spawner_seeker", props -> new ItemProjectileDungeon(props));
  public static final DeferredItem<Item> MOB_CONTAINER = ITEMS.registerItem("mob_container", props -> new ItemMobContainer(props.stacksTo(1)));
  public static final DeferredItem<Item> MOB_CONTAINER_EMPTY = ITEMS.registerItem("mob_container_empty", props -> new ItemMagicNet(props));
  public static final DeferredItem<Item> ENDER_EYE_REUSE = ITEMS.registerItem("ender_eye_reuse", props -> new EnderEyeReuseItem(props));
  public static final DeferredItem<Item> ENDER_PEARL_REUSE = ITEMS.registerItem("ender_pearl_reuse", props -> new EnderPearlReuse(props));
  public static final DeferredItem<Item> ENDER_PEARL_MOUNTED = ITEMS.registerItem("ender_pearl_mounted", props -> new EnderPearlMount(props));
  // ----
  public static final DeferredItem<Item> SLINGSHOT = ITEMS.registerItem("slingshot", props -> new SlingshotItem(props.durability(64)));
  public static final DeferredItem<Item> BOOMERANG_STUN = ITEMS.registerItem("boomerang_stun", props -> new BoomerangItem(Boomer.STUN, props.durability(256)));
  public static final DeferredItem<Item> BOOMERANG_CARRY = ITEMS.registerItem("boomerang_carry", props -> new BoomerangItem(Boomer.CARRY, props.durability(256)));
  public static final DeferredItem<Item> BOOMERANG_DAMAGE = ITEMS.registerItem("boomerang_damage", props -> new BoomerangItem(Boomer.DAMAGE, props.durability(256)));

  // ---- scepter like
  public static final DeferredItem<Item> FIRE_KILLER = ITEMS.registerItem("fire_killer", props -> new FireExtinguishItem(props));
  public static final DeferredItem<Item> FLUTE_SUMMONING = ITEMS.registerItem("flute_summoning", props -> new FluteItem(props));
  public static final DeferredItem<Item> TORCH_LAUNCHER = ITEMS.registerItem("torch_launcher", props -> new ItemTorchThrower(props));
  public static final DeferredItem<Item> LASER_CANNON = ITEMS.registerItem("laser_cannon", props -> new LaserItem(props));
  public static final DeferredItem<Item> SCEPTER_HYPNO = ITEMS.registerItem("scepter_hypno", props -> new WandHypnoItem(props));
  public static final DeferredItem<Item> SCEPTER_MISSILE = ITEMS.registerItem("scepter_missile", props -> new WandMissileItem(props));
  public static final DeferredItem<Item> SCEPTER_BUILD = ITEMS.registerItem("scepter_build", props -> new BuilderItem(props, BuildStyle.NORMAL));
  public static final DeferredItem<Item> SCEPTER_REPLACE = ITEMS.registerItem("scepter_replace", props -> new BuilderItem(props, BuildStyle.REPLACE));
  public static final DeferredItem<Item> SCEPTER_OFFSET = ITEMS.registerItem("scepter_offset", props -> new BuilderItem(props, BuildStyle.OFFSET));
  public static final DeferredItem<Item> SCEPTER_RANDOMIZE = ITEMS.registerItem("scepter_randomize", props -> new RandomizerItem(props));
  public static final DeferredItem<Item> SCEPTER_SNOW = ITEMS.registerItem("scepter_snow", props -> new SnowScepter(props.durability(256)));
  public static final DeferredItem<Item> SCEPTER_FIRE = ITEMS.registerItem("scepter_fire", props -> new FireScepter(props.durability(256)));
  public static final DeferredItem<Item> SCEPTER_LIGHTNING = ITEMS.registerItem("scepter_lightning", props -> new LightningScepter(props.durability(256)));
  public static final DeferredItem<Item> SCEPTER_WATER = ITEMS.registerItem("scepter_water", props -> new WaterSpreaderItem(props.durability(256)));
  public static final DeferredItem<Item> SCEPTER_ICE = ITEMS.registerItem("scepter_ice", props -> new IceWand(props.durability(256)));
  public static final DeferredItem<Item> EVOKER_FANG = ITEMS.registerItem("evoker_fang", props -> new EvokerFangItem(props));
  public static final DeferredItem<Item> SPELUNKER = ITEMS.registerItem("spelunker", props -> new SpelunkerCaveFinder(props.durability(256)));
  public static final DeferredItem<Item> PROSPECTOR = ITEMS.registerItem("prospector", props -> new OreProspector(props.durability(256)));
  public static final DeferredItem<Item> SPAWNINSPECTOR = ITEMS.registerItem("spawn_inspector", props -> new SpawnInspectorTool(props.durability(256)));
  public static final DeferredItem<Item> SCEPTER_ELEVATION = ITEMS.registerItem("scepter_elevation", props -> new ElevationWandItem(props.durability(256)));
  public static final DeferredItem<Item> SCEPTER_TELEPORT = ITEMS.registerItem("scepter_teleport", props -> new TeleporterWandItem(props.durability(64)));
  public static final DeferredItem<Item> SCEPTER_ANTIMATTER = ITEMS.registerItem("scepter_antimatter", props -> new AntimatterEvaporatorWandItem(props.durability(1024)));
  public static final DeferredItem<Item> SCEPTER_ROTATION = ITEMS.registerItem("scepter_rotation", props -> new RotatorItem(props.durability(256)));
  public static final DeferredItem<Item> SCYTHE_BRUSH = ITEMS.registerItem("scythe_brush", props -> new ScytheBrush(props.durability(256)));
  public static final DeferredItem<Item> SCYTHE_FORAGE = ITEMS.registerItem("scythe_forage", props -> new ScytheForage(props.durability(256)));
  public static final DeferredItem<Item> SCYTHE_LEAVES = ITEMS.registerItem("scythe_leaves", props -> new ScytheLeaves(props.durability(256)));
  public static final DeferredItem<Item> SCYTHE_HARVEST = ITEMS.registerItem("scythe_harvest", props -> new ScytheHarvest(props.durability(1024)));
  public static final DeferredItem<Item> STIRRUPS = ITEMS.registerItem("stirrups", props -> new StirrupsItem(props.durability(256)));
  public static final DeferredItem<Item> STIRRUPS_REVERSE = ITEMS.registerItem("stirrups_reverse", props -> new StirrupsReverseItem(props.durability(256)));

  // ---- equippable
  public static final DeferredItem<Item> GLOWING_HELMET = ITEMS.registerItem("glowing_helmet", props -> new GlowingHelmetItem(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.GLOWING, ArmorType.HELMET)));
  public static final DeferredItem<Item> CRYSTAL_BOOTS = ITEMS.registerItem("crystal_boots", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.GEMOBSIDIAN, ArmorType.BOOTS)));
  public static final DeferredItem<Item> CRYSTAL_HELMET = ITEMS.registerItem("crystal_helmet", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.GEMOBSIDIAN, ArmorType.HELMET)));
  public static final DeferredItem<Item> CRYSTAL_CHESTPLATE = ITEMS.registerItem("crystal_chestplate", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.GEMOBSIDIAN, ArmorType.CHESTPLATE)));
  public static final DeferredItem<Item> CRYSTAL_LEGGINGS = ITEMS.registerItem("crystal_leggings", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.GEMOBSIDIAN, ArmorType.LEGGINGS)));
  public static final DeferredItem<Item> COPPER_BOOTS = ITEMS.registerItem("copper_boots", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.COPPER, ArmorType.BOOTS)));
  public static final DeferredItem<Item> COPPER_HELMET = ITEMS.registerItem("copper_helmet", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.COPPER, ArmorType.HELMET)));
  public static final DeferredItem<Item> COPPER_CHESTPLATE = ITEMS.registerItem("copper_chestplate", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.COPPER, ArmorType.CHESTPLATE)));
  public static final DeferredItem<Item> COPPER_LEGGINGS = ITEMS.registerItem("copper_leggings", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.COPPER, ArmorType.LEGGINGS)));
  public static final DeferredItem<Item> EMERALD_BOOTS = ITEMS.registerItem("emerald_boots", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.EMERALD, ArmorType.BOOTS)));
  public static final DeferredItem<Item> EMERALD_HELMET = ITEMS.registerItem("emerald_helmet", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.EMERALD, ArmorType.HELMET)));
  public static final DeferredItem<Item> EMERALD_CHESTPLATE = ITEMS.registerItem("emerald_chestplate", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.EMERALD, ArmorType.CHESTPLATE)));
  public static final DeferredItem<Item> EMERALD_LEGGINGS = ITEMS.registerItem("emerald_leggings", props -> new Item(props.stacksTo(1).humanoidArmor(MaterialRegistry.ArmorMats.EMERALD, ArmorType.LEGGINGS)));
  public static final DeferredItem<Item> AMETHYST_PICKAXE = ITEMS.registerItem("amethyst_pickaxe", props -> new AmethystPickaxeItem(MaterialRegistry.ToolMats.AMETHYST, 1F, -2.8F, props));
  public static final DeferredItem<Item> COPPER_PICKAXE = ITEMS.registerItem("copper_pickaxe", props -> new Item(props.pickaxe(MaterialRegistry.ToolMats.COPPER, 1F, -2.8F)));
  public static final DeferredItem<Item> EMERALD_PICKAXE = ITEMS.registerItem("emerald_pickaxe", props -> new Item(props.pickaxe(MaterialRegistry.ToolMats.EMERALD, 1F, -2.8F)));
  public static final DeferredItem<Item> CRYSTAL_PICKAXE = ITEMS.registerItem("crystal_pickaxe", props -> new Item(props.pickaxe(MaterialRegistry.ToolMats.GEMOBSIDIAN, 1F, -2.8F)));
  public static final DeferredItem<Item> SANDSTONE_PICKAXE = ITEMS.registerItem("sandstone_pickaxe", props -> new Item(props.pickaxe(MaterialRegistry.ToolMats.SANDSTONE, 1F, -2.8F)));
  public static final DeferredItem<Item> NETHERBRICK_PICKAXE = ITEMS.registerItem("netherbrick_pickaxe", props -> new Item(props.pickaxe(MaterialRegistry.ToolMats.NETHERBRICK, 1F, -2.8F)));
  public static final DeferredItem<Item> AMETHYST_AXE = ITEMS.registerItem("amethyst_axe", props -> new AmethystAxeItem(MaterialRegistry.ToolMats.AMETHYST, 6.0F, -3.1F, props));
  public static final DeferredItem<Item> COPPER_AXE = ITEMS.registerItem("copper_axe", props -> new AxeItem(MaterialRegistry.ToolMats.COPPER, 6.0F, -3.1F, props));
  public static final DeferredItem<Item> EMERALD_AXE = ITEMS.registerItem("emerald_axe", props -> new AxeItem(MaterialRegistry.ToolMats.EMERALD, 6.0F, -3.1F, props));
  public static final DeferredItem<Item> CRYSTAL_AXE = ITEMS.registerItem("crystal_axe", props -> new AxeItem(MaterialRegistry.ToolMats.GEMOBSIDIAN, 6.0F, -3.1F, props));
  public static final DeferredItem<Item> SANDSTONE_AXE = ITEMS.registerItem("sandstone_axe", props -> new AxeItem(MaterialRegistry.ToolMats.SANDSTONE, 6.0F, -3.1F, props));
  public static final DeferredItem<Item> NETHERBRICK_AXE = ITEMS.registerItem("netherbrick_axe", props -> new AxeItem(MaterialRegistry.ToolMats.NETHERBRICK, 6.0F, -3.1F, props));
  public static final DeferredItem<Item> AMETHYST_HOE = ITEMS.registerItem("amethyst_hoe", props -> new AmethystHoeItem(ToolMats.AMETHYST, -4F, 0F, props));
  public static final DeferredItem<Item> COPPER_HOE = ITEMS.registerItem("copper_hoe", props -> new HoeItem(ToolMats.COPPER, -4F, 0F, props));
  public static final DeferredItem<Item> EMERALD_HOE = ITEMS.registerItem("emerald_hoe", props -> new HoeItem(ToolMats.EMERALD, -4F, 0F, props));
  public static final DeferredItem<Item> CRYSTAL_HOE = ITEMS.registerItem("crystal_hoe", props -> new HoeItem(ToolMats.GEMOBSIDIAN, -4F, 0F, props));
  public static final DeferredItem<Item> SANDSTONE_HOE = ITEMS.registerItem("sandstone_hoe", props -> new HoeItem(ToolMats.SANDSTONE, -4F, 0F, props));
  public static final DeferredItem<Item> NETHERBRICK_HOE = ITEMS.registerItem("netherbrick_hoe", props -> new HoeItem(ToolMats.NETHERBRICK, -4F, 0F, props));
  public static final DeferredItem<Item> AMETHYST_SHOVEL = ITEMS.registerItem("amethyst_shovel", props -> new AmethystShovelItem(MaterialRegistry.ToolMats.AMETHYST, 1.5F, -3.0F, props));
  public static final DeferredItem<Item> COPPER_SHOVEL = ITEMS.registerItem("copper_shovel", props -> new ShovelItem(MaterialRegistry.ToolMats.COPPER, 1.5F, -3.0F, props));
  public static final DeferredItem<Item> EMERALD_SHOVEL = ITEMS.registerItem("emerald_shovel", props -> new ShovelItem(MaterialRegistry.ToolMats.EMERALD, 1.5F, -3.0F, props));
  public static final DeferredItem<Item> CRYSTAL_SHOVEL = ITEMS.registerItem("crystal_shovel", props -> new ShovelItem(MaterialRegistry.ToolMats.GEMOBSIDIAN, 1.5F, -3.0F, props));
  public static final DeferredItem<Item> SANDSTONE_SHOVEL = ITEMS.registerItem("sandstone_shovel", props -> new ShovelItem(MaterialRegistry.ToolMats.SANDSTONE, 1.5F, -3.0F, props));
  public static final DeferredItem<Item> NETHERBRICK_SHOVEL = ITEMS.registerItem("netherbrick_shovel", props -> new ShovelItem(MaterialRegistry.ToolMats.NETHERBRICK, 1.5F, -3.0F, props));
  public static final DeferredItem<Item> AMETHYST_SWORD = ITEMS.registerItem("amethyst_sword", props -> new Item(props.sword(MaterialRegistry.ToolMats.AMETHYST, 3F, -2.4F)));
  public static final DeferredItem<Item> COPPER_SWORD = ITEMS.registerItem("copper_sword", props -> new Item(props.sword(MaterialRegistry.ToolMats.COPPER, 3F, -2.4F)));
  public static final DeferredItem<Item> EMERALD_SWORD = ITEMS.registerItem("emerald_sword", props -> new Item(props.sword(MaterialRegistry.ToolMats.EMERALD, 3F, -2.4F)));
  public static final DeferredItem<Item> CRYSTAL_SWORD = ITEMS.registerItem("crystal_sword", props -> new Item(props.sword(MaterialRegistry.ToolMats.GEMOBSIDIAN, 3F, -2.4F)));
  public static final DeferredItem<Item> SANDSTONE_SWORD = ITEMS.registerItem("sandstone_sword", props -> new Item(props.sword(MaterialRegistry.ToolMats.SANDSTONE, 3F, -2.4F)));
  public static final DeferredItem<Item> NETHERBRICK_SWORD = ITEMS.registerItem("netherbrick_sword", props -> new Item(props.sword(MaterialRegistry.ToolMats.NETHERBRICK, 3F, -2.4F)));
  public static final DeferredItem<Item> SHIELD_WOOD = ITEMS.registerItem("shield_wood", props -> new ShieldCyclicItem(props.durability(84), ShieldType.WOOD));
  public static final DeferredItem<Item> SHIELD_LEATHER = ITEMS.registerItem("shield_leather", props -> new ShieldCyclicItem(props.durability(168), ShieldType.LEATHER));
  public static final DeferredItem<Item> SHIELD_FLINT = ITEMS.registerItem("shield_flint", props -> new ShieldCyclicItem(props.durability(168 + 84), ShieldType.FLINT));
  public static final DeferredItem<Item> SHIELD_OBSIDIAN = ITEMS.registerItem("shield_obsidian", props -> new ShieldCyclicItem(props.durability(168 * 8), ShieldType.OBSIDIAN));
  public static final DeferredItem<Item> SHIELD_BONE = ITEMS.registerItem("shield_bone", props -> new ShieldCyclicItem(props.durability(168 + 84), ShieldType.BONE));
  public static final DeferredItem<Item> MATTOCK = ITEMS.registerItem("mattock", props -> new MattockItem(ToolMaterial.DIAMOND, 9000, props, 1));
  public static final DeferredItem<Item> MATTOCK_NETHER = ITEMS.registerItem("mattock_nether", props -> new MattockItem(ToolMaterial.NETHERITE, 9001, props, 2));
  public static final DeferredItem<Item> MATTOCK_STONE = ITEMS.registerItem("mattock_stone", props -> new MattockItem(ToolMaterial.STONE, 1024, props, 1));
  public static final DeferredItem<Item> SHEARS_FLINT = ITEMS.registerItem("shears_flint", props -> new ShearsMaterial(props.durability(64)));
  public static final DeferredItem<Item> SHEARS_OBSIDIAN = ITEMS.registerItem("shears_obsidian", props -> new ShearsMaterial(props.durability(1024 * 1024)));

  //
  public static final DeferredItem<Item> CARBON_PAPER = ITEMS.registerItem("carbon_paper", props -> new CarbonPaperItem(props));
  public static final DeferredItem<Item> SLEEPING_MAT = ITEMS.registerItem("sleeping_mat", props -> new SleepingMatItem(props));

  // ---- inventory like
  public static final DeferredItem<Item> LUNCHBOX = ITEMS.registerItem("lunchbox", props -> new ItemLunchbox(props));
  public static final DeferredItem<Item> ENDER_BOOK = ITEMS.registerItem("ender_book", props -> new EnderBookItem(props.durability(8)));
  public static final DeferredItem<Item> ENDER_BAG = ITEMS.registerItem("ender_bag", props -> new EnderBagItem(props));
  public static final DeferredItem<Item> STORAGE_BAG = ITEMS.registerItem("storage_bag", props -> new ItemStorageBag(props.stacksTo(1)));
  public static final DeferredItem<Item> CRAFTING_BAG = ITEMS.registerItem("crafting_bag", props -> new CraftingBagItem(props.stacksTo(1)));
  public static final DeferredItem<Item> CRAFTING_STICK = ITEMS.registerItem("crafting_stick", props -> new CraftingStickItem(props.stacksTo(1)));
  public static final DeferredItem<Item> TILE_TRANSPORTER_EMPTY = ITEMS.registerItem("tile_transporter_empty", props -> new TileTransporterEmptyItem(props));
  public static final DeferredItem<Item> TILE_TRANSPORTER = ITEMS.registerItem("tile_transporter", props -> new TileTransporterItem(props));


  // ---- core ----
  public static final DeferredItem<Item> GPS_COMPASS = ITEMS.registerItem("compass_gps", props -> new GpsCompassItem(props.stacksTo(1)));
  public static final DeferredItem<Item> LOCATION_DATA = ITEMS.registerItem("location_data", props -> new LocationGpsCard(props));
  public static final DeferredItem<Item> SETTINGS_DATA = ITEMS.registerItem("settings_data", props -> new SettingsCard(props));
  public static final DeferredItem<Item> SHAPE_DATA = ITEMS.registerItem("shape_data", props -> new ShapeCard(props));
  public static final DeferredItem<Item> FILTER_DATA = ITEMS.registerItem("filter_data", props -> new FilterCardItem(props));
  public static final DeferredItem<Item> FILTER_FLUID = ITEMS.registerItem("fluid_data", props -> new FluidFilterCardItem(props));
  public static final DeferredItem<Item> SOUND_DATA = ITEMS.registerItem("sound_data", props -> new SoundCard(props));
  public static final DeferredItem<Item> ENTITY_DATA = ITEMS.registerItem("entity_data", props -> new EntityDataCard(props));
  public static final DeferredItem<Item> BLOCKSTATE_DATA = ITEMS.registerItem("blockstate_data", props -> new BlockstateCard(props));

  public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.registerItem("copper_nugget", props -> new GemstoneItem(props));
  public static final DeferredItem<Item> NETHERITE_NUGGET = ITEMS.registerItem("netherite_nugget", props -> new GemstoneItem(props));

  // ---- Deco ----
  public static final DeferredItem<Item> GOLD_BARS = ITEMS.registerItem("gold_bars", props -> new BlockItem(BlockRegistry.GOLD_BARS.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GOLD_CHAIN = ITEMS.registerItem("gold_chain", props -> new BlockItem(BlockRegistry.GOLD_CHAIN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GOLD_LANTERN = ITEMS.registerItem("gold_lantern", props -> new BlockItem(BlockRegistry.GOLD_LANTERN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GOLD_SOUL_LANTERN = ITEMS.registerItem("gold_soul_lantern", props -> new BlockItem(BlockRegistry.GOLD_SOUL_LANTERN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COPPER_BARS = ITEMS.registerItem("copper_bars", props -> new BlockItem(BlockRegistry.COPPER_BARS.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COPPER_CHAIN = ITEMS.registerItem("copper_chain", props -> new BlockItem(BlockRegistry.COPPER_CHAIN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COPPER_LANTERN = ITEMS.registerItem("copper_lantern", props -> new BlockItem(BlockRegistry.COPPER_LANTERN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COPPER_SOUL_LANTERN = ITEMS.registerItem("copper_soul_lantern", props -> new BlockItem(BlockRegistry.COPPER_SOUL_LANTERN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COPPER_PRESSURE_PLATE = ITEMS.registerItem("copper_pressure_plate", props -> new BlockItem(BlockRegistry.COPPER_PRESSURE_PLATE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> NETHERITE_BARS = ITEMS.registerItem("netherite_bars", props -> new BlockItem(BlockRegistry.NETHERITE_BARS.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> NETHERITE_CHAIN = ITEMS.registerItem("netherite_chain", props -> new BlockItem(BlockRegistry.NETHERITE_CHAIN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> NETHERITE_PRESSURE_PLATE = ITEMS.registerItem("netherite_pressure_plate", props -> new BlockItem(BlockRegistry.NETHERITE_PRESSURE_PLATE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> NETHERITE_LANTERN = ITEMS.registerItem("netherite_lantern", props -> new BlockItem(BlockRegistry.NETHERITE_LANTERN.get(), props.useBlockDescriptionPrefix()));


  // ---- Redstone stuff ----

  public static final DeferredItem<Item> OBSIDIAN_PRESSURE_PLATE = ITEMS.registerItem("obsidian_pressure_plate", props -> new BlockItem(BlockRegistry.OBSIDIAN_PRESSURE_PLATE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BUTTON_BASALT = ITEMS.registerItem("button_basalt", props -> new BlockItem(BlockRegistry.BUTTON_BASALT.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BUTTON_BLACKSTONE = ITEMS.registerItem("button_blackstone", props -> new BlockItem(BlockRegistry.BUTTON_BLACKSTONE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BUTTON_DEEPSLATE = ITEMS.registerItem("button_deepslate", props -> new BlockItem(BlockRegistry.BUTTON_DEEPSLATE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BUTTON_TUFF = ITEMS.registerItem("button_tuff", props -> new BlockItem(BlockRegistry.BUTTON_TUFF.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> LEVER_REMOTE = ITEMS.registerItem("lever_remote", props -> new LeverRemote(props.stacksTo(1)));
  public static final DeferredItem<Item> CLOCK = ITEMS.registerItem("clock", props -> new BlockItem(BlockRegistry.CLOCK.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> WAXED_REDSTONE = ITEMS.registerItem("waxed_redstone", props -> new BlockItem(BlockRegistry.WAXED_REDSTONE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> LAMP = ITEMS.registerItem("lamp", props -> new BlockItem(BlockRegistry.LAMP.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FIREPLACE = ITEMS.registerItem("fireplace", props -> new BlockItem(BlockRegistry.FIREPLACE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DETECTOR_ITEM = ITEMS.registerItem("detector_item", props -> new BlockItem(BlockRegistry.DETECTOR_ITEM.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DETECTOR_ENTITY = ITEMS.registerItem("detector_entity", props -> new BlockItem(BlockRegistry.DETECTOR_ENTITY.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DETECTOR_MOON = ITEMS.registerItem("detector_moon", props -> new BlockItem(BlockRegistry.DETECTOR_MOON.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DETECTOR_WEATHER = ITEMS.registerItem("detector_weather", props -> new BlockItem(BlockRegistry.DETECTOR_WEATHER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> WIRELESS_TRANSMITTER = ITEMS.registerItem("wireless_transmitter", props -> new BlockItem(BlockRegistry.WIRELESS_TRANSMITTER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> WIRELESS_RECEIVER = ITEMS.registerItem("wireless_receiver", props -> new BlockItem(BlockRegistry.WIRELESS_RECEIVER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> MAGNET_BLOCK = ITEMS.registerItem("magnet_block", props -> new BlockItem(BlockRegistry.MAGNET_BLOCK.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DICE = ITEMS.registerItem("dice", props -> new BlockItem(BlockRegistry.DICE.get(), props.useBlockDescriptionPrefix()));

  // ----

  public static final DeferredItem<Item> EYE_REDSTONE = ITEMS.registerItem("ender_eye_block", props -> new BlockItem(BlockRegistry.EYE_REDSTONE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> EYE_TELEPORT = ITEMS.registerItem("ender_pearl_block", props -> new BlockItem(BlockRegistry.EYE_TELEPORT.get(), props.useBlockDescriptionPrefix()));


  // ---- simple? blocks
  public static final DeferredItem<Item> WORKBENCH = ITEMS.registerItem("workbench", props -> new BlockItem(BlockRegistry.WORKBENCH.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COMPRESSED_COBBLESTONE = ITEMS.registerItem("compressed_cobblestone", props -> new BlockItem(BlockRegistry.COMPRESSED_COBBLESTONE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FLINT_BLOCK = ITEMS.registerItem("flint_block", props -> new BlockItem(BlockRegistry.FLINT_BLOCK.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> SPONGE_LAVA = ITEMS.registerItem("sponge_lava", props -> new BlockItem(BlockRegistry.SPONGE_LAVA.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SPONGE_MILK = ITEMS.registerItem("sponge_milk", props -> new BlockItem(BlockRegistry.SPONGE_MILK.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> UNBREAKABLE_BLOCK = ITEMS.registerItem("unbreakable_block", props -> new BlockItem(BlockRegistry.UNBREAKABLE_BLOCK.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> UNBREAKABLE_REACTIVE = ITEMS.registerItem("unbreakable_reactive", props -> new BlockItem(BlockRegistry.UNBREAKABLE_REACTIVE.get(), props.useBlockDescriptionPrefix()));

  // ----
  public static final DeferredItem<Item> MEMBRANE = ITEMS.registerItem("membrane", props -> new BlockItem(BlockRegistry.MEMBRANE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CLOUD = ITEMS.registerItem("cloud", props -> new BlockItem(BlockRegistry.CLOUD.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CLOUD_MEMBRANE = ITEMS.registerItem("cloud_membrane", props -> new BlockItem(BlockRegistry.CLOUD_MEMBRANE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CLOUD_BARRIER = ITEMS.registerItem("cloud_barrier", props -> new BlockItem(BlockRegistry.CLOUD_BARRIER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CLOUD_GHOST = ITEMS.registerItem("cloud_ghost", props -> new BlockItem(BlockRegistry.CLOUD_GHOST.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> DOORBELL = ITEMS.registerItem("doorbell", props -> new BlockItem(BlockRegistry.DOORBELL.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SOUND_RECORDER = ITEMS.registerItem("sound_recorder", props -> new BlockItem(BlockRegistry.SOUND_RECORDER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SOUND_PLAYER = ITEMS.registerItem("sound_player", props -> new BlockItem(BlockRegistry.SOUND_PLAYER.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> SPIKES_DIAMOND = ITEMS.registerItem("spikes_diamond", props -> new BlockItem(BlockRegistry.SPIKES_DIAMOND.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SPIKES_IRON = ITEMS.registerItem("spikes_iron", props -> new BlockItem(BlockRegistry.SPIKES_IRON.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SPIKES_CURSE = ITEMS.registerItem("spikes_curse", props -> new BlockItem(BlockRegistry.SPIKES_CURSE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SPIKES_FIRE = ITEMS.registerItem("spikes_fire", props -> new BlockItem(BlockRegistry.SPIKES_FIRE.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> SHEARING = ITEMS.registerItem("shearing", props -> new BlockItem(BlockRegistry.SHEARING.get(), props.useBlockDescriptionPrefix()));


  // ---- ENERGY
  public static final DeferredItem<Item> GENERATOR_FUEL = ITEMS.registerItem("generator_fuel", props -> new BlockItem(BlockRegistry.GENERATOR_FUEL.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GENERATOR_FOOD = ITEMS.registerItem("generator_food", props -> new BlockItem(BlockRegistry.GENERATOR_FOOD.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GENERATOR_FLUID = ITEMS.registerItem("generator_fluid", props -> new BlockItem(BlockRegistry.GENERATOR_FLUID.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GENERATOR_ITEM = ITEMS.registerItem("generator_item", props -> new BlockItem(BlockRegistry.GENERATOR_ITEM.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GENERATOR_SOLAR = ITEMS.registerItem("generator_solar", props -> new BlockItem(BlockRegistry.GENERATOR_SOLAR.get(), props.useBlockDescriptionPrefix()));


  // -- peat and flowers
  public static final DeferredItem<Item> FLOWER_CYAN = ITEMS.registerItem("flower_cyan", props -> new BlockItem(BlockRegistry.FLOWER_CYAN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FLOWER_PURPLE = ITEMS.registerItem("flower_purple_tulip", props -> new BlockItem(BlockRegistry.FLOWER_PURPLE_TULIP.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FLOWER_BROWN = ITEMS.registerItem("flower_absalon_tulip", props -> new BlockItem(BlockRegistry.FLOWER_ABSALON_TULIP.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FLOWER_LIME = ITEMS.registerItem("flower_lime_carnation", props -> new BlockItem(BlockRegistry.FLOWER_LIME_CARNATION.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SOIL = ITEMS.registerItem("soil", props -> new BlockItem(BlockRegistry.SOIL.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> TERRA_PRETA = ITEMS.registerItem("terra_preta", props -> new BlockItem(BlockRegistry.TERRA_PRETA.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BIOMASS = ITEMS.registerItem("biomass", props -> new ItemBaseCyclic(props));
  public static final DeferredItem<Item> PEAT_FUEL = ITEMS.registerItem("peat_fuel", props -> new ItemBaseCyclic(props));
  public static final DeferredItem<Item> PEAT_FUEL_ENRICHED = ITEMS.registerItem("peat_fuel_enriched", props -> new ItemBaseCyclic(props));
  public static final DeferredItem<Item> PEAT_UNBAKED = ITEMS.registerItem("peat_unbaked", props -> new BlockItem(BlockRegistry.PEAT_UNBAKED.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> PEAT_BAKED = ITEMS.registerItem("peat_baked", props -> new BlockItem(BlockRegistry.PEAT_BAKED.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> PEAT_FARM = ITEMS.registerItem("peat_farm", props -> new BlockItem(BlockRegistry.PEAT_FARM.get(), props.useBlockDescriptionPrefix()));
  // ---- energy machine blocks
  public static final DeferredItem<Item> BATTERY_CLAY = ITEMS.registerItem("battery_clay", props -> new ItemBlockClayBattery(BlockRegistry.BATTERY_CLAY.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BATTERY = ITEMS.registerItem("battery", props -> new ItemBlockBattery(BlockRegistry.BATTERY.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CRAFTER = ITEMS.registerItem("crafter", props -> new BlockItem(BlockRegistry.CRAFTER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> PACKAGER = ITEMS.registerItem("packager", props -> new BlockItem(BlockRegistry.PACKAGER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CRUSHER = ITEMS.registerItem("crusher", props -> new BlockItem(BlockRegistry.CRUSHER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> MELTER = ITEMS.registerItem("melter", props -> new BlockItem(BlockRegistry.MELTER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SOLIDIFIER = ITEMS.registerItem("solidifier", props -> new BlockItem(BlockRegistry.SOLIDIFIER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ROTATOR = ITEMS.registerItem("rotator", props -> new BlockItem(BlockRegistry.ROTATOR.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> PLACER = ITEMS.registerItem("placer", props -> new BlockItem(BlockRegistry.PLACER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BREAKER = ITEMS.registerItem("breaker", props -> new BlockItem(BlockRegistry.BREAKER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> USER = ITEMS.registerItem("user", props -> new BlockItem(BlockRegistry.USER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DROPPER = ITEMS.registerItem("dropper", props -> new BlockItem(BlockRegistry.DROPPER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FORESTER = ITEMS.registerItem("forester", props -> new BlockItem(BlockRegistry.FORESTER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> MINER = ITEMS.registerItem("miner", props -> new BlockItem(BlockRegistry.MINER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> STRUCTURE = ITEMS.registerItem("structure", props -> new BlockItem(BlockRegistry.STRUCTURE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COMPUTER_SHAPE = ITEMS.registerItem("computer_shape", props -> new BlockItem(BlockRegistry.COMPUTER_SHAPE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> HARVESTER = ITEMS.registerItem("harvester", props -> new BlockItem(BlockRegistry.HARVESTER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COLLECTOR = ITEMS.registerItem("collector", props -> new BlockItem(BlockRegistry.COLLECTOR.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> COLLECTOR_FLUID = ITEMS.registerItem("collector_fluid", props -> new BlockItem(BlockRegistry.COLLECTOR_FLUID.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> PLACER_FLUID = ITEMS.registerItem("placer_fluid", props -> new BlockItem(BlockRegistry.PLACER_FLUID.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CASK = ITEMS.registerItem("cask", props -> new ItemBlockCask(BlockRegistry.CASK.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> TANK = ITEMS.registerItem("tank", props -> new ItemBlockTank(BlockRegistry.TANK.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> HOPPER_FLUID = ITEMS.registerItem("hopper_fluid", props -> new BlockItem(BlockRegistry.HOPPER_FLUID.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> HOPPER = ITEMS.registerItem("hopper", props -> new BlockItem(BlockRegistry.HOPPER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> HOPPER_GOLD = ITEMS.registerItem("hopper_gold", props -> new BlockItem(BlockRegistry.HOPPER_GOLD.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> ENERGY_PIPE = ITEMS.registerItem("energy_pipe", props -> new BlockItem(BlockRegistry.ENERGY_PIPE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ITEM_PIPE = ITEMS.registerItem("item_pipe", props -> new BlockItem(BlockRegistry.ITEM_PIPE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FLUID_PIPE = ITEMS.registerItem("fluid_pipe", props -> new BlockItem(BlockRegistry.FLUID_PIPE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BUNDLED_PIPE = ITEMS.registerItem("bundled_pipe", props -> new BlockItem(BlockRegistry.BUNDLED_PIPE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> WRENCH = ITEMS.registerItem("wrench", props -> new CableWrench(props));

  public static final DeferredItem<Item> WIRELESS_ENERGY = ITEMS.registerItem("wireless_energy", props -> new BlockItem(BlockRegistry.WIRELESS_ENERGY.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> WIRELESS_ITEM = ITEMS.registerItem("wireless_item", props -> new BlockItem(BlockRegistry.WIRELESS_ITEM.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> WIRELESS_FLUID = ITEMS.registerItem("wireless_fluid", props -> new BlockItem(BlockRegistry.WIRELESS_FLUID.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> TRASH = ITEMS.registerItem("trash", props -> new BlockItem(BlockRegistry.TRASH.get(), props.useBlockDescriptionPrefix()));


  // more blocks
  public static final DeferredItem<Item> SHELF = ITEMS.registerItem("shelf", props -> new BlockItem(BlockRegistry.SHELF.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CRATE = ITEMS.registerItem("crate", props -> new BlockItem(BlockRegistry.CRATE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> MINI_CRATE = ITEMS.registerItem("crate_mini", props -> new BlockItem(BlockRegistry.CRATE_MINI.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> PLATE_LAUNCH_REDSTONE = ITEMS.registerItem("plate_launch_redstone", props -> new BlockItem(BlockRegistry.PLATE_LAUNCH_REDSTONE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> PLATE_LAUNCH = ITEMS.registerItem("plate_launch", props -> new BlockItem(BlockRegistry.PLATE_LAUNCH.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SCREEN = ITEMS.registerItem("screen", props -> new BlockItem(BlockRegistry.SCREEN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> UNCRAFTER = ITEMS.registerItem("uncrafter", props -> new BlockItem(BlockRegistry.UNCRAFTER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FISHER = ITEMS.registerItem("fisher", props -> new BlockItem(BlockRegistry.FISHER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> DISENCHANTER = ITEMS.registerItem("disenchanter", props -> new BlockItem(BlockRegistry.DISENCHANTER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FAN = ITEMS.registerItem("fan", props -> new BlockItem(BlockRegistry.FAN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> FAN_SLAB = ITEMS.registerItem("fan_slab", props -> new BlockItem(BlockRegistry.FAN_SLAB.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> LIGHT_CAMO = ITEMS.registerItem("light_camo", props -> new BlockItem(BlockRegistry.LIGHT_CAMO.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SOUNDPROOFING_GHOST = ITEMS.registerItem("soundproofing_ghost", props -> new BlockItem(BlockRegistry.SOUNDPROOFING_GHOST.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SOUNDPROOFING = ITEMS.registerItem("soundproofing", props -> new BlockItem(BlockRegistry.SOUNDPROOFING.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ANVIL = ITEMS.registerItem("anvil", props -> new BlockItem(BlockRegistry.ANVIL.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ANVIL_MAGMA = ITEMS.registerItem("anvil_magma", props -> new BlockItem(BlockRegistry.ANVIL_MAGMA.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ANVILVOID = ITEMS.registerItem("anvil_void", props -> new BlockItem(BlockRegistry.ANVILVOID.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> LASER = ITEMS.registerItem("laser", props -> new BlockItem(BlockRegistry.LASER.get(), props.useBlockDescriptionPrefix()));


  // altars and beacons
  public static final DeferredItem<Item> CANDLE_WATER = ITEMS.registerItem("candle_water", props -> new BlockItem(BlockRegistry.CANDLE_WATER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> CANDLE_PEACE = ITEMS.registerItem("candle_peace", props -> new BlockItem(BlockRegistry.CANDLE_PEACE.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> ALTAR_DESTRUCTION = ITEMS.registerItem("altar_destruction", props -> new BlockItem(BlockRegistry.ALTAR_DESTRUCTION.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ALTAR_SOLICITING = ITEMS.registerItem("altar_soliciting", props -> new BlockItem(BlockRegistry.ALTAR_SOLICITING.get(), props.useBlockDescriptionPrefix()));

  public static final DeferredItem<Item> BEACON = ITEMS.registerItem("beacon", props -> new BlockItem(BlockRegistry.BEACON.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BEACON_SPONGE = ITEMS.registerItem("beacon_sponge", props -> new BlockItem(BlockRegistry.BEACON_SPONGE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> BEACON_REDSTONE = ITEMS.registerItem("beacon_redstone", props -> new BlockItem(BlockRegistry.BEACON_REDSTONE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GLASS_CONNECTED = ITEMS.registerItem("glass_connected", props -> new BlockItem(BlockRegistry.GLASS_CONNECTED.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GLASS_DARK = ITEMS.registerItem("glass_dark", props -> new BlockItem(BlockRegistry.GLASS_DARK.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GLASS_DARK_CONNECTED = ITEMS.registerItem("glass_dark_connected", props -> new BlockItem(BlockRegistry.GLASS_DARK_CONNECTED.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> GLASS_TERRA = ITEMS.registerItem("glass_terra", props -> new BlockItem(BlockRegistry.GLASS_TERRA.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SPRINKLER = ITEMS.registerItem("sprinkler", props -> new BlockItem(BlockRegistry.SPRINKLER.get(), props.useBlockDescriptionPrefix()));


  public static final DeferredItem<Item> CONVEYOR = ITEMS.registerItem("conveyor", props -> new BlockItem(BlockRegistry.CONVEYOR.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> TELEPORT = ITEMS.registerItem("teleport", props -> new BlockItem(BlockRegistry.TELEPORT.get(), props.useBlockDescriptionPrefix()));


  public static final DeferredItem<Item> ENDER_SHELF = ITEMS.registerItem("ender_shelf", props -> new BlockItem(BlockRegistry.ENDER_SHELF.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ENDER_CONTROLLER = ITEMS.registerItem("ender_controller", props -> new BlockItem(BlockRegistry.ENDER_CONTROLLER.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> EXPERIENCE_PYLON = ITEMS.registerItem("experience_pylon", props -> new ItemBlockPylon(BlockRegistry.EXPERIENCE_PYLON.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> EXPERIENCE_FOUNTAIN = ITEMS.registerItem("experience_fountain", props -> new BlockItem(BlockRegistry.EXPERIENCE_FOUNTAIN.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> EXPERIENCE_FOOD = ITEMS.registerItem("experience_food", props -> new ExpItemGain(props));
  public static final DeferredItem<Item> SCAFFOLD_REPLACE = ITEMS.registerItem("scaffold_replace", props -> new ItemScaffolding(BlockRegistry.SCAFFOLD_REPLACE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SCAFFOLD_FRAGILE = ITEMS.registerItem("scaffold_fragile", props -> new ItemScaffolding(BlockRegistry.SCAFFOLD_FRAGILE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> SCAFFOLD_RESPONSIVE = ITEMS.registerItem("scaffold_responsive", props -> new ItemScaffolding(BlockRegistry.SCAFFOLD_RESPONSIVE.get(), props.useBlockDescriptionPrefix()));

  // creative test items
  public static final DeferredItem<Item> BATTERY_INFINITE = ITEMS.registerItem("battery_infinite", props -> new BlockItem(BlockRegistry.BATTERY_INFINITE.get(), props.useBlockDescriptionPrefix()));
  public static final DeferredItem<Item> ITEM_INFINITE = ITEMS.registerItem("item_infinite", props -> new BlockItem(BlockRegistry.ITEM_INFINITE.get(), props.useBlockDescriptionPrefix()));
}
