package com.lothrazar.cyclicmagic;

 
import amerifrance.guideapi.GuideMod;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.Page;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.GuiHelper;
import amerifrance.guideapi.api.util.TextHelper;
import amerifrance.guideapi.gui.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;

//@EqualsAndHashCode(callSuper = true)
public class PageBrewingRecipe extends Page {
  public BrewingRecipe recipe;
  public ItemStack ingredient;
  private ItemStack input;
  private ItemStack output;
  /**
   * @param input
   *          - Input BrewingRecipe
   */
  public PageBrewingRecipe(BrewingRecipe recipe) {
    super();
    this.recipe = recipe;
    this.ingredient = recipe.getIngredient();
    this.input = recipe.getInput();
    this.output = recipe.getOutput();
  }
  @Override
  @SideOnly(Side.CLIENT)
  
  public void draw(Book book, CategoryAbstract category, EntryAbstract entry, int guiLeft, int guiTop, int mouseX, int mouseY, GuiBase guiBase, FontRenderer fontRendererObj) {
    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Const.MODID, "textures/recipe_elements.png"));
//    guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 65, 105, 65);
    guiBase.drawTexturedModalRect(guiLeft + 42, guiTop + 53, 0, 132, 1112, 112);
    List<String> badTip = new ArrayList<String>();
    badTip.add(TextHelper.localizeEffect("text.brewing.error"));
    guiBase.drawCenteredString(fontRendererObj, TextHelper.localizeEffect("text.brewing.smelting"), guiLeft + guiBase.xSize / 2, guiTop + 12, 0);
    int xmiddle =  guiLeft + guiBase.xSize / 2 - 8;
    int x = xmiddle;//since item stack is approx 16 wide
    int y = guiTop + 40;
    //start input
    GuiHelper.drawItemStack(ingredient, x, y);
    
    List<String> tooltip = null;
    if (GuiHelper.isMouseBetween(mouseX, mouseY, x, y, 15, 15))
      tooltip = GuiHelper.getTooltip(ingredient);
    //the three bottles
    int hSpacing = 26;
    y += 40;
    GuiHelper.drawItemStack(input, x, y);
    if (GuiHelper.isMouseBetween(mouseX, mouseY, x, y, 15, 15))
      tooltip = GuiHelper.getTooltip(input);
    x -= hSpacing;
    y -= 10;
    GuiHelper.drawItemStack(input, x, y);
    if (GuiHelper.isMouseBetween(mouseX, mouseY, x, y, 15, 15))
      tooltip = GuiHelper.getTooltip(input);
    x += hSpacing*2;
    GuiHelper.drawItemStack(input, x, y);
    if (GuiHelper.isMouseBetween(mouseX, mouseY, x, y, 15, 15))
      tooltip = GuiHelper.getTooltip(input);
    
    //start output
    x = xmiddle;
    y += 40;
    GuiHelper.drawItemStack(output, x, y);
    if (GuiHelper.isMouseBetween(mouseX, mouseY, x, y, 15, 15))
      tooltip = output.getItem() == Item.getItemFromBlock(Blocks.BARRIER) ? badTip : GuiHelper.getTooltip(output);
    if (output.getItem() == Item.getItemFromBlock(Blocks.BARRIER))
      guiBase.drawCenteredString(fontRendererObj, TextHelper.localizeEffect("text.brewing.error"), guiLeft + guiBase.xSize / 2, guiTop + 4 * guiBase.ySize / 6, 0xED073D);
    if (tooltip != null)
      guiBase.drawHoveringText(tooltip, mouseX, mouseY);
  }
}