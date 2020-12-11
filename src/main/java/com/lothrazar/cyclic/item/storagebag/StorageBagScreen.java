package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.LinkedList;
import java.util.List;

public class StorageBagScreen extends ScreenBase<StorageBagContainer> {

  private static final int BUTTON_OFFSET_Y = 10;
  private static final int BUTTON_GAP = 28;

  public StorageBagScreen(StorageBagContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
  }

  @Override
  protected void init() {
    super.init();

    ToggleButton pickup = new ToggleButton(guiLeft - 18, guiTop + BUTTON_OFFSET_Y,
            new TranslationTextComponent("item.cyclic.storage_bag.pickup.nothing.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.pickup.nothing"), (p) -> {
      System.out.println("P1");
    });
    pickup.addState(new TranslationTextComponent("item.cyclic.storage_bag.pickup.everything.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.pickup.everything"), (p) -> {
      System.out.println("P2");
    });
    pickup.addState(new TranslationTextComponent("item.cyclic.storage_bag.pickup.filter.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.pickup.filter"), (p) -> {
              System.out.println("P3");
    });

    ToggleButton dump = new ToggleButton(guiLeft - 18, guiTop + BUTTON_OFFSET_Y + BUTTON_GAP,
            new TranslationTextComponent("item.cyclic.storage_bag.deposit.nothing.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.deposit.nothing"), (p) -> {
      System.out.println("P1");
    });
    dump.addState(new TranslationTextComponent("item.cyclic.storage_bag.deposit.dump.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.deposit.dump"), (p) -> {
              System.out.println("P2");
            });
    dump.addState(new TranslationTextComponent("item.cyclic.storage_bag.deposit.merge.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.deposit.merge"), (p) -> {
              System.out.println("P3");
            });

    ToggleButton refill = new ToggleButton(guiLeft - 18, guiTop + BUTTON_OFFSET_Y + BUTTON_GAP * 2,
            new TranslationTextComponent("item.cyclic.storage_bag.refill.nothing.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.refill.nothing"), (p) -> {
      System.out.println("P1");
    });
    refill.addState(new TranslationTextComponent("item.cyclic.storage_bag.refill.hotbar.button"),
            new TranslationTextComponent("item.cyclic.storage_bag.refill.hotbar"), (p) -> {
              System.out.println("P2");
            });

    this.addButton(pickup);
    this.addButton(dump);
    this.addButton(refill);
  }

  @Override
  public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {
    drawString(ms, this.container.bag.getTranslationKey(),
            (this.getXSize() - this.font.getStringWidth(this.container.bag.getTranslationKey())) / 2,
            6.0F);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY);
    this.minecraft.getTextureManager().bindTexture(TextureRegistry.INVENTORY_SIDEBAR);
    Screen.blit(ms, this.guiLeft - 24, this.guiTop, 0, 0, 27, 101, 27, 101);

    this.container.bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
      for (int i = 0; i < h.getSlots(); i++) {
        int row = (int) i / 9;
        int col = i % 9;
        int xPos = 7 + col * Const.SQ;
        int yPos = 18 + row * Const.SQ;

        this.drawSlot(ms, xPos, yPos);
      }
    });
  }

  private class ToggleButton extends Button {

    List<IPressable> pressables;
    List<ITextComponent> titles;
    List<ITextComponent> tooltips;
    int index;

    public ToggleButton(int x, int y, ITextComponent defaultTitle, ITextComponent defaultTooltip, IPressable defaultPressable) {
      super(x, y, 0, 20, defaultTitle, (p -> {}), Button::renderToolTip);
      this.width = StorageBagScreen.this.font.getStringWidth(defaultTitle.getString()) + 8;
      index = 0;
      titles = new LinkedList<>();
      tooltips = new LinkedList<>();
      pressables = new LinkedList<>();
      titles.add(defaultTitle);
      tooltips.add(defaultTooltip);
      pressables.add(defaultPressable);
    }


    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
      super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
      StorageBagScreen.this.renderTooltip(matrixStack, tooltips.get(index), mouseX, mouseY);
    }

    @Override
    public void onPress() {
      super.onPress();
      this.pressables.get(index).onPress(this);
      if (pressables.size() == 0)
        return;
      if (++index >= pressables.size())
        index = 0;
      this.setMessage(titles.get(index));
    }

    public void addState(ITextComponent title, ITextComponent tooltip, IPressable pressable) {
      this.titles.add(title);
      this.tooltips.add(tooltip);
      this.pressables.add(pressable);
    }
  }
}
