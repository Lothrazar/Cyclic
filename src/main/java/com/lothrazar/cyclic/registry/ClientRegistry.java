package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
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
    initColours();
  }

  private static void initColours() {
    Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
      if (stack.hasTag() && tintIndex > 0) {
        EntityType<?> thing = ForgeRegistries.ENTITIES.getValue(
            new ResourceLocation(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)));
        for (SpawnEggItem spawneggitem : SpawnEggItem.getEggs()) {
          if (spawneggitem.getType(null) == thing) {
            return spawneggitem.getColor(tintIndex - 1);
          }
        }
      }
      return -1;
    }, ItemRegistry.mob_container);
  }
}
