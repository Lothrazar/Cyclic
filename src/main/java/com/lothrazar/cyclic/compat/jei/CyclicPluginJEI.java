package com.lothrazar.cyclic.compat.jei;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.crafter.ContainerCrafter;
import com.lothrazar.cyclic.block.melter.ContainerMelter;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.melter.ScreenMelter;
import com.lothrazar.cyclic.block.solidifier.ContainerSolidifier;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.block.solidifier.ScreenSolidifier;
import com.lothrazar.cyclic.item.crafting.CraftingBagContainer;
import com.lothrazar.cyclic.item.craftingsave.CraftingStickContainer;
import com.lothrazar.cyclic.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class CyclicPluginJEI implements IModPlugin {

  private ResourceLocation id = new ResourceLocation(ModCyclic.MODID, "jei");

  @Override
  public ResourceLocation getPluginUid() {
    return id;
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new MelterRecipeCategory(guiHelper));
    registry.addRecipeCategories(new SolidifierRecipeCategory(guiHelper));
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.crafter.asItem(), 1), VanillaRecipeCategoryUid.CRAFTING);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    registry.addRecipes(RecipeMelter.RECIPES, MelterRecipeCategory.id);
    registry.addRecipes(RecipeSolidifier.RECIPES, SolidifierRecipeCategory.id);
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registry) {
    registry.addRecipeClickArea(ScreenMelter.class,
        75, 20,
        40, 26, MelterRecipeCategory.id);
    registry.addRecipeClickArea(ScreenSolidifier.class,
        75, 20,
        40, 26, SolidifierRecipeCategory.id);
  }

  @Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
    registry.addRecipeTransferHandler(ContainerMelter.class, MelterRecipeCategory.id,
        0, 2, //recipeSLotStart, recipeSlotCount
        2, 4 * 9);// inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerSolidifier.class, SolidifierRecipeCategory.id,
        0, 3, //recipeSLotStart, recipeSlotCount
        3, 4 * 9);// inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerCrafter.class, VanillaRecipeCategoryUid.CRAFTING,
        10, 9, //recipeSLotStart, recipeSlotCount
        30, 4 * 9);// inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(CraftingBagContainer.class, VanillaRecipeCategoryUid.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, 4 * 9);// inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(CraftingStickContainer.class, VanillaRecipeCategoryUid.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, 4 * 9);// inventorySlotStart, inventorySlotCount
  }
}
