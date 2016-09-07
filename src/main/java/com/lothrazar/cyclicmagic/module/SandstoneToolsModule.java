package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneAxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneHoe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstonePickaxe;
import com.lothrazar.cyclicmagic.item.gear.ItemSandstoneSpade;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;

public class SandstoneToolsModule extends BaseModule {
  private boolean enableSandstoneTools;
  // thanks for help:
  // http://bedrockminer.jimdo.com/modding-tutorials/basic-modding-1-7/custom-tools-swords/
  public static ToolMaterial TOOL_MATERIAL;
  public SandstoneToolsModule() {
    super();
    //materials is kind of a SUB-MODULE
    this.registerMaterials();
  }
  //from ArmorMaterial.DIAMOND, second constuctor param
  //used as a ratio for durability
  // only because theyre private, with no getters
  //  private static final int    diamondDurability       = 33;
  //private static final int[]  diamondreductionAmounts = new int[] { 3, 6, 8, 3 };
  private void registerMaterials() {
    //max uses is durability ex The number of uses this material allows.
    //as of 1.9.4 :  (wood = 59, stone = 131, iron = 250, diamond = 1561, gold = 32)
    TOOL_MATERIAL = EnumHelper.addToolMaterial("sandstone",
        ToolMaterial.STONE.getHarvestLevel(),
        (ToolMaterial.STONE.getMaxUses() + ToolMaterial.WOOD.getMaxUses()) / 2,
        ToolMaterial.STONE.getEfficiencyOnProperMaterial(),
        (ToolMaterial.STONE.getDamageVsEntity() + ToolMaterial.WOOD.getDamageVsEntity()) / 2,
        ToolMaterial.STONE.getEnchantability());
    TOOL_MATERIAL.setRepairItem(new ItemStack(Blocks.SANDSTONE));
    // EnumHelper.addToolMaterial("emerald", 3, harvestLevel 3 same as diamond
    // 1600,3.5F, 5+25 );
  }
  @Override
  public void onInit() {
    if (enableSandstoneTools) {
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
