package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.IHasOreDict;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.oredict.OreDictionary;

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
  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<Item> event) {
    // event.getRegistry().registerAll(ItemRegistry.itemMap.values().toArray(new Item[0]));
    //new registries are crazy wacky. so ore dict DOES NOT WORK in block reg, stack becomes empty
    for (Item item : ItemRegistry.itemMap.values()) {
      event.getRegistry().register(item);
      Block blockItem = Block.getBlockFromItem(item);
      if (blockItem != null && blockItem != Blocks.AIR) {
        if (blockItem instanceof IHasOreDict) {
          String oreName = ((IHasOreDict) blockItem).getOre();
          OreDictionary.registerOre(oreName, blockItem);
          ModCyclic.logger.info("Registered ore dict entry " + oreName + " : " + blockItem);
        }
        //hacky-ish way to register smelting.. we do not have ability do to this inside block class anymore
        if (blockItem instanceof BlockDimensionOre) {
          BlockDimensionOre ore = (BlockDimensionOre) blockItem;
          GameRegistry.addSmelting(item, ore.getSmeltingOutput(), 1);
        }
        if (blockItem instanceof IHasRecipe) {
          //ModCyclic.logger.info("item to block recipe?? " + blockItem);
          ((IHasRecipe) blockItem).addRecipe();
        }
      }
    }
    //    
    event.getRegistry().register(new ItemBlock(Block.getBlockFromName(Const.MODRES + "item_pipe"))
        .setRegistryName(new ResourceLocation(Const.MODID, "item_pipe")));
    //  
    event.getRegistry().register(new ItemBlock(Block.getBlockFromName(Const.MODRES + "fluid_pipe"))
        .setRegistryName(new ResourceLocation(Const.MODID, "fluid_pipe")));
    //  
    event.getRegistry().register(new ItemBlock(Block.getBlockFromName(Const.MODRES + "energy_pipe"))
        .setRegistryName(new ResourceLocation(Const.MODID, "energy_pipe")));
    
    
    
    
  }
//  @SideOnly(Side.CLIENT)
//  private static final ModelResourceLocation ITEM_MODEL = new ModelResourceLocation(
//      new ResourceLocation(Const.MODID, "cable"), "inventory");
  @SideOnly(Side.CLIENT)
  private static final IStateMapper STATE_MAPPER = new StateMapperBase() {
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
      return new ModelResourceLocation(state.getBlock().getRegistryName(), "normal");
    }
  };
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    //insomniaKitten
    Block CABLE_BLOCK = Block.getBlockFromName(Const.MODRES + "item_pipe");
    Item CABLE_ITEM = Item.getItemFromBlock(CABLE_BLOCK);
    ModelLoader.setCustomModelResourceLocation(CABLE_ITEM, 0,  new ModelResourceLocation(
        new ResourceLocation(Const.MODID, "item_pipe"), "inventory"));
    ModelLoader.setCustomStateMapper(CABLE_BLOCK, STATE_MAPPER);
    //TODO: CABLE REGISTRY OR SOMETHING
    Block FCABLE_BLOCK = Block.getBlockFromName(Const.MODRES + "fluid_pipe");
    Item FCABLE_ITEM = Item.getItemFromBlock(FCABLE_BLOCK);
    ModelLoader.setCustomModelResourceLocation(FCABLE_ITEM, 0,  new ModelResourceLocation(
        new ResourceLocation(Const.MODID, "fluid_pipe"), "inventory"));
    ModelLoader.setCustomStateMapper(FCABLE_BLOCK, STATE_MAPPER);
    //TODO: CABLE REGISTRY OR SOMETHING
    Block ECABLE_BLOCK = Block.getBlockFromName(Const.MODRES + "energy_pipe");
    Item ECABLE_ITEM = Item.getItemFromBlock(ECABLE_BLOCK);
    ModelLoader.setCustomModelResourceLocation(ECABLE_ITEM, 0,  new ModelResourceLocation(
        new ResourceLocation(Const.MODID, "energy_pipe"), "inventory"));
    ModelLoader.setCustomStateMapper(FCABLE_BLOCK, STATE_MAPPER);
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
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name));
      if (b instanceof IBlockHasTESR) {
        ((IBlockHasTESR) b).initModel();
      }
    }
    for (String key : ItemRegistry.itemMap.keySet()) {
      item = ItemRegistry.itemMap.get(key);
      if (item instanceof ItemBlock) {
        continue;
      }
      name = Const.MODRES + item.getUnlocalizedName().replaceAll("item.", "");
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
    }
  }
}
