package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.block.BlockConveyor;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.block.BlockMagnet;
import com.lothrazar.cyclicmagic.block.BlockMagnetAnti;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityMagnet;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityMagnetAnti;
import com.lothrazar.cyclicmagic.registry.AchievementRegistry;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPlateModule extends BaseModule implements IHasConfig {
  private boolean enableConveyor;
  private boolean launchPads;
  private boolean enableMagnet;
  private boolean enableInterdict;
  public void onInit() {
    if (enableInterdict) {
      BlockMagnetAnti magnet_anti_block = new BlockMagnetAnti();
      BlockRegistry.registerBlock(magnet_anti_block, "magnet_anti_block");
      GameRegistry.registerTileEntity(TileEntityMagnetAnti.class, "magnet_anti_block_te");
    }
    if (enableMagnet) {
      BlockMagnet magnet_block = new BlockMagnet();
      BlockRegistry.registerBlock(magnet_block, "magnet_block");
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
      BlockConveyor plate_push = new BlockConveyor(0.16F);
      BlockRegistry.registerBlock(plate_push, "plate_push");
      AchievementRegistry.registerItemAchievement(Item.getItemFromBlock(plate_push));
      GameRegistry.addRecipe(new ItemStack(plate_push, 8),
          "sbs",
          "bxb",
          "sbs",
          's', new ItemStack(Items.IRON_INGOT),
          'x', new ItemStack(Blocks.SLIME_BLOCK),
          'b', new ItemStack(Items.DYE, 1, EnumDyeColor.PURPLE.getDyeDamage()));
      BlockConveyor plate_push_fast = new BlockConveyor(0.32F);
      BlockRegistry.registerBlock(plate_push_fast, "plate_push_fast");
      GameRegistry.addShapelessRecipe(new ItemStack(plate_push_fast), new ItemStack(plate_push), Items.REDSTONE);
      BlockConveyor plate_push_slow = new BlockConveyor(0.08F);
      BlockRegistry.registerBlock(plate_push_slow, "plate_push_slow");
      GameRegistry.addShapelessRecipe(new ItemStack(plate_push_slow), new ItemStack(plate_push),
          new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
      BlockConveyor plate_push_slowest = new BlockConveyor(0.04F);
      BlockRegistry.registerBlock(plate_push_slowest, "plate_push_slowest");
      GameRegistry.addShapelessRecipe(new ItemStack(plate_push_slowest), new ItemStack(plate_push),
          new ItemStack(Items.DYE, 1, EnumDyeColor.LIGHT_BLUE.getDyeDamage()));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableInterdict = config.getBoolean("InterdictionPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = config.getBoolean("MagnetBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchPads = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockConveyor.keepEntityGrounded = config.getBoolean("SlimeConveyorKeepEntityGrounded", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will keep entities grounded so they dont get sudden bursts of speed when falling down a block onto a conveyor on a lower level");
    BlockConveyor.doCorrections = config.getBoolean("SlimeConveyorPullCenter", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will auto-correct entities towards the center while they are moving (keeping them away from the edge)");
  }
}
