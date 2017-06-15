package com.lothrazar.cyclicmagic.component.password;
import java.io.IOException;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.password.PacketTilePassword.PacketType;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiPassword extends GuiBaseContainer {
  private static final ResourceLocation table = new ResourceLocation(Const.MODID, "textures/gui/password.png");
  private GuiTextField txtPassword;
  private ContainerPassword ctr;
  private ButtonPassword buttonActiveType;
  private ButtonPassword buttonUserPerm;
  private String namePref;
  private ButtonPassword buttonUserClaim;
  public GuiPassword(TileEntityPassword tileEntity) {
    super(new ContainerPassword(tileEntity), tileEntity);
    ctr = (ContainerPassword) this.inventorySlots;
    this.ySize = 79;//texture size in pixels
    namePref = tileEntity.getBlockType().getUnlocalizedName() + ".";
  }
  @Override
  public void initGui() {
    super.initGui();
    int width = 127, height = 20;
    int x = (xSize / 2 - width / 2), y = 26 + (height / 2);
    txtPassword = new GuiTextField(0, this.fontRendererObj, x, y, width, height);
    txtPassword.setMaxStringLength(40);
    txtPassword.setText(ctr.tile.getMyPassword());
    txtPassword.setFocused(true);
    x = this.guiLeft - 82;
    y = this.guiTop;
    buttonActiveType = new ButtonPassword(PacketType.ACTIVETYPE, x, y);
    this.addButton(buttonActiveType);
    y += height + 4;
    buttonUserClaim = new ButtonPassword(PacketType.USERCLAIM, x, y);
    this.addButton(buttonUserClaim);
    y += height + 4;
    buttonUserPerm = new ButtonPassword(PacketType.USERSALLOWED, x, y);
    this.addButton(buttonUserPerm);
    updateVisibility();
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    String s;// = UtilChat.lang(namePref + "name");
    int y = 6;
    int xCenter = this.xSize / 2;
    //    this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
    if (txtPassword != null) {
      txtPassword.drawTextBox();
    }
    if (ctr.tile.isClaimedBySomeone()) {
      if (ctr.tile.isClaimedBy(ModCyclic.proxy.getClientPlayer())) {
        s = UtilChat.lang(namePref + "userclaim.ismine");
        y = 18;
        this.drawString(s, xCenter - this.fontRendererObj.getStringWidth(s) / 2, y);
      }
      else {
        s = UtilChat.lang(namePref + "userclaim.isclaimed");
        y = 18;
        this.drawString(s, xCenter - this.fontRendererObj.getStringWidth(s) / 2, y);
        y = 32;
        s = ctr.tile.userName;//ctr.tile.getClaimedHash();
        this.drawString(s, xCenter - this.fontRendererObj.getStringWidth(s) / 2, y);
      }
      this.buttonUserClaim.displayString = UtilChat.lang(namePref + this.buttonUserClaim.type.name().toLowerCase() + ".unclaim");
    }
    else {
      this.buttonUserClaim.displayString = UtilChat.lang(namePref + "userclaim.claim");
    }
    this.buttonActiveType.displayString = UtilChat.lang(namePref + buttonActiveType.type.name().toLowerCase() + "." + ctr.tile.getType().name().toLowerCase());
    this.buttonUserPerm.displayString = UtilChat.lang(namePref + buttonUserPerm.type.name().toLowerCase() + "." + ctr.tile.getUserPerm().name().toLowerCase());
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
  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button instanceof ButtonPassword) {
      ButtonPassword btn = (ButtonPassword) button;
      ModCyclic.network.sendToServer(new PacketTilePassword(btn.type, "", ctr.tile.getPos()));
    }
  }
  // http://www.minecraftforge.net/forum/index.php?topic=22378.0
  // below is all the stuff that makes the text box NOT broken
  @Override
  public void updateScreen() {
    super.updateScreen();
    if (txtPassword != null) {
      txtPassword.updateCursorCounter();
      updateVisibility();
    }
  }
  private void updateVisibility() {
    boolean isMine = ctr.tile.isClaimedBy(ModCyclic.proxy.getClientPlayer());
    boolean visible = !ctr.tile.isClaimedBySomeone() || isMine;
    if (txtPassword != null) {
      txtPassword.setVisible(visible);
      txtPassword.setEnabled(visible);
    }
    buttonActiveType.visible = visible;
    buttonUserClaim.visible = visible;
    buttonUserPerm.visible = isMine;
  }
  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) == false) {
      super.keyTyped(typedChar, keyCode);
    }
    if (txtPassword != null && txtPassword.isFocused()) {
      txtPassword.textboxKeyTyped(typedChar, keyCode);
      ModCyclic.network.sendToServer(new PacketTilePassword(PacketType.PASSTEXT, txtPassword.getText(), ctr.tile.getPos()));
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
