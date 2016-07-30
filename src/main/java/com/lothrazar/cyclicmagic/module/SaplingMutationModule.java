package com.lothrazar.cyclicmagic.module;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SaplingMutationModule extends BaseEventModule implements IHasConfig {
  private List<Integer> acaciaBiomes;
  private List<Integer> spruceBiomes;
  private List<Integer> oakBiomes;
  private List<Integer> birchBiomes;
  private List<Integer> darkoakBiomes;
  private List<Integer> jungleBiomes;
  private boolean saplingDespawnGrow = true;
  private boolean enableHomeBiomes;
  @SubscribeEvent
  public void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    if (enableHomeBiomes) {
      World world = event.getWorld();
      BlockPos pos = event.getPos();
      Block b = world.getBlockState(pos).getBlock();
      // this may not always be true: such as trees
      // added by Mods, so not a vanilla tree, but
      // throwing same event
      if (b != Blocks.SAPLING) { return; }
      boolean treeAllowedToGrow = false;
      int meta = Blocks.SAPLING.getMetaFromState(world.getBlockState(pos));
      int biomeID = Biome.getIdForBiome(world.getBiomeGenForCoords(pos));// event.world.getBiomeGenForCoords(event.pos).biomeID;
      int growth_data = 8;// 0-5 is the type, then it adds on a 0x8
      // and we know that it is always maxed out at ready to grow 8 since
      // it is turning into a tree.
      int tree_type = meta - growth_data;
      // IDS:
      // http://www.minecraftforum.net/forums/minecraft-discussion/recent-updates-and-snapshots/381405-full-list-of-biome-ids-as-of-13w36b
      // as of 12 march 2015, it seems biome id 168 does not exist, so 167
      // is highest used (vanilla minecraft)
      switch (tree_type) {
      case Const.sapling_acacia:
        treeAllowedToGrow = acaciaBiomes.contains(biomeID);
        break;
      case Const.sapling_spruce:
        treeAllowedToGrow = spruceBiomes.contains(biomeID);
        break;
      case Const.sapling_oak:
        treeAllowedToGrow = oakBiomes.contains(biomeID);
        break;
      case Const.sapling_birch:
        treeAllowedToGrow = birchBiomes.contains(biomeID);
        break;
      case Const.sapling_darkoak:
        treeAllowedToGrow = darkoakBiomes.contains(biomeID);
        break;
      case Const.sapling_jungle:
        treeAllowedToGrow = jungleBiomes.contains(biomeID);
        break;
      }
      if (treeAllowedToGrow == false) {
        event.setResult(Result.DENY);
        // overwrite the sapling. - we could set to Air first, but dont
        // see much reason to
        world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
        UtilEntity.dropItemStackInWorld(world, pos, new ItemStack(Blocks.SAPLING, 1, tree_type));
      }
    }
  } 
  //from one of my mods https://github.com/LothrazarMinecraftMods/SaplingGrowthControl/blob/master/src/main/java/com/lothrazar/samssaplings/ModConfig.java
  @SubscribeEvent
  public void onItemExpireEvent(ItemExpireEvent event) {
    if (saplingDespawnGrow) { 
      EntityItem entityItem = event.getEntityItem();
      Entity entity = event.getEntity();
      ItemStack is = entityItem.getEntityItem();
      if (is == null) { return; } // has not happened in the wild, yet
      Block blockhere = entity.worldObj.getBlockState(entityItem.getPosition()).getBlock();
      Block blockdown = entity.worldObj.getBlockState(entityItem.getPosition().down()).getBlock();
      if (blockhere == Blocks.AIR && blockdown == Blocks.DIRT || blockdown == Blocks.GRASS) {
        // plant the sapling, replacing the air and on top of dirt/plantable
        //BlockSapling.TYPE
        if (Block.getBlockFromItem(is.getItem()) == Blocks.SAPLING)
          entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.SAPLING.getStateFromMeta(is.getItemDamage()));
        else if (Block.getBlockFromItem(is.getItem()) == Blocks.RED_MUSHROOM)
          entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.RED_MUSHROOM.getDefaultState());
        else if (Block.getBlockFromItem(is.getItem()) == Blocks.BROWN_MUSHROOM)
          entity.worldObj.setBlockState(entityItem.getPosition(), Blocks.BROWN_MUSHROOM.getDefaultState());
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    saplingDespawnGrow = config.getBoolean("Plant Despawning Saplings", category, true, "Plant saplings (and mushrooms) if they despawn on grass/dirt");
    enableHomeBiomes = config.getBoolean("Sapling Home Biomes", category, true, "Saplings are only allowed to grow into trees in their home biome, otherwise they turn to dead bushes.  (Biome ids listed in config file)");
    category = Const.ConfigCategory.saplingBiomes;
    config.addCustomCategoryComment(category, "Tweak the 'Sapling Home Biomes' feature: A list of biome IDs that each sapling is allowed to grow in.  Useful for modpacks that add extra biomes.  ");
    String oakCSV = config.get(category, "oak", "4, 18, 132, 39, 166, 167, 21, 23, 151, 149, 22, 6, 134, 3, 20, 34, 12, 29, 157").getString();
    oakBiomes = csvToInt(oakCSV);
    String acaciaCSV = config.get(category, "acacia", "35, 36, 38, 163, 164").getString();
    acaciaBiomes = csvToInt(acaciaCSV);
    String spruceCSV = config.get(category, "spruce", "5, 19, 32, 160, 161, 33, 30, 31, 158, 3, 20, 34, 21, 12, 13").getString();
    spruceBiomes = csvToInt(spruceCSV);
    String birchCSV = config.get(category, "birch", "27, 28, 155, 156, 4, 18, 132, 29, 157").getString();
    birchBiomes = csvToInt(birchCSV);
    String darkCSV = config.get(category, "dark_oak", "29, 157").getString();
    darkoakBiomes = csvToInt(darkCSV);
    String jungleCSV = config.get(category, "jungle", "21, 23, 22, 149, 151").getString();
    jungleBiomes = csvToInt(jungleCSV);
  }
  private static List<Integer> csvToInt(String csv) {
    // does not check validity of biome ids
    List<Integer> bi = new ArrayList<Integer>();
    String[] list = csv.split(",");
    int biome;
    for (String s_id : list) {
      try {
        biome = Integer.parseInt(s_id.trim());
        bi.add(biome);
      }
      catch (Exception e) {
        ModMain.logger.warn("Invalid biome id from config file, must be integer: " + s_id);
      }
    }
    return bi;
  }
}
