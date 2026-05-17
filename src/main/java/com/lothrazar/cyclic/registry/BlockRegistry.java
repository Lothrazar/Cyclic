package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
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
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockRegistry {

  public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModCyclic.MODID);


  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_BLOCKS = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
          .icon(() -> new ItemStack(BlockRegistry.TRASH.get()))
          .title(Component.translatable("itemGroup." + ModCyclic.MODID))
          .displayItems((displayParameters, output) -> {
            //
            // important: keep FQCN
            if (net.neoforged.fml.ModList.get().isLoaded(CompatConstants.PATCHOULI)) {
              try {
                ItemStack guideBook = vazkii.patchouli.api.PatchouliAPI.get().getBookStack(
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(ModCyclic.MODID, "guide_book"));
                if (!guideBook.isEmpty()) {
                  output.accept(guideBook);
                }
              }
              catch (Exception e) {
                ModCyclic.LOGGER.error("Could not add Patchouli guide_book to creative tab", e);
              }
            }
            // Next add all items (includes blocks that have an item version)
            List<ItemStack> stacks = ItemRegistry.ITEMS.getEntries().stream()
                .map(reg -> new ItemStack(reg.get())).toList();
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
                  .filter(holder -> holder.key().location().getNamespace().equals(ModCyclic.MODID))
                  .forEach(holder -> {
                    ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                    book.enchant(holder, holder.value().getMaxLevel());
                    output.accept(book);
                  });
            });
          }).build());

  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModCyclic.MODID);
  public static final DeferredBlock<Block> COMPRESSED_COBBLESTONE = BLOCKS.register("compressed_cobblestone", () -> new BlockFlib(Block.Properties.of().strength(1.0F, 7.0F), new BlockFlib.Settings().noTooltip()));
  public static final DeferredBlock<Block> FLINT_BLOCK = BLOCKS.register("flint_block", () -> new BlockFlib(Block.Properties.of().strength(1.3F, 5.0F), new BlockFlib.Settings().noTooltip()) );
  public static final DeferredBlock<Block> SPIKES_IRON = BLOCKS.register("spikes_iron", () -> new SpikesBlock(Block.Properties.of(), EnumSpikeType.PLAIN));
  public static final DeferredBlock<Block> SPIKES_FIRE = BLOCKS.register("spikes_fire", () -> new SpikesBlock(Block.Properties.of(), EnumSpikeType.FIRE));
  public static final DeferredBlock<Block> SPIKES_CURSE = BLOCKS.register("spikes_curse", () -> new SpikesBlock(Block.Properties.of(), EnumSpikeType.CURSE));
  public static final DeferredBlock<Block> SPIKES_DIAMOND = BLOCKS.register("spikes_diamond", () -> new SpikesDiamond(Block.Properties.of()));
  public static final DeferredBlock<Block> FLUIDHOPPER = BLOCKS.register("hopper_fluid", () -> new BlockFluidHopper(Block.Properties.of()));
  public static final DeferredBlock<Block> HOPPER = BLOCKS.register("hopper", () -> new BlockSimpleHopper(Block.Properties.of()));
  public static final DeferredBlock<Block> HOPPERGOLD = BLOCKS.register("hopper_gold", () -> new BlockGoldHopper(Block.Properties.of()));
  public static final DeferredBlock<Block> ANVILVOID = BLOCKS.register("anvil_void", () -> new BlockAnvilVoid(Block.Properties.of()));
  public static final DeferredBlock<Block> FANSLAB = BLOCKS.register("fan_slab", () -> new BlockFanSlab(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> ROTATOR = BLOCKS.register("rotator", () -> new BlockRotator(Block.Properties.of()));
  public static final DeferredBlock<Block> DETECTORMOON = BLOCKS.register("detector_moon", () -> new BlockMoon(Block.Properties.of()));
  public static final DeferredBlock<Block> DETECTORWEATHER = BLOCKS.register("detector_weather", () -> new BlockWeather(Block.Properties.of()));
  public static final DeferredBlock<Block> TERRAGLASS = BLOCKS.register("terra_glass", () -> new BlockTerraGlass(Block.Properties.ofFullCopy(Blocks.GLASS)));
  public static final DeferredBlock<Block> SPRINKLER = BLOCKS.register("sprinkler", () -> new BlockSprinkler(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> SHEARING = BLOCKS.register("shearing", () -> new BlockShearing(Block.Properties.of()));
  public static final DeferredBlock<Block> DARK_GLASS_CONNECTED = BLOCKS.register("dark_glass_connected", () -> new DarkGlassConnectedBlock(Block.Properties.of()));
  public static final DeferredBlock<Block> GLASS_CONNECTED = BLOCKS.register("glass_connected", () -> new GlassConnectedBlock(Block.Properties.of().sound(SoundType.GLASS).strength(0.3F)));
  public static final DeferredBlock<Block> ENDER_ITEM_SHELF = BLOCKS.register("ender_item_shelf", () -> new BlockItemShelf(Block.Properties.of()));
  public static final DeferredBlock<Block> DOORBELL = BLOCKS.register("doorbell", () -> new DoorbellButton(Block.Properties.of()));
  public static final DeferredBlock<Block> WIRELESS_ENERGY = BLOCKS.register("wireless_energy", () -> new BlockWirelessEnergy(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> WIRELESS_ITEM = BLOCKS.register("wireless_item", () -> new BlockWirelessItem(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> WIRELESS_FLUID = BLOCKS.register("wireless_fluid", () -> new BlockWirelessFluid(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> SOUND_RECORDER = BLOCKS.register("sound_recorder", () -> new BlockSoundRecorder(Block.Properties.of()));
  public static final DeferredBlock<Block> SOUND_PLAYER = BLOCKS.register("sound_player", () -> new BlockSoundPlayer(Block.Properties.of()));
  public static final DeferredBlock<Block> GENERATOR_FUEL = BLOCKS.register("generator_fuel", () -> new BlockGeneratorFuel(Block.Properties.of()));
  public static final DeferredBlock<Block> GENERATOR_FOOD = BLOCKS.register("generator_food", () -> new BlockGeneratorFood(Block.Properties.of()));
  public static final DeferredBlock<Block> GENERATOR_FLUID = BLOCKS.register("generator_fluid", () -> new BlockGeneratorFluid(Block.Properties.of()));
  public static final DeferredBlock<Block> GENERATOR_ITEM = BLOCKS.register("generator_item", () -> new BlockGeneratorDrops(Block.Properties.of()));
  public static final DeferredBlock<Block> PACKAGER = BLOCKS.register("packager", () -> new BlockPackager(Block.Properties.of()));
  public static final DeferredBlock<Block> TERRA_PRETA = BLOCKS.register("terra_preta", () -> new BlockTerraPreta(Block.Properties.of().sound(SoundType.GRAVEL)));
  public static final DeferredBlock<Block> LIGHT_CAMO = BLOCKS.register("light_camo", () -> new BlockLightFacade(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> LASER = BLOCKS.register("laser", () -> new BlockLaser(Block.Properties.of()));
  public static final DeferredBlock<Block> FLOWER_CYAN = BLOCKS.register("flower_cyan", () -> new FlowerBlock(MobEffects.REGENERATION,
          4.0F,
          Block.Properties.of()
                  .mapColor(MapColor.PLANT)
                  .noCollission()
                  .instabreak()
                  .sound(SoundType.GRASS)
                  .offsetType(BlockBehaviour.OffsetType.XZ)
                  .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> FLOWER_PURPLE_TULIP = BLOCKS.register("flower_purple_tulip", () -> new FlowerBlock(MobEffects.REGENERATION,
          4.0F,
          Block.Properties.of()
                  .mapColor(MapColor.PLANT)
                  .noCollission()
                  .instabreak()
                  .sound(SoundType.GRASS)
                  .offsetType(BlockBehaviour.OffsetType.XZ)
                  .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> FLOWER_LIME_CARNATION = BLOCKS.register("flower_lime_carnation", () -> new FlowerBlock(MobEffects.REGENERATION,
          4.0F,
          Block.Properties.of()
                  .mapColor(MapColor.PLANT)
                  .noCollission()
                  .instabreak()
                  .sound(SoundType.GRASS)
                  .offsetType(BlockBehaviour.OffsetType.XZ)
                  .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> FLOWER_ABSALON_TULIP = BLOCKS.register("flower_absalon_tulip", () -> new FlowerBlock(MobEffects.REGENERATION,
          4.0F,
          Block.Properties.of()
                  .mapColor(MapColor.PLANT)
                  .noCollission()
                  .instabreak()
                  .sound(SoundType.GRASS)
                  .offsetType(BlockBehaviour.OffsetType.XZ)
                  .pushReaction(PushReaction.DESTROY)
  ));
  public static final DeferredBlock<Block> MEMBRANE = BLOCKS.register("membrane", () -> new MembraneBlock(Block.Properties.of()));
  public static final DeferredBlock<Block> LAMP = BLOCKS.register("lamp", () -> new MembraneLamp(Block.Properties.ofFullCopy(Blocks.REDSTONE_LAMP)));
  public static final DeferredBlock<Block> SOIL = BLOCKS.register("soil", () -> new SoilBlock(Block.Properties.of().sound(SoundType.ROOTED_DIRT)));
  public static final DeferredBlock<Block> CLOUD = BLOCKS.register("cloud", () -> new CloudBlock(Block.Properties.of()));
  public static final DeferredBlock<Block> CLOUD_MEMBRANE = BLOCKS.register("cloud_membrane", () -> new CloudPlayerBlock(Block.Properties.of()));
  public static final DeferredBlock<Block> GHOST = BLOCKS.register("ghost", () -> new GhostBlock(Block.Properties.of(), false));
  public static final DeferredBlock<Block> GHOST_PHANTOM = BLOCKS.register("ghost_phantom", () -> new GhostBlock(Block.Properties.of(), true));
  public static final DeferredBlock<Block> WORKBENCH = BLOCKS.register("workbench", () -> new BlockWorkbench(Block.Properties.of()));
  public static final DeferredBlock<Block> OBSIDIAN_PRESSURE_PLATE = BLOCKS.register("obsidian_pressure_plate", () -> new PressurePlateMetal(Block.Properties.of().noCollission().strength(0.5F)));
  public static final DeferredBlock<Block> GOLD_BARS = BLOCKS.register("gold_bars", () -> new MetalBarsBlock(Block.Properties.of().strength(3.0F, 6.0F)));
  public static final DeferredBlock<Block> GOLD_CHAIN = BLOCKS.register("gold_chain", () -> new ChainBlock(BlockBehaviour.Properties.of().strength(1).sound(SoundType.CHAIN).noOcclusion()));
  public static final DeferredBlock<Block> GOLD_LANTERN = BLOCKS.register("gold_lantern", () -> new LanternBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 14)));
  public static final DeferredBlock<Block> GOLD_SOUL_LANTERN = BLOCKS.register("gold_soul_lantern", () -> new LanternBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 15)));
  public static final DeferredBlock<Block> COPPER_BARS = BLOCKS.register("copper_bars", () -> new MetalBarsBlock(Block.Properties.of().strength(3.0F, 6.0F)));
  public static final DeferredBlock<Block> COPPER_CHAIN = BLOCKS.register("copper_chain", () -> new ChainBlock(BlockBehaviour.Properties.of().strength(1.0F).sound(SoundType.CHAIN).noOcclusion()));
  public static final DeferredBlock<Block> COPPER_LANTERN = BLOCKS.register("copper_lantern", () -> new LanternBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 12))); //soul_lantern=10
  public static final DeferredBlock<Block> COPPER_SOUL_LANTERN = BLOCKS.register("copper_soul_lantern", () -> new LanternBlock(BlockBehaviour.Properties.of().noOcclusion().strength(1.5F).sound(SoundType.LANTERN).lightLevel(p -> 13))); //soul_lantern=10
  public static final DeferredBlock<Block> COPPER_PRESSURE_PLATE = BLOCKS.register("copper_pressure_plate", () -> new PressurePlateBlock(BlockSetType.COPPER, Block.Properties.of().noCollission().strength(0.5F)) {

    @Override
    protected int getSignalForState(BlockState st) {
      return st.getValue(POWERED) ? 8 : 0;
    }
  });
  public static final DeferredBlock<Block> NETHERITE_BARS = BLOCKS.register("netherite_bars", () -> new MetalBarsBlock(Block.Properties.of().strength(6.0F, 12.0F)));
  public static final DeferredBlock<Block> NETHERTIE_CHAIN = BLOCKS.register("netherite_chain", () -> new ChainBlock(BlockBehaviour.Properties.of().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion()));
  public static final DeferredBlock<Block> NETHERITE_LANTERN = BLOCKS.register("netherite_lantern", () -> new LanternBlock(BlockBehaviour.Properties.of().noOcclusion().strength(3.5F).sound(SoundType.LANTERN).lightLevel(p -> 15))); // same as lantern=15
  public static final DeferredBlock<Block> NETHERITE_PRESSURE_PLATE = BLOCKS.register("netherite_pressure_plate", () -> new PressurePlateBlock(BlockSetType.STONE, Block.Properties.of().noCollission().strength(0.5F)));
  public static final DeferredBlock<Block> SPONGE_LAVA = BLOCKS.register("sponge_lava", () -> new LavaSpongeBlock(Block.Properties.of().sound(SoundType.SPORE_BLOSSOM).lightLevel(p -> 2)));
  public static final DeferredBlock<Block> SPONGE_MILK = BLOCKS.register("sponge_milk", () -> new MilkSpongeBlock(Block.Properties.of().lightLevel(p -> 1)));
  public static final DeferredBlock<Block> CRUSHER = BLOCKS.register("crusher", () -> new BlockCrusher(Block.Properties.of()));
  public static final DeferredBlock<Block> PEACE_CANDLE = BLOCKS.register("peace_candle", () -> new CandlePeaceBlock(Block.Properties.of()
      .lightLevel(p -> p.getValue(BlockCyclic.LIT) ? 6 : 0)));
  public static final DeferredBlock<Block> WATER_CANDLE = BLOCKS.register("water_candle", () -> new CandleWaterBlock(Block.Properties.of()
      .lightLevel(p -> p.getValue(BlockCyclic.LIT) ? 1 : 0)));
  public static final DeferredBlock<Block> TELEPORT = BLOCKS.register("teleport", () -> new BlockTeleport(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> APPLE_SPROUT_EMERALD = BLOCKS.register("apple_sprout_emerald", () -> new AppleCropBlock(Block.Properties.of(), false));
  public static final DeferredBlock<Block> APPLE_SPROUT_DIAMOND = BLOCKS.register("apple_sprout_diamond", () -> new AppleCropBlock(Block.Properties.of(), false));
  public static final DeferredBlock<Block> APPLE_SPROUT = BLOCKS.register("apple_sprout", () -> new AppleCropBlock(Block.Properties.of(), true));
  public static final DeferredBlock<Block> COMPUTER_SHAPE = BLOCKS.register("computer_shape", () -> new BlockShapedata(Block.Properties.of()));
  public static final DeferredBlock<Block> SCAFFOLD_FRAGILE = BLOCKS.register("scaffold_fragile", () -> new BlockScaffolding(Block.Properties.of(), true));
  public static final DeferredBlock<Block> SCAFFOLD_RESPONSIVE = BLOCKS.register("scaffold_responsive", () -> new BlockScaffoldingResponsive(Block.Properties.of(), false));
  public static final DeferredBlock<Block> SCAFFOLD_REPLACE = BLOCKS.register("scaffold_replace", () -> new BlockScaffoldingReplace(Block.Properties.of()));
  public static final DeferredBlock<Block> DARK_GLASS = BLOCKS.register("dark_glass", () -> new DarkGlassBlock(Block.Properties.of()));
  public static final DeferredBlock<Block> PEAT_UNBAKED = BLOCKS.register("peat_unbaked", () -> new PeatBlock(Block.Properties.of().sound(SoundType.GRAVEL)));
  public static final DeferredBlock<Block> PEAT_BAKED = BLOCKS.register("peat_baked", () -> new PeatFuelBlock(Block.Properties.of().sound(SoundType.GRAVEL)));
  public static final DeferredBlock<Block> PEAT_FARM = BLOCKS.register("peat_farm", () -> new BlockPeatFarm(Block.Properties.of()));
  public static final DeferredBlock<Block> SOLIDIFIER = BLOCKS.register("solidifier", () -> new BlockSolidifier(Block.Properties.of()));
  public static final DeferredBlock<Block> MELTER = BLOCKS.register("melter", () -> new BlockMelter(Block.Properties.of()));
  public static final DeferredBlock<Block> BATTERY = BLOCKS.register("battery", () -> new BlockBattery(Block.Properties.of()));
  public static final DeferredBlock<Block> CASK = BLOCKS.register("cask", () -> new BlockCask(Block.Properties.of()));
  public static final DeferredBlock<Block> CRATE = BLOCKS.register("crate", () -> new BlockCrate(Block.Properties.of()));
  public static final DeferredBlock<Block> CRATE_MINI = BLOCKS.register("crate_mini", () -> new BlockCrateMini(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> EYE_REDSTONE = BLOCKS.register("eye_redstone", () -> new BlockEye(Block.Properties.of()));
  public static final DeferredBlock<Block> EYE_TELEPORT = BLOCKS.register("eye_teleport", () -> new BlockEyeTp(Block.Properties.of()));
  public static final DeferredBlock<Block> PLACER = BLOCKS.register("placer", () -> new BlockPlacer(Block.Properties.of()));
  public static final DeferredBlock<Block> BREAKER = BLOCKS.register("breaker", () -> new BlockBreaker(Block.Properties.of()));
  public static final DeferredBlock<Block> DROPPER = BLOCKS.register("dropper", () -> new BlockDropper(Block.Properties.of()));
  public static final DeferredBlock<Block> FORESTER = BLOCKS.register("forester", () -> new BlockForester(Block.Properties.of()));
  public static final DeferredBlock<Block> HARVESTER = BLOCKS.register("harvester", () -> new BlockHarvester(Block.Properties.of()));
  public static final DeferredBlock<Block> MINER = BLOCKS.register("miner", () -> new BlockMiner(Block.Properties.of()));
  public static final DeferredBlock<Block> PLACER_FLUID = BLOCKS.register("placer_fluid", () -> new BlockPlacerFluid(Block.Properties.of()));
  public static final DeferredBlock<Block> USER = BLOCKS.register("user", () -> new BlockUser(Block.Properties.of()));
  public static final DeferredBlock<Block> COLLECTOR = BLOCKS.register("collector", () -> new BlockItemCollector(Block.Properties.of()));
  public static final DeferredBlock<Block> COLLECTOR_FLUID = BLOCKS.register("collector_fluid", () -> new BlockFluidCollect(Block.Properties.of()));
  public static final DeferredBlock<Block> STRUCTURE = BLOCKS.register("structure", () -> new BlockStructure(Block.Properties.of()));
  public static final DeferredBlock<Block> UNCRAFTER = BLOCKS.register("uncrafter", () -> new BlockUncraft(Block.Properties.of()));
  public static final DeferredBlock<Block> CRAFTER = BLOCKS.register("crafter", () -> new BlockCrafter(Block.Properties.of()));
  public static final DeferredBlock<Block> CONVEYOR = BLOCKS.register("conveyor", () -> new BlockConveyor(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> TANK = BLOCKS.register("tank", () -> new BlockFluidTank(Block.Properties.of()));
  public static final DeferredBlock<Block> ANVIL = BLOCKS.register("anvil", () -> new BlockAnvilAuto(Block.Properties.of().sound(SoundType.ANVIL)));
  public static final DeferredBlock<Block> ANVIL_MAGMA = BLOCKS.register("anvil_magma", () -> new BlockAnvilMagma(Block.Properties.of().sound(SoundType.ANVIL)));
  public static final DeferredBlock<Block> BEACON = BLOCKS.register("beacon", () -> new BlockPotion(Block.Properties.of()));
  public static final DeferredBlock<Block> BEACON_REDSTONE = BLOCKS.register("beacon_redstone", () -> new BlockBeaconRedstone(Block.Properties.of().lightLevel(p -> 4)));
  public static final DeferredBlock<BlockAntiBeacon> ANTI_BEACON = BLOCKS.register("anti_beacon", () -> new BlockAntiBeacon(Block.Properties.of().lightLevel(p -> 2)));
  public static final DeferredBlock<Block> SOUNDPROOFING_GHOST = BLOCKS.register("soundproofing_ghost", () -> new SoundmufflerBlockFacade(Block.Properties.of()));
  public static final DeferredBlock<Block> SOUNDPROOFING = BLOCKS.register("soundproofing", () -> new SoundmufflerBlock(Block.Properties.of()));
  public static final DeferredBlock<Block> CLOCK = BLOCKS.register("clock", () -> new BlockRedstoneClock(Block.Properties.of()));
  public static final DeferredBlock<Block> WIRELESS_RECEIVER = BLOCKS.register("wireless_receiver", () -> new BlockWirelessRec(Block.Properties.of()));
  public static final DeferredBlock<Block> WIRELESS_TRANSMITTER = BLOCKS.register("wireless_transmitter", () -> new BlockWirelessTransmit(Block.Properties.of()));
  public static final DeferredBlock<Block> FISHER = BLOCKS.register("fisher", () -> new BlockFisher(Block.Properties.of()));
  public static final DeferredBlock<Block> DISENCHANTER = BLOCKS.register("disenchanter", () -> new BlockDisenchant(Block.Properties.of()));
  public static final DeferredBlock<Block> EXPERIENCE_PYLON = BLOCKS.register("experience_pylon", () -> new BlockExpPylon(Block.Properties.of()));
  public static final DeferredBlock<Block> FAN = BLOCKS.register("fan", () -> new BlockFan(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> TRASH = BLOCKS.register("trash", () -> new BlockTrash(Block.Properties.of()));
  public static final DeferredBlock<Block> DICE = BLOCKS.register("dice", () -> new BlockDice(Block.Properties.of()));
  public static final DeferredBlock<Block> SCREEN = BLOCKS.register("screen", () -> new BlockScreentext(Block.Properties.of()));
  public static final DeferredBlock<Block> DETECTOR_ITEM = BLOCKS.register("detector_item", () -> new BlockDetectorItem(Block.Properties.of()));
  public static final DeferredBlock<Block> DETECTOR_ENTITY = BLOCKS.register("detector_entity", () -> new BlockDetector(Block.Properties.of()));
  public static final DeferredBlock<Block> ENERGY_PIPE = BLOCKS.register("energy_pipe", () -> new BlockCableEnergy(Block.Properties.of().sound(SoundType.STONE).forceSolidOn()));
  public static final DeferredBlock<Block> ITEM_PIPE = BLOCKS.register("item_pipe", () -> new BlockCableItem(Block.Properties.of().sound(SoundType.STONE).forceSolidOn()));
  public static final DeferredBlock<Block> FLUID_PIPE = BLOCKS.register("fluid_pipe", () -> new BlockCableFluid(Block.Properties.of().sound(SoundType.STONE).forceSolidOn()));
  public static final DeferredBlock<Block> PLATE_LAUNCH = BLOCKS.register("plate_launch", () -> new LaunchBlock(Block.Properties.of(), false));
  public static final DeferredBlock<Block> PLATE_LAUNCH_REDSTONE = BLOCKS.register("plate_launch_redstone", () -> new LaunchBlock(Block.Properties.of(), true));
  public static final DeferredBlock<Block> BATTERY_INFINITE = BLOCKS.register("battery_infinite", () -> new BlockBatteryInfinite(Block.Properties.of()));
  public static final DeferredBlock<Block> ITEM_INFINITE = BLOCKS.register("item_infinite", () -> new BlockItemInfinite(Block.Properties.of()));
  public static final DeferredBlock<Block> FIREPLACE = BLOCKS.register("fireplace", () -> new FireplaceBlock(Block.Properties.of()));
  public static final DeferredBlock<Block> UNBREAKABLE_BLOCK = BLOCKS.register("unbreakable_block", () -> new UnbreakableBlock(Block.Properties.of())); //stable, only changes with player interaction
  public static final DeferredBlock<Block> UNBREAKABLE_REACTIVE = BLOCKS.register("unbreakable_reactive", () -> new UnbreakablePoweredBlock(Block.Properties.of())); //reactive and unstable, ignores players and reads redstone 
  public static final DeferredBlock<Block> ENDER_SHELF = BLOCKS.register("ender_shelf", () -> new BlockEnderShelf(Block.Properties.of()));
  public static final DeferredBlock<Block> ENDER_CONTROLLER = BLOCKS.register("ender_controller", () -> new BlockEnderCtrl(Block.Properties.of()));
  public static final DeferredBlock<Block> MAGNET_BLOCK = BLOCKS.register("magnet_block", () -> new BlockMagnetPanel(Block.Properties.of().forceSolidOn()));
  public static final DeferredBlock<Block> BUTTON_BASALT = BLOCKS.register("button_basalt", () -> new ButtonBlockMat(Block.Properties.of(), 30, true, 4));
  public static final DeferredBlock<Block> BUTTON_BLACKSTONE = BLOCKS.register("button_blackstone", () -> new ButtonBlockMat(Block.Properties.of(), 20, true, 8));
  public static final DeferredBlock<Block> BATTERY_CLAY = BLOCKS.register("battery_clay", () -> new ClayBattery(Block.Properties.of()));// NOGUI
  public static final DeferredBlock<Block> GENERATOR_SOLAR = BLOCKS.register("generator_solar", () -> new BlockGeneratorSolar(Block.Properties.of().forceSolidOn())); // NOGUI
  public static final DeferredBlock<Block> NO_SOLICITING = BLOCKS.register("no_soliciting", () -> new BlockAltarNoTraders(Block.Properties.of()
      .lightLevel(p -> p.getValue(BlockCyclic.LIT) ? 3 : 0).forceSolidOn()));
  public static final DeferredBlock<Block> ALTAR_DESTRUCTION = BLOCKS.register("altar_destruction", () -> new BlockDestruction(Block.Properties.of()));
  public static final DeferredBlock<Block> WAXED_REDSTONE = BLOCKS.register("waxed_redstone", () -> new BlockWaxedRedstone(Block.Properties.of())); // , MaterialColor.FIRE
  //
  //  public static final DeferredBlock<Block> PLATE_VECTOR= BLOCKS.register("plate_vector", () -> new Block(Block.Properties.of()));
  //  public static final DeferredBlock<Block> ENCHANTER = BLOCKS.register("enchanter", () -> new Block(Block.Properties.of()));
}
