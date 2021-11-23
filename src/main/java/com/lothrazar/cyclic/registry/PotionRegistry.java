package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.potion.effect.StunEffect;
import com.lothrazar.cyclic.potion.effect.SwimEffect;
import com.lothrazar.cyclic.recipe.ModBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionRegistry {

  @SubscribeEvent
  public static void onPotEffectRegistry(RegistryEvent.Register<MobEffect> event) {
    IForgeRegistry<MobEffect> r = event.getRegistry();
    PotionEffects.stun = register(r, new StunEffect(MobEffectCategory.HARMFUL, 0xcccc00), "stun");
    PotionEffects.swimspeed = register(r, new SwimEffect(MobEffectCategory.BENEFICIAL, 0x663300), "swimspeed");
    //from 1.12.2 
    //slowfall NIX in vanilla
    //ender aura - pearl + awkward - no pearl/tp dmg
    //snow - snowball + awkward(change?) = snow
    //butterfingers - gold + awkward (change to ?)
    //waterwalking-  fish + snow
    //bouncy - slime + ender
    //frost walker ice + snow
    //magnetism - lapis + awk
    //saturation from hunger +cake
    //    r.register(new Potion(ModCyclic.MODID + "_saturation", new EffectInstance(Effects.SATURATION, 3600)).setRegistryName(ModCyclic.MODID + ":saturation"));
  }

  private static TickableEffect register(IForgeRegistry<MobEffect> r, TickableEffect pot, String name) {
    pot.setRegistryName(new ResourceLocation(ModCyclic.MODID, name));
    r.register(pot);
    PotionEffects.EFFECTS.add(pot);
    return pot;
  }

  @SubscribeEvent
  public static void onPotRegistry(RegistryEvent.Register<Potion> event) {
    IForgeRegistry<Potion> r = event.getRegistry();
    int normal = 3600;
    int smal = 1800;
    r.register(new Potion(ModCyclic.MODID + "_haste", new MobEffectInstance(MobEffects.DIG_SPEED, normal)).setRegistryName(ModCyclic.MODID + ":haste"));
    r.register(new Potion(ModCyclic.MODID + "_strong_haste", new MobEffectInstance(MobEffects.DIG_SPEED, smal, 1)).setRegistryName(ModCyclic.MODID + ":strong_haste")); // strong 1 level instead of default 0
    r.register(new Potion(ModCyclic.MODID + "_stun", new MobEffectInstance(PotionEffects.stun, smal)).setRegistryName(ModCyclic.MODID + ":stun"));
    r.register(new Potion(ModCyclic.MODID + "_swimspeed", new MobEffectInstance(PotionEffects.swimspeed, normal)).setRegistryName(ModCyclic.MODID + ":swimspeed"));
    r.register(new Potion(ModCyclic.MODID + "_blind", new MobEffectInstance(MobEffects.BLINDNESS, normal)).setRegistryName(ModCyclic.MODID + ":blind"));
    r.register(new Potion(ModCyclic.MODID + "_levitation", new MobEffectInstance(MobEffects.LEVITATION, smal)).setRegistryName(ModCyclic.MODID + ":levitation"));
    r.register(new Potion(ModCyclic.MODID + "_hunger", new MobEffectInstance(MobEffects.HUNGER, normal)).setRegistryName(ModCyclic.MODID + ":hunger"));
    r.register(new Potion(ModCyclic.MODID + "_strong_hunger", new MobEffectInstance(MobEffects.HUNGER, smal, 1)).setRegistryName(ModCyclic.MODID + ":strong_hunger")); // strong 1 level instead of default 0
    r.register(new Potion(ModCyclic.MODID + "_wither", new MobEffectInstance(MobEffects.WITHER, smal)).setRegistryName(ModCyclic.MODID + ":wither"));
    r.register(new Potion(ModCyclic.MODID + "_resistance", new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, smal)).setRegistryName(ModCyclic.MODID + ":resistance"));
  }

  public static class PotionEffects {

    //for events
    public static final List<TickableEffect> EFFECTS = new ArrayList<TickableEffect>();
    @ObjectHolder(ModCyclic.MODID + ":stun")
    public static TickableEffect stun;
    @ObjectHolder(ModCyclic.MODID + ":swimspeed")
    public static TickableEffect swimspeed;
  }

  public static class PotionItem {

    @ObjectHolder(ModCyclic.MODID + ":strong_haste")
    public static Potion STRONG_HASTE;
    @ObjectHolder(ModCyclic.MODID + ":haste")
    public static Potion HASTE;
    @ObjectHolder(ModCyclic.MODID + ":stun")
    public static Potion STUN;
    @ObjectHolder(ModCyclic.MODID + ":swimspeed")
    public static Potion SWIMSPEED;
    @ObjectHolder(ModCyclic.MODID + ":blind")
    public static Potion BLIND;
    @ObjectHolder(ModCyclic.MODID + ":levitation")
    public static Potion LEVITATION;
    @ObjectHolder(ModCyclic.MODID + ":hunger")
    public static Potion HUNGER;
    @ObjectHolder(ModCyclic.MODID + ":strong_hunger")
    public static Potion STRONG_HUNGER;
    @ObjectHolder(ModCyclic.MODID + ":wither")
    public static Potion WITHER;
    @ObjectHolder(ModCyclic.MODID + ":resistance")
    public static Potion RESISTANCE;
  }
  //resistance : strength pot + iron ingot
  //wither : fermented spider eye + weakness pot 
  //saturation = cake + hunger pot 

  public static void setup(FMLCommonSetupEvent event) {
    ///haste recipes
    final ItemStack awkwardPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD);
    final ItemStack thickPotion = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.THICK);
    basicBrewing(awkwardPotion.copy(), PotionRegistry.PotionItem.HASTE, Items.EMERALD);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionItem.HASTE),
        PotionRegistry.PotionItem.STRONG_HASTE, Items.REDSTONE);
    //STUN recipes
    basicBrewing(awkwardPotion.copy(), PotionRegistry.PotionItem.STUN, Items.CLAY);
    //swimspeed recipes
    basicBrewing(awkwardPotion.copy(), PotionRegistry.PotionItem.SWIMSPEED, Items.DRIED_KELP_BLOCK);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.NIGHT_VISION), PotionRegistry.PotionItem.BLIND, Items.BEETROOT);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SLOW_FALLING), PotionItem.LEVITATION, Items.FERMENTED_SPIDER_EYE);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.STRENGTH), PotionItem.RESISTANCE, Items.IRON_INGOT);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WEAKNESS), PotionItem.WITHER, Items.NETHER_BRICK);
    basicBrewing(thickPotion.copy(), PotionRegistry.PotionItem.HUNGER, Items.ROTTEN_FLESH);
    basicBrewing(PotionUtils.setPotion(new ItemStack(Items.POTION), PotionItem.HUNGER),
        PotionRegistry.PotionItem.STRONG_HUNGER, Items.REDSTONE);
  }

  private static void basicBrewing(ItemStack inputPot, Potion pot, Item item) {
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(inputPot, Ingredient.of(item), PotionUtils.setPotion(new ItemStack(Items.POTION), pot)));
  }

  static void splashBrewing(Potion pot, Item item) {
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(PotionUtils.setPotion(
        new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD),
        Ingredient.of(new ItemStack(item)),
        PotionUtils.setPotion(
            new ItemStack(Items.SPLASH_POTION), pot)));
  }

  static void lingerBrewing(Potion pot, Item item) {
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(PotionUtils.setPotion(
        new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD),
        Ingredient.of(new ItemStack(item)),
        PotionUtils.setPotion(
            new ItemStack(Items.LINGERING_POTION), pot)));
  }
}
