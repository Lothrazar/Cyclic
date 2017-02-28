package com.lothrazar.cyclicmagic.gui.password;
import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import com.lothrazar.cyclicmagic.net.PacketTilePassword;
import com.lothrazar.cyclicmagic.net.PacketTilePassword.PacketType;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPassword extends GuiContainer {
  private static final ResourceLocation table = new ResourceLocation(Const.MODID, "textures/gui/password.png");
  private GuiTextField txtPassword;
  private ContainerPassword ctr;
  private ButtonPassword buttonActiveType;
  private ButtonPassword buttonUserPerm;
  private String namePref;
  public GuiPassword(TileEntityPassword tileEntity) {
    super(new ContainerPassword(tileEntity));
    ctr = (ContainerPassword) this.inventorySlots;
    this.ySize = 79;//texture size in pixels
    namePref = tileEntity.getBlockType().getUnlocalizedName() + ".";
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    String s = UtilChat.lang(namePref + ".name");
    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
    if (txtPassword != null) {
      txtPassword.drawTextBox();
    }
  }
  @Override
  public void initGui() {
    super.initGui();
    int width = 120, height = 20;
    txtPassword = new GuiTextField(0, this.fontRendererObj, (xSize / 2 - width / 2), 20 + (height / 2), 127, height);
    txtPassword.setMaxStringLength(40);
    txtPassword.setText(ctr.tile.getMyPassword());
    txtPassword.setFocused(true);
    int id = 1, x = 50, y = 50;
    buttonActiveType = new ButtonPassword(id, x, y, PacketType.ACTIVETYPE);
    this.addButton(buttonActiveType);
    id++;
    //    x += 20;
    y += 20;
    buttonUserPerm = new ButtonPassword(id, x, y, PacketType.USERSALLOWED);
    this.addButton(buttonUserPerm);
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(table);
    int thisX = (this.width - this.xSize) / 2;
    int thisY = (this.height - this.ySize) / 2;
    int texture_width = 176;
    int texture_height = 79;
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, texture_width, texture_height);
  }
  @SuppressWarnings("incomplete-switch")
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button instanceof ButtonPassword) {
      ButtonPassword btn = (ButtonPassword) button;
      int tData = -1;
      switch (btn.type) {
        case ACTIVETYPE:
          tData = ctr.tile.getType().ordinal();
        break;
        case USERSALLOWED:
          tData = ctr.tile.getUserPerm().ordinal();
        break;
      }
      if (tData >= 0)
        ModCyclic.network.sendToServer(new PacketTilePassword(btn.type, tData, "", ctr.tile.getPos()));
    }
  }
  // http://www.minecraftforge.net/forum/index.php?topic=22378.0
  // below is all the stuff that makes the text box NOT broken
  @Override
  public void updateScreen() {
    super.updateScreen();
    if (txtPassword != null) {
      txtPassword.updateCursorCounter();
    }
    this.buttonActiveType.displayString = namePref + "active." + ctr.tile.getType().name().toLowerCase();
    this.buttonUserPerm.displayString = namePref + "userp." + ctr.tile.getUserPerm().name().toLowerCase();
  }
  @Override
  protected void keyTyped(char par1, int par2) throws IOException {
    super.keyTyped(par1, par2);
    if (txtPassword != null && txtPassword.isFocused()) {
      txtPassword.textboxKeyTyped(par1, par2);
      ModCyclic.network.sendToServer(new PacketTilePassword(PacketType.PASSTEXT, -1, txtPassword.getText(), ctr.tile.getPos()));
    }
  }
  @Override
  protected void mouseClicked(int x, int y, int btn) throws IOException {
    super.mouseClicked(x, y, btn);// x/y pos is 33/30
    if (txtPassword != null) {
      txtPassword.mouseClicked(x, y, btn);
      txtPassword.setFocused(true);
    }
  }
  // ok end of textbox fixing stuff
}
