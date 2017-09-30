package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
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
  public static ArrayList<PotionType> potions = new ArrayList<PotionType>();
  public static PotionType potionTypeSlowfall;
  private static PotionType potionTypeBounce;
  private static final int SHORT = 1800;
  private static final int NORMAL = 3600;
  private static final int LONG = 9600;
  public static void register() {
    potionTypeSlowfall = new PotionType(
        new PotionEffect[] { new PotionEffect(PotionEffectRegistry.SLOWFALL, NORMAL), new PotionEffect(MobEffects.GLOWING, NORMAL) }).setRegistryName(new ResourceLocation(Const.MODID, "slowfall"));
    //    potionTypeSlowfall.setRegistryName(new ResourceLocation(Const.MODID, "slowfall"));
    potions.add(potionTypeSlowfall);
    potionTypeBounce = new PotionType(
        new PotionEffect[] { new PotionEffect(PotionEffectRegistry.BOUNCE, NORMAL) }).setRegistryName(new ResourceLocation(Const.MODID, "bounce"));
    potions.add(potionTypeBounce);
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<PotionType> event) {
    PotionTypeRegistry.register();
    for (PotionType b : potions) {
      event.getRegistry().register(b);
 
    }
    //    PotionHelper.addMix(PotionTypes.AWKWARD, Items.APPLE,PotionTypes.THICK);
    PotionHelper.addMix(PotionTypes.AWKWARD, Items.APPLE, potionTypeSlowfall);
    //    RecipeRegistry.addShapedOreRecipe(
    //
    //        BrewingRecipeRegistry.addRecipe(
    //            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD), 
    //            new ItemStack(Items.APPLE), 
    //            PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypeRegistry.potionTypeSlowfall))
    //        
    //        );
  }
  @SubscribeEvent
  public static void onDrink(LivingEntityUseItemEvent.Finish event) {
    List<PotionEffect> effects = PotionUtils.getEffectsFromStack(event.getItem());
    Item item = event.getItem().getItem();
    //cant double up because vanilla addpotioneffect just merges times, does not add them
    //WHAT DOES THIS FIX? Well, when i create a custom PotionType, it works with vanilla potions but not mine
    //so. lol. yep. brute force it is then eh? yup.
    if (item instanceof ItemPotion) {
      for (PotionEffect effect : effects) {
        ResourceLocation potionReg = effect.getPotion().getRegistryName();
        if (potionReg != null && potionReg.getResourceDomain().equals(Const.MODID)) {
          event.getEntityLiving().addPotionEffect(new PotionEffect(effect));
        }
      }
    }
  }
}
