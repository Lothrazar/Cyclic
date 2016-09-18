package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneAxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneHoe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstonePickaxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneSpade;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

public class SandstoneToolsModule extends BaseModule {
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
      ItemRegistry.sandstone_pickaxe = new ItemSandstonePickaxe();
      ItemRegistry.addItem(ItemRegistry.sandstone_pickaxe, ItemSandstonePickaxe.name);
      ItemRegistry.sandstone_axe = new ItemSandstoneAxe();
      ItemRegistry.addItem(ItemRegistry.sandstone_axe, ItemSandstoneAxe.name);
      ItemRegistry.sandstone_spade = new ItemSandstoneSpade();
      ItemRegistry.addItem(ItemRegistry.sandstone_spade, ItemSandstoneSpade.name);
      ItemRegistry.sandstone_hoe = new ItemSandstoneHoe();
      ItemRegistry.addItem(ItemRegistry.sandstone_hoe, ItemSandstoneHoe.name);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableSandstoneTools = config.getBoolean("SandstoneTools", Const.ConfigCategory.content, true, "Sandstone tools are between wood and stone. " + Const.ConfigCategory.contentDefaultText);
  }
}
