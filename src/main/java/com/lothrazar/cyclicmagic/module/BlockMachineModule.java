package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockSpikesRetractable;
import com.lothrazar.cyclicmagic.component.autouser.BlockUser;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser;
import com.lothrazar.cyclicmagic.component.beacon.BlockBeaconPowered;
import com.lothrazar.cyclicmagic.component.beacon.TileEntityBeaconPowered;
import com.lothrazar.cyclicmagic.component.beaconpotion.BlockBeaconPotion;
import com.lothrazar.cyclicmagic.component.beaconpotion.TileEntityBeaconPotion;
import com.lothrazar.cyclicmagic.component.builder.BlockStructureBuilder;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.component.clock.BlockRedstoneClock;
import com.lothrazar.cyclicmagic.component.clock.TileEntityClock;
import com.lothrazar.cyclicmagic.component.controlledminer.BlockMinerSmart;
import com.lothrazar.cyclicmagic.component.controlledminer.TileEntityControlledMiner;
import com.lothrazar.cyclicmagic.component.harvester.BlockHarvester;
import com.lothrazar.cyclicmagic.component.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.component.hydrator.BlockHydrator;
import com.lothrazar.cyclicmagic.component.hydrator.ItemBlockHydrator;
import com.lothrazar.cyclicmagic.component.hydrator.TileEntityHydrator;
import com.lothrazar.cyclicmagic.component.miner.BlockMiner;
import com.lothrazar.cyclicmagic.component.miner.TileEntityBlockMiner;
import com.lothrazar.cyclicmagic.component.password.BlockPassword;
import com.lothrazar.cyclicmagic.component.password.TileEntityPassword;
import com.lothrazar.cyclicmagic.component.pattern.BlockPatternBuilder;
import com.lothrazar.cyclicmagic.component.pattern.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.component.placer.BlockPlacer;
import com.lothrazar.cyclicmagic.component.placer.TileEntityPlacer;
import com.lothrazar.cyclicmagic.component.pylonexp.BlockXpPylon;
import com.lothrazar.cyclicmagic.component.pylonexp.ItemBlockPylon;
import com.lothrazar.cyclicmagic.component.pylonexp.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.component.sprinkler.BlockSprinkler;
import com.lothrazar.cyclicmagic.component.sprinkler.TileSprinkler;
import com.lothrazar.cyclicmagic.component.uncrafter.BlockUncrafting;
import com.lothrazar.cyclicmagic.component.uncrafter.TileEntityUncrafter;
import com.lothrazar.cyclicmagic.component.vacuum.BlockVacuum;
import com.lothrazar.cyclicmagic.component.vacuum.TileEntityVacuum;
import com.lothrazar.cyclicmagic.component.wandblaze.BlockFireSafe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.FluidsRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMachineModule extends BaseModule implements IHasConfig {
  private boolean enableUncrafter;
  private boolean enableBuilderBlock;
  private boolean enableHarvester;
  private boolean enableMiner;
  private boolean enablePlacer;
  private boolean enablePassword;
  private boolean enableMinerSmart;
  private boolean enableUser;
  private boolean enablePattern;
  private boolean expPylon;
  private boolean enableVacuum;
  private boolean enableHydrator;
  private boolean enableClock;
  private boolean enableSprinkler;
  private boolean enableSpikes;
  private boolean emptyBeacon;
  public void onPreInit() {
    BlockFireSafe fire = new BlockFireSafe();
    BlockRegistry.registerBlock(fire, "fire_dark", null);
    
    
    
    BlockBeaconPotion beacon_potion = new BlockBeaconPotion();
    BlockRegistry.registerBlock(beacon_potion, "beacon_potion", null);
    GameRegistry.registerTileEntity(TileEntityBeaconPotion.class, "beacon_potion_te");
    
    if (emptyBeacon) {
      BlockBeaconPowered beacon_redstone = new BlockBeaconPowered();
      BlockRegistry.registerBlock(beacon_redstone, "beacon_redstone", null);
      GameRegistry.registerTileEntity(TileEntityBeaconPowered.class, "beacon_redstone_te");
    }
    if (enableClock) {
      BlockRedstoneClock clock = new BlockRedstoneClock();
      BlockRegistry.registerBlock(clock, "clock", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityClock.class, "clock_te");
    }
    if (enableSprinkler) {
      BlockSprinkler sprinkler = new BlockSprinkler();
      BlockRegistry.registerBlock(sprinkler, "sprinkler", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileSprinkler.class, "sprinkler_te");
    }
    if (enableSpikes) {
      BlockSpikesRetractable spikes_iron = new BlockSpikesRetractable(false);
      BlockRegistry.registerBlock(spikes_iron, "spikes_iron", GuideCategory.BLOCK);
      BlockSpikesRetractable spikes_redstone_diamond = new BlockSpikesRetractable(true);
      BlockRegistry.registerBlock(spikes_redstone_diamond, "spikes_diamond", GuideCategory.BLOCK);
    }
    if (enableVacuum) {
      BlockVacuum vacuum_block = new BlockVacuum();
      BlockRegistry.registerBlock(vacuum_block, "block_vacuum", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityVacuum.class, "vacuum_block_te");
    }
    if (enableHydrator) {
      BlockHydrator block_hydrator = new BlockHydrator();
      BlockRegistry.registerBlock(block_hydrator, new ItemBlockHydrator(block_hydrator), "block_hydrator", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityHydrator.class, "block_hydrator_te");
    }
    if (expPylon) {
      FluidsRegistry.registerExp();//it needs EXP fluid to work
      BlockXpPylon exp_pylon = new BlockXpPylon();
      BlockRegistry.registerBlock(exp_pylon, new ItemBlockPylon(exp_pylon), "exp_pylon", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityXpPylon.class, "exp_pylon_te");
    }
    if (enablePattern) {
      BlockPatternBuilder builder_pattern = new BlockPatternBuilder();
      BlockRegistry.registerBlock(builder_pattern, "builder_pattern", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPatternBuilder.class, "builder_pattern_te");
    }
    if (enableBuilderBlock) {
      BlockStructureBuilder builder_block = new BlockStructureBuilder();
      BlockRegistry.registerBlock(builder_block, "builder_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityStructureBuilder.class, "builder_te");
    }
    if (enableHarvester) {
      BlockHarvester harvester_block = new BlockHarvester();
      BlockRegistry.registerBlock(harvester_block, "harvester_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityHarvester.class, "harveseter_te");
    }
    if (enableUncrafter) {
      BlockUncrafting uncrafting_block = new BlockUncrafting();
      BlockRegistry.registerBlock(uncrafting_block, "uncrafting_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityUncrafter.class, "uncrafting_block_te");
    }
    if (enableMiner) {
      BlockMiner miner_block = new BlockMiner();
      BlockRegistry.registerBlock(miner_block, "block_miner", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityBlockMiner.class, "miner_te");
    }
    if (enableMinerSmart) {
      BlockMinerSmart block_miner_smart = new BlockMinerSmart();
      BlockRegistry.registerBlock(block_miner_smart, "block_miner_smart", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityControlledMiner.class, Const.MODID + "miner_smart_te");
    }
    if (enablePlacer) {
      BlockPlacer placer_block = new BlockPlacer();
      BlockRegistry.registerBlock(placer_block, "placer_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPlacer.class, "placer_block_te");
    }
    if (enablePassword) {
      BlockPassword password_block = new BlockPassword();
      BlockRegistry.registerBlock(password_block, "password_block", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPassword.class, "password_block_te");
      ModCyclic.instance.events.register(password_block);
    }
    if (enableUser) {
      BlockUser block_user = new BlockUser();
      BlockRegistry.registerBlock(block_user, "block_user", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityUser.class, Const.MODID + "block_user_te");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    emptyBeacon = config.getBoolean("EmptyBeacon", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableClock = config.getBoolean("Clock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSprinkler = config.getBoolean("Sprinkler", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpikes = config.getBoolean("Spikes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHydrator = config.getBoolean("Hydrator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVacuum = config.getBoolean("ItemCollector", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    expPylon = config.getBoolean("ExperiencePylon", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePattern = config.getBoolean("PatternReplicator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableUser = config.getBoolean("AutomatedUser", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePassword = config.getBoolean("PasswordTrigger", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePlacer = config.getBoolean("BlockPlacer", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMiner = config.getBoolean("MinerBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableBuilderBlock = config.getBoolean("BuilderBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityStructureBuilder.maxSize = config.getInt("builder.maxRange", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum range of the builder block that you can increase it to in the GUI");
    TileEntityStructureBuilder.maxHeight = config.getInt("builder.maxHeight", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum height of the builder block that you can increase it to in the GUI");
    enableHarvester = config.getBoolean("HarvesterBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableUncrafter = config.getBoolean("UncraftingGrinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMinerSmart = config.getBoolean("ControlledMiner", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityControlledMiner.maxHeight = config.getInt("ControlledMiner.maxHeight", Const.ConfigCategory.modpackMisc, 32, 3, 128, "Maximum height of the controlled miner block that you can increase it to in the GUI");
  }
}
