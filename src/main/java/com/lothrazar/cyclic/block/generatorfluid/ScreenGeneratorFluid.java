package com.lothrazar.cyclic.block.generatorfluid;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.EnergyBar;
import com.lothrazar.cyclic.gui.FluidBar;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.gui.TimerBar;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class ScreenGeneratorFluid extends ScreenBase<ContainerGeneratorFluid> {

  private ButtonMachine btnToggle;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private TimerBar timer;
  private FluidBar fluid;

  public ScreenGeneratorFluid(ContainerGeneratorFluid screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.energy = new EnergyBar(this, TileGeneratorFluid.MAX);
    this.timer = new TimerBar(this, 70, 60, 1);
    fluid = new FluidBar(this, 32, 8, TileGeneratorFluid.CAPACITY);
  }

  @Override
  public void init() {
    super.init();
    energy.visible = true; //TileGeneratorFuel.POWERCONF.get() > 0;
    fluid.guiLeft = timer.guiLeft = energy.guiLeft = guiLeft;
    fluid.guiTop = timer.guiTop = energy.guiTop = guiTop;
    int x, y;
    x = guiLeft + 8;
    y = guiTop + 8;
    btnRedstone = addButton(new ButtonMachineField(x, y, TileGeneratorFluid.Fields.REDSTONE.ordinal(), container.tile.getPos()));
    x = guiLeft + 132;
    y = guiTop + 8;
    btnToggle = addButton(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      int f = TileGeneratorFluid.Fields.FLOWING.ordinal();
      int tog = (container.tile.getField(f) + 1) % 2;
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, tog, container.tile.getPos()));
    }));
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
    energy.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getEnergy());
    timer.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getField(TileGeneratorFluid.Fields.TIMER.ordinal()));
    fluid.renderHoveredToolTip(ms, mouseX, mouseY, container.tile.getFluid());
    btnRedstone.onValueUpdate(container.tile);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    //    this.drawName(ms, this.title.getString());
    int fld = TileGeneratorFluid.Fields.FLOWING.ordinal();
    btnToggle.setTooltip(UtilChat.lang("gui.cyclic.flowing" + container.tile.getField(fld)));
    btnToggle.setTextureId(container.tile.getField(fld) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    energy.draw(ms, container.tile.getEnergy());
    timer.capacity = container.tile.getField(TileGeneratorFluid.Fields.BURNMAX.ordinal());
    timer.visible = (timer.capacity > 0);
    timer.draw(ms, container.tile.getField(TileGeneratorFluid.Fields.TIMER.ordinal()));
    fluid.draw(ms, container.tile.getFluid());
  }
}
