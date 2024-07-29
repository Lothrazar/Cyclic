package com.lothrazar.cyclic.compat.jei;

import java.util.List;
import java.util.Objects;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.crusher.ContainerCrusher;
import com.lothrazar.cyclic.block.crusher.ScreenCrusher;
import com.lothrazar.cyclic.block.generatorfluid.ScreenGeneratorFluid;
import com.lothrazar.cyclic.block.generatoritem.ContainerGeneratorDrops;
import com.lothrazar.cyclic.block.generatoritem.ScreenGeneratorDrops;
import com.lothrazar.cyclic.block.melter.ContainerMelter;
import com.lothrazar.cyclic.block.melter.ScreenMelter;
import com.lothrazar.cyclic.block.packager.ContainerPackager;
import com.lothrazar.cyclic.block.packager.ScreenPackager;
import com.lothrazar.cyclic.block.solidifier.ContainerSolidifier;
import com.lothrazar.cyclic.block.solidifier.ScreenSolidifier;
import com.lothrazar.cyclic.block.workbench.ContainerWorkbench;
import com.lothrazar.cyclic.item.crafting.CraftingBagContainer;
import com.lothrazar.cyclic.item.crafting.simple.CraftingStickContainer;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

@JeiPlugin
public class CyclicPluginJEI implements IModPlugin {

  private static final int PLAYER_INV_SIZE = 4 * 9;
  private static final ResourceLocation ID = new ResourceLocation(ModCyclic.MODID, "jei");

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}

  @Override
  public ResourceLocation getPluginUid() {
    return ID;
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
    registry.addRecipeCategories(new MelterRecipeCategory(guiHelper));
    registry.addRecipeCategories(new SolidifierRecipeCategory(guiHelper));
    registry.addRecipeCategories(new GenitemRecipeCategory(guiHelper));
    registry.addRecipeCategories(new GenfluidRecipeCategory(guiHelper));
    registry.addRecipeCategories(new PackagerRecipeCategory(guiHelper));
    registry.addRecipeCategories(new CrusherRecipeCategory(guiHelper));
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.PACKAGER.get()), PackagerRecipeCategory.TYPE);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CRAFTER.get()), RecipeTypes.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(ItemRegistry.CRAFTING_BAG.get()), RecipeTypes.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(ItemRegistry.CRAFTING_STICK.get()), RecipeTypes.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.WORKBENCH.get()), RecipeTypes.CRAFTING);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.MELTER.get()), MelterRecipeCategory.TYPE);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.SOLIDIFIER.get()), SolidifierRecipeCategory.TYPE);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.GENERATOR_ITEM.get()), GenitemRecipeCategory.TYPE);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.GENERATOR_FLUID.get()), GenfluidRecipeCategory.TYPE);
    registration.addRecipeCatalyst(new ItemStack(BlockRegistry.CRUSHER.get()), CrusherRecipeCategory.TYPE);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    ClientLevel world = Objects.requireNonNull(Minecraft.getInstance().level);
    RecipeManager rm = world.getRecipeManager();
    registry.addRecipes(RecipeTypes.CRAFTING, rm.getAllRecipesFor(RecipeType.CRAFTING));
    registry.addRecipes(MelterRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(CyclicRecipeType.MELTER.get())));
    registry.addRecipes(SolidifierRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(CyclicRecipeType.SOLID.get())));
    registry.addRecipes(GenitemRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(CyclicRecipeType.GENERATOR_ITEM.get())));
    registry.addRecipes(GenfluidRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(CyclicRecipeType.GENERATOR_FLUID.get())));
    registry.addRecipes(CrusherRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(CyclicRecipeType.CRUSHER.get())));
    registry.addRecipes(PackagerRecipeCategory.TYPE, List.copyOf(rm.getAllRecipesFor(RecipeType.CRAFTING)));
    for (RegistryObject<Item> item : ItemRegistry.ITEMS.getEntries()) {
      ItemStack st = new ItemStack(item.get());
      if (!st.isEmpty() && (st.getItem() instanceof BucketItem == false)) {
        registry.addIngredientInfo(st, VanillaTypes.ITEM_STACK, Component.translatable(item.get().getDescriptionId() + ".guide"));
      }
    }
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registry) {
    registry.addRecipeClickArea(ScreenMelter.class,
        68, 37,
        24, 17, MelterRecipeCategory.TYPE);
    registry.addRecipeClickArea(ScreenSolidifier.class,
        68, 37,
        24, 17, SolidifierRecipeCategory.TYPE);
    registry.addRecipeClickArea(ScreenGeneratorDrops.class,
        38, 6,
        100, 10, GenitemRecipeCategory.TYPE);
    registry.addRecipeClickArea(ScreenGeneratorFluid.class,
        28, 6,
        120, 10, GenfluidRecipeCategory.TYPE);
    registry.addRecipeClickArea(ScreenPackager.class,
        60, 4,
        52, 12, PackagerRecipeCategory.TYPE);
    registry.addRecipeClickArea(ScreenCrusher.class,
        76, 38,
        20, 20, CrusherRecipeCategory.TYPE);
  }

  @Override
  public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
    registry.addRecipeTransferHandler(ContainerMelter.class, MenuTypeRegistry.MELTER.get(), MelterRecipeCategory.TYPE,
        0, 2, //recipeSLotStart, recipeSlotCount
        2, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerSolidifier.class, MenuTypeRegistry.SOLIDIFIER.get(), SolidifierRecipeCategory.TYPE,
        0, 3, //recipeSLotStart, recipeSlotCount
        4, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(CraftingBagContainer.class, MenuTypeRegistry.CRAFTING_BAG.get(), RecipeTypes.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(CraftingStickContainer.class, MenuTypeRegistry.CRAFTING_STICK.get(), RecipeTypes.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerWorkbench.class, MenuTypeRegistry.WORKBENCH.get(), RecipeTypes.CRAFTING,
        1, 9, //recipeSLotStart, recipeSlotCount
        10, PLAYER_INV_SIZE);
    registry.addRecipeTransferHandler(ContainerGeneratorDrops.class, MenuTypeRegistry.GENERATOR_DROPS.get(), GenitemRecipeCategory.TYPE,
        0, 1, //recipeSLotStart, recipeSlotCount
        1, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerCrusher.class, MenuTypeRegistry.CRUSHER.get(), CrusherRecipeCategory.TYPE,
        0, 1, //recipeSLotStart, recipeSlotCount
        1, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount
    registry.addRecipeTransferHandler(ContainerPackager.class, MenuTypeRegistry.PACKAGER.get(), PackagerRecipeCategory.TYPE,
        0, 1, //recipeSLotStart, recipeSlotCount
        1, PLAYER_INV_SIZE); // inventorySlotStart, inventorySlotCount 
  }
}
