package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.storagebag.StorageBagItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

@OnlyIn(Dist.CLIENT)
public class ClientRegistry {

  public static void setup() {
    for (BlockBase b : BlockRegistry.blocks) {
      b.registerClient();
    }
    for (ItemBase i : ItemRegistry.items) {
      i.registerClient();
    }
    initColours();
  }

  private static void initColours() {
    Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
      if (stack.getItem() == ItemRegistry.storage_bag) {
        // ok
        if (tintIndex == 0) {//layer zero is outline, ignore this 
          return 0xFFFFFFFF;
        }
        //layer 1 is overlay  
        int c = StorageBagItem.getColour(stack);
        return c;
      }
      else if (stack.getItem() == ItemRegistry.mob_container) {
        if (stack.hasTag() && tintIndex > 0) {
          //what entity is inside
          EntityType<?> thing = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)));
          //pull the colours from the egg
          for (SpawnEggItem spawneggitem : SpawnEggItem.getEggs()) {
            if (spawneggitem.getType(null) == thing) {
              return spawneggitem.getColor(tintIndex - 1);
            }
          }
        }
      }
      return -1;
    }, ItemRegistry.mob_container, ItemRegistry.storage_bag);
  }
}
