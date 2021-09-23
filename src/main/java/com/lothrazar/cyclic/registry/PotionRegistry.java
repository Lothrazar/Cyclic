package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.potion.effect.StunEffect;
import com.lothrazar.cyclic.potion.effect.SwimEffect;
import com.lothrazar.cyclic.recipe.ModBrewingRecipe;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
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
  public static void onPotEffectRegistry(RegistryEvent.Register<Effect> event) {
    IForgeRegistry<Effect> r = event.getRegistry();
    PotionEffects.stun = register(r, new StunEffect(EffectType.HARMFUL, 0xcccc00), "stun");
    PotionEffects.swimspeed = register(r, new SwimEffect(EffectType.BENEFICIAL, 0x663300), "swimspeed");
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

  private static TickableEffect register(IForgeRegistry<Effect> r, TickableEffect pot, String name) {
    pot.setRegistryName(new ResourceLocation(ModCyclic.MODID, name));
    r.register(pot);
    PotionEffects.EFFECTS.add(pot);
    return pot;
  }

  @SubscribeEvent
  public static void onPotRegistry(RegistryEvent.Register<Potion> event) {
    IForgeRegistry<Potion> r = event.getRegistry();
    //TODO: config options for potions?
    int normal = 3600;
    int smal = 1800;
    r.register(new Potion(ModCyclic.MODID + "_haste", new EffectInstance(Effects.HASTE, normal)).setRegistryName(ModCyclic.MODID + ":haste"));
    r.register(new Potion(ModCyclic.MODID + "_strong_haste", new EffectInstance(Effects.HASTE, smal, 1)).setRegistryName(ModCyclic.MODID + ":strong_haste"));
    r.register(new Potion(ModCyclic.MODID + "_stun", new EffectInstance(PotionEffects.stun, smal)).setRegistryName(ModCyclic.MODID + ":stun"));
    r.register(new Potion(ModCyclic.MODID + "_swimspeed", new EffectInstance(PotionEffects.swimspeed, normal)).setRegistryName(ModCyclic.MODID + ":swimspeed"));
    r.register(new Potion(ModCyclic.MODID + "_blind", new EffectInstance(Effects.BLINDNESS, normal)).setRegistryName(ModCyclic.MODID + ":blind"));
    r.register(new Potion(ModCyclic.MODID + "_levitation", new EffectInstance(Effects.LEVITATION, smal)).setRegistryName(ModCyclic.MODID + ":levitation"));
    r.register(new Potion(ModCyclic.MODID + "_hunger", new EffectInstance(Effects.HUNGER, normal)).setRegistryName(ModCyclic.MODID + ":hunger"));
    r.register(new Potion(ModCyclic.MODID + "_wither", new EffectInstance(Effects.WITHER, smal)).setRegistryName(ModCyclic.MODID + ":wither"));
    r.register(new Potion(ModCyclic.MODID + "_resistance", new EffectInstance(Effects.RESISTANCE, smal)).setRegistryName(ModCyclic.MODID + ":resistance"));
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
    public static Potion strong_haste;
    @ObjectHolder(ModCyclic.MODID + ":haste")
    public static Potion haste;
    @ObjectHolder(ModCyclic.MODID + ":stun")
    public static Potion stun;
    @ObjectHolder(ModCyclic.MODID + ":swimspeed")
    public static Potion swimspeed;
    @ObjectHolder(ModCyclic.MODID + ":blind")
    public static Potion blind;
    @ObjectHolder(ModCyclic.MODID + ":levitation")
    public static Potion levitation;
    @ObjectHolder(ModCyclic.MODID + ":hunger")
    public static Potion hunger;
    @ObjectHolder(ModCyclic.MODID + ":wither")
    public static Potion wither;
    @ObjectHolder(ModCyclic.MODID + ":resistance")
    public static Potion resistance;
    //    @ObjectHolder(ModCyclic.MODID + ":saturation")
    //    public static Potion saturation;
  }
  //resistance : strength pot + iron ingot
  //wither : fermented spider eye + weakness pot 
  //saturation = cake + hunger pot 

  public static void setup(FMLCommonSetupEvent event) {
    ///haste recipes
    final ItemStack awkwardPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD);
    final ItemStack thickPotion = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.THICK);
    basicBrewing(awkwardPotion.copy(), PotionRegistry.PotionItem.haste, Items.EMERALD);
    //    splashBrewing(PotionRegistry.PotionItem.haste, Items.EMERALD);
    basicBrewing(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionItem.haste),
        PotionRegistry.PotionItem.strong_haste, Items.REDSTONE);
    //    splashBrewing(PotionRegistry.PotionItem.strong_haste, Items.EMERALD);
    //    lingerBrewing(PotionRegistry.PotionItem.haste, Items.EMERALD);
    //STUN recipes
    basicBrewing(awkwardPotion.copy(), PotionRegistry.PotionItem.stun, Items.CLAY);
    //    splashBrewing(PotionRegistry.PotionItem.stun, Items.CLAY);
    //    lingerBrewing(PotionRegistry.PotionItem.stun, Items.CLAY);
    //swimspeed recipes
    basicBrewing(awkwardPotion.copy(), PotionRegistry.PotionItem.swimspeed, Items.DRIED_KELP_BLOCK);
    //    splashBrewing(PotionRegistry.PotionItem.swimspeed, Items.DRIED_KELP_BLOCK);
    //    lingerBrewing(PotionRegistry.PotionItem.swimspeed, Items.DRIED_KELP_BLOCK);
    //    PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionItem.haste).gr
    basicBrewing(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.NIGHT_VISION), PotionRegistry.PotionItem.blind, Items.BEETROOT);
    //    splashBrewing(PotionRegistry.PotionItem.blind, Items.BEETROOT);
    //    lingerBrewing(PotionRegistry.PotionItem.blind, Items.BEETROOT);
    basicBrewing(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.SLOW_FALLING), PotionItem.levitation, Items.FERMENTED_SPIDER_EYE);
    basicBrewing(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.STRENGTH), PotionItem.resistance, Items.IRON_INGOT);
    basicBrewing(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WEAKNESS), PotionItem.wither, Items.NETHER_BRICK);
    //    basicBrewing(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), PotionItem.hunger), PotionItem.saturation, Items.CAKE);
    basicBrewing(thickPotion.copy(), PotionRegistry.PotionItem.hunger, Items.ROTTEN_FLESH);
  }

  private static void basicBrewing(ItemStack inputPot, Potion pot, Item item) {
    //hmm wat 
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(inputPot, Ingredient.fromItems(item),
        PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), pot)));
  }

  static void splashBrewing(Potion pot, Item item) {
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(PotionUtils.addPotionToItemStack(
        new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD),
        Ingredient.fromStacks(new ItemStack(item)),
        PotionUtils.addPotionToItemStack(
            new ItemStack(Items.SPLASH_POTION), pot)));
  }

  static void lingerBrewing(Potion pot, Item item) {
    BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(PotionUtils.addPotionToItemStack(
        new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD),
        Ingredient.fromStacks(new ItemStack(item)),
        PotionUtils.addPotionToItemStack(
            new ItemStack(Items.LINGERING_POTION), pot)));
  }
}
