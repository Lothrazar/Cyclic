package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SlimepadModule extends BaseModule {
  private boolean moduleEnabled;
  public void onInit() {
    BlockLaunch plate_launch_small = new BlockLaunch(0.8F, SoundEvents.BLOCK_SLIME_STEP);
    BlockLaunch plate_launch_med = new BlockLaunch(1.3F, SoundEvents.BLOCK_SLIME_FALL);
    BlockLaunch plate_launch_large = new BlockLaunch(1.8F, SoundEvents.BLOCK_SLIME_BREAK);
    BlockRegistry.registerBlock(plate_launch_small, "plate_launch_small");
    BlockRegistry.registerBlock(plate_launch_med, "plate_launch_med");
    BlockRegistry.registerBlock(plate_launch_large, "plate_launch_large");
    GameRegistry.addRecipe(new ItemStack(plate_launch_small, 6),
        "sss", "ggg", "iii",
        's', Blocks.SLIME_BLOCK,
        'g', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
        'i', Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
    GameRegistry.addShapelessRecipe(new ItemStack(plate_launch_med),
        new ItemStack(plate_launch_small),
        new ItemStack(Items.QUARTZ));
    GameRegistry.addShapelessRecipe(new ItemStack(plate_launch_large),
        new ItemStack(plate_launch_med),
        new ItemStack(Blocks.END_STONE));
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
