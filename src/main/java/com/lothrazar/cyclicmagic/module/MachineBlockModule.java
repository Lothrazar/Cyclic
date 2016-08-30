package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.BlockStructureBuilder;
import com.lothrazar.cyclicmagic.block.BlockHarvester;
import com.lothrazar.cyclicmagic.block.BlockMagnet;
import com.lothrazar.cyclicmagic.block.BlockMiner;
import com.lothrazar.cyclicmagic.block.BlockMinerSmart;
import com.lothrazar.cyclicmagic.block.BlockPassword;
import com.lothrazar.cyclicmagic.block.BlockPlacer;
import com.lothrazar.cyclicmagic.block.BlockUncrafting;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineStructureBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineHarvester;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityMagnet;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMiner;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineMinerSmart;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachinePlacer;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MachineBlockModule extends BaseModule {
  private boolean enableUncrafter;
  private boolean enableBuilderBlock;
  private boolean enableHarvester;
  private boolean enableMagnet;
  private boolean enableMiner;
  private boolean enableMinerEnhanced;
  private boolean enablePlacer;
  private boolean enablePassword;
  private boolean enableMinerSmart;
  public void onInit() {
    if (enableBuilderBlock) {
      BlockRegistry.builder_block = new BlockStructureBuilder();
      BlockRegistry.registerBlock(BlockRegistry.builder_block, "builder_block");
      BlockRegistry.builder_block.addRecipe();
      GameRegistry.registerTileEntity(TileMachineStructureBuilder.class, "builder_te");
    }
    if (enableHarvester) {
      BlockRegistry.harvester_block = new BlockHarvester();
      BlockRegistry.registerBlock(BlockRegistry.harvester_block, "harvester_block");
      BlockRegistry.harvester_block.addRecipe();
      GameRegistry.registerTileEntity(TileMachineHarvester.class, "harveseter_te");
    }
    if (enableUncrafter) {
      BlockRegistry.uncrafting_block = new BlockUncrafting();
      BlockRegistry.registerBlock(BlockRegistry.uncrafting_block, "uncrafting_block");
      BlockRegistry.uncrafting_block.addRecipe();
      GameRegistry.registerTileEntity(TileMachineUncrafter.class, "uncrafting_block_te");
    }
    if (enableMagnet) {
      BlockRegistry.magnet_block = new BlockMagnet();
      BlockRegistry.registerBlock(BlockRegistry.magnet_block, "magnet_block");
      BlockRegistry.magnet_block.addRecipe();
      GameRegistry.registerTileEntity(TileEntityMagnet.class, "magnet_block_te");
    }
    if (enableMiner) {
      BlockRegistry.miner_block = new BlockMiner(BlockMiner.MinerType.SINGLE);
      BlockRegistry.registerBlock(BlockRegistry.miner_block, "block_miner");
      BlockRegistry.miner_block.addRecipe();
    }
    if (enableMinerEnhanced) {
      BlockRegistry.block_miner_tunnel = new BlockMiner(BlockMiner.MinerType.TUNNEL);
      BlockRegistry.registerBlock(BlockRegistry.block_miner_tunnel, "block_miner_tunnel");
      BlockRegistry.block_miner_tunnel.addRecipe();
    }
    if (enableMiner || enableMinerEnhanced) {
      GameRegistry.registerTileEntity(TileMachineMiner.class, "miner_te");
    }
    if (enableMinerSmart) {
      BlockRegistry.block_miner_smart = new BlockMinerSmart();
      BlockRegistry.registerBlock(BlockRegistry.block_miner_smart, "block_miner_smart");
      BlockRegistry.block_miner_smart.addRecipe();
      GameRegistry.registerTileEntity(TileMachineMinerSmart.class, Const.MODID + "miner_smart_te");
    }
    if (enablePlacer) {
      BlockRegistry.placer_block = new BlockPlacer();
      BlockRegistry.registerBlock(BlockRegistry.placer_block, "placer_block");
      BlockRegistry.placer_block.addRecipe();
      GameRegistry.registerTileEntity(TileMachinePlacer.class, "placer_block_te");
    }
    if (enablePassword) {
      BlockRegistry.password_block = new BlockPassword();
      BlockRegistry.registerBlock(BlockRegistry.password_block, "password_block");
      BlockRegistry.password_block.addRecipe();
      GameRegistry.registerTileEntity(TileEntityPassword.class, "password_block_te");
      ModMain.instance.events.addEvent(BlockRegistry.password_block);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enablePassword = config.getBoolean("PasswordTrigger", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePlacer = config.getBoolean("BlockPlacer", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMiner = config.getBoolean("MinerBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText + ".  This is the one that mines a single block");
    enableMinerEnhanced = config.getBoolean("MinerBlockAdvanced", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText + ".  This is the one that mines a 3x3x3 area");
    enableBuilderBlock = config.getBoolean("BuilderBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileMachineStructureBuilder.maxSize = config.getInt("builder.maxRange", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum range of the builder block that you can increase it to in the GUI");
    TileMachineStructureBuilder.maxHeight = config.getInt("builder.maxHeight", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum height of the builder block that you can increase it to in the GUI");
    enableHarvester = config.getBoolean("HarvesterBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = config.getBoolean("MagnetBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableUncrafter = config.getBoolean("UncraftingGrinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMinerSmart = config.getBoolean("ControlledMiner", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    //TODO: LOOP/LIST of blocks so we can hit their recipes AND configs in the loop just like items
    if (BlockRegistry.magnet_block != null) {
      BlockRegistry.magnet_block.syncConfig(config);
    }
    if (BlockRegistry.uncrafting_block != null) {
      BlockRegistry.uncrafting_block.syncConfig(config);
    }
    TileMachineHarvester.HARVEST_RADIUS = config.getInt("HarvesterBlockRadius", Const.ConfigCategory.modpackMisc, 16, 4, 128, "Maximum radius of harvester area (remember its not centered on the block, it harvests in front)");
    TileMachineMinerSmart.maxHeight = config.getInt("ControlledMiner.maxHeight", Const.ConfigCategory.modpackMisc, 32, 3, 128, "Maximum height of the controlled miner block that you can increase it to in the GUI");
  }
}
