package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
  public static Map<String, Item> itemMap = new HashMap<String, Item>();
  public static void register(Item item, String key, GuideCategory cat) {
    item.setUnlocalizedName(key);
    item.setRegistryName(new ResourceLocation(Const.MODID, key));
    itemMap.put(key, item);
    item = ItemRegistry.itemMap.get(key);
    item.setCreativeTab(ModCyclic.TAB);
    if (item instanceof IHasConfig) {
      ConfigRegistry.register((IHasConfig) item);
    }
    IRecipe recipe = null;
    if (item instanceof IHasRecipe) {
      recipe = ((IHasRecipe) item).addRecipe();
    }
    if (cat != null) {
      GuideRegistry.register(cat, item, recipe, null);
    }
  }
  public static void register(Item item, String key) {
    register(item, key, GuideCategory.ITEM);//defaults to in guide book with its own standalone page
  }
  public static void registerWithJeiDescription(Item item) {
    JeiDescriptionRegistry.registerWithJeiDescription(item);
  }
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Item> event) {
    Item item;
    for (String key : ItemRegistry.itemMap.keySet()) {
      item = ItemRegistry.itemMap.get(key);
      event.getRegistry().register(item);
    }
  }
}
