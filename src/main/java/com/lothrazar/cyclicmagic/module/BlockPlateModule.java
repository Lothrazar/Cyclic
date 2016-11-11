package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockConveyor;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.block.BlockMagnet;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityMagnet;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPlateModule extends BaseModule {
  private boolean enableConveyor;
  private boolean launchPads;
  private boolean enableMagnet;
  public void onInit() {
    if (enableMagnet) {
      BlockRegistry.magnet_block = new BlockMagnet();
      BlockRegistry.registerBlock(BlockRegistry.magnet_block, "magnet_block");
      BlockRegistry.magnet_block.addRecipe();
      GameRegistry.registerTileEntity(TileEntityMagnet.class, "magnet_block_te");
    }
    if (launchPads) {
      BlockLaunch plate_launch_small = new BlockLaunch(0.8F, SoundEvents.BLOCK_SLIME_STEP);
      BlockRegistry.registerBlock(plate_launch_small, "plate_launch_small");
      BlockLaunch plate_launch_med = new BlockLaunch(1.3F, SoundEvents.BLOCK_SLIME_FALL);
      BlockRegistry.registerBlock(plate_launch_med, "plate_launch_med");
      BlockLaunch plate_launch_large = new BlockLaunch(1.8F, SoundEvents.BLOCK_SLIME_BREAK);
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
      AchievementRegistry.registerItemAchievement(Item.getItemFromBlock(plate_launch_large));
    }
    if (enableConveyor) {
      BlockConveyor plate_push = new BlockConveyor(0.16F, SoundEvents.BLOCK_ANVIL_BREAK);
      BlockRegistry.registerBlock(plate_push, "plate_push");
      plate_push.addRecipe();
      AchievementRegistry.registerItemAchievement(Item.getItemFromBlock(plate_push));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = config.getBoolean("MagnetBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchPads = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
