package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockBuilder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BuilderBlockModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    BlockRegistry.builder_block = new BlockBuilder();
    BlockRegistry.registerBlock(BlockRegistry.builder_block, "builder_block");
    BlockRegistry.builder_block.addRecipe();
    GameRegistry.registerTileEntity(TileEntityBuilder.class, "builder_te");
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("BuilderBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockRegistry.builder_block.syncConfig(config);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
