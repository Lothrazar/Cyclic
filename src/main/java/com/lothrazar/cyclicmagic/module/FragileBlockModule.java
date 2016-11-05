package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockFishing;
import com.lothrazar.cyclicmagic.block.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.item.itemblock.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FragileBlockModule extends BaseModule {
  private boolean fragileEnabled;
  private boolean fishingBlock;
  public void onInit() {
    if (fragileEnabled) {
      BlockRegistry.block_fragile = new BlockScaffolding();
      BlockRegistry.registerBlock(BlockRegistry.block_fragile, new ItemBlockScaffolding(BlockRegistry.block_fragile), BlockScaffolding.name);
      BlockRegistry.block_fragile.addRecipe();
    }
    if (fishingBlock) {
      BlockRegistry.block_fishing = new BlockFishing();
      BlockRegistry.registerBlock(BlockRegistry.block_fishing, "block_fishing");
      BlockRegistry.block_fishing.addRecipe();
      GameRegistry.registerTileEntity(TileEntityFishing.class, Const.MODID + "block_fishing_te");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fishingBlock = config.getBoolean("FishingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityFishing.SPEED = config.getFloat("AutoFisherSpeed", Const.ConfigCategory.modpackMisc, 0.07F, 0.01F, 0.99F, "Speed of the Auto fisher, bigger is faster.  0.07 is 7% chance.");
  }
}
