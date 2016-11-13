package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.registry.ConfigRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class IngameConfigGui extends GuiConfig {
  public IngameConfigGui(GuiScreen parent) {
    super(parent, new ConfigElement(ConfigRegistry.getConfig().getCategory(Const.MODID)).getChildElements(), Const.MODID,
        false, false, "Cyclic");
  }
  @Override
  public void initGui() {
    super.initGui();
  }
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
  }
  @Override
  protected void actionPerformed(GuiButton button) {
    super.actionPerformed(button);
  }
}
