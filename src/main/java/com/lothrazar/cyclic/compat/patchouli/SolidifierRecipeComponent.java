package com.lothrazar.cyclic.compat.patchouli;

import java.util.function.UnaryOperator;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.library.render.FluidRenderMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class SolidifierRecipeComponent implements ICustomComponent {

  private String recipe;
  private transient int x;
  private transient int y;
  private transient RecipeSolidifier resolvedRecipe;

  @Override
  public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider ra) {
    if (recipe != null) {
      recipe = lookup.apply(IVariable.wrap(recipe, ra)).asString();
    }
  }

  @Override
  public void build(int bx, int by, int pageNum) {
    x = bx;
    y = by;
    Level level = Minecraft.getInstance().level;
    if (level == null || recipe == null) {
      return;
    }
    Identifier rl = Identifier.tryParse(recipe);
    if (rl == null) {
      return;
    }
    // Full Recipe objects are no longer synced to the client; reading the local integrated
    // server's RecipeManager is the only way to get a real instance here (dedicated-server
    // connections simply won't resolve this recipe).
    MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
    if (server == null) {
      return;
    }
    resolvedRecipe = server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, rl))
        .filter(h -> h.value() instanceof RecipeSolidifier)
        .map(h -> (RecipeSolidifier) h.value())
        .orElse(null);
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor graphics, IComponentRenderContext ctx, float partialTicks, int mouseX, int mouseY) {
    if (resolvedRecipe == null) {
      return;
    }
    Font font = Minecraft.getInstance().font;
    ctx.renderItemStack(graphics, x + 50, y, mouseX, mouseY, new ItemStack(BlockRegistry.SOLIDIFIER.get()));
    renderFluidSlot(graphics, x + 2, y + 45, resolvedRecipe.getRecipeFluid());
    resolvedRecipe.at(0).ifPresent(i -> ctx.renderIngredient(graphics, x + 22, y + 27, mouseX, mouseY, i));
    resolvedRecipe.at(1).ifPresent(i -> ctx.renderIngredient(graphics, x + 22, y + 45, mouseX, mouseY, i));
    resolvedRecipe.at(2).ifPresent(i -> ctx.renderIngredient(graphics, x + 22, y + 63, mouseX, mouseY, i));
    graphics.text(font, "->", x + 42, y + 48, 0xFF404040, false);
    ctx.renderItemStack(graphics, x + 58, y + 45, mouseX, mouseY, resolvedRecipe.getResult());
    int rfpt = resolvedRecipe.getEnergy().getRfPertick();
    int total = resolvedRecipe.getEnergy().getEnergyTotal();
    int mB = resolvedRecipe.getAmount();
    graphics.text(font, rfpt + " RF/t  " + total + " RF", x, y + 85, 0xFF404040, false);
    graphics.text(font, resolvedRecipe.getRecipeFluid().getHoverName().getString() + " x" + mB + "mB", x, y + 94, 0xFF404040, false);
  }

  private void renderFluidSlot(GuiGraphicsExtractor graphics, int fx, int fy, FluidStack fluid) {
    graphics.fill(fx - 1, fy - 1, fx + 17, fy + 17, 0xFF808080);
    graphics.fill(fx, fy, fx + 16, fy + 16, 0xFF303030);
    if (!fluid.isEmpty()) {
      var sprite = FluidRenderMap.getFluidTexture(fluid, FluidRenderMap.FluidFlow.STILL);
      int tint = FluidHelpers.getColorFromFluid(fluid);
      graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, fx, fy, 16, 16, tint);
    }
  }

//  private void renderFluidSlot(GuiGraphics graphics, int fx, int fy, FluidStack fluid) {
//    graphics.fill(fx - 1, fy - 1, fx + 17, fy + 17, 0xFF808080);
//    graphics.fill(fx, fy, fx + 16, fy + 16, 0xFF303030);
//    if (!fluid.isEmpty()) {
//      int color = FluidHelpers.getColorFromFluid(fluid) | 0xFF000000;
//      graphics.fill(fx + 1, fy + 1, fx + 15, fy + 15, color);
//    }
//  }
}
