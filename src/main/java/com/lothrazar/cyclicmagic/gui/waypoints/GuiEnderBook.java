package com.lothrazar.cyclicmagic.gui.waypoints;
import java.io.IOException;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.ButtonClose;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.item.ItemEnderBook;
import com.lothrazar.cyclicmagic.item.ItemEnderBook.BookLocation;
import com.lothrazar.cyclicmagic.net.PacketDeleteWaypoint;
import com.lothrazar.cyclicmagic.net.PacketNewButton;
import com.lothrazar.cyclicmagic.util.UtilSearchWorld;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;// http://www.minecraftforge.net/forum/index.php?topic=22378.0
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiEnderBook extends GuiScreen {
  private final EntityPlayer entityPlayer;
  private ItemStack bookStack;
  // public final ResourceLocation texture = new
  // ResourceLocation(ModSamsContent.MODID,
  // "textures/enderbook/textures/gui/book_ender.png" );
  final int maxNameLen = 20;
  public GuiEnderBook(EntityPlayer entityPlayer, ItemStack book) {
    this.entityPlayer = entityPlayer;
    bookStack = book;
  }
  public static int buttonIdNew;
  GuiButton buttonNew;
  GuiTextField txtNew;
  final int DELETE_OFFSET = 1000;
  @Override
  public void initGui() {
    // great tips here
    // http://www.minecraftforge.net/forum/index.php?topic=29945.0
    if (bookStack.hasTagCompound() == false) {
      bookStack.setTagCompound(new NBTTagCompound());
    }
    int buttonID = 0, w = 70, h = 20, ypad = 1, delete_w = 20, rowpad = 8;
    buttonIdNew = buttonID;
    buttonID++;
    ArrayList<BookLocation> list = ItemEnderBook.getLocations(bookStack);
    buttonNew = new ButtonWaypointNew(buttonIdNew, this.width / 2 - w, // x
        20, // y
        w, h, buttonIdNew);
    buttonList.add(buttonNew);
    ButtonClose buttonClose = new ButtonClose(9999, this.width / 2 - w - 50, 20);
    buttonList.add(buttonClose);
    if (bookStack != null && ItemEnderBook.getLocations(bookStack).size() >= ItemEnderBook.maximumSaved) {
      buttonNew.enabled = false;// also a tooltip?
    }
    txtNew = new GuiTextField(buttonID++, this.fontRendererObj, buttonNew.xPosition + buttonNew.width + 20, buttonNew.yPosition, w, h);
    txtNew.setMaxStringLength(maxNameLen);
    // default to the current biome
    txtNew.setText(entityPlayer.worldObj.getBiomeGenForCoords(entityPlayer.getPosition()).getBiomeName());
    txtNew.setFocused(true);
    ButtonWaypointTeleport btn;
    GuiButton del;
    BookLocation loc;
    String buttonText;
    int yStart = 45;
    int xStart = (this.width / 10);
    int x = xStart;
    int y = yStart;
    for (int i = 0; i < list.size(); i++) {
      loc = list.get(i);
      buttonText = (loc.display == null) ? I18n.format("gui.enderbook.go") : loc.display;
      if (i % ItemEnderBook.BTNS_PER_COLUMN == 0) // do we start a new row?
      {
        x += w + delete_w + rowpad;
        y = yStart;
      }
      else {
        y += h + ypad;
      }
      btn = new ButtonWaypointTeleport(buttonID++, x, y, w, h, buttonText, loc.id);
      BlockPos toPos = list.get(i).toBlockPos();
      int distance = (int) UtilSearchWorld.distanceBetweenHorizontal(toPos, entityPlayer.getPosition());
      int cost = ItemEnderBook.getExpCostPerTeleport(entityPlayer, bookStack, loc.id);
      btn.addTooltipLine(list.get(i).coordsDisplay());
      btn.addTooltipLine(I18n.format("button.waypoint.distance") + " " + distance);
      btn.addTooltipLine(I18n.format("button.waypoint.cost") + " " + cost);
      btn.enabled = (loc.dimension == this.entityPlayer.dimension);
      buttonList.add(btn);
      del = new ButtonWaypointDelete(buttonID++, x - delete_w - 2, y, delete_w, h, "X", loc.id);
      buttonList.add(del);
    }
  }
  @Override
  public void drawScreen(int x, int y, float par3) {
    drawDefaultBackground();
    drawCenteredString(fontRendererObj, I18n.format("gui.enderbook.title"), width / 2, 6, 16777215);
    // http://www.minecraftforge.net/forum/index.php?topic=22378.0
    // no idea why this is sometimes randomly null and only on world start if i
    // open it too quick??
    if (txtNew != null) {
      txtNew.drawTextBox();
    }
    super.drawScreen(x, y, par3);
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton) {
        ITooltipButton btn = (ITooltipButton) buttonList.get(i);
        drawHoveringText(btn.getTooltips(), x, y, fontRendererObj);
      }
    }
  }
  @Override
  protected void actionPerformed(GuiButton btn) {
    if (btn.id == buttonIdNew) {
      ModMain.network.sendToServer(new PacketNewButton(txtNew.getText()));
    }
    else if (btn instanceof ButtonWaypointDelete) {
      ModMain.network.sendToServer(new PacketDeleteWaypoint(((ButtonWaypointDelete) btn).getSlot()));
    }
    else if (btn instanceof ButtonWaypointTeleport) {
      // moved to btn class
    }
    this.entityPlayer.closeScreen();
  }
  @Override
  public boolean doesGuiPauseGame() {
    return false;
  }
  // http://www.minecraftforge.net/forum/index.php?topic=22378.0
  // below is all the stuff that makes the text box NOT broken
  @Override
  public void updateScreen() {
    super.updateScreen();
    if (txtNew != null) {
      txtNew.updateCursorCounter();
    }
  }
  @Override
  protected void keyTyped(char par1, int par2) throws IOException {
    super.keyTyped(par1, par2);
    if (txtNew != null) {
      txtNew.textboxKeyTyped(par1, par2);
    }
  }
  @Override
  protected void mouseClicked(int x, int y, int btn) throws IOException {
    super.mouseClicked(x, y, btn);
    if (txtNew != null) {
      txtNew.mouseClicked(x, y, btn);
    }
  }
  // ok end of textbox fixing stuff
}
