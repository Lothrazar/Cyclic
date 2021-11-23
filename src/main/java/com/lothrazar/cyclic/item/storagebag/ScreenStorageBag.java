package com.lothrazar.cyclic.item.storagebag;

import java.util.LinkedList;
import java.util.List;
import com.lothrazar.cyclic.gui.ScreenBase;
import com.lothrazar.cyclic.net.PacketStorageBagScreen;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class ScreenStorageBag extends ScreenBase<ContainerStorageBag> {

  private static final int BUTTON_OFFSET_Y = 10;
  private static final int BUTTON_GAP = 28;

  public ScreenStorageBag(ContainerStorageBag screenContainer, Inventory inv, Component titleIn) {
    super(screenContainer, inv, titleIn);
    this.imageHeight = 256;
  }

  @Override
  protected void init() {
    super.init();
    CompoundTag nbt = this.menu.bag.getOrCreateTag();
    ToggleButton pickup = new ToggleButton(leftPos - 18, topPos + BUTTON_OFFSET_Y,
        nbt, StringTag.valueOf("pickup_mode"), StringTag.valueOf("nothing"),
        new TranslatableComponent("item.cyclic.storage_bag.disabled.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.pickup",
            new TranslatableComponent("item.cyclic.storage_bag.disabled")));
    pickup.addState(
        new TranslatableComponent("item.cyclic.storage_bag.pickup.everything.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.pickup",
            new TranslatableComponent("item.cyclic.storage_bag.pickup.everything")),
        StringTag.valueOf("everything"));
    pickup.addState(
        new TranslatableComponent("item.cyclic.storage_bag.pickup.filter.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.pickup",
            new TranslatableComponent("item.cyclic.storage_bag.pickup.filter")),
        StringTag.valueOf("filter"));
    ToggleButton dump = new ToggleButton(leftPos - 18, topPos + BUTTON_OFFSET_Y + BUTTON_GAP,
        nbt, StringTag.valueOf("deposit_mode"), StringTag.valueOf("nothing"),
        new TranslatableComponent("item.cyclic.storage_bag.disabled.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.deposit",
            new TranslatableComponent("item.cyclic.storage_bag.disabled")));
    dump.addState(
        new TranslatableComponent("item.cyclic.storage_bag.deposit.dump.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.deposit",
            new TranslatableComponent("item.cyclic.storage_bag.deposit.dump")),
        StringTag.valueOf("dump"));
    dump.addState(
        new TranslatableComponent("item.cyclic.storage_bag.deposit.merge.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.deposit",
            new TranslatableComponent("item.cyclic.storage_bag.deposit.merge")),
        StringTag.valueOf("merge"));
    ToggleButton refill = new ToggleButton(leftPos - 18, topPos + BUTTON_OFFSET_Y + BUTTON_GAP * 2,
        nbt, StringTag.valueOf("refill_mode"), StringTag.valueOf("nothing"),
        new TranslatableComponent("item.cyclic.storage_bag.disabled.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.refill",
            new TranslatableComponent("item.cyclic.storage_bag.disabled")));
    refill.addState(
        new TranslatableComponent("item.cyclic.storage_bag.refill.hotbar.button"),
        new TranslatableComponent("item.cyclic.storage_bag.tooltip.refill",
            new TranslatableComponent("item.cyclic.storage_bag.refill.hotbar")),
        StringTag.valueOf("hotbar"));
    this.addRenderableWidget(pickup);
    this.addRenderableWidget(dump);
    this.addRenderableWidget(refill);
  }

  @Override
  public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(ms);
    super.render(ms, mouseX, mouseY, partialTicks);
    this.renderTooltip(ms, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack ms, int x, int y) {}

  @Override
  protected void renderBg(PoseStack ms, float partialTicks, int x, int y) {
    this.drawBackground(ms, TextureRegistry.INVENTORY_LARGE);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, TextureRegistry.INVENTORY_SIDEBAR);
    Screen.blit(ms, this.leftPos - 24, this.topPos, 0, 0, 27, 101, 27, 101);
  }

  private class ToggleButton extends Button {

    List<Component> titles;
    List<Component> tooltips;
    List<Tag> nbtValues;
    StringTag nbtKey;
    CompoundTag nbt;
    int index;

    public ToggleButton(int x, int y, CompoundTag nbt, StringTag key, Tag defaultValue, Component defaultTitle, Component defaultTooltip) {
      super(x, y, 0, 20, defaultTitle, (p -> {
        //do nothing
      }), Button::renderToolTip);
      this.width = ScreenStorageBag.this.font.width(defaultTitle.getString()) + 8;
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
    public void renderToolTip(PoseStack matrixStack, int mouseX, int mouseY) {
      ScreenStorageBag.this.renderTooltip(matrixStack, tooltips.get(index), mouseX, mouseY);
    }

    @Override
    public void onPress() {
      super.onPress();
      if (++index >= nbtValues.size()) {
        index = 0;
      }
      this.setMessage(titles.get(index));
      PacketRegistry.INSTANCE.sendToServer(new PacketStorageBagScreen(
          ScreenStorageBag.this.menu.bag, ScreenStorageBag.this.menu.slot, nbtValues.get(index).getId(), nbtKey, nbtValues.get(index)));
    }

    public void addState(Component title, Component tooltip, Tag nbtValue) {
      this.titles.add(title);
      this.tooltips.add(tooltip);
      this.nbtValues.add(nbtValue);
      if (this.nbt.get(nbtKey.getAsString()) != null &&
          this.nbt.get(nbtKey.getAsString()).equals(nbtValue)) {
        this.index = this.nbtValues.indexOf(nbtValue);
        this.setMessage(this.titles.get(index));
      }
    }
  }
}
