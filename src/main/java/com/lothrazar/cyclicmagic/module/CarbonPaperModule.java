package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class CarbonPaperModule extends BaseModule {
  private boolean enableCarbonPaper;
  public void onInit() {
    if(enableCarbonPaper){
      ItemRegistry.carbon_paper = new ItemPaperCarbon();
      ItemRegistry.addItem(ItemRegistry.carbon_paper, "carbon_paper");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableCarbonPaper = config.getBoolean("CarbonPaper", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
