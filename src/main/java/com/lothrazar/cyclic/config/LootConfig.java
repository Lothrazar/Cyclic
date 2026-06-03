package com.lothrazar.cyclic.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclic.ModCyclic;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

/**
 * Standalone config for vanilla loot-chest injection.
 * Lives in its own file (cyclic-loot.toml) so pack authors can edit chest loot
 * without scrolling through the giant common config.
 *
 * Each entry is a CSV string: "modid:item,min,max,chance"
 *   min/max  = stack size range (inclusive)
 *   chance   = percent (0-100) that this entry is rolled per chest
 */
public class LootConfig {

  public static final ModConfigSpec SPEC;
  /** key = vanilla loot table path (e.g. "chests/simple_dungeon"), value = configured CSV lines */
  public static final Map<String, ConfigValue<List<? extends String>>> ENTRIES = new LinkedHashMap<>();

  /**
   * The set of vanilla loot tables we expose to configuration.
   * Each pair = { table_path_relative_to_minecraft_namespace, default CSV lines }.
   * Defaults are intentionally conservative (one cyclic item, low chance) so we don't
   * surprise players who installed the mod but never opened the config.
   *
   * custom_name is italicized by default (vanilla treats renamed items like anvil output). To suppress it: custom_name='{"text":"Cool","italic":false}'.
   * lore lines are italic + purple by default for the same reason. Override per-line with "italic":false,"color":"white" etc.
   *
   If you want the whole item to look "vanilla-renamed-but-not-italic", that's custom_name='{"text":"Cool","italic":false,"color":"gold"}'.
   */
  private static final String[][] TABLES = {
      // Underground / overworld dungeons
      { "chests/simple_dungeon",             """
cyclic:charm_speed,1,1,5
cyclic:ender_bag,1,1,5
cyclic:quiver_damage,1,1,12
cyclic:copper_sword[damage=15],1,1,5
cyclic:netherite_nugget,1,1,2
cyclic:boomerang_damage,1,1,6
 cyclic:heart,1,1,1
      """.strip()  },
      { "chests/abandoned_mineshaft",        """
cyclic:charm_luck,1,1,5
cyclic:apple_bone,2,6,30
cyclic:copper_pickaxe,1,1,15
cyclic:storage_bag,1,1,8
cyclic:heart,1,1,2
cyclic:netherite_nugget,1,1,2
cyclic:boomerang_carry,1,1,6
cyclic:gem_amber,1,1,1
cyclic:gem_obsidian,1,1,1
      """.strip()  },
      { "chests/buried_treasure",         "cyclic:charm_crit,1,1,15" },
      { "chests/spawn_bonus_chest",     """
cyclic:sandstone_axe[enchantments={levels:{"minecraft:unbreaking":3}}],1,1,40
cyclic:sandstone_shovel[enchantments={levels:{"minecraft:unbreaking":3}}],1,1,40
 cyclic:sleeping_mat,1,1,60
 cyclic:slingshot,1,1,40
 
""".strip() },
      // Strongholds
      { "chests/stronghold_corridor",   """
cyclic:heart,1,1,2
cyclic:netherite_nugget,1,1,2
cyclic:emerald_sword[enchantments={levels:{"minecraft:unbreaking":3}},lore=['{"text":"Ancient","color":"green","italic":true}'],custom_name='{"text":"Whispering Edge","italic":false,"color":"green"}'],1,1,3""".strip() },
      { "chests/stronghold_crossing",      """
      
cyclic:ender_eye_reuse,1,1,5
      """.strip() },
      { "chests/stronghold_library",       """

cyclic:netherite_nugget,1,1,2
cyclic:shield_leather,1,1,8
cyclic:ender_book,1,1,15
      """.strip() },
      // Desert / jungle / igloo
      { "chests/desert_pyramid",           """
cyclic:apple_emerald,2,8,15
cyclic:charm_xp_speed,1,1,10
cyclic:sandstone_sword,1,1,15
cyclic:boomerang_damage,1,1,6
cyclic:heart,1,1,2
      """.strip()  },
      { "chests/jungle_temple",             """
cyclic:apple_bone,2,6,30
cyclic:gem_amber,1,1,2
cyclic:gem_obsidian,1,1,2
cyclic:netherite_nugget,1,1,2
cyclic:boomerang_carry,1,1,6
cyclic:boomerang_stun,1,1,6
cyclic:heart,1,1,2
      """.strip()  },
      { "chests/jungle_temple_dispenser",  """
minecraft:tipped_arrow[potion_contents={potion:"minecraft:gravity",custom_effects:[{id:"minecraft:slowness",amplifier:2,duration:100}]}],32,64,15
minecraft:tipped_arrow[potion_contents={potion:"minecraft:stun"]}],16,64,5
      """.strip() },
      { "chests/igloo_chest",              """
cyclic:spell_ice[damage=15],1,1,15
cyclic:spell_water[damage=15],1,1,15
cyclic:antimatter_wand[damage=15],1,1,5
      """.strip()  },
      // Pillager / woodland
      { "chests/pillager_outpost",         """
cyclic:quiver_lightning,1,1,10
cyclic:shield_obsidian,1,1,8
cyclic:charm_crit,1,1,12
cyclic:boomerang_stun,1,1,6
""".strip() },
      { "chests/woodland_mansion",         """
cyclic:wand_hypno,1,1,8
cyclic:glowing_helmet,1,1,10
cyclic:charm_invisible,1,1,3
cyclic:netherite_nugget,2,8,25
cyclic:evoker_fang,1,1,8
cyclic:heart,1,1,2
      """.strip() },
      // Nether
      { "chests/nether_bridge",           """
cyclic:mattock_nether,1,1,5
cyclic:charm_fire,1,1,5
cyclic:netherbrick_sword,1,1,5
      """.strip() },
      { "chests/bastion_treasure",        """
cyclic:spikes_diamond,1,1,1
cyclic:charm_fire,1,1,1
cyclic:charm_wing,1,1,5
cyclic:netherite_nugget,1,1,2
cyclic:gem_amber,1,1,2
cyclic:gem_obsidian,1,1,2
cyclic:netherbrick_sword,1,1,5
cyclic:fire_scepter,1,1,8
cyclic:evoker_fang,1,1,5
 cyclic:heart,1,1,2
      """.strip() },
      { "chests/bastion_other",           """
 cyclic:heart,1,1,2
  cyclic:netherbrick_axe,1,1,5
  cyclic:netherite_nugget,1,1,2
  cyclic:gem_amber,1,1,2
  cyclic:gem_obsidian,1,1,2
      """.strip() },
      { "chests/bastion_bridge",          """
cyclic:quiver_lightning,1,1,10
cyclic:shield_obsidian,1,1,8
 cyclic:heart,1,1,2
      """.strip() },
      { "chests/bastion_hoglin_stable",   """
      
      """.strip() },
      { "chests/ruined_portal",            """
cyclic:amethyst_pickaxe,1,1,10
      """.strip() },
      // End
      { "chests/end_city_treasure",        """
cyclic:amethyst_sword,1,1,10
cyclic:charm_crit,1,1,1
cyclic:teleport_wand,1,1,5
cyclic:apple_ender,2,8,15
cyclic:emerald_chestplate,1,1,1
cyclic:ice_scepter,1,1,8
cyclic:fire_scepter,1,1,8
cyclic:lightning_scepter,1,1,8
cyclic:evoker_fang,1,1,5
 cyclic:heart,1,1,1
cyclic:chorus_flight,1,1,1
  cyclic:gem_amber,1,1,1
  cyclic:gem_obsidian,1,1,1
      """.strip() },
      // Ocean / shipwrecks / ruins
      { "chests/shipwreck_map",           """
cyclic:teleport_wand,1,1,5
      """.strip()},
      { "chests/shipwreck_supply",        """
cyclic:apple_honey,8,16,10
cyclic:peat_fuel,4,32,25
      """.strip()},
      { "chests/shipwreck_treasure",      """
cyclic:apple_diamond,4,6,15
cyclic:teleport_wand,1,1,5
  cyclic:gem_amber,1,1,2
   cyclic:heart,1,1,2
  cyclic:gem_obsidian,1,1,2
      """.strip()},
      { "chests/underwater_ruin_big",     """
cyclic:amethyst_pickaxe,1,1,5
 cyclic:heart,1,1,2
      """.strip()},
      { "chests/underwater_ruin_small",   """
      
  cyclic:gem_amber,1,1,2
  cyclic:gem_obsidian,1,1,2
      """.strip()},
      // Ancient city
      { "chests/ancient_city",             """
cyclic:ender_bag,1,1,5
cyclic:lightning_scepter,1,1,6
cyclic:evoker_fang,1,1,5

  cyclic:heart,1,1,2
  cyclic:netherite_nugget,1,1,2
  cyclic:soulstone,1,1,2
  cyclic:gem_amber,1,1,2
  cyclic:gem_obsidian,1,1,2


      """.strip() },
      { "chests/ancient_city_ice_box",    """

  cyclic:netherite_nugget,1,1,2
  cyclic:soulstone,1,1,2
      """.strip() },
      // Trial chambers (1.21)
      { "chests/trial_chambers/reward_common",           """
cyclic:amethyst_sword,1,1,5
  cyclic:gem_amber,1,1,2
  cyclic:gem_obsidian,1,1,2
      """.strip() },
      { "chests/trial_chambers/reward_rare",             """
cyclic:emerald_sword[enchantments={levels:{"minecraft:sharpness":3}}],1,1,15
  cyclic:netherite_nugget,1,1,2
      """.strip() },
      { "chests/trial_chambers/reward_unique",           """
cyclic:wand_missile,1,1,8
cyclic:apple_lofty_stature,2,4,8
cyclic:soulstone,1,1,2
cyclic:ice_scepter,1,1,6
  cyclic:heart,1,1,2
cyclic:charm_void,1,1,2
  cyclic:charm_knockback_resistance,1,1,2
  cyclic:charm_magicdefense,1,1,5
  cyclic:charm_creeper,1,1,2
  cyclic:charm_attack_speed,1,1,2
  cyclic:soulstone,1,1,2
      """.strip() },
      { "chests/trial_chambers/reward_ominous_common",   """
  cyclic:charm_wing,1,1,3
  cyclic:charm_knockback_resistance,1,1,8
      """.strip() },
      { "chests/trial_chambers/reward_ominous_rare",     """
  cyclic:wand_missile,1,1,8
    cyclic:charm_wing,1,1,3
  cyclic:charm_magicdefense,1,1,8
  cyclic:evoker_fang,1,1,5
  cyclic:heart,1,1,5
  cyclic:charm_creeper,1,1,8
  cyclic:soulstone,1,1,2
  cyclic:netherite_nugget,1,1,8
  cyclic:charm_ultimate,1,1,2
      """.strip() },
      { "chests/trial_chambers/reward_ominous_unique",   """
 cyclic:antimatter_wand,1,1,2
  cyclic:chorus_flight,6,16,5
  cyclic:heart,1,1,5
  cyclic:charm_magicdefense,1,1,8
  cyclic:charm_creeper,1,1,8
  cyclic:charm_attack_speed,1,1,8
  cyclic:charm_knockback_resistance,1,1,8
  cyclic:soulstone,1,1,2
  cyclic:charm_ultimate,1,1,2
      """.strip() },
      { "chests/trial_chambers/supply",                  """

cyclic:shield_obsidian[enchantments={levels:{"minecraft:unbreaking":3}}],1,1,8
      """.strip() },
      // Villages
      { "chests/village/village_armorer",       """
  cyclic:charm_longfall,1,1,8
  cyclic:charm_venom,1,1,8
  cyclic:charm_xp_speed,1,1,8
      """.strip() },
      { "chests/village/village_butcher",           """
          
            cyclic:apple_bone,1,2,30
            cyclic:lunchbox,1,1,5
      """.strip() },
      { "chests/village/village_cartographer",      """

  cyclic:charm_longfall,1,1,5
  cyclic:charm_venom,1,1,5
      """.strip() },
      { "chests/village/village_desert_house",      """
  cyclic:randomize_scepter,1,1,5
  cyclic:sandstone_pickaxe,1,1,5
  cyclic:sandstone_shovel,1,1,5
      """.strip() },
      { "chests/village/village_fisher",            """
       cyclic:flippers,1,1,15
      """.strip() },
      { "chests/village/village_fletcher",          """
      
      """.strip() },
      { "chests/village/village_mason",             """
      
      """.strip() },
      { "chests/village/village_plains_house",      """
  cyclic:lunchbox,1,1,10
      """.strip() },
      { "chests/village/village_savanna_house",     """
  cyclic:lunchbox,1,1,10
      """.strip() },
      { "chests/village/village_shepherd",          """
  cyclic:shearing,1,1,15
      """.strip() },
      { "chests/village/village_snowy_house",       """
      
  cyclic:scythe_brush,1,1,10
      """.strip() },
      { "chests/village/village_taiga_house",       """
  cyclic:scythe_harvest,1,1,8
      """.strip() },
      { "chests/village/village_tannery",           """
      
cyclic:shield_leather,1,1,25
      
      """.strip() },
      { "chests/village/village_temple",            """
          
  cyclic:heart,1,1,1
            cyclic:charm_antipotion,1,1,10
      """.strip() },
      { "chests/village/village_toolsmith",     """
            cyclic:mattock,1,1,12
            cyclic:scythe_harvest,1,1,8
      """.strip() },
      { "chests/village/village_weaponsmith",  """
           cyclic:emerald_sword,1,1,10
           cyclic:spikes_iron,1,2,10
      """.strip()},
  };

  static {
    ModConfigSpec.Builder b = new ModConfigSpec.Builder();
    b.comment(
        "Cyclic loot-chest injection.",
        "Add items to vanilla chest loot tables without touching datapacks.",
        "Each list entry is a CSV string: modid:item,min,max,chance",
        "  min   = minimum stack size (>=1)",
        "  max   = maximum stack size (>= min)",
        "  chance = percent (0-100) that this entry rolls in a given chest",
        "Unknown items are skipped with a warning. Empty list = no injection.",
        "Delete a line to disable that entry; add as many lines per chest as you want.  " +
        "Adding new/modded chests is supported if they are not already listed")
        .push("loot_inject");
    for (String[] pair : TABLES) {
      String path = pair[0];
      String defaultLine = pair[1];
      // Allow text-block defaults: each non-blank line becomes its own list entry.
      List<String> defaults;
      if (defaultLine.isEmpty()) {
        defaults = Collections.emptyList();
      }
      else {
        defaults = new java.util.ArrayList<>();
        for (String ln : defaultLine.split("\\R")) {
          String t = ln.trim();
          if (!t.isEmpty()) {
            defaults.add(t);
          }
        }
      }
      // dotted TOML key: convert slashes to dots so the table renders nicely as a single key
      String key = path.replace('/', '.');
      ConfigValue<List<? extends String>> cv = b.defineListAllowEmpty(
          Collections.singletonList(key),
          () -> defaults,
          () -> "",
          o -> o instanceof String);
      ENTRIES.put(path, cv);
    }
    b.pop();
    SPEC = b.build();
  }

  /**
   * Split a CSV line on commas, ignoring commas that sit inside [], {}, '...' or "...".
   * Needed because data-component syntax can contain commas inside [enchantments={...}, lore=[...]].
   */
  static String[] splitRespectingBrackets(String line) {
    java.util.List<String> out = new java.util.ArrayList<>();
    int bracket = 0, brace = 0;
    boolean inSingle = false, inDouble = false, escape = false;
    int start = 0;
    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);
      if (escape) {
        escape = false;
        continue;
      }
      if (c == '\\') {
        escape = true;
        continue;
      }
      if (inSingle) {
        if (c == '\'') {
          inSingle = false;
        }
        continue;
      }
      if (inDouble) {
        if (c == '"') {
          inDouble = false;
        }
        continue;
      }
      switch (c) {
        case '\'':
          inSingle = true;
          break;
        case '"':
          inDouble = true;
          break;
        case '[':
          bracket++;
          break;
        case ']':
          bracket--;
          break;
        case '{':
          brace++;
          break;
        case '}':
          brace--;
          break;
        case ',':
          if (bracket == 0 && brace == 0) {
            out.add(line.substring(start, i));
            start = i + 1;
          }
          break;
        default:
          break;
      }
    }
    out.add(line.substring(start));
    return out.toArray(new String[0]);
  }

  /**
   * Parsed form of one CSV line. Returns null on a bad line (and logs).
   * Item spec uses the same bracket syntax as /give:
   *   cyclic:charm_crit[damage=23,custom_name='{"text":"Cool"}']
   */
  public static ParsedEntry parse(String csv, HolderLookup.Provider registries) {
    if (csv == null || csv.isEmpty()) {
      return null;
    }
    String[] parts = splitRespectingBrackets(csv);
    if (parts.length != 4) {
      ModCyclic.LOGGER.error("[cyclic-loot] bad entry (expected 'item,min,max,chance'): {}", csv);
      return null;
    }
    try {
      ItemParser.ItemResult result = new ItemParser(registries).parse(new StringReader(parts[0].trim()));
      Holder<Item> holder = result.item();
      DataComponentPatch patch = result.components();
      int min = Integer.parseInt(parts[1].trim());
      int max = Integer.parseInt(parts[2].trim());
      int chance = Integer.parseInt(parts[3].trim());
      if (min < 1 || max < min || chance < 0 || chance > 100) {
        ModCyclic.LOGGER.error("[cyclic-loot] bad numbers in '{}': min>=1, max>=min, chance 0-100", csv);
        return null;
      }
      return new ParsedEntry(holder, patch, min, max, chance);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("[cyclic-loot] failed to parse '" + csv + "': " + e.getMessage());
      return null;
    }
  }

  public static final class ParsedEntry {

    public final Holder<Item> item;
    public final DataComponentPatch patch;
    public final int min;
    public final int max;
    public final int chancePercent;

    public ParsedEntry(Holder<Item> item, DataComponentPatch patch, int min, int max, int chancePercent) {
      this.item = item;
      this.patch = patch;
      this.min = min;
      this.max = max;
      this.chancePercent = chancePercent;
    }

    public ItemStack roll(net.minecraft.util.RandomSource rand) {
      int count = (min == max) ? min : (min + rand.nextInt(max - min + 1));
      return new ItemStack(item, count, patch);
    }
  }
}
