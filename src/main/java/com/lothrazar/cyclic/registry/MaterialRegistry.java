package com.lothrazar.cyclic.registry;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.material.EmeraldArmorMaterial;
import com.lothrazar.cyclic.material.GemArmorMaterial;
import com.lothrazar.cyclic.material.GlowingArmorMaterial;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
  public static IntValue EMERALD_BOOTS;
  public static IntValue EMERALD_LEG;
  public static IntValue EMERALD_CHEST;
  public static IntValue EMERALD_HELM;
  public static IntValue OBS_BOOTS;
  public static IntValue OBS_LEG;
  public static IntValue OBS_CHEST;
  public static IntValue OBS_HELM;
  public static DoubleValue EMERALD_TOUGH;
  public static DoubleValue EMERALD_DMG;
  public static DoubleValue OBS_TOUGH;
  public static DoubleValue OBS_DMG;

  public static class ArmorMats {

    public static final ArmorMaterial EMERALD = new EmeraldArmorMaterial();
    public static final ArmorMaterial GEMOBSIDIAN = new GemArmorMaterial();
    public static final ArmorMaterial GLOWING = new GlowingArmorMaterial();
  }

  public static class ToolMats {

    //NB is outside
    public static final Tier NETHERBRICK = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.IRON.getLevel(),
            (Tiers.IRON.getUses() + Tiers.GOLD.getUses()) / 2,
            (Tiers.IRON.getSpeed() + Tiers.GOLD.getSpeed()) / 2,
            (Tiers.IRON.getAttackDamageBonus() + Tiers.GOLD.getAttackDamageBonus()) / 2,
            Tiers.GOLD.getEnchantmentValue() + 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_nether_bricks_tool")),
            () -> Ingredient.of(Items.NETHER_BRICKS)),
        new ResourceLocation(ModCyclic.MODID, "nether_bricks"),
        List.of(Tiers.GOLD), List.of(Tiers.DIAMOND));
    //
    //WOOD
    //then stuff between wood and stone
    public static final Tier SANDSTONE = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.STONE.getLevel(),
            Tiers.STONE.getUses() + 20, Tiers.STONE.getSpeed(),
            (Tiers.WOOD.getAttackDamageBonus() + Tiers.STONE.getAttackDamageBonus()) / 2,
            Tiers.IRON.getEnchantmentValue() + 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_sandstone_tool")),
            () -> Ingredient.of(Items.SANDSTONE)),
        new ResourceLocation(ModCyclic.MODID, "sandstone"),
        List.of(Tiers.WOOD), List.of(Tiers.STONE));
    //after stone then COPPER
    public static final Tier COPPER = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.IRON.getLevel(),
            (Tiers.STONE.getUses() + Tiers.IRON.getUses()) / 2, (Tiers.STONE.getSpeed() + Tiers.IRON.getSpeed()) / 2, // uses aka durability
            (Tiers.STONE.getAttackDamageBonus() + Tiers.IRON.getAttackDamageBonus()) / 2,
            Tiers.DIAMOND.getEnchantmentValue() + 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_copper_tool")),
            () -> Ingredient.of(Items.COPPER_INGOT)),
        new ResourceLocation(ModCyclic.MODID, "copper"),
        List.of(Tiers.STONE), List.of(Tiers.IRON));
    //then RON
    //after iron is AMYTH
    public static final Tier AMETHYST = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.IRON.getLevel(),
            Tiers.IRON.getUses() + 5, Tiers.IRON.getSpeed() + 0.2F, // uses aka durability 
            Tiers.IRON.getAttackDamageBonus() + 0.1F, Tiers.GOLD.getEnchantmentValue() * 2,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_amethyst_tool")),
            () -> Ingredient.of(Items.AMETHYST_SHARD)),
        new ResourceLocation(ModCyclic.MODID, "amethyst"),
        List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
    //then diamond
    //after diamond is 
    public static final Tier EMERALD = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.DIAMOND.getLevel(),
            Tiers.DIAMOND.getUses() + Tiers.GOLD.getUses(), Tiers.DIAMOND.getSpeed() * 2, // uses aka durability  
            EMERALD_DMG.get().floatValue(), Tiers.GOLD.getEnchantmentValue() + 1,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_emerald_tool")),
            () -> Ingredient.of(Items.EMERALD)),
        new ResourceLocation(ModCyclic.MODID, "emerald"),
        List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE));
    //then netherite then
    //after netherite
    public static final Tier GEMOBSIDIAN = TierSortingRegistry.registerTier(
        //harvestLevel, uses, toolSpeed, damage, enchantability
        new ForgeTier(Tiers.NETHERITE.getLevel(),
            Tiers.DIAMOND.getUses() * 4, Tiers.DIAMOND.getSpeed() * 4, // uses aka durability  
            OBS_DMG.get().floatValue(), Tiers.GOLD.getEnchantmentValue() + 1,
            BlockTags.create(new ResourceLocation(ModCyclic.MODID, "needs_obsidian_tool")),
            () -> Ingredient.of(ItemRegistry.GEM_OBSIDIAN.get())),
        new ResourceLocation(ModCyclic.MODID, "gem_obsidian"),
        List.of(Tiers.NETHERITE), List.of());
  }
}
