package com.lothrazar.cyclic.enchant;

import java.util.Map;
import org.jetbrains.annotations.Nullable;
import com.lothrazar.cyclic.compat.CompatConstants;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.TagDataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import static com.lothrazar.library.util.EnchantUtil.getCurrentLevelTool;

public class BeheadingEnchant {

  public static IntValue PERCDROP;
  public static IntValue PERCPERLEVEL;
  public static final String ID = "beheading";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  private int percentForLevel(int level) {
    int drop = PERCDROP == null ? 20 : PERCDROP.get();
    int perLevel = PERCPERLEVEL == null ? 25 : PERCPERLEVEL.get();
    return drop + (level - 1) * perLevel;
  }


  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (!isEnabled()) {
      return;
    }
    if (event.getSource().getEntity() instanceof Player) {
      Player attacker = (Player) event.getSource().getEntity();
      int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.BEHEADING, attacker.level()), attacker);
      if (level <= 0) {
        return;
      }
      Level world = attacker.level();
      if (Mth.nextInt(world.random, 0, 100) > percentForLevel(level)) {
        return;
      }
      LivingEntity target = event.getEntity();
      if (target == null) {
        return;
      } //probably wont happen just extra safe
      BlockPos pos = target.blockPosition();
      if (target instanceof Player) {
        //player head
        ItemStackUtil.drop(world, pos, TagDataUtil.buildNamedPlayerSkull((Player) target));
        return;
      }
      //else the random number was less than 10, so it passed the 10% chance req
      @Nullable
      ResourceLocation type = BuiltInRegistries.ENTITY_TYPE.getKey(target.getType());
      String key = type == null ? "" : type.toString();
      ////we allow all these, which include config, to override the vanilla skulls below
      Map<String, String> mappedBeheading = ConfigRegistry.getMappedBeheading();
      if (target.getType() == EntityType.ENDER_DRAGON) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.DRAGON_HEAD));
      }
      else if (target.getType() == EntityType.CREEPER) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.CREEPER_HEAD));
      }
      else if (target.getType() == EntityType.ZOMBIE) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.ZOMBIE_HEAD));
      }
      else if (target.getType() == EntityType.SKELETON) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.WITHER_SKELETON) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL));
      }
      else if (target.getType() == EntityType.PIGLIN) {
        ItemStackUtil.drop(world, pos, new ItemStack(Items.PIGLIN_HEAD));
      }
      else if (target.getType() == EntityType.WITHER) { //Drop number of heads equal to level of enchant [1,3]
        ItemStackUtil.drop(world, pos, new ItemStack(Items.WITHER_SKELETON_SKULL, Math.max(level, 3)));
      }
      else if (ModList.get().isLoaded(CompatConstants.TCONSTRUCT)) {
        //tconstruct: drowned_head husk_head enderman_head cave_spider_head stray_head
        String id = CompatConstants.TCONSTRUCT;
        ItemStack tFound = ItemStack.EMPTY;
        if (target.getType() == EntityType.DROWNED) {
          tFound = ItemStackUtil.findItem(id + ":drowned_head");
        }
        else if (target.getType() == EntityType.HUSK) {
          tFound = ItemStackUtil.findItem(id + ":husk_head");
        }
        else if (target.getType() == EntityType.ENDERMAN) {
          tFound = ItemStackUtil.findItem(id + ":enderman_head");
        }
        else if (target.getType() == EntityType.SPIDER) {
          tFound = ItemStackUtil.findItem(id + ":spider_head");
        }
        else if (target.getType() == EntityType.CAVE_SPIDER) {
          tFound = ItemStackUtil.findItem(id + ":cave_spider_head");
        }
        else if (target.getType() == EntityType.STRAY) {
          tFound = ItemStackUtil.findItem(id + ":stray_head");
        }
        else if (target.getType() == EntityType.BLAZE) {
          tFound = ItemStackUtil.findItem(id + ":blaze_head");
        }
        if (!tFound.isEmpty()) {
          ItemStackUtil.drop(world, pos, tFound);
          return;
        }
      }
      else if (mappedBeheading.containsKey(key)) {
        //otherwise not a real mob, try the config last
        ItemStackUtil.drop(world, pos, TagDataUtil.buildNamedPlayerSkull(mappedBeheading.get(key)));
      }
    }
  }
}
