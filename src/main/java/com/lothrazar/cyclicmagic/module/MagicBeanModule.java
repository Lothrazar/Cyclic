package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockSprout;
import com.lothrazar.cyclicmagic.item.ItemSproutSeeds;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MagicBeanModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    BlockSprout sprout = new BlockSprout();
    BlockRegistry.registerBlock(sprout, "sprout", true);
    ItemRegistry.sprout_seed = new ItemSproutSeeds(sprout, Blocks.FARMLAND);
    ItemRegistry.sprout_seed.setUnlocalizedName("sprout_seed");
    ItemRegistry.registerItem(ItemRegistry.sprout_seed, "sprout_seed");
    ItemRegistry.itemMap.put("sprout_seed", ItemRegistry.sprout_seed);
    GameRegistry.addRecipe(new ItemStack(ItemRegistry.sprout_seed),
        "waw",
        "bEc",
        "wdw",
        'w', Items.WHEAT_SEEDS,
        'E', Items.EMERALD,
        'a', Items.BEETROOT_SEEDS,
        'b', Items.MELON_SEEDS,
        'c', Items.PUMPKIN_SEEDS,
        'd', Items.NETHER_WART);
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("MagicBean", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
