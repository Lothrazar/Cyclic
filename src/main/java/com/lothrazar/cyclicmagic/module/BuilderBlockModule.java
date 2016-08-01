package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockBuilder;
import com.lothrazar.cyclicmagic.block.BlockHarvester;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityHarvester;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BuilderBlockModule extends BaseModule {
  private boolean enableBuilderBlock;
  private boolean enableHarvester=true;
  public void onInit() {
    if(enableBuilderBlock){
      BlockRegistry.builder_block = new BlockBuilder();
      BlockRegistry.registerBlock(BlockRegistry.builder_block, "builder_block");
      BlockRegistry.builder_block.addRecipe();
      GameRegistry.registerTileEntity(TileEntityBuilder.class, "builder_te");
    }
    if(enableHarvester){
      BlockRegistry.harvester_block = new BlockHarvester();
      BlockRegistry.registerBlock(BlockRegistry.harvester_block, "harvester_block");
      BlockRegistry.harvester_block.addRecipe();
      GameRegistry.registerTileEntity(TileEntityHarvester.class, "harveseter_te");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableBuilderBlock = config.getBoolean("BuilderBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityBuilder.maxSize = config.getInt("builder.maxRange", Const.ConfigCategory.modpackMisc, 10, 3, 32, "Maximum range of the builder block that you can increase it to in the GUI");
    TileEntityBuilder.maxHeight = config.getInt("builder.maxHeight", Const.ConfigCategory.modpackMisc, 10, 3, 32, "Maximum height of the builder block that you can increase it to in the GUI");
  }
}
