package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.spell.*;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SpellRegistry {
  public static boolean renderOnLeft;
  private static Map<Integer, ISpell> hashbook;
  public static class Spells {
    // on purpose, not all spells are in here. only ones that needed to be
    // exposed
    public static SpellRangeRotate rotate;
    public static SpellRangePush push;
    public static SpellRangePull pull;
    private static SpellInventory inventory;
    private static SpellRangeBuild reachdown;
    private static SpellRangeBuild reachup;
    private static SpellRangeBuild reachplace;
  }
  public static void register() {
    hashbook = new HashMap<Integer, ISpell>();
    int spellId = -1;// the smallest spell gets id zero
    Spells.inventory = new SpellInventory(++spellId, "inventory");
    registerSpell(Spells.inventory);
    Spells.rotate = new SpellRangeRotate(++spellId, "rotate");
    registerSpell(Spells.rotate);
    Spells.push = new SpellRangePush(++spellId, "push");
    registerSpell(Spells.push);
    Spells.pull = new SpellRangePull(++spellId, "pull");
    registerSpell(Spells.pull);
    Spells.reachup = new SpellRangeBuild(++spellId, "reachup", SpellRangeBuild.PlaceType.UP);
    registerSpell(Spells.reachup);
    Spells.reachplace = new SpellRangeBuild(++spellId, "reachplace", SpellRangeBuild.PlaceType.PLACE);
    registerSpell(Spells.reachplace);
    Spells.reachdown = new SpellRangeBuild(++spellId, "reachdown", SpellRangeBuild.PlaceType.DOWN);
    registerSpell(Spells.reachdown);
    SpellRangeBuild reachleft = new SpellRangeBuild(++spellId, "reachleft", SpellRangeBuild.PlaceType.LEFT);
    registerSpell(reachleft);
    SpellRangeBuild reachright = new SpellRangeBuild(++spellId, "reachright", SpellRangeBuild.PlaceType.RIGHT);
    registerSpell(reachright);
    ArrayList<ISpell> spellbookBuild = new ArrayList<ISpell>();
    spellbookBuild.add(Spells.inventory);
    spellbookBuild.add(Spells.reachup);
    spellbookBuild.add(Spells.reachplace);
    spellbookBuild.add(Spells.reachdown);
    spellbookBuild.add(reachleft);
    spellbookBuild.add(reachright);
    ItemRegistry.cyclic_wand_build.setSpells(spellbookBuild);
  }
  private static void registerSpell(ISpell spell) {
    hashbook.put(spell.getID(), spell);
  }
  public static boolean spellsEnabled(EntityPlayer player) {
    // current requirement is only a wand
    return UtilSpellCaster.getPlayerWandIfHeld(player) != null;
  }
  public static ISpell getSpellFromID(int id) {
    if (hashbook.containsKey(id)) { return hashbook.get(id); }
    return null;
  }
  public static List<ISpell> getSpellbook(ItemStack wand) {
    return ((ItemCyclicWand) wand.getItem()).getSpells();
  }
  public static ISpell next(ItemStack wand, ISpell spell) {
    List<ISpell> book = getSpellbook(wand);
    int indexCurrent = book.indexOf(spell);
    int indexNext = indexCurrent + 1;
    if (indexNext >= book.size()) {
      indexNext = 0;
    }
    return book.get(indexNext);
  }
  public static ISpell prev(ItemStack wand, ISpell spell) {
    List<ISpell> book = getSpellbook(wand);
    int indexCurrent = book.indexOf(spell);
    int indexPrev;
    if (indexCurrent <= 0) // not that it ever WOULD be.. negative.. yeah
      indexPrev = book.size() - 1;
    else
      indexPrev = indexCurrent - 1;
    ISpell ret = book.get(indexPrev);
    return ret;
  }
}
