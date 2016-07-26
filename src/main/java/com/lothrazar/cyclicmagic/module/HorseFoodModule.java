package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.ItemFoodHorse;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class HorseFoodModule extends BaseModule {
  private boolean moduleEnabled;
  @Override
  public void register() {
    ItemRegistry.addItem(new ItemFoodHorse(new ItemStack(Items.EMERALD)), "horse_upgrade_type");
    ItemRegistry.addItem(new ItemFoodHorse(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage())), "horse_upgrade_variant");
    ItemRegistry.addItem(new ItemFoodHorse(new ItemStack(Items.DIAMOND)), "horse_upgrade_health");
    ItemRegistry.addItem(new ItemFoodHorse(new ItemStack(Items.REDSTONE)), "horse_upgrade_speed");
    ItemRegistry.addItem(new ItemFoodHorse(new ItemStack(Items.ENDER_EYE)), "horse_upgrade_jump");
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    ItemFoodHorse.HEARTS_MAX = config.getInt("HorseFood Max Hearts", category, 20, 1, 100, "Maximum number of upgraded hearts");
    ItemFoodHorse.JUMP_MAX = config.getInt("HorseFood Max Jump", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
    ItemFoodHorse.SPEED_MAX = config.getInt("HorseFood Max Speed", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");
    category = Const.ConfigCategory.content;
    moduleEnabled = config.getBoolean("HorseFood", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
  public boolean isEnabled() {
    return moduleEnabled;
  }
}
