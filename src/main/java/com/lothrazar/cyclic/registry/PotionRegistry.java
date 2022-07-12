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
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionRegistry {

  public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ModCyclic.MODID);
  //from 1.12.2 
  //slowfall NIX in vanilla
  //ender aura - pearl + awkward - no pearl/tp dmg
  //bouncy - slime + ender
  //frost walker ice + snow
  //magnetism - lapis + awk 
  //
  static final int normal = 3600;
  static final int smal = 1800;
  public static final RegistryObject<Potion> HASTE = POTIONS.register("haste", () -> new Potion(ModCyclic.MODID + "_haste", new MobEffectInstance(MobEffects.DIG_SPEED, normal)));
  public static final RegistryObject<Potion> STRONG_HASTE = POTIONS.register("strong_haste", () -> new Potion(ModCyclic.MODID + "_strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, smal, 1)));
  public static final RegistryObject<Potion> STUN = POTIONS.register("stun", () -> new Potion(ModCyclic.MODID + "_stun", new MobEffectInstance(PotionEffectRegistry.STUN.get(), smal)));
  public static final RegistryObject<Potion> SWIMSPEED = POTIONS.register("swimspeed", () -> new Potion(ModCyclic.MODID + "_swimspeed", new MobEffectInstance(PotionEffectRegistry.SWIMSPEED.get(), normal)));
  public static final RegistryObject<Potion> BLIND = POTIONS.register("blind", () -> new Potion(ModCyclic.MODID + "_blind", new MobEffectInstance(MobEffects.BLINDNESS, normal)));
  public static final RegistryObject<Potion> LEVITATION = POTIONS.register("levitation", () -> new Potion(ModCyclic.MODID + "_levitation", new MobEffectInstance(MobEffects.LEVITATION, smal)));
  public static final RegistryObject<Potion> HUNGER = POTIONS.register("hunger", () -> new Potion(ModCyclic.MODID + "_hunger", new MobEffectInstance(MobEffects.HUNGER, normal)));
  public static final RegistryObject<Potion> STRONG_HUNGER = POTIONS.register("strong_hunger", () -> new Potion(ModCyclic.MODID + "_strong_hunger", new MobEffectInstance(MobEffects.HUNGER, smal, 1)));
  public static final RegistryObject<Potion> WITHER = POTIONS.register("wither", () -> new Potion(ModCyclic.MODID + "_wither", new MobEffectInstance(MobEffects.WITHER, smal)));
  public static final RegistryObject<Potion> RESISTANCE = POTIONS.register("resistance", () -> new Potion(ModCyclic.MODID + "_resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, smal)));
  public static final RegistryObject<Potion> WATERWALK = POTIONS.register("waterwalk", () -> new Potion(ModCyclic.MODID + "_waterwalk", new MobEffectInstance(PotionEffectRegistry.WATERWALK.get(), smal)));
  public static final RegistryObject<Potion> SNOWWALK = POTIONS.register("snowwalk", () -> new Potion(ModCyclic.MODID + "_snow", new MobEffectInstance(PotionEffectRegistry.SNOWWALK.get(), smal)));

  public static void setup() {
    final ItemStack awkwardPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
    final ItemStack thickPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.THICK);
    // Potion recipes 
    basicBrewing(awkwardPotion.copy(), PotionRegistry.HASTE.get(), Items.EMERALD);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionRegistry.HASTE.get()), PotionRegistry.STRONG_HASTE.get(), Items.REDSTONE);
    basicBrewing(awkwardPotion.copy(), PotionRegistry.STUN.get(), Items.CLAY);
    basicBrewing(awkwardPotion.copy(), PotionRegistry.SWIMSPEED.get(), Items.DRIED_KELP_BLOCK);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION), PotionRegistry.BLIND.get(), Items.BEETROOT);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING), PotionRegistry.LEVITATION.get(), Items.FERMENTED_SPIDER_EYE);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH), PotionRegistry.RESISTANCE.get(), Items.IRON_INGOT);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WEAKNESS), PotionRegistry.WITHER.get(), Items.NETHER_BRICK);
    basicBrewing(thickPotion.copy(), PotionRegistry.HUNGER.get(), Items.ROTTEN_FLESH);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), HUNGER.get()), PotionRegistry.STRONG_HUNGER.get(), Items.REDSTONE);
    basicBrewing(awkwardPotion.copy(), PotionRegistry.WATERWALK.get(), Items.PRISMARINE_SHARD);
    basicBrewing(awkwardPotion.copy(), PotionRegistry.SNOWWALK.get(), Items.SNOWBALL);
  }

  private static void basicBrewing(ItemStack inputPot, Potion pot, Item item) {
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(inputPot, Ingredient.of(item), PotionUtils.setPotion(new ItemStack(Items.POTION), pot)));
  }
}
