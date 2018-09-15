package com.lothrazar.cyclicmagic.guide;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class GuideItem {

  public GuideCategory cat;
  public Item icon;
  public String title;
  public List<GuidePage> pages = new ArrayList<GuidePage>();

  public GuideItem(@Nonnull GuideCategory cat, @Nonnull Item icon, @Nonnull String title, @Nonnull String text, @Nullable IRecipe recipe) {
    this.cat = cat;
    this.icon = icon;
    this.title = UtilChat.lang(title);
    if (text != null) {
      this.pages.add(new GuidePage(UtilChat.lang(text)));
    }
    if (recipe != null) {
      this.pages.add(new GuidePage(recipe));
    }
  }

  public void addRecipePage(IRecipe t) {
    if (t == null) {
      return;
    }
    this.pages.add(new GuidePage(t));
  }

  public void addRecipePage(BrewingRecipe t) {
    if (t == null) {
      return;
    }
    this.pages.add(new GuidePage(t));
  }

  public void addTextPage(String t) {
    this.pages.add(new GuidePage(t));
  }
}