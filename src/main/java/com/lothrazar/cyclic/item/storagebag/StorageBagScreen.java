package com.lothrazar.cyclic.item.storagebag;

import com.lothrazar.cyclic.base.ScreenBase;
import com.lothrazar.cyclic.net.PacketItemStackNBT;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class StorageBagScreen extends ScreenBase<StorageBagContainer> {

  private static final int BUTTON_OFFSET_Y = 10;
  private static final int BUTTON_GAP = 28;

  public StorageBagScreen(StorageBagContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
    super(screenContainer, inv, titleIn);
    this.ySize = 256;
  }

  @Override
  protected void init() {
    super.init();
    CompoundNBT nbt = this.container.bag.getOrCreateTag();
    ToggleButton pickup = new ToggleButton(guiLeft - 18, guiTop + BUTTON_OFFSET_Y,
        nbt, StringNBT.valueOf("pickup_mode"), StringNBT.valueOf("nothing"),
        new TranslationTextComponent("item.cyclic.storage_bag.disabled.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.pickup",
            new TranslationTextComponent("item.cyclic.storage_bag.disabled")));
    pickup.addState(
        new TranslationTextComponent("item.cyclic.storage_bag.pickup.everything.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.pickup",
            new TranslationTextComponent("item.cyclic.storage_bag.pickup.everything")),
        StringNBT.valueOf("everything"));
    pickup.addState(
        new TranslationTextComponent("item.cyclic.storage_bag.pickup.filter.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.pickup",
            new TranslationTextComponent("item.cyclic.storage_bag.pickup.filter")),
        StringNBT.valueOf("filter"));
    ToggleButton dump = new ToggleButton(guiLeft - 18, guiTop + BUTTON_OFFSET_Y + BUTTON_GAP,
        nbt, StringNBT.valueOf("deposit_mode"), StringNBT.valueOf("nothing"),
        new TranslationTextComponent("item.cyclic.storage_bag.disabled.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.deposit",
            new TranslationTextComponent("item.cyclic.storage_bag.disabled")));
    dump.addState(
        new TranslationTextComponent("item.cyclic.storage_bag.deposit.dump.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.deposit",
            new TranslationTextComponent("item.cyclic.storage_bag.deposit.dump")),
        StringNBT.valueOf("dump"));
    dump.addState(
        new TranslationTextComponent("item.cyclic.storage_bag.deposit.merge.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.deposit",
            new TranslationTextComponent("item.cyclic.storage_bag.deposit.merge")),
        StringNBT.valueOf("merge"));
    ToggleButton refill = new ToggleButton(guiLeft - 18, guiTop + BUTTON_OFFSET_Y + BUTTON_GAP * 2,
        nbt, StringNBT.valueOf("refill_mode"), StringNBT.valueOf("nothing"),
        new TranslationTextComponent("item.cyclic.storage_bag.disabled.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.refill",
            new TranslationTextComponent("item.cyclic.storage_bag.disabled")));
    refill.addState(
        new TranslationTextComponent("item.cyclic.storage_bag.refill.hotbar.button"),
        new TranslationTextComponent("item.cyclic.storage_bag.tooltip.refill",
            new TranslationTextComponent("item.cyclic.storage_bag.refill.hotbar")),
        StringNBT.valueOf("hotbar"));
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
  protected void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {}

  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE);
    this.minecraft.getTextureManager().bindTexture(TextureRegistry.INVENTORY_SIDEBAR);
    Screen.blit(ms, this.guiLeft - 24, this.guiTop, 0, 0, 27, 101, 27, 101);
    //this.container.bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
    //  for (int i = 0; i < h.getSlots(); i++) {
    //    int row = (int) i / 9;
    //    int col = i % 9;
    //    int xPos = 7 + col * Const.SQ;
    //    int yPos = 18 + row * Const.SQ;
    //
    //    this.drawSlot(ms, xPos, yPos);
    //  }
    //});
  }

  private class ToggleButton extends Button {
    List<ITextComponent> titles;
    List<ITextComponent> tooltips;
    List<INBT> nbtValues;
    StringNBT nbtKey;
    CompoundNBT nbt;
    int index;

    public ToggleButton(int x, int y, CompoundNBT nbt, StringNBT key, INBT defaultValue, ITextComponent defaultTitle, ITextComponent defaultTooltip) {
      super(x, y, 0, 20, defaultTitle, (p -> {
        //do nothing
      }), Button::renderToolTip);
      this.width = StorageBagScreen.this.font.getStringWidth(defaultTitle.getString()) + 8;
      index = 0;
      titles = new LinkedList<>();
      tooltips = new LinkedList<>();
      nbtValues = new LinkedList<>();
      this.nbt = nbt;
      this.nbtKey = key;
      titles.add(defaultTitle);
      tooltips.add(defaultTooltip);
      nbtValues.add(defaultValue);
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
      StorageBagScreen.this.renderTooltip(matrixStack, tooltips.get(index), mouseX, mouseY);
    }

    @Override
    public void onPress() {
      super.onPress();
      if (++index >= nbtValues.size()) {
        index = 0;
      }
      this.setMessage(titles.get(index));
      PacketRegistry.INSTANCE.sendToServer(new PacketItemStackNBT(
          StorageBagScreen.this.container.bag, StorageBagScreen.this.container.slot, nbtValues.get(index).getId(), nbtKey, nbtValues.get(index)));
    }

    public void addState(ITextComponent title, ITextComponent tooltip, INBT nbtValue) {
      this.titles.add(title);
      this.tooltips.add(tooltip);
      this.nbtValues.add(nbtValue);
      if (this.nbt.get(nbtKey.getString()) != null &&
          this.nbt.get(nbtKey.getString()).equals(nbtValue)) {
        this.index = this.nbtValues.indexOf(nbtValue);
        this.setMessage(this.titles.get(index));
      }
    }
  }
}
