package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
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
        new PotionEffect[] { new PotionEffect(PotionEffectRegistry.SLOWFALL, NORMAL) 
            ,new PotionEffect(MobEffects.GLOWING, NORMAL) 
            
        }).setRegistryName(new ResourceLocation(Const.MODID, "slowfall"));
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
      //      System.out.println("ESSSSSSSSSSSS"+b.getRegistryName());//
      // PotionHelper.addMix(PotionTypes.AWKWARD, Items.APPLE,b);
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
}
