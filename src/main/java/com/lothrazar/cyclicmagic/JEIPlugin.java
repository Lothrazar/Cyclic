package com.lothrazar.cyclicmagic;
import com.lothrazar.cyclicmagic.component.crafter.ContainerCrafter;
import com.lothrazar.cyclicmagic.component.hydrator.BlockHydrator;
import com.lothrazar.cyclicmagic.component.hydrator.GuiHydrator;
import com.lothrazar.cyclicmagic.component.hydrator.RecipeHydrate;
import com.lothrazar.cyclicmagic.component.playerext.crafting.ContainerPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.component.workbench.ContainerWorkBench;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@mezz.jei.api.JEIPlugin
public class JEIPlugin extends mezz.jei.api.BlankModPlugin {
  private static final String RECIPE_CATEGORY_HYDRATOR = "hydrator";
  @Override
  public void register(IModRegistry registry) {
    ////////////////first register all crafting GUI's
    // thanks to http://www.minecraftforum.net/forums/mapping-and-modding/mapping-and-modding-tutorials/1571434-tutorial-modding-with-apis
    //and of course readme on https://github.com/mezz/JustEnoughItems
    // setup [+] feature
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerPlayerExtWorkbench.class, VanillaRecipeCategoryUid.CRAFTING,
        6, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 5 armor slots
        9, // @param recipeSlotCount    the number of slots for recipe inputs //3x3
        15, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9+6
        36);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerCrafter.class, VanillaRecipeCategoryUid.CRAFTING,
        10, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 10 INPUT
        9, // @param recipeSlotCount    the number of slots for recipe inputs //3x3
        29, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9+6
        4 * 9);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerWorkBench.class, VanillaRecipeCategoryUid.CRAFTING,
        1, // @param recipeSlotStart    the first slot for recipe inputs // skip over the 1 output and the 5 armor slots
        9, // @param recipeSlotCount    the number of slots for recipe inputs //3x3
        10, //@param inventorySlotStart the first slot of the available inventory (usually player inventory) =9
        4 * 9);//@param inventorySlotCount the number of slots of the available inventory //top right including hotbar =4*9
    ////////////////// custom recipe hook
    registry.addRecipeClickArea(GuiHydrator.class, 70, 16, 20, 20, RECIPE_CATEGORY_HYDRATOR);
    registry.handleRecipes(RecipeHydrate.class, new HydratorFactory(), RECIPE_CATEGORY_HYDRATOR);
    registry.addRecipes(BlockHydrator.recipeList, RECIPE_CATEGORY_HYDRATOR);
  }
  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    registry.addRecipeCategories(new HydratorRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
  }
  public static class HydratorFactory implements IRecipeWrapperFactory<RecipeHydrate> {
    @Override
    public IRecipeWrapper getRecipeWrapper(RecipeHydrate recipe) {
      return new HydratorWrapper(recipe);
    }
  }
  public static class HydratorWrapper implements IRecipeWrapper {
    private RecipeHydrate src;
    public HydratorWrapper(RecipeHydrate source) {
      this.src = source;
    }
    public ItemStack getOut() {
      return src.getRecipeOutput();
    }
    @Override
    public void getIngredients(IIngredients ingredients) {
      ingredients.setInput(ItemStack.class, src.getRecipeInput());
      ingredients.setOutput(ItemStack.class, src.getRecipeOutput());
    }
  }
  public static class HydratorRecipeCategory implements IRecipeCategory<HydratorWrapper> {
    private IDrawable gui;
    private IDrawable icon;
    public HydratorRecipeCategory(IGuiHelper helper) {
      gui = helper.createDrawable(new ResourceLocation(Const.MODID, "textures/gui/hydrator_recipe.png"), 0, 0, 169, 69, 169, 69);
      //TOD: block is wrong of course, just POC
      icon = helper.createDrawable(new ResourceLocation(Const.MODID, "textures/blocks/block_workbench_top.png"), 0, 0, 16, 16, 16, 16);
    }
    @Override
    public String getUid() {
      return RECIPE_CATEGORY_HYDRATOR;
    }
    @Override
    public String getTitle() {
      return UtilChat.lang("tile.block_hydrator.name");
    }
    @Override
    public String getModName() {
      return Const.MODID;
    }
    @Override
    public IDrawable getIcon() {
      return icon;
    }
    @Override
    public IDrawable getBackground() {
      return gui;
    }
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, HydratorWrapper recipeWrapper, IIngredients ingredients) {
      IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
      guiItemStacks.init(0, true, 3, 18);
      guiItemStacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
      guiItemStacks.init(1, false, 129, 18);
      guiItemStacks.set(1, recipeWrapper.getOut());
    }
  }
}
