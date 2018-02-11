package com.lothrazar.cyclicmagic.config;
import java.util.HashSet;
import java.util.Set;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class IngameConfigFactory implements IModGuiFactory {
  @Override
  public void initialize(Minecraft mc) {}
  //  @Override
  //  public Class<? extends GuiScreen> mainConfigGuiClass() {
  //    return IngameConfigGui.class;// the only line we need to add
  //  }
  @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return new HashSet() {
      {
        new RuntimeOptionCategoryElement(null, Const.MODID);
      }
    };
  }
  @Override
  public boolean hasConfigGui() {
    return true;
  }
  @Override
  public GuiScreen createConfigGui(GuiScreen parentScreen) {
    return new IngameConfigGui(parentScreen);
  }
}
