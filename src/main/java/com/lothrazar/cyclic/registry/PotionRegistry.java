package com.lothrazar.cyclic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.potion.TickableEffect;
import com.lothrazar.cyclic.potion.effect.StunEffect;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipe;
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
    StunEffect stun = new StunEffect(EffectType.HARMFUL, 14605835);
    stun.setRegistryName(new ResourceLocation(ModCyclic.MODID, "stun"));
    stun.addAttributesModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070636", -50, AttributeModifier.Operation.ADDITION);
    //    stun.addAttributesModifier(SharedMonsterAttributes.MAX_HEALTH, "92AEAA56-376B-4498-935B-2F7F68070636", 2, AttributeModifier.Operation.ADDITION);
    r.register(stun);
    PotionEffects.effects.add(stun);
  }

  @SubscribeEvent
  public static void onPotRegistry(RegistryEvent.Register<Potion> event) {
    IForgeRegistry<Potion> r = event.getRegistry();
    r.register(new Potion(ModCyclic.MODID + "_stun", new EffectInstance(PotionEffects.stun, 1800)).setRegistryName(ModCyclic.MODID + ":stun"));
  }

  public static class PotionEffects {

    public static final List<TickableEffect> effects = new ArrayList<TickableEffect>();
    @ObjectHolder(ModCyclic.MODID + ":stun")
    public static StunEffect stun;
  }

  public static class PotionItem {

    @ObjectHolder(ModCyclic.MODID + ":stun")
    public static Potion stun;
  }

  public static void setup(FMLCommonSetupEvent event) {
    ItemStack AWKWARD = PotionUtils.addPotionToItemStack(
        new ItemStack(Items.POTION), Potions.AWKWARD);
    //hmm wat 
    BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.fromStacks(AWKWARD), Ingredient.fromItems(Items.CLAY),
        PotionUtils.addPotionToItemStack(
            new ItemStack(Items.POTION), PotionRegistry.PotionItem.stun)));
    Ingredient GUNPOWDER = Ingredient.fromStacks(new ItemStack(Items.GUNPOWDER));
    //    Ingredient GLOWSTONE = Ingredient.fromStacks(new ItemStack(Items.GLOWSTONE));
    //    Ingredient REDSTONE = Ingredient.fromStacks(new ItemStack(Items.REDSTONE));
    //    Ingredient DRAG = Ingredient.fromStacks(new ItemStack(Items.DRAGON_BREATH));
    //
    BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(
        new ItemStack(Items.POTION), PotionRegistry.PotionItem.stun)), GUNPOWDER, PotionUtils.addPotionToItemStack(
            new ItemStack(Items.SPLASH_POTION), PotionRegistry.PotionItem.stun)));
    //lingering potion recipe
    BrewingRecipeRegistry.addRecipe(new BrewingRecipe(Ingredient.fromStacks(PotionUtils.addPotionToItemStack(
        new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD)), Ingredient.fromItems(Items.CLAY),
        PotionUtils.addPotionToItemStack(
            new ItemStack(Items.LINGERING_POTION), PotionRegistry.PotionItem.stun)));
  }
}
