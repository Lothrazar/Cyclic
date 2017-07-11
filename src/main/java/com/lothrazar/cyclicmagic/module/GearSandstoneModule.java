package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneAxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneHoe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstonePickaxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneSpade;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class GearSandstoneModule extends BaseModule implements IHasConfig {
  private boolean enableSandstoneTools;
 
  @Override
  public void onPreInit() {
    if (enableSandstoneTools) {
      Item sandstone_pickaxe = new ItemSandstonePickaxe();
      ItemRegistry.register(sandstone_pickaxe, "sandstone_pickaxe", null);
      Item sandstone_axe = new ItemSandstoneAxe();
      ItemRegistry.register(sandstone_axe, "sandstone_axe", null);
      Item sandstone_spade = new ItemSandstoneSpade();
      ItemRegistry.register(sandstone_spade, "sandstone_spade", null);
      Item sandstone_hoe = new ItemSandstoneHoe();
      ItemRegistry.register(sandstone_hoe, "sandstone_hoe", null);
      LootTableRegistry.registerLoot(sandstone_pickaxe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_axe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_spade, ChestType.BONUS);
      GuideRegistry.register(GuideCategory.GEAR, new ItemStack(sandstone_axe), "item.sandstonegear.title", "item.sandstonegear.guide");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableSandstoneTools = config.getBoolean("SandstoneTools", Const.ConfigCategory.content, true, "Sandstone tools are between wood and stone. " + Const.ConfigCategory.contentDefaultText);
  }
}
