package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockConveyor;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.common.config.Configuration;

public class ConveyorPlateModule extends BaseModule {
  private boolean moduleEnabled;
  public void register() {
    BlockConveyor plate_push = new BlockConveyor(0.16F, SoundEvents.BLOCK_ANVIL_BREAK);
    BlockRegistry.registerBlock(plate_push, "plate_push");
    plate_push.addRecipe();
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("ConveyorPlate", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
