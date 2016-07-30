package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class UnbreakableSpawnerModule extends BaseModule {
  private boolean spawnersUnbreakable;
  @Override
  public boolean isEnabled() {
    return true;
  }
  @Override
  public void onInit() {
    updateHardness();
  }
  private void updateHardness() {
    if (spawnersUnbreakable) {
      Blocks.MOB_SPAWNER.setBlockUnbreakable();//just like .setHardness(-1.0F);
    }
    else {
      Blocks.MOB_SPAWNER.setHardness(5.0F);//reset to normal http://minecraft.gamepedia.com/Monster_Spawner
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    spawnersUnbreakable = config.getBoolean("Spawners Unbreakable", category, true, "Make mob spawners unbreakable");
    updateHardness();
  }
}
