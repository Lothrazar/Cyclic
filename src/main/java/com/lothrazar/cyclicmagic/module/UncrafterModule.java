package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockUncrafting;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class UncrafterModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    BlockRegistry.uncrafting_block = new BlockUncrafting();
    BlockRegistry.registerBlock(BlockRegistry.uncrafting_block, "uncrafting_block");
    BlockRegistry.uncrafting_block.addRecipe();
    GameRegistry.registerTileEntity(TileEntityUncrafting.class, "uncrafting_block_te");
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("UncraftingGrinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockRegistry.uncrafting_block.syncConfig(config);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
