package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.BlockWaxedRedstone;
import com.lothrazar.cyclic.block.ButtonBlockMat;
import com.lothrazar.cyclic.block.CandleWaterBlock;
import com.lothrazar.cyclic.block.DoorbellButton;
import com.lothrazar.cyclic.block.FireplaceBlock;
import com.lothrazar.cyclic.block.LaunchBlock;
import com.lothrazar.cyclic.block.LavaSpongeBlock;
import com.lothrazar.cyclic.block.MetalBarsBlock;
import com.lothrazar.cyclic.block.PeatBlock;
import com.lothrazar.cyclic.block.PeatFuelBlock;
import com.lothrazar.cyclic.block.PressurePlateMetal;
import com.lothrazar.cyclic.block.antipotion.BlockAntiBeacon;
import com.lothrazar.cyclic.block.antipotion.MilkSpongeBlock;
import com.lothrazar.cyclic.block.anvil.BlockAnvilAuto;
import com.lothrazar.cyclic.block.anvilmagma.BlockAnvilMagma;
import com.lothrazar.cyclic.block.anvilvoid.BlockAnvilVoid;
import com.lothrazar.cyclic.block.apple.AppleCropBlock;
import com.lothrazar.cyclic.block.battery.BlockBattery;
import com.lothrazar.cyclic.block.batteryclay.ClayBattery;
import com.lothrazar.cyclic.block.batterycreative.BlockBatteryInfinite;
import com.lothrazar.cyclic.block.beaconpotion.BlockPotion;
import com.lothrazar.cyclic.block.beaconredstone.BlockBeaconRedstone;
import com.lothrazar.cyclic.block.bedrock.UnbreakableBlock;
import com.lothrazar.cyclic.block.bedrock.UnbreakablePoweredBlock;
import com.lothrazar.cyclic.block.breaker.BlockBreaker;
import com.lothrazar.cyclic.block.cable.bundled.BlockCableBundled;
import com.lothrazar.cyclic.block.cable.energy.BlockCableEnergy;
import com.lothrazar.cyclic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclic.block.cable.item.BlockCableItem;
import com.lothrazar.cyclic.block.clock.BlockRedstoneClock;
import com.lothrazar.cyclic.block.collectfluid.BlockFluidCollect;
import com.lothrazar.cyclic.block.collectitem.BlockItemCollector;
import com.lothrazar.cyclic.block.conveyor.BlockConveyor;
import com.lothrazar.cyclic.block.crafter.BlockCrafter;
import com.lothrazar.cyclic.block.crate.BlockCrate;
import com.lothrazar.cyclic.block.cratemini.BlockCrateMini;
import com.lothrazar.cyclic.block.creativeitem.BlockItemInfinite;
import com.lothrazar.cyclic.block.crusher.BlockCrusher;
import com.lothrazar.cyclic.block.detectmoon.BlockMoon;
import com.lothrazar.cyclic.block.detectorentity.BlockDetector;
import com.lothrazar.cyclic.block.detectoritem.BlockDetectorItem;
import com.lothrazar.cyclic.block.detectweather.BlockWeather;
import com.lothrazar.cyclic.block.dice.BlockDice;
import com.lothrazar.cyclic.block.disenchant.BlockDisenchant;
import com.lothrazar.cyclic.block.dropper.BlockDropper;
import com.lothrazar.cyclic.block.enderctrl.BlockEnderCtrl;
import com.lothrazar.cyclic.block.enderitemshelf.BlockItemShelf;
import com.lothrazar.cyclic.block.endershelf.BlockEnderShelf;
import com.lothrazar.cyclic.block.expcollect.BlockExpPylon;
import com.lothrazar.cyclic.block.expfountain.BlockExperienceFountain;
import com.lothrazar.cyclic.block.eye.BlockEye;
import com.lothrazar.cyclic.block.eyetp.BlockEyeTp;
import com.lothrazar.cyclic.block.facade.light.BlockLightFacade;
import com.lothrazar.cyclic.block.facade.soundmuff.SoundmufflerBlockFacade;
import com.lothrazar.cyclic.block.fan.BlockFan;
import com.lothrazar.cyclic.block.fanslab.BlockFanSlab;
import com.lothrazar.cyclic.block.fishing.BlockFisher;
import com.lothrazar.cyclic.block.forester.BlockForester;
import com.lothrazar.cyclic.block.generatorexpl.BlockDestruction;
import com.lothrazar.cyclic.block.generatorfluid.BlockGeneratorFluid;
import com.lothrazar.cyclic.block.generatorfood.BlockGeneratorFood;
import com.lothrazar.cyclic.block.generatorfuel.BlockGeneratorFuel;
import com.lothrazar.cyclic.block.generatoritem.BlockGeneratorDrops;
import com.lothrazar.cyclic.block.generatorsolar.BlockGeneratorSolar;
import com.lothrazar.cyclic.block.glass.DarkGlassBlock;
import com.lothrazar.cyclic.block.glass.DarkGlassConnectedBlock;
import com.lothrazar.cyclic.block.glass.GlassConnectedBlock;
import com.lothrazar.cyclic.block.harvester.BlockHarvester;
import com.lothrazar.cyclic.block.hopper.BlockSimpleHopper;
import com.lothrazar.cyclic.block.hopperfluid.BlockFluidHopper;
import com.lothrazar.cyclic.block.hoppergold.BlockGoldHopper;
import com.lothrazar.cyclic.block.laser.BlockLaser;
import com.lothrazar.cyclic.block.magnet.BlockMagnetPanel;
import com.lothrazar.cyclic.block.melter.BlockMelter;
import com.lothrazar.cyclic.block.miner.BlockMiner;
import com.lothrazar.cyclic.block.packager.BlockPackager;
import com.lothrazar.cyclic.block.peatfarm.BlockPeatFarm;
import com.lothrazar.cyclic.block.phantom.CloudBlock;
import com.lothrazar.cyclic.block.phantom.CloudPlayerBlock;
import com.lothrazar.cyclic.block.phantom.GhostBlock;
import com.lothrazar.cyclic.block.phantom.MembraneBlock;
import com.lothrazar.cyclic.block.phantom.MembraneLamp;
import com.lothrazar.cyclic.block.phantom.SoilBlock;
import com.lothrazar.cyclic.block.placer.BlockPlacer;
import com.lothrazar.cyclic.block.placerfluid.BlockPlacerFluid;
import com.lothrazar.cyclic.block.rotator.BlockRotator;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffoldingReplace;
import com.lothrazar.cyclic.block.scaffolding.BlockScaffoldingResponsive;
import com.lothrazar.cyclic.block.screen.BlockScreentext;
import com.lothrazar.cyclic.block.shapebuilder.BlockStructure;
import com.lothrazar.cyclic.block.shapedata.BlockShapedata;
import com.lothrazar.cyclic.block.shears.BlockShearing;
import com.lothrazar.cyclic.block.solidifier.BlockSolidifier;
import com.lothrazar.cyclic.block.soundmuff.SoundmufflerBlock;
import com.lothrazar.cyclic.block.soundplay.BlockSoundPlayer;
import com.lothrazar.cyclic.block.soundrecord.BlockSoundRecorder;
import com.lothrazar.cyclic.block.spawntriggers.BlockAltarNoTraders;
import com.lothrazar.cyclic.block.spawntriggers.CandlePeaceBlock;
import com.lothrazar.cyclic.block.spikes.EnumSpikeType;
import com.lothrazar.cyclic.block.spikes.SpikesBlock;
import com.lothrazar.cyclic.block.spikes.SpikesDiamond;
import com.lothrazar.cyclic.block.sprinkler.BlockSprinkler;
import com.lothrazar.cyclic.block.tank.BlockFluidTank;
import com.lothrazar.cyclic.block.tankcask.BlockCask;
import com.lothrazar.cyclic.block.terraglass.BlockTerraGlass;
import com.lothrazar.cyclic.block.terrasoil.BlockTerraPreta;
import com.lothrazar.cyclic.block.tp.BlockTeleport;
import com.lothrazar.cyclic.block.trash.BlockTrash;
import com.lothrazar.cyclic.block.uncrafter.BlockUncraft;
import com.lothrazar.cyclic.block.user.BlockUser;
import com.lothrazar.cyclic.block.wireless.energy.BlockWirelessEnergy;
import com.lothrazar.cyclic.block.wireless.fluid.BlockWirelessFluid;
import com.lothrazar.cyclic.block.wireless.item.BlockWirelessItem;
import com.lothrazar.cyclic.block.wireless.redstone.BlockWirelessRec;
import com.lothrazar.cyclic.block.wireless.redstone.BlockWirelessTransmit;
import com.lothrazar.cyclic.block.workbench.BlockWorkbench;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.library.block.BlockFlib;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class BlockRegistry {

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModCyclic.MODID);


  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
      .icon(() -> new ItemStack(BlockRegistry.TRASH.get()))
      .title(Component.translatable("itemGroup." + ModCyclic.MODID))
      .displayItems((displayParameters, output) -> {
        //
        if (ModList.get().isLoaded(CompatConstants.PATCHOULI)) {
          try {
            // important: keep FQCN
            ItemStack guideBook = vazkii.patchouli.api.PatchouliAPI.get().getBookStack(
                net.minecraft.resources.Identifier.fromNamespaceAndPath(ModCyclic.MODID, "guide_book"));
            if (!guideBook.isEmpty()) {
              output.accept(guideBook);
            }
          } catch (Exception e) {
            ModCyclic.LOGGER.error("Could not add Patchouli guide_book to creative tab", e);
          }
        }
        // Next add all items (includes blocks that have an item version)
        List<ItemStack> stacks = ItemRegistry.ITEMS.getEntries().stream()
            .map(reg -> new ItemStack(reg.get()))
            .filter(stack -> !stack.is(ItemRegistry.MOB_CONTAINER.get()))
            .toList();
        output.acceptAll(stacks);
        // all potion and enchantments at the end
        HolderLookup.Provider lookup = displayParameters.holders();
        lookup.lookup(Registries.POTION).ifPresent(potionRegistry -> {
          PotionRegistry.POTIONS.getEntries().forEach(reg -> {
            potionRegistry.get(reg.getKey()).ifPresent(potionHolder -> {
              output.accept(PotionContents.createItemStack(Items.POTION, potionHolder));
              output.accept(PotionContents.createItemStack(Items.SPLASH_POTION, potionHolder));
              output.accept(PotionContents.createItemStack(Items.LINGERING_POTION, potionHolder));
              output.accept(PotionContents.createItemStack(Items.TIPPED_ARROW, potionHolder));
            });
          });
        });
        lookup.lookup(Registries.ENCHANTMENT).ifPresent(enchantRegistry -> {
          enchantRegistry.listElements()
              // 26.1: ResourceKey#location() renamed to identifier()
              .filter(holder -> holder.key().identifier().getNamespace().equals(ModCyclic.MODID))
              .forEach(holder -> {
                ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                book.enchant(holder, holder.value().getMaxLevel());
                output.accept(book);
              });
        });
      }).build());

  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModCyclic.MODID);
  public static final DeferredBlock<Block> COMPRESSED_COBBLESTONE = BLOCKS.registerBlock("compressed_cobblestone", props -> new BlockFlib(props.strength(1.0F, 7.0F), new BlockFlib.Settings().noTooltip()));
  public static final DeferredBlock<Block> FLINT_BLOCK = BLOCKS.registerBlock("flint_block", props -> new BlockFlib(props.strength(1.3F, 5.0F), new BlockFlib.Settings().noTooltip()));
  public static final DeferredBlock<Block> SPIKES_IRON = BLOCKS.registerBlock("spikes_iron", props -> new SpikesBlock(props, EnumSpikeType.PLAIN));
  public static final DeferredBlock<Block> SPIKES_FIRE = BLOCKS.registerBlock("spikes_fire", props -> new SpikesBlock(props, EnumSpikeType.FIRE));
  public static final DeferredBlock<Block> SPIKES_CURSE = BLOCKS.registerBlock("spikes_curse", props -> new SpikesBlock(props, EnumSpikeType.CURSE));
  public static final DeferredBlock<Block> SPIKES_DIAMOND = BLOCKS.registerBlock("spikes_diamond", props -> new SpikesDiamond(props));
  public static final DeferredBlock<Block> HOPPER_FLUID = BLOCKS.registerBlock("hopper_fluid", props -> new BlockFluidHopper(props.noOcclusion()));
  public static final DeferredBlock<Block> HOPPER = BLOCKS.registerBlock("hopper", props -> new BlockSimpleHopper(props.noOcclusion()));
  public static final DeferredBlock<Block> HOPPER_GOLD = BLOCKS.registerBlock("hopper_gold", props -> new BlockGoldHopper(props.noOcclusion()));
  public static final DeferredBlock<Block> FAN_SLAB = BLOCKS.registerBlock("fan_slab", props -> new BlockFanSlab(props.forceSolidOn()));
  public static final DeferredBlock<Block> ROTATOR = BLOCKS.registerBlock("rotator", props -> new BlockRotator(props));
  public static final DeferredBlock<Block> DETECTOR_MOON = BLOCKS.registerBlock("detector_moon", props -> new BlockMoon(props));
  public static final DeferredBlock<Block> DETECTOR_WEATHER = BLOCKS.registerBlock("detector_weather", props -> new BlockWeather(props));
  public static final DeferredBlock<Block> GLASS_TERRA = BLOCKS.registerBlock("glass_terra", props -> new BlockTerraGlass(props), () -> Block.Properties.ofFullCopy(Blocks.GLASS));
  public static final DeferredBlock<Block> SPRINKLER = BLOCKS.registerBlock("sprinkler", props -> new BlockSprinkler(props.forceSolidOn()));
  public static final DeferredBlock<Block> SHEARING = BLOCKS.registerBlock("shearing", props -> new BlockShearing(props));
  public static final DeferredBlock<Block> GLASS_DARK_CONNECTED = BLOCKS.registerBlock("glass_dark_connected", props -> new DarkGlassConnectedBlock(props));
  public static final DeferredBlock<Block> GLASS_CONNECTED = BLOCKS.registerBlock("glass_connected", props -> new GlassConnectedBlock(props.sound(SoundType.GLASS).strength(0.3F)));
  public static final DeferredBlock<Block> SHELF = BLOCKS.registerBlock("shelf", props -> new BlockItemShelf(props));
  public static final DeferredBlock<Block> DOORBELL = BLOCKS.registerBlock("doorbell", props -> new DoorbellButton(props));
  public static final DeferredBlock<Block> WIRELESS_ENERGY = BLOCKS.registerBlock("wireless_energy", props -> new BlockWirelessEnergy(props.forceSolidOn()));
  public static final DeferredBlock<Block> WIRELESS_ITEM = BLOCKS.registerBlock("wireless_item", props -> new BlockWirelessItem(props.forceSolidOn()));
  public static final DeferredBlock<Block> WIRELESS_FLUID = BLOCKS.registerBlock("wireless_fluid", props -> new BlockWirelessFluid(props.forceSolidOn()));
  public static final DeferredBlock<Block> SOUND_RECORDER = BLOCKS.registerBlock("sound_recorder", props -> new BlockSoundRecorder(props));
  public static final DeferredBlock<Block> SOUND_PLAYER = BLOCKS.registerBlock("sound_player", props -> new BlockSoundPlayer(props));
  public static final DeferredBlock<Block> GENERATOR_FUEL = BLOCKS.registerBlock("generator_fuel", props -> new BlockGeneratorFuel(props));
  public static final DeferredBlock<Block> GENERATOR_FOOD = BLOCKS.registerBlock("generator_food", props -> new BlockGeneratorFood(props));
  public static final DeferredBlock<Block> GENERATOR_FLUID = BLOCKS.registerBlock("generator_fluid", props -> new BlockGeneratorFluid(props));
  public static final DeferredBlock<Block> GENERATOR_ITEM = BLOCKS.registerBlock("generator_item", props -> new BlockGeneratorDrops(props));
  public static final DeferredBlock<Block> PACKAGER = BLOCKS.registerBlock("packager", props -> new BlockPackager(props));
  public static final DeferredBlock<Block> TERRA_PRETA = BLOCKS.registerBlock("terra_preta", props -> new BlockTerraPreta(props.sound(SoundType.GRAVEL)));
  public static final DeferredBlock<Block> LIGHT_CAMO = BLOCKS.registerBlock("light_camo", props -> new BlockLightFacade(props.forceSolidOn()));
  public static final DeferredBlock<Block> LASER = BLOCKS.registerBlock("laser", props -> new BlockLaser(props));
  public static final DeferredBlock<Block> FLOWER_CYAN = BLOCKS.registerBlock("flower_cyan", props -> new FlowerBlock(MobEffects.REGENERATION,
      4.0F,
      props
          .mapColor(MapColor.PLANT)
          .noCollision()
          .instabreak()
          .sound(SoundType.GRASS)
          .offsetType(BlockBehaviour.OffsetType.XZ)
          .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> FLOWER_PURPLE_TULIP = BLOCKS.registerBlock("flower_purple_tulip", props -> new FlowerBlock(MobEffects.REGENERATION,
      4.0F,
      props
          .mapColor(MapColor.PLANT)
          .noCollision()
          .instabreak()
          .sound(SoundType.GRASS)
          .offsetType(BlockBehaviour.OffsetType.XZ)
          .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> FLOWER_LIME_CARNATION = BLOCKS.registerBlock("flower_lime_carnation", props -> new FlowerBlock(MobEffects.REGENERATION,
      4.0F,
      props
          .mapColor(MapColor.PLANT)
          .noCollision()
          .instabreak()
          .sound(SoundType.GRASS)
          .offsetType(BlockBehaviour.OffsetType.XZ)
          .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> FLOWER_ABSALON_TULIP = BLOCKS.registerBlock("flower_absalon_tulip", props -> new FlowerBlock(MobEffects.REGENERATION,
      4.0F,
      props
          .mapColor(MapColor.PLANT)
          .noCollision()
          .instabreak()
          .sound(SoundType.GRASS)
          .offsetType(BlockBehaviour.OffsetType.XZ)
          .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> MEMBRANE = BLOCKS.registerBlock("membrane", props -> new MembraneBlock(props));
  public static final DeferredBlock<Block> LAMP = BLOCKS.registerBlock("lamp", props -> new MembraneLamp(props), () -> Block.Properties.ofFullCopy(Blocks.REDSTONE_LAMP));
  public static final DeferredBlock<Block> SOIL = BLOCKS.registerBlock("soil", props -> new SoilBlock(props.sound(SoundType.ROOTED_DIRT)));
  public static final DeferredBlock<Block> CLOUD = BLOCKS.registerBlock("cloud", props -> new CloudBlock(props));
  public static final DeferredBlock<Block> CLOUD_MEMBRANE = BLOCKS.registerBlock("cloud_membrane", props -> new CloudPlayerBlock(props));
  public static final DeferredBlock<Block> CLOUD_GHOST = BLOCKS.registerBlock("cloud_ghost", props -> new GhostBlock(props, false));
  public static final DeferredBlock<Block> CLOUD_BARRIER = BLOCKS.registerBlock("cloud_barrier", props -> new GhostBlock(props, true));
  public static final DeferredBlock<Block> WORKBENCH = BLOCKS.registerBlock("workbench", props -> new BlockWorkbench(props));
  public static final DeferredBlock<Block> OBSIDIAN_PRESSURE_PLATE = BLOCKS.registerBlock("obsidian_pressure_plate", props -> new PressurePlateMetal(props.noCollision().strength(0.5F)));
  public static final DeferredBlock<Block> GOLD_BARS = BLOCKS.registerBlock("gold_bars", props -> new MetalBarsBlock(props.strength(3.0F, 6.0F)));
  public static final DeferredBlock<Block> GOLD_CHAIN = BLOCKS.registerBlock("gold_chain", props -> new ChainBlock(props.strength(1).sound(SoundType.CHAIN).noOcclusion()));
  public static final DeferredBlock<Block> GOLD_LANTERN = BLOCKS.registerBlock("gold_lantern", props -> new LanternBlock(props.noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 14)));
  public static final DeferredBlock<Block> GOLD_SOUL_LANTERN = BLOCKS.registerBlock("gold_soul_lantern", props -> new LanternBlock(props.noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 15)));
  public static final DeferredBlock<Block> COPPER_BARS = BLOCKS.registerBlock("copper_bars", props -> new MetalBarsBlock(props.strength(3.0F, 6.0F)));
  public static final DeferredBlock<Block> COPPER_CHAIN = BLOCKS.registerBlock("copper_chain", props -> new ChainBlock(props.strength(1.0F).sound(SoundType.CHAIN).noOcclusion()));
  public static final DeferredBlock<Block> COPPER_LANTERN = BLOCKS.registerBlock("copper_lantern", props -> new LanternBlock(props.noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 12))); //soul_lantern=10
  public static final DeferredBlock<Block> COPPER_SOUL_LANTERN = BLOCKS.registerBlock("copper_soul_lantern", props -> new LanternBlock(props.noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 13))); //soul_lantern=10
  public static final DeferredBlock<Block> COPPER_PRESSURE_PLATE = BLOCKS.registerBlock("copper_pressure_plate", props -> new PressurePlateBlock(BlockSetType.COPPER, props.noCollision().strength(0.5F)) {

    @Override
    protected int getSignalForState(BlockState st) {
      return st.getValue(POWERED) ? 8 : 0;
    }
  });
  public static final DeferredBlock<Block> NETHERITE_BARS = BLOCKS.registerBlock("netherite_bars", props -> new MetalBarsBlock(props.strength(6.0F, 12.0F)));
  public static final DeferredBlock<Block> NETHERITE_CHAIN = BLOCKS.registerBlock("netherite_chain", props -> new ChainBlock(props.strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()));
  public static final DeferredBlock<Block> NETHERITE_LANTERN = BLOCKS.registerBlock("netherite_lantern", props -> new LanternBlock(props.noOcclusion().strength(3.5F).sound(SoundType.LANTERN).lightLevel(p -> 15))); // same as lantern=15
  public static final DeferredBlock<Block> NETHERITE_PRESSURE_PLATE = BLOCKS.registerBlock("netherite_pressure_plate", props -> new PressurePlateBlock(BlockSetType.STONE, props.noCollision().strength(0.5F)));
  public static final DeferredBlock<Block> SPONGE_LAVA = BLOCKS.registerBlock("sponge_lava", props -> new LavaSpongeBlock(props.sound(SoundType.SPORE_BLOSSOM).lightLevel(p -> 2)));
  public static final DeferredBlock<Block> SPONGE_MILK = BLOCKS.registerBlock("sponge_milk", props -> new MilkSpongeBlock(props.lightLevel(p -> 1)));
  public static final DeferredBlock<Block> CRUSHER = BLOCKS.registerBlock("crusher", props -> new BlockCrusher(props));
  public static final DeferredBlock<Block> CANDLE_PEACE = BLOCKS.registerBlock("candle_peace", props -> new CandlePeaceBlock(props
      .lightLevel(p -> p.getValue(BlockCyclic.LIT) ? 6 : 0)));
  public static final DeferredBlock<Block> CANDLE_WATER = BLOCKS.registerBlock("candle_water", props -> new CandleWaterBlock(props
      .lightLevel(p -> p.getValue(BlockCyclic.LIT) ? 1 : 0)));
  public static final DeferredBlock<Block> TELEPORT = BLOCKS.registerBlock("teleport", props -> new BlockTeleport(props.forceSolidOn()));
  public static final DeferredBlock<Block> APPLE_SPROUT_EMERALD = BLOCKS.registerBlock("apple_sprout_emerald", props -> new AppleCropBlock(props, false));
  public static final DeferredBlock<Block> APPLE_SPROUT_DIAMOND = BLOCKS.registerBlock("apple_sprout_diamond", props -> new AppleCropBlock(props, false));
  public static final DeferredBlock<Block> APPLE_SPROUT = BLOCKS.registerBlock("apple_sprout", props -> new AppleCropBlock(props, true));
  public static final DeferredBlock<Block> COMPUTER_SHAPE = BLOCKS.registerBlock("computer_shape", props -> new BlockShapedata(props));
  public static final DeferredBlock<Block> SCAFFOLD_FRAGILE = BLOCKS.registerBlock("scaffold_fragile", props -> new BlockScaffolding(props, true));
  public static final DeferredBlock<Block> SCAFFOLD_RESPONSIVE = BLOCKS.registerBlock("scaffold_responsive", props -> new BlockScaffoldingResponsive(props, false));
  public static final DeferredBlock<Block> SCAFFOLD_REPLACE = BLOCKS.registerBlock("scaffold_replace", props -> new BlockScaffoldingReplace(props));
  public static final DeferredBlock<Block> GLASS_DARK = BLOCKS.registerBlock("glass_dark", props -> new DarkGlassBlock(props));
  public static final DeferredBlock<Block> PEAT_UNBAKED = BLOCKS.registerBlock("peat_unbaked", props -> new PeatBlock(props.sound(SoundType.GRAVEL)));
  public static final DeferredBlock<Block> PEAT_BAKED = BLOCKS.registerBlock("peat_baked", props -> new PeatFuelBlock(props.sound(SoundType.GRAVEL)));
  public static final DeferredBlock<Block> PEAT_FARM = BLOCKS.registerBlock("peat_farm", props -> new BlockPeatFarm(props));
  public static final DeferredBlock<Block> SOLIDIFIER = BLOCKS.registerBlock("solidifier", props -> new BlockSolidifier(props));
  public static final DeferredBlock<Block> MELTER = BLOCKS.registerBlock("melter", props -> new BlockMelter(props));
  public static final DeferredBlock<Block> BATTERY = BLOCKS.registerBlock("battery", props -> new BlockBattery(props));
  public static final DeferredBlock<Block> CASK = BLOCKS.registerBlock("cask", props -> new BlockCask(props));
  public static final DeferredBlock<Block> CRATE = BLOCKS.registerBlock("crate", props -> new BlockCrate(props));
  public static final DeferredBlock<Block> CRATE_MINI = BLOCKS.registerBlock("crate_mini", props -> new BlockCrateMini(props.forceSolidOn()));
  public static final DeferredBlock<Block> EYE_REDSTONE = BLOCKS.registerBlock("ender_eye_block", props -> new BlockEye(props));
  public static final DeferredBlock<Block> EYE_TELEPORT = BLOCKS.registerBlock("ender_pearl_block", props -> new BlockEyeTp(props));
  public static final DeferredBlock<Block> PLACER = BLOCKS.registerBlock("placer", props -> new BlockPlacer(props));
  public static final DeferredBlock<Block> BREAKER = BLOCKS.registerBlock("breaker", props -> new BlockBreaker(props));
  public static final DeferredBlock<Block> DROPPER = BLOCKS.registerBlock("dropper", props -> new BlockDropper(props));
  public static final DeferredBlock<Block> FORESTER = BLOCKS.registerBlock("forester", props -> new BlockForester(props));
  public static final DeferredBlock<Block> HARVESTER = BLOCKS.registerBlock("harvester", props -> new BlockHarvester(props));
  public static final DeferredBlock<Block> MINER = BLOCKS.registerBlock("miner", props -> new BlockMiner(props));
  public static final DeferredBlock<Block> PLACER_FLUID = BLOCKS.registerBlock("placer_fluid", props -> new BlockPlacerFluid(props));
  public static final DeferredBlock<Block> USER = BLOCKS.registerBlock("user", props -> new BlockUser(props));
  public static final DeferredBlock<Block> COLLECTOR = BLOCKS.registerBlock("collector", props -> new BlockItemCollector(props));
  public static final DeferredBlock<Block> COLLECTOR_FLUID = BLOCKS.registerBlock("collector_fluid", props -> new BlockFluidCollect(props));
  public static final DeferredBlock<Block> STRUCTURE = BLOCKS.registerBlock("structure", props -> new BlockStructure(props));
  public static final DeferredBlock<Block> UNCRAFTER = BLOCKS.registerBlock("uncrafter", props -> new BlockUncraft(props));
  public static final DeferredBlock<Block> CRAFTER = BLOCKS.registerBlock("crafter", props -> new BlockCrafter(props));
  public static final DeferredBlock<Block> CONVEYOR = BLOCKS.registerBlock("conveyor", props -> new BlockConveyor(props.forceSolidOn()));
  public static final DeferredBlock<Block> TANK = BLOCKS.registerBlock("tank", props -> new BlockFluidTank(props));
  public static final DeferredBlock<Block> ANVIL = BLOCKS.registerBlock("anvil", props -> new BlockAnvilAuto(props.sound(SoundType.ANVIL)));
  public static final DeferredBlock<Block> ANVIL_MAGMA = BLOCKS.registerBlock("anvil_magma", props -> new BlockAnvilMagma(props.sound(SoundType.ANVIL)));
  public static final DeferredBlock<Block> ANVILVOID = BLOCKS.registerBlock("anvil_void", props -> new BlockAnvilVoid(props));
  public static final DeferredBlock<Block> BEACON = BLOCKS.registerBlock("beacon", props -> new BlockPotion(props));
  public static final DeferredBlock<Block> BEACON_REDSTONE = BLOCKS.registerBlock("beacon_redstone", props -> new BlockBeaconRedstone(props.lightLevel(p -> 4)));
  public static final DeferredBlock<BlockAntiBeacon> BEACON_SPONGE = BLOCKS.registerBlock("beacon_sponge", props -> new BlockAntiBeacon(props.lightLevel(p -> 2)));
  public static final DeferredBlock<Block> SOUNDPROOFING_GHOST = BLOCKS.registerBlock("soundproofing_ghost", props -> new SoundmufflerBlockFacade(props));
  public static final DeferredBlock<Block> SOUNDPROOFING = BLOCKS.registerBlock("soundproofing", props -> new SoundmufflerBlock(props));
  public static final DeferredBlock<Block> CLOCK = BLOCKS.registerBlock("clock", props -> new BlockRedstoneClock(props));
  public static final DeferredBlock<Block> WIRELESS_RECEIVER = BLOCKS.registerBlock("wireless_receiver", props -> new BlockWirelessRec(props));
  public static final DeferredBlock<Block> WIRELESS_TRANSMITTER = BLOCKS.registerBlock("wireless_transmitter", props -> new BlockWirelessTransmit(props));
  public static final DeferredBlock<Block> FISHER = BLOCKS.registerBlock("fisher", props -> new BlockFisher(props));
  public static final DeferredBlock<Block> DISENCHANTER = BLOCKS.registerBlock("disenchanter", props -> new BlockDisenchant(props));
  public static final DeferredBlock<Block> EXPERIENCE_PYLON = BLOCKS.registerBlock("experience_pylon", props -> new BlockExpPylon(props));
  public static final DeferredBlock<Block> EXPERIENCE_FOUNTAIN = BLOCKS.registerBlock("experience_fountain", props -> new BlockExperienceFountain(props.forceSolidOn()));
  public static final DeferredBlock<Block> FAN = BLOCKS.registerBlock("fan", props -> new BlockFan(props.forceSolidOn()));
  public static final DeferredBlock<Block> TRASH = BLOCKS.registerBlock("trash", props -> new BlockTrash(props));
  public static final DeferredBlock<Block> DICE = BLOCKS.registerBlock("dice", props -> new BlockDice(props));
  public static final DeferredBlock<Block> SCREEN = BLOCKS.registerBlock("screen", props -> new BlockScreentext(props));
  public static final DeferredBlock<Block> DETECTOR_ITEM = BLOCKS.registerBlock("detector_item", props -> new BlockDetectorItem(props));
  public static final DeferredBlock<Block> DETECTOR_ENTITY = BLOCKS.registerBlock("detector_entity", props -> new BlockDetector(props));
  public static final DeferredBlock<Block> ENERGY_PIPE = BLOCKS.registerBlock("energy_pipe", props -> new BlockCableEnergy(props.sound(SoundType.STONE).forceSolidOn()));
  public static final DeferredBlock<Block> ITEM_PIPE = BLOCKS.registerBlock("item_pipe", props -> new BlockCableItem(props.sound(SoundType.STONE).forceSolidOn()));
  public static final DeferredBlock<Block> FLUID_PIPE = BLOCKS.registerBlock("fluid_pipe", props -> new BlockCableFluid(props.sound(SoundType.STONE).forceSolidOn()));
  public static final DeferredBlock<Block> BUNDLED_PIPE = BLOCKS.registerBlock("bundled_pipe", props -> new BlockCableBundled(props.sound(SoundType.STONE).forceSolidOn()));
  public static final DeferredBlock<Block> PLATE_LAUNCH = BLOCKS.registerBlock("plate_launch", props -> new LaunchBlock(props, false));
  public static final DeferredBlock<Block> PLATE_LAUNCH_REDSTONE = BLOCKS.registerBlock("plate_launch_redstone", props -> new LaunchBlock(props, true));
  public static final DeferredBlock<Block> BATTERY_INFINITE = BLOCKS.registerBlock("battery_infinite", props -> new BlockBatteryInfinite(props));
  public static final DeferredBlock<Block> ITEM_INFINITE = BLOCKS.registerBlock("item_infinite", props -> new BlockItemInfinite(props));
  public static final DeferredBlock<Block> FIREPLACE = BLOCKS.registerBlock("fireplace", props -> new FireplaceBlock(props));
  public static final DeferredBlock<Block> UNBREAKABLE_BLOCK = BLOCKS.registerBlock("unbreakable_block", props -> new UnbreakableBlock(props)); //stable, only changes with player interaction
  public static final DeferredBlock<Block> UNBREAKABLE_REACTIVE = BLOCKS.registerBlock("unbreakable_reactive", props -> new UnbreakablePoweredBlock(props)); //reactive and unstable, ignores players and reads redstone 
  public static final DeferredBlock<Block> ENDER_SHELF = BLOCKS.registerBlock("ender_shelf", props -> new BlockEnderShelf(props));
  public static final DeferredBlock<Block> ENDER_CONTROLLER = BLOCKS.registerBlock("ender_controller", props -> new BlockEnderCtrl(props));
  public static final DeferredBlock<Block> MAGNET_BLOCK = BLOCKS.registerBlock("magnet_block", props -> new BlockMagnetPanel(props.forceSolidOn()));
  public static final DeferredBlock<Block> BUTTON_BASALT = BLOCKS.registerBlock("button_basalt", props -> new ButtonBlockMat(props, 30, true, 2));
  public static final DeferredBlock<Block> BUTTON_BLACKSTONE = BLOCKS.registerBlock("button_blackstone", props -> new ButtonBlockMat(props, 20, true, 10));
  public static final DeferredBlock<Block> BUTTON_DEEPSLATE = BLOCKS.registerBlock("button_deepslate", props -> new ButtonBlockMat(props, 40, false, 12));//hardest material so highest
  public static final DeferredBlock<Block> BUTTON_TUFF = BLOCKS.registerBlock("button_tuff", props -> new ButtonBlockMat(props, 30, true, 8));
  public static final DeferredBlock<Block> BATTERY_CLAY = BLOCKS.registerBlock("battery_clay", props -> new ClayBattery(props));// NOGUI
  public static final DeferredBlock<Block> GENERATOR_SOLAR = BLOCKS.registerBlock("generator_solar", props -> new BlockGeneratorSolar(props.forceSolidOn())); // NOGUI
  public static final DeferredBlock<Block> ALTAR_SOLICITING = BLOCKS.registerBlock("altar_soliciting", props -> new BlockAltarNoTraders(props
      .lightLevel(p -> p.getValue(BlockCyclic.LIT) ? 3 : 0).forceSolidOn()));
  public static final DeferredBlock<Block> ALTAR_DESTRUCTION = BLOCKS.registerBlock("altar_destruction", props -> new BlockDestruction(props));
  public static final DeferredBlock<Block> WAXED_REDSTONE = BLOCKS.registerBlock("waxed_redstone", props -> new BlockWaxedRedstone(props)); // , MaterialColor.FIRE

}
