package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.item.tool.ItemCyclicWand;
import com.lothrazar.cyclicmagic.spell.*;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class SpellRegistry {
//  public static boolean renderOnLeft;
  private static Map<Integer, ISpell> hashbook;
  public static boolean doParticles;
  public static class Spells {
    private static SpellInventory inventory;
    private static SpellRangeBuild reachdown;
    private static SpellRangeBuild reachup;
    private static SpellRangeBuild reachplace;
    private static SpellRangeBuild reachleft;
    private static SpellRangeBuild reachright;
  }
  public static void register(ItemCyclicWand wand) {
    hashbook = new HashMap<Integer, ISpell>();
    /*
     * SPELL REG 0 Inventory SPELL REG 1 Rotation SPELL REG 2 Push Block SPELL
     * REG 3 Pull Block SPELL REG 4 Build Up SPELL REG 5 Place Block SPELL REG 6
     * Build Down SPELL REG 7 Build Left SPELL REG 8 Build Right
     */
    Spells.inventory = new SpellInventory(0, "inventory");
    registerSpell(Spells.inventory);
    Spells.reachup = new SpellRangeBuild(4, "reachup", SpellRangeBuild.PlaceType.UP);
    registerSpell(Spells.reachup);
    Spells.reachplace = new SpellRangeBuild(5, "reachplace", SpellRangeBuild.PlaceType.PLACE);
    registerSpell(Spells.reachplace);
    Spells.reachdown = new SpellRangeBuild(6, "reachdown", SpellRangeBuild.PlaceType.DOWN);
    registerSpell(Spells.reachdown);
    Spells.reachleft = new SpellRangeBuild(7, "reachleft", SpellRangeBuild.PlaceType.LEFT);
    registerSpell(Spells.reachleft);
    Spells.reachright = new SpellRangeBuild(8, "reachright", SpellRangeBuild.PlaceType.RIGHT);
    registerSpell(Spells.reachright);
    ArrayList<ISpell> spellbookBuild = new ArrayList<ISpell>();
    spellbookBuild.add(Spells.inventory);
    spellbookBuild.add(Spells.reachup);
    spellbookBuild.add(Spells.reachplace);
    spellbookBuild.add(Spells.reachdown);
    spellbookBuild.add(Spells.reachleft);
    spellbookBuild.add(Spells.reachright);
    wand.setSpells(spellbookBuild);
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
