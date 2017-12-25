package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockConveyor;
import com.lothrazar.cyclicmagic.block.BlockConveyor.SpeedType;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.component.magnet.BlockMagnet;
import com.lothrazar.cyclicmagic.component.magnet.TileEntityMagnet;
import com.lothrazar.cyclicmagic.component.magnetanti.BlockMagnetAnti;
import com.lothrazar.cyclicmagic.component.magnetanti.TileEntityMagnetAnti;
import com.lothrazar.cyclicmagic.component.vector.BlockVectorPlate;
import com.lothrazar.cyclicmagic.component.vector.ItemBlockVectorPlate;
import com.lothrazar.cyclicmagic.component.vector.TileEntityVector;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPlateModule extends BaseModule implements IHasConfig {
  private boolean enableConveyor;
  private boolean launchPads;
  private boolean enableMagnet;
  private boolean enableInterdict;
  private boolean vectorPlate;
  public void onPreInit() {
    if (enableInterdict) {
      BlockMagnetAnti magnet_anti_block = new BlockMagnetAnti();
      BlockRegistry.registerBlock(magnet_anti_block, "magnet_anti_block", GuideCategory.BLOCKPLATE);
      GameRegistry.registerTileEntity(TileEntityMagnetAnti.class, "magnet_anti_block_te");
    }
    if (enableMagnet) {
      BlockMagnet magnet_block = new BlockMagnet();
      BlockRegistry.registerBlock(magnet_block, "magnet_block", GuideCategory.BLOCKPLATE);
      GameRegistry.registerTileEntity(TileEntityMagnet.class, "magnet_block_te");
    }
    if (launchPads) {
      //med
      BlockLaunch plate_launch_med = new BlockLaunch(BlockLaunch.LaunchType.MEDIUM, SoundEvents.BLOCK_SLIME_FALL);
      BlockRegistry.registerBlock(plate_launch_med, "plate_launch_med", GuideCategory.BLOCKPLATE);
      //small
      BlockLaunch plate_launch_small = new BlockLaunch(BlockLaunch.LaunchType.SMALL, SoundEvents.BLOCK_SLIME_STEP);
      BlockRegistry.registerBlock(plate_launch_small, "plate_launch_small", null);
      //large
      BlockLaunch plate_launch_large = new BlockLaunch(BlockLaunch.LaunchType.LARGE, SoundEvents.BLOCK_SLIME_BREAK);
      BlockRegistry.registerBlock(plate_launch_large, "plate_launch_large", null);
    }
    if (enableConveyor) {
      BlockConveyor plate_push = new BlockConveyor(SpeedType.MEDIUM);
      BlockRegistry.registerBlock(plate_push, "plate_push", GuideCategory.BLOCKPLATE);
      //other speeds
      BlockConveyor plate_push_fast = new BlockConveyor(SpeedType.LARGE);
      BlockRegistry.registerBlock(plate_push_fast, "plate_push_fast", null);
      BlockConveyor plate_push_slow = new BlockConveyor(SpeedType.SMALL);
      BlockRegistry.registerBlock(plate_push_slow, "plate_push_slow", null);
      BlockConveyor plate_push_slowest = new BlockConveyor(SpeedType.TINY);
      BlockRegistry.registerBlock(plate_push_slowest, "plate_push_slowest", null);
    }
    if (vectorPlate) {
      BlockVectorPlate plate_vector = new BlockVectorPlate();
      BlockRegistry.registerBlock(plate_vector, new ItemBlockVectorPlate(plate_vector), "plate_vector", null);
      GuideRegistry.register(GuideCategory.BLOCKPLATE, plate_vector);
      GameRegistry.registerTileEntity(TileEntityVector.class, "plate_vector_te");
      ModCyclic.instance.events.register(plate_vector);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    vectorPlate = config.getBoolean("AerialFaithPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInterdict = config.getBoolean("InterdictionPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMagnet = config.getBoolean("MagnetBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchPads = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockConveyor.keepEntityGrounded = config.getBoolean("SlimeConveyorKeepEntityGrounded", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will keep entities grounded so they dont get sudden bursts of speed when falling down a block onto a conveyor on a lower level");
    BlockConveyor.doCorrections = config.getBoolean("SlimeConveyorPullCenter", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will auto-correct entities towards the center while they are moving (keeping them away from the edge)");
    BlockConveyor.sneakPlayerAvoid = config.getBoolean("SlimeConveyorSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being pushed");
    BlockLaunch.sneakPlayerAvoid = config.getBoolean("SlimePlateSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being jumped");
  }
}
