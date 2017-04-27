package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.autouser.BlockUser;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser;
import com.lothrazar.cyclicmagic.component.builder.BlockStructureBuilder;
import com.lothrazar.cyclicmagic.component.builder.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.component.controlledminer.BlockMinerSmart;
import com.lothrazar.cyclicmagic.component.controlledminer.TileEntityControlledMiner;
import com.lothrazar.cyclicmagic.component.harvester.BlockHarvester;
import com.lothrazar.cyclicmagic.component.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.component.miner.BlockMiner;
import com.lothrazar.cyclicmagic.component.miner.TileEntityBlockMiner;
import com.lothrazar.cyclicmagic.component.password.BlockPassword;
import com.lothrazar.cyclicmagic.component.password.TileEntityPassword;
import com.lothrazar.cyclicmagic.component.pattern.BlockPatternBuilder;
import com.lothrazar.cyclicmagic.component.pattern.TileEntityPatternBuilder;
import com.lothrazar.cyclicmagic.component.placer.BlockPlacer;
import com.lothrazar.cyclicmagic.component.placer.TileEntityPlacer;
import com.lothrazar.cyclicmagic.component.pylonexp.BlockXpPylon;
import com.lothrazar.cyclicmagic.component.pylonexp.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.component.uncrafter.BlockUncrafting;
import com.lothrazar.cyclicmagic.component.uncrafter.TileEntityUncrafter;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ConfigRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMachineModule extends BaseModule implements IHasConfig {
  private boolean enableUncrafter;
  private boolean enableBuilderBlock;
  private boolean enableHarvester;
  private boolean enableMiner;
  private boolean enableMinerEnhanced;
  private boolean enablePlacer;
  private boolean enablePassword;
  private boolean enableMinerSmart;
  private boolean enableUser;
  private boolean enablePattern;
  private boolean expPylon;
  public void onPreInit(){// onInit() {
    if (expPylon) {
      BlockXpPylon exp_pylon = new BlockXpPylon();
      BlockRegistry.registerBlock(exp_pylon, "exp_pylon",GuideCategory.BLOCK);
    }
    GameRegistry.registerTileEntity(TileEntityXpPylon.class, "exp_pylon_te");
    if (enablePattern) {
      BlockPatternBuilder builder_pattern = new BlockPatternBuilder();
      BlockRegistry.registerBlock(builder_pattern, "builder_pattern",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityPatternBuilder.class, "builder_pattern_te");
    }
    if (enableBuilderBlock) {
      BlockStructureBuilder builder_block = new BlockStructureBuilder();
      BlockRegistry.registerBlock(builder_block, "builder_block",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityStructureBuilder.class, "builder_te");
    }
    if (enableHarvester) {
      BlockHarvester harvester_block = new BlockHarvester();
      BlockRegistry.registerBlock(harvester_block, "harvester_block",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityHarvester.class, "harveseter_te");
      ConfigRegistry.register(harvester_block);
    }
    if (enableUncrafter) {
      BlockUncrafting uncrafting_block = new BlockUncrafting();
      BlockRegistry.registerBlock(uncrafting_block, "uncrafting_block",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityUncrafter.class, "uncrafting_block_te");
    }
    if (enableMiner) {
      BlockMiner miner_block = new BlockMiner(BlockMiner.MinerType.SINGLE);
      BlockRegistry.registerBlock(miner_block, "block_miner",GuideCategory.BLOCK);
    }
    if (enableMinerEnhanced) {
      BlockMiner block_miner_tunnel = new BlockMiner(BlockMiner.MinerType.TUNNEL);
      BlockRegistry.registerBlock(block_miner_tunnel, "block_miner_tunnel",GuideCategory.BLOCK);
    }
    if (enableMiner || enableMinerEnhanced) {
      GameRegistry.registerTileEntity(TileEntityBlockMiner.class, "miner_te");
    }
    if (enableMinerSmart) {
      BlockMinerSmart block_miner_smart = new BlockMinerSmart();
      BlockRegistry.registerBlock(block_miner_smart, "block_miner_smart",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityControlledMiner.class, Const.MODID + "miner_smart_te");
    }
    if (enablePlacer) {
      BlockPlacer placer_block = new BlockPlacer();
      BlockRegistry.registerBlock(placer_block, "placer_block",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityPlacer.class, "placer_block_te");
    }
    if (enablePassword) {
      BlockPassword password_block = new BlockPassword();
      BlockRegistry.registerBlock(password_block, "password_block",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityPassword.class, "password_block_te");
      ModCyclic.instance.events.register(password_block);
    }
    if (enableUser) {
      BlockUser block_user = new BlockUser();
      BlockRegistry.registerBlock(block_user, "block_user",GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityUser.class, Const.MODID + "block_user_te");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    expPylon = config.getBoolean("ExperiencePylon", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePattern = config.getBoolean("PatternReplicator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableUser = config.getBoolean("AutomatedUser", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePassword = config.getBoolean("PasswordTrigger", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePlacer = config.getBoolean("BlockPlacer", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMiner = config.getBoolean("MinerBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText + ".  This is the one that mines a single block");
    enableMinerEnhanced = config.getBoolean("MinerBlockAdvanced", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText + ".  This is the one that mines a 3x3x3 area");
    enableBuilderBlock = config.getBoolean("BuilderBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityStructureBuilder.maxSize = config.getInt("builder.maxRange", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum range of the builder block that you can increase it to in the GUI");
    TileEntityStructureBuilder.maxHeight = config.getInt("builder.maxHeight", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum height of the builder block that you can increase it to in the GUI");
    enableHarvester = config.getBoolean("HarvesterBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableUncrafter = config.getBoolean("UncraftingGrinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMinerSmart = config.getBoolean("ControlledMiner", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityControlledMiner.maxHeight = config.getInt("ControlledMiner.maxHeight", Const.ConfigCategory.modpackMisc, 32, 3, 128, "Maximum height of the controlled miner block that you can increase it to in the GUI");
  }
}
