package com.lothrazar.cyclic.block.generatorfluid;

import org.joml.Vector4f;
import com.lothrazar.cyclic.gui.ButtonMachine;
import com.lothrazar.cyclic.gui.ButtonMachineField;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.gui.EnergyBar;
import com.lothrazar.library.gui.FluidBar;
import com.lothrazar.library.gui.TexturedProgress;
import com.lothrazar.library.util.ChatUtil;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ScreenGeneratorFluid extends ScreenBase<ContainerGeneratorFluid> {

  private ButtonMachine btnToggle;
  private ButtonMachineField btnRedstone;
  private EnergyBar energy;
  private TexturedProgress progress;
  private FluidBar fluid;

  public ScreenGeneratorFluid(ContainerGeneratorFluid screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  public void init() {
    super.init();
    energy = new EnergyBar(this.font, TileGeneratorFluid.MAX);
    progress = new TexturedProgress(this.font, 76, 60, TextureRegistry.LAVA_PROG);
    fluid = new FluidBar(this.font, 39, 57, TileGeneratorFluid.CAPACITY);
    energy.visible = true;
    fluid.guiLeft = progress.guiLeft = energy.guiLeft = leftPos;
    fluid.guiTop = progress.guiTop = energy.guiTop = topPos;
    int x, y;
    x = leftPos + 6;
    y = topPos + 6;
    btnRedstone = addRenderableWidget(new ButtonMachineField(x, y, TileGeneratorFluid.Fields.REDSTONE.ordinal(), menu.tile.getBlockPos()));
    x = leftPos + 132;
    y = topPos + 36;
    btnToggle = addRenderableWidget(new ButtonMachine(x, y, 14, 14, "", (p) -> {
      int f = TileGeneratorFluid.Fields.FLOWING.ordinal();
      int tog = (menu.tile.getField(f) + 1) % 2;
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(f, tog, menu.tile.getBlockPos()));
    }));
  }

  @Override
  public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(gg);
    super.render(gg, mouseX, mouseY, partialTicks);
    this.renderTooltip(gg, mouseX, mouseY);
    energy.renderHoveredToolTip(gg, mouseX, mouseY, menu.tile.getEnergy());
    progress.renderHoveredToolTip(gg, mouseX, mouseY, menu.tile.getField(TileGeneratorFluid.Fields.TIMER.ordinal()));

    btnRedstone.onValueUpdate(menu.tile);
    var pose = gg.pose();
    pose.pushPose();
    pose.translate(this.width / 2, this.height / 2, 0);
    pose.mulPose(Axis.ZP.rotationDegrees(-90));
    pose.translate(-this.width / 2, -this.height / 2, 0);
    Vector4f vec = new Vector4f(mouseX, mouseY, 0, 1);
    // 
    vec = pose.last().pose().transform(vec);
    pose.popPose(); //Look, it's a bit hacky, but it gets the job done.  Rotation Math!
    if (fluid.isMouseover((int) vec.x(), (int) vec.y())) {
      fluid.renderTooltip(gg, mouseX, mouseY, menu.tile.getFluid());
    }
  }

  @Override
  protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
    this.drawButtonTooltips(ms, mouseX, mouseY);
    this.drawName(ms, this.title.getString());
    int fld = TileGeneratorFluid.Fields.FLOWING.ordinal();
    btnToggle.setTooltip(ChatUtil.lang("gui.cyclic.flowing" + menu.tile.getField(fld)));
    btnToggle.setTextureId(menu.tile.getField(fld) == 1 ? TextureEnum.POWER_MOVING : TextureEnum.POWER_STOP);
  }

  @Override
  protected void renderBg(GuiGraphics gg, float partialTicks, int mouseX, int mouseY) {
    this.drawBackground(gg, TextureRegistry.INVENTORY);
    energy.draw(gg, menu.tile.getEnergy());
    progress.max = menu.tile.getField(TileGeneratorFluid.Fields.BURNMAX.ordinal());
    progress.draw(gg, menu.tile.getField(TileGeneratorFluid.Fields.TIMER.ordinal()));
    var pose = gg.pose();
    pose.pushPose();
    pose.translate(this.width / 2, this.height / 2, 0);
    pose.mulPose(Axis.ZP.rotationDegrees(90));
    pose.translate(-this.width / 2, -this.height / 2, 0);
    fluid.draw(gg, menu.tile.getFluid());
    pose.popPose();
  }
}
