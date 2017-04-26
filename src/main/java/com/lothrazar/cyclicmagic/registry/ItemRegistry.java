package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.CyclicGuideBook;
import com.lothrazar.cyclicmagic.CyclicGuideBook.CategoryType;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
  public static Map<String, Item> itemMap = new HashMap<String, Item>();
  public static Map<Item, CategoryType> inGuidebookMap = new HashMap<Item, CategoryType>();
  public static Item addItem(Item item, String key, CategoryType inGuidebook) {
    item.setUnlocalizedName(key);
    itemMap.put(key, item);
    if (inGuidebook != null) {
      inGuidebookMap.put(item, inGuidebook);
    }
    return item;
  }
  public static Item addItem(Item item, String key) {
    return addItem(item, key, CategoryType.ITEM);//defaults to in guide book with its own standalone page
  }
  public static void registerWithJeiDescription(Item item) {
    JeiDescriptionRegistry.registerWithJeiDescription(item);
  }
 
  public static void register() {
    Item item;
    for (String key : ItemRegistry.itemMap.keySet()) {
      item = ItemRegistry.itemMap.get(key);
      GameRegistry.register(item, new ResourceLocation(Const.MODID, key));
      item.setCreativeTab(ModCyclic.TAB);
      if (item instanceof IHasConfig) {
        ConfigRegistry.register((IHasConfig) item);
      }
      IRecipe recipe = null;
      if (item instanceof IHasRecipe) {
        recipe = ((IHasRecipe) item).addRecipe();
      }
      if (inGuidebookMap.get(item) != null) {
        CyclicGuideBook.addPageItem(item, recipe,inGuidebookMap.get(item));
      }
    }
  }
}
