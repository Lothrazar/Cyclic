package com.lothrazar.cyclic.item.datacard.fluid;

import com.lothrazar.cyclic.gui.ButtonTextured;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.gui.TextureEnum;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.core.Const;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ScreenFluidFilterCard extends ScreenBase<ContainerFluidFilterCard> {

  private ButtonTextured btnType;
  private ButtonTextured btnTagMatch;
  private final ContainerFluidFilterCard screenContainer;

  public ScreenFluidFilterCard(ContainerFluidFilterCard screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.screenContainer = screenContainer;
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    super.extractRenderState(ms, mouseX, mouseY, partialTicks);
  }

  @Override
  public void init() {
    super.init();
    int x = leftPos + 150;
    int y = topPos + 8;
    int size = 20;
    btnType = this.addRenderableWidget(new ButtonTextured(x, y, size, size, TextureEnum.RENDER_HIDE, "", b -> {
      ClientPacketDistributor.sendToServer(new PacketFluidFilterCard(PacketFluidFilterCard.TOGGLE_IGNORE));
      FluidFilterCardItem.toggleFilterType(screenContainer.bag);
    }));
    size=14;
    btnTagMatch = this.addRenderableWidget(new ButtonTextured(x+3, topPos + 51, size, size, TextureEnum.CRAFT_EMPTY, "", b -> {
      ClientPacketDistributor.sendToServer(new PacketFluidFilterCard(PacketFluidFilterCard.TOGGLE_TAGMATCH));
      FluidFilterCardItem.toggleTagMatch(screenContainer.bag);
    }));
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor ms, int mouseX, int mouseY) {
    super.extractLabels(ms, mouseX, mouseY);
    this.drawButtonTooltips(ms, mouseX, mouseY);
    boolean filter = screenContainer.bag != null && FluidFilterCardItem.getIsIgnoreList(screenContainer.bag);
    btnType.setTextureId(filter ? TextureEnum.RENDER_HIDE : TextureEnum.RENDER_SHOW);
    btnType.setTooltip("cyclic.screen.filter." + filter);
    boolean tagMatch = screenContainer.bag != null && FluidFilterCardItem.getIsTagMatch(screenContainer.bag);
    btnTagMatch.setTextureId(tagMatch ? TextureEnum.CRAFT_MATCH : TextureEnum.CRAFT_EMPTY);
    btnTagMatch.setTooltip("cyclic.screen.filter.tagmatch." + tagMatch);
  }

  @Override
  public void extractBackground(GuiGraphicsExtractor ms, int mouseX, int mouseY, float partialTicks) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    for (int i = 0; i < 9; i++) {
      this.drawSlot(ms, 7 + i * Const.SQ, 31);
    }
  }
}
