package com.lothrazar.cyclic.registry;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
// import com.lothrazar.cyclic.material.EmeraldArmorMaterial;
// import com.lothrazar.cyclic.material.GemArmorMaterial;
// import com.lothrazar.cyclic.material.GlowingArmorMaterial;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = ModCyclic.MODID)
public class MaterialRegistry {

  //
  //  public static final CreativeModeTab BLOCK_GROUP = new CreativeModeTab(ModCyclic.MODID) {
  //
  //    @Override
  //    public ItemStack makeIcon() {
  //      return new ItemStack(BlockRegistry.TRASH.get());
  //    }
  //  };
  //  public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(ModCyclic.MODID + "items") {
  //
  //    @Override
  //    public ItemStack makeIcon() {
  //      return new ItemStack(ItemRegistry.GEM_AMBER.get());
  //    }
  //  };
  public static ModConfigSpec.IntValue EMERALD_BOOTS;
  public static ModConfigSpec.IntValue EMERALD_LEG;
  public static ModConfigSpec.IntValue EMERALD_CHEST;
  public static ModConfigSpec.IntValue EMERALD_HELM;
  public static ModConfigSpec.IntValue OBS_BOOTS;
  public static ModConfigSpec.IntValue OBS_LEG;
  public static ModConfigSpec.IntValue OBS_CHEST;
  public static ModConfigSpec.IntValue OBS_HELM;
  public static ModConfigSpec.DoubleValue EMERALD_TOUGH;
  public static ModConfigSpec.DoubleValue EMERALD_DMG;
  public static ModConfigSpec.DoubleValue OBS_TOUGH;
  public static ModConfigSpec.DoubleValue OBS_DMG;

  public static class ArmorMats {
    public static final net.minecraft.core.Holder<ArmorMaterial> EMERALD = net.minecraft.world.item.ArmorMaterials.DIAMOND;
    public static final net.minecraft.core.Holder<ArmorMaterial> GEMOBSIDIAN = net.minecraft.world.item.ArmorMaterials.DIAMOND;
    public static final net.minecraft.core.Holder<ArmorMaterial> GLOWING = net.minecraft.world.item.ArmorMaterials.DIAMOND;
  } /*

    public static final net.minecraft.core.Holder<ArmorMaterial> EMERALD = ArmorMaterials.DIAMOND;
    public static final net.minecraft.core.Holder<ArmorMaterial> GEMOBSIDIAN = ArmorMaterials.DIAMOND;
    public static final net.minecraft.core.Holder<ArmorMaterial> GLOWING = ArmorMaterials.DIAMOND;
  }

    */
  public static class ToolMats {
    public static final Tier NETHERBRICK = net.minecraft.world.item.Tiers.GOLD;
    public static final Tier SANDSTONE = net.minecraft.world.item.Tiers.STONE;
    public static final Tier COPPER = net.minecraft.world.item.Tiers.IRON;
    public static final Tier AMETHYST = net.minecraft.world.item.Tiers.IRON;
    public static final Tier EMERALD = net.minecraft.world.item.Tiers.DIAMOND;
    public static final Tier GEMOBSIDIAN = net.minecraft.world.item.Tiers.NETHERITE;
  }
}
