package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.module.ItemPotionModule;
import com.lothrazar.cyclicmagic.potion.PotionTypeCyclic;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PotionTypeRegistry {
  public static List<PotionTypeCyclic> potions = new ArrayList<PotionTypeCyclic>();
  private static PotionTypeCyclic potionTypeSlowfall;
  private static PotionTypeCyclic potionTypeBounce;
  private static PotionTypeCyclic potionTypeWaterwalk;
  private static PotionTypeCyclic potionTypeSnow;
  private static PotionTypeCyclic potionTypeSwim;
  private static PotionTypeCyclic potionTypeMagnet;
  private static PotionTypeCyclic potionTypeLevitation;
  private static PotionTypeCyclic potionTypeHaste;
  private static PotionTypeCyclic potionTypeResistance;
  private static PotionTypeCyclic potionHealth;
  private static PotionTypeCyclic potionEnder;
  private static PotionTypeCyclic potionTypeLuck;
  private static final int SHORT = 1800;
  private static final int NORMAL = 3600;
  private static final int LONG = 9600;
  public static void register() {
    if (ItemPotionModule.enableSlowfall) {
      potionTypeSlowfall = addPotionType(new PotionEffect(PotionEffectRegistry.SLOWFALL, NORMAL), "slowfall", new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()));
      potions.add(potionTypeSlowfall);
    }
    if (ItemPotionModule.enableWaterwalk) {
      potionTypeWaterwalk = addPotionType(new PotionEffect(PotionEffectRegistry.WATERWALK, NORMAL), "waterwalk", Items.BLAZE_ROD);
      potions.add(potionTypeWaterwalk);
    }
    if (ItemPotionModule.enableSnow) {
      potionTypeSnow = addPotionType(new PotionEffect(PotionEffectRegistry.SNOW, NORMAL), "snow", Items.SNOWBALL);
      potions.add(potionTypeSnow);
    }
    if (ItemPotionModule.enableSwimspeed) {
      potionTypeSwim = addPotionType(new PotionEffect(PotionEffectRegistry.SWIMSPEED, NORMAL), "swim", Items.CARROT_ON_A_STICK);
      potions.add(potionTypeSwim);
    }
    if (ItemPotionModule.enableBounce) {
      potionTypeBounce = addPotionType(new PotionEffect(PotionEffectRegistry.BOUNCE, NORMAL), "bounce", Items.SLIME_BALL);
      potions.add(potionTypeBounce);
    }
    if (ItemPotionModule.enableMagnet) {
      potionTypeMagnet = addPotionType(new PotionEffect(PotionEffectRegistry.MAGNET, NORMAL), "magnet", new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
      potions.add(potionTypeMagnet);
    }
    if (ItemPotionModule.enableLevit) {
      potionTypeLevitation = addPotionType(new PotionEffect(MobEffects.LEVITATION, NORMAL), "levitation", Items.CHORUS_FRUIT);
      potions.add(potionTypeLevitation);
    }
    if (ItemPotionModule.enableHaste) {
      potionTypeHaste = addPotionType(new PotionEffect(MobEffects.HASTE, NORMAL), "haste", Items.EMERALD);
      potions.add(potionTypeHaste);
    }
    if (ItemPotionModule.enableResist) {
      potionTypeResistance = addPotionType(new PotionEffect(MobEffects.RESISTANCE, NORMAL), "resistance", Items.DIAMOND);
      potions.add(potionTypeResistance);
    }
    if (ItemPotionModule.enableHBoost) {
      potionHealth = addPotionType(new PotionEffect(MobEffects.HEALTH_BOOST, NORMAL, Const.Potions.V), "healthboost", Items.GOLDEN_APPLE);
      potions.add(potionHealth);
    }
    if (ItemPotionModule.enableEnder) {
      potionEnder = addPotionType(new PotionEffect(PotionEffectRegistry.ENDER, NORMAL), "ender", Items.ENDER_PEARL);
      potions.add(potionEnder);
    }
    if(ItemPotionModule.enableLuck){

      potionTypeLuck = addPotionType(new PotionEffect(MobEffects.LUCK, NORMAL), "luck",     new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage()));
      potions.add(potionTypeLuck);
    }
    //health boost  Items.GOLDEN_APPLE
    //ender pearl 
    //resistance
    //
  }
  private static PotionTypeCyclic addPotionType(PotionEffect eff, String name, ItemStack item) {
    return new PotionTypeCyclic(name, new PotionEffect[] { eff }, item);
  }
  private static PotionTypeCyclic addPotionType(PotionEffect eff, String name, Item item) {
    return addPotionType(eff, name, new ItemStack(item));
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<PotionType> event) {
    PotionTypeRegistry.register();
    for (PotionTypeCyclic pt : potions) {
      event.getRegistry().register(pt);
      pt.addMix();
    }
    //    //    PotionHelper.addMix(PotionTypes.AWKWARD, Items.APPLE,PotionTypes.THICK);
    //    PotionHelper.addMix(PotionTypes.AWKWARD, Items.APPLE, potionTypeSlowfall);
    //    RecipeRegistry.addShapedOreRecipe(
    //
    //        BrewingRecipeRegistry.addRecipe(
    //            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), 
    //            new ItemStack(Items.APPLE), 
    //            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypeRegistry.potionTypeSlowfall))
    //        
    //        );
  }
  //  @SubscribeEvent
  //  public static void onDrink(LivingEntityUseItemEvent.Finish event) {
  //    List<PotionEffect> effects = PotionUtils.getEffectsFromStack(event.getItem());
  //    Item item = event.getItem().getItem();
  //    //cant double up because vanilla addpotioneffect just merges times, does not add them
  //    //WHAT DOES THIS FIX? Well, when i create a custom PotionType, it works with vanilla potions but not mine
  //    //so. lol. yep. brute force it is then eh? yup.
  //    if (item instanceof ItemPotion) {
  //      for (PotionEffect effect : effects) {
  //        ResourceLocation potionReg = effect.getPotion().getRegistryName();
  //        if (potionReg != null && potionReg.getResourceDomain().equals(Const.MODID)) {
  //          event.getEntityLiving().addPotionEffect(new PotionEffect(effect));
  //        }
  //      }
  //    }
  //  }
}
