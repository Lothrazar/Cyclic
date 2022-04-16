package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class MaterialShieldRegistry {

  // InventoryMenu.BLOCK_ATLAS was TextureAtlas.LOCATION_BLOCKS
  public static final Material SHIELD_BASE_WOOD = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModCyclic.MODID, "entity/shield/wood_base"));
  public static final Material SHIELD_BASE_WOOD_NOPATTERN = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModCyclic.MODID, "entity/shield/wood_base_nopattern"));
  public static final Material SHIELD_BASE_LEATHER = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModCyclic.MODID, "entity/shield/leather_base"));
  public static final Material SHIELD_BASE_LEATHER_NOPATTERN = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModCyclic.MODID, "entity/shield/leather_base_nopattern"));
  public static final Material SHIELD_BASE_FLINT = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModCyclic.MODID, "entity/shield/flint_base"));
  public static final Material SHIELD_BASE_FLINT_NOPATTERN = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(ModCyclic.MODID, "entity/shield/flint_base_nopattern"));
}
