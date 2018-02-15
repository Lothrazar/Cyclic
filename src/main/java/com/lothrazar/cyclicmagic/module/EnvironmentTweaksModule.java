package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilOreDictionary;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.monster.SkeletonType;
//import net.minecraft.entity.monster.ZombieType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnvironmentTweaksModule extends BaseEventModule implements IHasConfig {
  private boolean saplingDespawnGrow;
  private boolean spawnersUnbreakable;
  @Override
  public void onInit() {
    updateHardness();
  }
  private void updateHardness() {
    if (spawnersUnbreakable) {
      Blocks.MOB_SPAWNER.setBlockUnbreakable();//just like .setHardness(-1.0F);
    }
    else {
      Blocks.MOB_SPAWNER.setHardness(5.0F);//reset to normal http://minecraft.gamepedia.com/Monster_Spawner
    }
  }
  @SubscribeEvent
  public void onItemExpireEvent(ItemExpireEvent event) {
    if (saplingDespawnGrow) {
      EntityItem entityItem = event.getEntityItem();
      BlockPos pos = entityItem.getPosition();
      Entity entity = event.getEntity();
      ItemStack is = entityItem.getItem();
      World world = entity.getEntityWorld();
      if (is.isEmpty()) {
        return;
      }
      // plant the sapling, replacing the air and on top of dirt/plantable
      if (Block.getBlockFromItem(is.getItem()) == Blocks.RED_MUSHROOM)
        world.setBlockState(entityItem.getPosition(), Blocks.RED_MUSHROOM.getDefaultState());
      else if (Block.getBlockFromItem(is.getItem()) == Blocks.BROWN_MUSHROOM)
        world.setBlockState(entityItem.getPosition(), Blocks.BROWN_MUSHROOM.getDefaultState());
      else if (UtilOreDictionary.doesMatchOreDict(is, "treeSapling")) {
        Block saplingBlock = Block.getBlockFromItem(is.getItem());
        if (saplingBlock != Blocks.AIR
            && saplingBlock.canPlaceBlockAt(world, pos)) {
          world.setBlockState(pos, UtilItemStack.getStateFromMeta(Block.getBlockFromItem(is.getItem()), is.getItemDamage()));
        }
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.blocks;
    saplingDespawnGrow = config.getBoolean("Plant Despawning Saplings", category, false, "Plant saplings (and mushrooms) if they despawn on grass/dirt");
    spawnersUnbreakable = config.getBoolean("Spawners Unbreakable", category, false, "Make mob spawners unbreakable");
    updateHardness();
  }
}
