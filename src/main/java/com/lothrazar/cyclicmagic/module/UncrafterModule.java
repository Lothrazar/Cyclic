package com.lothrazar.cyclicmagic.module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclicmagic.block.BlockUncrafting;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityUncrafting;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class UncrafterModule extends BaseModule {
  private boolean moduleEnabled;
  public void onInit() {
    BlockRegistry.uncrafting_block = new BlockUncrafting();
    BlockRegistry.registerBlock(BlockRegistry.uncrafting_block, "uncrafting_block");
    BlockRegistry.uncrafting_block.addRecipe();
    GameRegistry.registerTileEntity(TileEntityUncrafting.class, "uncrafting_block_te");
  }
  @Override
  public void syncConfig(Configuration config) {
    moduleEnabled = config.getBoolean("UncraftingGrinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String category = Const.ConfigCategory.uncrafter;
 
    UtilUncraft.dictionaryFreedom = config.getBoolean("PickFirstMeta", category, true, "If you change this to true, then the uncrafting will just take the first of many options in any recipe that takes multiple input types.  For example, false means chests cannot be uncrafted, but true means chests will ALWAYS give oak wooden planks.");
    config.addCustomCategoryComment(category, "Here you can blacklist any thing, vanilla or modded.  Mostly for creating modpacks.  Input means you cannot uncraft it at all.  Output means it will not come out of a recipe.");
    // so when uncrafting cake, you do not get milk buckets back
    String def = "";
    String csv = config.getString("BlacklistInput", category, def, "Items that cannot be uncrafted; not allowed in the slots.  EXAMPLE : 'item.stick,tile.hayBlock,tile.chest'  ");
    // [item.stick, tile.cloth]
    UtilUncraft.blacklistInput = (List<String>) Arrays.asList(csv.split(","));
    if (UtilUncraft.blacklistInput == null) {
      UtilUncraft.blacklistInput = new ArrayList<String>();
    }
    def = "item.milk";
    csv = config.getString("BlacklistOutput", category, def, "Comma seperated items that cannot come out of crafting recipes.  For example, if milk is in here, then cake is uncrafted you get all items except the milk buckets.  ");
    UtilUncraft.blacklistOutput = (List<String>) Arrays.asList(csv.split(","));
    if (UtilUncraft.blacklistOutput == null) {
      UtilUncraft.blacklistOutput = new ArrayList<String>();
    }
  }
  @Override
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
