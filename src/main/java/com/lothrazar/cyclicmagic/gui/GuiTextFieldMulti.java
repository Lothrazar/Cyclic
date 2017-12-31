package com.lothrazar.cyclicmagic.gui;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * started as extending GuiTextField to alter drawtext ... but everything is private insetad of package so i had to clone
 * 
 * @author Sam
 *
 */
@SideOnly(Side.CLIENT)
public class GuiTextFieldMulti extends Gui {
  private final int id;
  private final FontRenderer fontRenderer;
  public int x;
  public int y;
  /** The width of this text field. */
  public int width;
  public int height;
  /** Has the current text being edited on the textbox. */
  private String text = "";
  private int maxStringLength = 32;
  private int cursorCounter;
  /** if true the textbox can lose focus by clicking elsewhere on the screen */
  private boolean canLoseFocus = true;
  /** If this value is true along with isEnabled, keyTyped will process the keys. */
  private boolean isFocused;
  /** If this value is true along with isFocused, keyTyped will process the keys. */
  private boolean isEnabled = true;
  /** The current character index that should be used as start of the rendered text. */
  private int lineScrollOffset;
  private int cursorPosition;
  /** other selection position, maybe the same as the cursor */
  private int selectionEnd;
  private int enabledColor = 14737632;
  private int disabledColor = 7368816;
  /** True if this textbox is visible */
  private boolean visible = true;
  private GuiPageButtonList.GuiResponder guiResponder;
  /** Called to check if the text is valid */
  private Predicate<String> validator = Predicates.<String> alwaysTrue();
  public GuiTextFieldMulti(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
    this.id = componentId;
    this.fontRenderer = fontrendererObj;
    this.x = x;
    this.y = y;
    this.width = par5Width;
    this.height = par6Height;
  }
  /**
   * Sets the GuiResponder associated with this text box.
   */
  public void setGuiResponder(GuiPageButtonList.GuiResponder guiResponderIn) {
    this.guiResponder = guiResponderIn;
  }
  /**
   * Increments the cursor counter
   */
  public void updateCursorCounter() {
    ++this.cursorCounter;
  }
  /**
   * Sets the text of the textbox, and moves the cursor to the end.
   */
  public void setText(String textIn) {
    if (this.validator.apply(textIn)) {
      if (textIn.length() > this.maxStringLength) {
        this.text = textIn.substring(0, this.maxStringLength);
      }
      else {
        this.text = textIn;
      }
      this.setCursorPositionEnd();
    }
  }
  /**
   * Returns the contents of the textbox
   */
  public String getText() {
    return this.text;
  }
  /**
   * returns the text between the cursor and selectionEnd
   */
  public String getSelectedText() {
    int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
    int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
    return this.text.substring(i, j);
  }
  public void setValidator(Predicate<String> theValidator) {
    this.validator = theValidator;
  }
  /**
   * Adds the given text after the cursor, or replaces the currently selected text if there is a selection.
   */
  public void writeText(String textToWrite) {
    String s = "";
    String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
    int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
    int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
    int k = this.maxStringLength - this.text.length() - (i - j);
    if (!this.text.isEmpty()) {
      s = s + this.text.substring(0, i);
    }
    int l;
    if (k < s1.length()) {
      s = s + s1.substring(0, k);
      l = k;
    }
    else {
      s = s + s1;
      l = s1.length();
    }
    if (!this.text.isEmpty() && j < this.text.length()) {
      s = s + this.text.substring(j);
    }
    if (this.validator.apply(s)) {
      this.text = s;
      this.moveCursorBy(i - this.selectionEnd + l);
      this.setResponderEntryValue(this.id, this.text);
    }
  }
  /**
   * Notifies this text box's {@linkplain GuiPageButtonList.GuiResponder responder} that the text has changed.
   */
  public void setResponderEntryValue(int idIn, String textIn) {
    if (this.guiResponder != null) {
      this.guiResponder.setEntryValue(idIn, textIn);
    }
  }
  /**
   * Deletes the given number of words from the current cursor's position, unless there is currently a selection, in which case the selection is deleted instead.
   */
  public void deleteWords(int num) {
    if (!this.text.isEmpty()) {
      if (this.selectionEnd != this.cursorPosition) {
        this.writeText("");
      }
      else {
        this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
      }
    }
  }
  /**
   * Deletes the given number of characters from the current cursor's position, unless there is currently a selection, in which case the selection is deleted instead.
   */
  public void deleteFromCursor(int num) {
    if (!this.text.isEmpty()) {
      if (this.selectionEnd != this.cursorPosition) {
        this.writeText("");
      }
      else {
        boolean flag = num < 0;
        int i = flag ? this.cursorPosition + num : this.cursorPosition;
        int j = flag ? this.cursorPosition : this.cursorPosition + num;
        String s = "";
        if (i >= 0) {
          s = this.text.substring(0, i);
        }
        if (j < this.text.length()) {
          s = s + this.text.substring(j);
        }
        if (this.validator.apply(s)) {
          this.text = s;
          if (flag) {
            this.moveCursorBy(num);
          }
          this.setResponderEntryValue(this.id, this.text);
        }
      }
    }
  }
  public int getId() {
    return this.id;
  }
  /**
   * Gets the starting index of the word at the specified number of words away from the cursor position.
   */
  public int getNthWordFromCursor(int numWords) {
    return this.getNthWordFromPos(numWords, this.getCursorPosition());
  }
  /**
   * Gets the starting index of the word at a distance of the specified number of words away from the given position.
   */
  public int getNthWordFromPos(int n, int pos) {
    return this.getNthWordFromPosWS(n, pos, true);
  }
  /**
   * Like getNthWordFromPos (which wraps this), but adds option for skipping consecutive spaces
   */
  public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
    int i = pos;
    boolean flag = n < 0;
    int j = Math.abs(n);
    for (int k = 0; k < j; ++k) {
      if (!flag) {
        int l = this.text.length();
        i = this.text.indexOf(32, i);
        if (i == -1) {
          i = l;
        }
        else {
          while (skipWs && i < l && this.text.charAt(i) == ' ') {
            ++i;
          }
        }
      }
      else {
        while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
          --i;
        }
        while (i > 0 && this.text.charAt(i - 1) != ' ') {
          --i;
        }
      }
    }
    return i;
  }
  /**
   * Moves the text cursor by a specified number of characters and clears the selection
   */
  public void moveCursorBy(int num) {
    this.setCursorPosition(this.selectionEnd + num);
  }
  /**
   * Sets the current position of the cursor.
   */
  public void setCursorPosition(int pos) {
    if (pos < 0) {
      pos = 0;
    }
    this.cursorPosition = pos;
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, this.text.length());
    this.setSelectionPos(this.cursorPosition);
  }
  /**
   * Moves the cursor to the very start of this text box.
   */
  public void setCursorPositionZero() {
    this.setCursorPosition(0);
  }
  /**
   * Moves the cursor to the very end of this text box.
   */
  public void setCursorPositionEnd() {
    this.setCursorPosition(this.text.length());
  }
  /**
   * Call this method from your GuiScreen to process the keys into the textbox
   */
  public boolean textboxKeyTyped(char typedChar, int keyCode) {
    if (!this.isFocused) {
      return false;
    }
    else if (GuiScreen.isKeyComboCtrlA(keyCode)) {
      this.setCursorPositionEnd();
      this.setSelectionPos(0);
      return true;
    }
    else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
      GuiScreen.setClipboardString(this.getSelectedText());
      return true;
    }
    else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
      if (this.isEnabled) {
        this.writeText(GuiScreen.getClipboardString());
      }
      return true;
    }
    else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
      GuiScreen.setClipboardString(this.getSelectedText());
      if (this.isEnabled) {
        this.writeText("");
      }
      return true;
    }
    else {
      switch (keyCode) {
        case 14:
          if (GuiScreen.isCtrlKeyDown()) {
            if (this.isEnabled) {
              this.deleteWords(-1);
            }
          }
          else if (this.isEnabled) {
            this.deleteFromCursor(-1);
          }
          return true;
        case 199:
          if (GuiScreen.isShiftKeyDown()) {
            this.setSelectionPos(0);
          }
          else {
            this.setCursorPositionZero();
          }
          return true;
        case 203:
          if (GuiScreen.isShiftKeyDown()) {
            if (GuiScreen.isCtrlKeyDown()) {
              this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
            }
            else {
              this.setSelectionPos(this.getSelectionEnd() - 1);
            }
          }
          else if (GuiScreen.isCtrlKeyDown()) {
            this.setCursorPosition(this.getNthWordFromCursor(-1));
          }
          else {
            this.moveCursorBy(-1);
          }
          return true;
        case 205:
          if (GuiScreen.isShiftKeyDown()) {
            if (GuiScreen.isCtrlKeyDown()) {
              this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
            }
            else {
              this.setSelectionPos(this.getSelectionEnd() + 1);
            }
          }
          else if (GuiScreen.isCtrlKeyDown()) {
            this.setCursorPosition(this.getNthWordFromCursor(1));
          }
          else {
            this.moveCursorBy(1);
          }
          return true;
        case 207:
          if (GuiScreen.isShiftKeyDown()) {
            this.setSelectionPos(this.text.length());
          }
          else {
            this.setCursorPositionEnd();
          }
          return true;
        case 211:
          if (GuiScreen.isCtrlKeyDown()) {
            if (this.isEnabled) {
              this.deleteWords(1);
            }
          }
          else if (this.isEnabled) {
            this.deleteFromCursor(1);
          }
          return true;
        default:
          if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            if (this.isEnabled) {
              this.writeText(Character.toString(typedChar));
            }
            return true;
          }
          else {
            return false;
          }
      }
    }
  }
  /**
   * Called when mouse is clicked, regardless as to whether it is over this button or not.
   */
  public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
    boolean flag = mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.height;
    if (this.canLoseFocus) {
      this.setFocused(flag);
    }
    if (this.isFocused && flag && mouseButton == 0) {
      int i = mouseX - this.x - 4;
      //      if (this.enableBackgroundDrawing) {
      //      i -= 4;
      //      }
      String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
      this.setCursorPosition(this.fontRenderer.trimStringToWidth(s, i).length() + this.lineScrollOffset);
      return true;
    }
    else {
      return false;
    }
  }
  /**
   * Draws the textbox
   */
  public void drawTextBox() {
    if (this.getVisible() == false) {
      return;
    }
    drawRect(this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, -6250336);
    drawRect(this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
    int colorCurrent = this.isEnabled ? this.enabledColor : this.disabledColor;
    boolean cursorBlinkFlag = this.isFocused && this.cursorCounter / 6 % 2 == 0;//&& isCursorPosWithin;
    int hPos = this.x + 4;// : this.x - 16;
    int yPos = this.y - 4;//: this.y;
    int hPosCurr = hPos;
    String textCopy = new String(this.text);
    int MAX_WIDTH = 16;
    String[] lines;
    int hPosCursor = 0;
    int vPosCursor = -1;
    int charsWritten = 0;
    try {
      lines = UtilChat.splitIntoLine(textCopy, MAX_WIDTH);
      int lineTest=0;
      int charsThisLine=0;
      for (String line : lines) {
        yPos += 8;
        charsThisLine=line.length();
        this.fontRenderer.drawStringWithShadow(line, (float) hPosCurr, (float) yPos, colorCurrent);

        // so if cursorPosition

        int cursorPosRelative = cursorPosition - charsWritten;
        //so charsWritten steps up line by line, 16, 32, 48 etc
        charsWritten += line.length();
        //so if cursorPos is 63, dont do it until the last one we pass
        if (vPosCursor < 0 && this.getCursorPosition() < charsWritten) {
//          ModCyclic.logger.log("VTEST"+cursorPosition+" cursorPosition "+cursorPosition+" <? written "+charsWritten);
//          ModCyclic.logger.log("cursorPosRelative = "+cursorPosRelative+" inside line: w/ charsThisLine "+charsThisLine);
          vPosCursor = yPos;
          //found the row hey
          // so we have [0,     curP,         strLength]  
          if (line.length() > 1) {
            hPosCursor = hPos + this.fontRenderer.getStringWidth(line.substring(0, cursorPosRelative));
//            ModCyclic.logger.log(">>>cursorPosRelative"+cursorPosRelative+" gives hposC "+hPosCursor);
          }
        }
        lineTest++;
      }
    }
    catch (Exception e) {
      //System.out.println("TODO use fontrenderer version ok");
    }
    if (vPosCursor < 0) {
      hPosCursor = hPos;
      vPosCursor = yPos;//FIRST TIME
    }
    if (cursorBlinkFlag) {
      Gui.drawRect(hPosCursor, vPosCursor - 1, hPosCursor + 1, vPosCursor + 1 + this.fontRenderer.FONT_HEIGHT, -3092272);
    }
  }
  /**
   * Sets the maximum length for the text in this text box. If the current text is longer than this length, the current text will be trimmed.
   */
  public void setMaxStringLength(int length) {
    this.maxStringLength = length;
    if (this.text.length() > length) {
      this.text = this.text.substring(0, length);
    }
  }
  /**
   * returns the maximum number of character that can be contained in this textbox
   */
  public int getMaxStringLength() {
    return this.maxStringLength;
  }
  /**
   * returns the current position of the cursor
   */
  public int getCursorPosition() {
    return this.cursorPosition;
  }
  /**
   * Sets the color to use when drawing this text box's text. A different color is used if this text box is disabled.
   */
  public void setTextColor(int color) {
    this.enabledColor = color;
  }
  /**
   * Sets the color to use for text in this text box when this text box is disabled.
   */
  public void setDisabledTextColour(int color) {
    this.disabledColor = color;
  }
  /**
   * Sets focus to this gui element
   */
  public void setFocused(boolean isFocusedIn) {
    if (isFocusedIn && !this.isFocused) {
      this.cursorCounter = 0;
    }
    this.isFocused = isFocusedIn;
    if (Minecraft.getMinecraft().currentScreen != null) {
      Minecraft.getMinecraft().currentScreen.setFocused(isFocusedIn);
    }
  }
  /**
   * Getter for the focused field
   */
  public boolean isFocused() {
    return this.isFocused;
  }
  /**
   * Sets whether this text box is enabled. Disabled text boxes cannot be typed in.
   */
  public void setEnabled(boolean enabled) {
    this.isEnabled = enabled;
  }
  /**
   * the side of the selection that is not the cursor, may be the same as the cursor
   */
  public int getSelectionEnd() {
    return this.selectionEnd;
  }
  /**
   * returns the width of the textbox depending on if background drawing is enabled
   */
  public int getWidth() {
    return this.width;// this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
  }
  /**
   * Sets the position of the selection anchor (the selection anchor and the cursor position mark the edges of the selection). If the anchor is set beyond the bounds of the current text, it will be
   * put back inside.
   */
  public void setSelectionPos(int position) {
    int i = this.text.length();
    if (position > i) {
      position = i;
    }
    if (position < 0) {
      position = 0;
    }
    this.selectionEnd = position;
    if (this.fontRenderer != null) {
      if (this.lineScrollOffset > i) {
        this.lineScrollOffset = i;
      }
      int j = this.getWidth();
      String s = this.fontRenderer.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
      int k = s.length() + this.lineScrollOffset;
      if (position == this.lineScrollOffset) {
        this.lineScrollOffset -= this.fontRenderer.trimStringToWidth(this.text, j, true).length();
      }
      if (position > k) {
        this.lineScrollOffset += position - k;
      }
      else if (position <= this.lineScrollOffset) {
        this.lineScrollOffset -= this.lineScrollOffset - position;
      }
      this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
    }
  }
  /**
   * Sets whether this text box loses focus when something other than it is clicked.
   */
  public void setCanLoseFocus(boolean canLoseFocusIn) {
    this.canLoseFocus = canLoseFocusIn;
  }
  /**
   * returns true if this textbox is visible
   */
  public boolean getVisible() {
    return this.visible;
  }
  /**
   * Sets whether or not this textbox is visible
   */
  public void setVisible(boolean isVisible) {
    this.visible = isVisible;
  }
}
