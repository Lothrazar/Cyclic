/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.cable.BlockCableBase;
import com.lothrazar.cyclicmagic.block.ore.BlockDimensionOre;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasOreDict;
import com.lothrazar.cyclicmagic.core.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import com.lothrazar.cyclicmagic.util.data.Const;
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

  public static List<Item> itemList = new ArrayList<Item>();

  public static void register(Item item, String key, GuideCategory cat) {
    item.setUnlocalizedName(key);
    item.setRegistryName(new ResourceLocation(Const.MODID, key));
    itemList.add(item);
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
    for (Item item : ItemRegistry.itemList) {
      event.getRegistry().register(item);
      Block blockItem = Block.getBlockFromItem(item);
      if (blockItem != null && blockItem != Blocks.AIR) {
        if (blockItem instanceof IHasOreDict) {
          String oreName = ((IHasOreDict) blockItem).getOreDict();
          OreDictionary.registerOre(oreName, blockItem);
          ModCyclic.logger.info("Registered ore dict entry " + oreName + " : " + blockItem);
        }
        //hacky-ish way to register smelting.. we do not have ability do to this inside block class anymore
        if (blockItem instanceof BlockDimensionOre) {
          BlockDimensionOre ore = (BlockDimensionOre) blockItem;
          if (ore.getSmeltingOutput() != null) {
            GameRegistry.addSmelting(item, ore.getSmeltingOutput(), 1);
          }
        }
        if (blockItem instanceof IHasRecipe) {
          //ModCyclic.logger.info("item to block recipe?? " + blockItem);
          ((IHasRecipe) blockItem).addRecipe();
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    final IStateMapper STATE_MAPPER = new StateMapperBase() {

      @Override
      protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(state.getBlock().getRegistryName(), "normal");
      }
    };
    // with help from
    // http://www.minecraftforge.net/forum/index.php?topic=32492.0
    // https://github.com/TheOnlySilverClaw/Birdmod/blob/master/src/main/java/silverclaw/birds/client/ClientProxyBirds.java
    // More info on proxy rendering
    // http://www.minecraftforge.net/forum/index.php?topic=27684.0
    // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod
    //    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    String name;
    // 
    for (Item item : ItemRegistry.itemList) {
      if (item instanceof ItemBlock) {
        continue;
      }
      name = Const.MODRES + item.getUnlocalizedName().replaceAll("item.", "");
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
    }
    Item item;
    for (Block b : BlockRegistry.blocks) {
      item = Item.getItemFromBlock(b);
      if (b instanceof BlockCableBase) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
            new ResourceLocation(Const.MODID, b.getUnlocalizedName().replaceAll("tile.", "")), "inventory"));
        ModelLoader.setCustomStateMapper(b, STATE_MAPPER);
        //TODO: CABLE REGISTRY OR SOMETHING
        continue;
      }
      name = Const.MODRES + b.getUnlocalizedName().replaceAll("tile.", "");
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "inventory"));
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name));
      if (b instanceof IBlockHasTESR) {
        ((IBlockHasTESR) b).initModel();
      }
    }
  }
}
