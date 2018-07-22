package com.lothrazar.cyclicmagic.block.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Mouse;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.sound.TileEntitySoundPlayer.Fields;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.net.PacketTileSetField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiSoundPlayer extends GuiBaseContainer {

  private GuiSoundList soundList;
  private List<ResourceLocation> allSounds;

  public GuiSoundPlayer(InventoryPlayer inventoryPlayer, TileEntitySoundPlayer tile) {
    super(new ContainerSoundPlayer(inventoryPlayer, tile), tile);
    this.setScreenSize(ScreenSize.PLAINWIDE);
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.allSounds = TileEntitySoundPlayer.getSoundList();
  }

  @Override
  public void initGui() {
    super.initGui();
    //TODO: TIMER SLIDER!?
    soundList = new GuiSoundList(198, 110, guiTop + 26, guiTop + 132, guiLeft + 8, 14);
    soundList.setSounds(allSounds);
    int sel = tile.getField(Fields.SOUNDINDEX.ordinal());
    soundList.selectIndex(sel);
  }

  @Override
  public void handleMouseInput() throws IOException {
    super.handleMouseInput();
    int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
    int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
    soundList.handleMouseInput(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    soundList.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    // ResourceLocation selectedIndex = soundList.getSelection();
    //  ModCyclic.logger.log("SEL" + selectedIndex + soundList.getSelectionIndex());
    ModCyclic.network.sendToServer(new PacketTileSetField(tile.getPos(), Fields.SOUNDINDEX.ordinal(), soundList.getSelectionIndex()));
  }

  // SOUND LIST: from this using MIT license authored by @ EdgarAllen
  // https://github.com/EdgarAllen/SuperSoundMuffler/blob/master/LICENSE
  //more direct citation https://github.com/EdgarAllen/SuperSoundMuffler/blob/master/src/main/java/edgarallen/soundmuffler/gui/GuiSoundMuffler.java#L137
  private final class GuiSoundList extends GuiScrollingList {

    private List<ResourceLocation> sounds;
    private final int slotHeight;
    private List<Integer> selectedIndicies = new ArrayList<>();

    GuiSoundList(int width, int height, int top, int bottom, int left, int slotHeight) {
      super(Minecraft.getMinecraft(), width, height, top, bottom, left, slotHeight, width, height);
      this.slotHeight = slotHeight;
    }

    @Override
    protected int getSize() {
      return sounds.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
      if (isCtrlKeyDown()) {
        if (isSelected(index)) {
          removeSelection(index);
        }
        else {
          selectIndex(index);
        }
      }
      else if (isShiftKeyDown()) {
        clearSelection();
        int start = index > selectedIndex ? selectedIndex : index;
        int end = index > selectedIndex ? index : selectedIndex;
        selectRange(start, end);
      }
      else {
        clearSelection();
        selectIndex(index);
      }
    }

    @Override
    protected boolean isSelected(int index) {
      for (int i : selectedIndicies) {
        if (i == index) {
          return true;
        }
      }
      return false;
    }

    void removeSelection(int index) {
      for (int i = 0; i < selectedIndicies.size(); i++) {
        if (selectedIndicies.get(i) == index) {
          selectedIndicies.remove(i);
          return;
        }
      }
    }

    void selectIndex(int index) {
      if (index >= 0 && index < sounds.size()) {
        removeSelection(index);
        selectedIndicies.add(index);
        selectedIndex = index;
      }
      //else out of bounds, maybe mod was removed or something
    }

    void clearSelection() {
      selectedIndicies.clear();
    }

    public int getSelectionIndex() {
      return selectedIndex;
    }

    public ResourceLocation getSelection() {
      if (selectedIndex >= 0 && selectedIndex < sounds.size())
        return this.sounds.get(selectedIndex);
      return null;
    }

    void selectRange(int start, int end) {
      for (int i = start; i <= end; i++) {
        selectedIndicies.add(i);
      }
      selectedIndex = end;
    }

    @Override
    protected void drawBackground() {}

    @Override
    protected int getContentHeight() {
      return (getSize()) * slotHeight + 1;
    }

    @Override
    protected void drawSlot(int idx, int right, int top, int height, Tessellator tess) {
      ResourceLocation sound = sounds.get(idx);
      fontRenderer.drawString(fontRenderer.trimStringToWidth(sound.toString(), listWidth - 10), left + 3, top + 2, 0xCCCCCC);
    }

    void setSounds(List<ResourceLocation> sounds) {
      this.sounds = sounds;
    }

    boolean hasSelectedElements() {
      return selectedIndicies.size() > 0;
    }

    List<ResourceLocation> getSelectedSounds() {
      List<ResourceLocation> ret = new ArrayList<>();
      for (int i : selectedIndicies) {
        ret.add(sounds.get(i));
      }
      return ret;
    }
  }
}
