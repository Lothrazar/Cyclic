package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneAxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneHoe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstonePickaxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneSpade;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

public class SandstoneToolsModule extends BaseModule  implements IHasConfig{
  private boolean enableSandstoneTools;
  private void registerMaterials() {
    MaterialRegistry.sandstoneToolMaterial = EnumHelper.addToolMaterial("sandstone",
        ToolMaterial.STONE.getHarvestLevel(),
        (ToolMaterial.STONE.getMaxUses() + ToolMaterial.WOOD.getMaxUses()) / 2,
        ToolMaterial.STONE.getEfficiencyOnProperMaterial(),
        (ToolMaterial.STONE.getDamageVsEntity() + ToolMaterial.WOOD.getDamageVsEntity()) / 2,
        ToolMaterial.STONE.getEnchantability());
    MaterialRegistry.sandstoneToolMaterial.setRepairItem(new ItemStack(Blocks.SANDSTONE));
  }
  @Override
  public void onInit() {
    if (enableSandstoneTools) {
      this.registerMaterials();
      Item sandstone_pickaxe = new ItemSandstonePickaxe();
      ItemRegistry.addItem(sandstone_pickaxe, ItemSandstonePickaxe.name);
      Item sandstone_axe = new ItemSandstoneAxe();
      ItemRegistry.addItem(sandstone_axe, ItemSandstoneAxe.name);
      Item sandstone_spade = new ItemSandstoneSpade();
      ItemRegistry.addItem(sandstone_spade, ItemSandstoneSpade.name);
      Item sandstone_hoe = new ItemSandstoneHoe();
      ItemRegistry.addItem(sandstone_hoe, ItemSandstoneHoe.name);
      LootTableRegistry.registerLoot(sandstone_pickaxe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_axe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_spade, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_hoe, ChestType.BONUS);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableSandstoneTools = config.getBoolean("SandstoneTools", Const.ConfigCategory.content, true, "Sandstone tools are between wood and stone. " + Const.ConfigCategory.contentDefaultText);
  }
}
