package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    event.getRegistry().registerAll(ItemRegistry.itemMap.values().toArray(new Item[0]));
 
  }
  
   

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    // with help from
    // http://www.minecraftforge.net/forum/index.php?topic=32492.0
    // https://github.com/TheOnlySilverClaw/Birdmod/blob/master/src/main/java/silverclaw/birds/client/ClientProxyBirds.java
    // More info on proxy rendering
    // http://www.minecraftforge.net/forum/index.php?topic=27684.0
    // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod
//    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    String name;
    Item item;
    for (Block b : BlockRegistry.blocks) {
      item = Item.getItemFromBlock(b);
      name = Const.MODRES + b.getUnlocalizedName().replaceAll("tile.", "");
//      mesher.register(item, 0, new ModelResourceLocation(name, "inventory"));
//      
      
//
System.out.println("registerModelsr   "+item);
System.out.println("registerModelsr   "+name);
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name));
      
      if (b instanceof IBlockHasTESR) {
        ((IBlockHasTESR) b).initModel();
      }
    }
    for (String key : ItemRegistry.itemMap.keySet()) {
      
      item = ItemRegistry.itemMap.get(key);
      
      if(item instanceof ItemBlock){continue;}
      name = Const.MODRES + item.getUnlocalizedName().replaceAll("item.", "");
      

      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
      
//      mesher.register(item, 0, new ModelResourceLocation(name, "inventory"));
    }
  }
  
}
