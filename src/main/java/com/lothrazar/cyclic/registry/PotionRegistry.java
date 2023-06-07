package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.ModBrewingRecipe;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionRegistry {

  public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ModCyclic.MODID);
  //TODO: ender aura - pearl + awkward - no pearl/tp dmg
  //TODO: bouncy - slime + ender above 
  static final int normal = 3600;
  static final int smal = 1800;
  public static final RegistryObject<Potion> ANTIGRAVITY = POTIONS.register("antigravity", () -> new Potion(ModCyclic.MODID + "_antigravity", new MobEffectInstance(PotionEffectRegistry.ANTIGRAVITY.get(), normal, 3)));
  public static final RegistryObject<Potion> ATTACK_RANGE = POTIONS.register("attack_range", () -> new Potion(ModCyclic.MODID + "_attack_range", new MobEffectInstance(PotionEffectRegistry.ATTACK_RANGE.get(), normal)));
  public static final RegistryObject<Potion> BLIND = POTIONS.register("blind", () -> new Potion(ModCyclic.MODID + "_blind", new MobEffectInstance(MobEffects.BLINDNESS, normal)));
  public static final RegistryObject<Potion> BUTTERFINGERS = POTIONS.register("butter", () -> new Potion(ModCyclic.MODID + "_butter", new MobEffectInstance(PotionEffectRegistry.BUTTERFINGERS.get(), normal)));
  public static final RegistryObject<Potion> FLIGHT = POTIONS.register("flight", () -> new Potion(ModCyclic.MODID + "_flight", new MobEffectInstance(PotionEffectRegistry.FLIGHT.get(), normal)));
  public static final RegistryObject<Potion> FROST_WALKER = POTIONS.register("frost_walker", () -> new Potion(ModCyclic.MODID + "_frost_walker", new MobEffectInstance(PotionEffectRegistry.FROST_WALKER.get(), normal)));
  public static final RegistryObject<Potion> GRAVITY = POTIONS.register("gravity", () -> new Potion(ModCyclic.MODID + "_gravity", new MobEffectInstance(PotionEffectRegistry.GRAVITY.get(), normal)));
  public static final RegistryObject<Potion> HASTE = POTIONS.register("haste", () -> new Potion(ModCyclic.MODID + "_haste", new MobEffectInstance(MobEffects.DIG_SPEED, normal)));
  public static final RegistryObject<Potion> HASTE_STRONG = POTIONS.register("strong_haste", () -> new Potion(ModCyclic.MODID + "_strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, smal, 1)));
  public static final RegistryObject<Potion> HUNGER = POTIONS.register("hunger", () -> new Potion(ModCyclic.MODID + "_hunger", new MobEffectInstance(MobEffects.HUNGER, normal)));
  public static final RegistryObject<Potion> LEVITATION = POTIONS.register("levitation", () -> new Potion(ModCyclic.MODID + "_levitation", new MobEffectInstance(MobEffects.LEVITATION, smal)));
  public static final RegistryObject<Potion> MAGNETIC = POTIONS.register("magnetic", () -> new Potion(ModCyclic.MODID + "_magnetic", new MobEffectInstance(PotionEffectRegistry.MAGNETIC.get(), normal)));
  public static final RegistryObject<Potion> REACH_DISTANCE = POTIONS.register("reach_distance", () -> new Potion(ModCyclic.MODID + "_reach_distance", new MobEffectInstance(PotionEffectRegistry.REACH_DISTANCE.get(), normal)));
  public static final RegistryObject<Potion> RESISTANCE = POTIONS.register("resistance", () -> new Potion(ModCyclic.MODID + "_resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, smal)));
  public static final RegistryObject<Potion> STUN = POTIONS.register("stun", () -> new Potion(ModCyclic.MODID + "_stun", new MobEffectInstance(PotionEffectRegistry.STUN.get(), smal)));
  public static final RegistryObject<Potion> SWIMSPEED = POTIONS.register("swimspeed", () -> new Potion(ModCyclic.MODID + "_swimspeed", new MobEffectInstance(PotionEffectRegistry.SWIMSPEED.get(), normal)));
  public static final RegistryObject<Potion> STRONG_HUNGER = POTIONS.register("strong_hunger", () -> new Potion(ModCyclic.MODID + "_strong_hunger", new MobEffectInstance(MobEffects.HUNGER, smal, 1)));
  public static final RegistryObject<Potion> SNOWWALK = POTIONS.register("snowwalk", () -> new Potion(ModCyclic.MODID + "_snow", new MobEffectInstance(PotionEffectRegistry.SNOWWALK.get(), smal)));
  public static final RegistryObject<Potion> WATERWALK = POTIONS.register("waterwalk", () -> new Potion(ModCyclic.MODID + "_waterwalk", new MobEffectInstance(PotionEffectRegistry.WATERWALK.get(), smal)));
  public static final RegistryObject<Potion> WITHER = POTIONS.register("wither", () -> new Potion(ModCyclic.MODID + "_wither", new MobEffectInstance(MobEffects.WITHER, smal)));

  public static class PotionRecipeConfig {

    public static BooleanValue ANTIGRAVITY;
    public static BooleanValue ATTACK_RANGE;
    public static BooleanValue BLIND;
    public static BooleanValue BUTTERFINGERS;
    public static BooleanValue FLIGHT;
    public static BooleanValue FROST_WALKER;
    public static BooleanValue GRAVITY;
    public static BooleanValue HASTE;
    public static BooleanValue HUNGER;
    public static BooleanValue LEVITATION;
    public static BooleanValue MAGNETIC;
    public static BooleanValue REACH_DISTANCE;
    public static BooleanValue RESISTANCE;
    public static BooleanValue STUN;
    public static BooleanValue SWIMSPEED;
    public static BooleanValue SNOWWALK;
    public static BooleanValue WATERWALK;
    public static BooleanValue WITHER;
  }

  public static void setup() {
    final ItemStack awkwardPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
    final ItemStack thickPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.THICK);
    // Potion recipes 
    if (PotionRecipeConfig.HASTE.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.HASTE.get(), Items.EMERALD);
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionRegistry.HASTE.get()), PotionRegistry.HASTE_STRONG.get(), Items.REDSTONE);
    }
    if (PotionRecipeConfig.STUN.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.STUN.get(), Items.CLAY);
    }
    if (PotionRecipeConfig.SWIMSPEED.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.SWIMSPEED.get(), Items.DRIED_KELP_BLOCK);
    }
    if (PotionRecipeConfig.BLIND.get()) {
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION), PotionRegistry.BLIND.get(), Items.BEETROOT);
    }
    if (PotionRecipeConfig.LEVITATION.get()) {
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING), PotionRegistry.LEVITATION.get(), Items.FERMENTED_SPIDER_EYE);
    }
    if (PotionRecipeConfig.RESISTANCE.get()) {
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH), PotionRegistry.RESISTANCE.get(), Items.IRON_INGOT);
    }
    if (PotionRecipeConfig.WITHER.get()) {
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WEAKNESS), PotionRegistry.WITHER.get(), Items.NETHER_BRICK);
    }
    if (PotionRecipeConfig.HUNGER.get()) {
      basicBrewing(thickPotion.copy(), PotionRegistry.HUNGER.get(), Items.ROTTEN_FLESH);
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), HUNGER.get()), PotionRegistry.STRONG_HUNGER.get(), Items.REDSTONE);
    }
    if (PotionRecipeConfig.WATERWALK.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.WATERWALK.get(), Items.PRISMARINE_SHARD);
      basicBrewing(thickPotion.copy(), PotionRegistry.WATERWALK.get(), Items.COD);
    }
    if (PotionRecipeConfig.SNOWWALK.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.SNOWWALK.get(), Items.SNOWBALL);
    }
    if (PotionRecipeConfig.BUTTERFINGERS.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.BUTTERFINGERS.get(), Items.GOLD_INGOT);
    }
    if (PotionRecipeConfig.FROST_WALKER.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.FROST_WALKER.get(), Blocks.ICE.asItem());
    }
    if (PotionRecipeConfig.MAGNETIC.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.MAGNETIC.get(), Items.LAPIS_LAZULI);
    }
    if (PotionRecipeConfig.GRAVITY.get()) {
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LEAPING), PotionRegistry.GRAVITY.get(), Items.COPPER_INGOT);
    }
    if (PotionRecipeConfig.REACH_DISTANCE.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.REACH_DISTANCE.get(), Items.AMETHYST_SHARD);
    }
    if (PotionRecipeConfig.ATTACK_RANGE.get()) {
      basicBrewing(awkwardPotion.copy(), PotionRegistry.ATTACK_RANGE.get(), Blocks.POINTED_DRIPSTONE.asItem());
    }
    if (PotionRecipeConfig.ANTIGRAVITY.get()) {
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionRegistry.GRAVITY.get()), PotionRegistry.ANTIGRAVITY.get(), Items.FERMENTED_SPIDER_EYE);
    }
    if (PotionRecipeConfig.FLIGHT.get()) {
      basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRONG_HEALING), PotionRegistry.FLIGHT.get(), Items.CHORUS_FRUIT);
    }
  }

  private static void basicBrewing(ItemStack inputPot, Potion pot, Item item) {
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(inputPot, Ingredient.of(item), PotionUtils.setPotion(new ItemStack(Items.POTION), pot)));
  }
}
