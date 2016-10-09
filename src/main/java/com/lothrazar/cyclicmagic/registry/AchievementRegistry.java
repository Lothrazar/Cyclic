package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementRegistry {
  //thanks to http://jabelarminecraft.blogspot.ca/p/minecraft-forge-creating-custom.html
  public static Map<Item, Achievement> map = new HashMap<Item, Achievement>();
  public static List<Achievement> list = new ArrayList<Achievement>();
  private static final int padding = 8;
  private static int xCur = padding;
  private static int yCur = 0;
  public static void registerItemAchievement(Item icon) {
    String lang = icon.getUnlocalizedName();
    Achievement ach = new Achievement(lang, lang, xCur, yCur, icon, (Achievement) null);
    list.add(ach);
    map.put(icon, ach);
    ach.registerStat();
    xCur += 16 + padding;
    if (xCur > 50) {
      yCur += 16 + padding;
      xCur = padding;
    }
  }
  public static void registerPage() {
    AchievementPage.registerAchievementPage(
        new AchievementPage("achievement." + Const.MODID + ".page", list.toArray(new Achievement[0])));
  }
  public static void trigger(EntityPlayer player, Item item) {
    if (item != null && map.containsKey(item)) {
      player.addStat(map.get(item));
    }
  }
}
