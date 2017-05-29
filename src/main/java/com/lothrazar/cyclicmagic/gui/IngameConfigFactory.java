package com.lothrazar.cyclicmagic.gui;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class IngameConfigFactory implements IModGuiFactory {
  @Override
  public void initialize(Minecraft mc) {}
  @Override
  public Class<? extends GuiScreen> mainConfigGuiClass() {
    return IngameConfigGui.class;// the only line we need to add
  }
  @Override
  public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    return null;
  }
  // remove in 1.11 - this was never fully implemented and will be removed
  //but i cant remove it NOW, the interface requires it here. but its safe to ignore this warning
  @Override
  public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
    return null;
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
