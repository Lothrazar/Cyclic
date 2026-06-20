package com.lothrazar.cyclic.compat.patchouli;

import java.util.function.UnaryOperator;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.FluidHelpers;
import com.lothrazar.library.render.FluidRenderMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
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
    ResourceLocation rl = ResourceLocation.tryParse(recipe);
    if (rl == null) {
      return;
    }
    resolvedRecipe = level.getRecipeManager().byKey(rl)
        .filter(h -> h.value() instanceof RecipeSolidifier)
        .map(h -> (RecipeSolidifier) h.value())
        .orElse(null);
  }

  @Override
  public void render(GuiGraphics graphics, IComponentRenderContext ctx, float partialTicks, int mouseX, int mouseY) {
    if (resolvedRecipe == null) {
      return;
    }
    Font font = Minecraft.getInstance().font;
    ctx.renderItemStack(graphics, x + 50, y, mouseX, mouseY, new ItemStack(BlockRegistry.SOLIDIFIER.get()));
    renderFluidSlot(graphics, x + 2, y + 45, resolvedRecipe.getRecipeFluid());
    ctx.renderIngredient(graphics, x + 22, y + 27, mouseX, mouseY, resolvedRecipe.at(0));
    ctx.renderIngredient(graphics, x + 22, y + 45, mouseX, mouseY, resolvedRecipe.at(1));
    ctx.renderIngredient(graphics, x + 22, y + 63, mouseX, mouseY, resolvedRecipe.at(2));
    graphics.drawString(font, "->", x + 42, y + 48, 0xFF404040, false);
    ctx.renderItemStack(graphics, x + 58, y + 45, mouseX, mouseY, resolvedRecipe.result);
    int rfpt = resolvedRecipe.getEnergy().getRfPertick();
    int total = resolvedRecipe.getEnergy().getEnergyTotal();
    int mB = resolvedRecipe.getAmount();
    graphics.drawString(font, rfpt + " RF/t  " + total + " RF", x, y + 85, 0xFF404040, false);
    graphics.drawString(font, resolvedRecipe.getRecipeFluid().getHoverName().getString() + " x" + mB + "mB", x, y + 94, 0xFF404040, false);
  }

  private void renderFluidSlot(GuiGraphics graphics, int fx, int fy, FluidStack fluid) {
    graphics.fill(fx - 1, fy - 1, fx + 17, fy + 17, 0xFF808080);
    graphics.fill(fx, fy, fx + 16, fy + 16, 0xFF303030);
    if (!fluid.isEmpty()) {
      var sprite = FluidRenderMap.getFluidTexture(fluid, FluidRenderMap.FluidFlow.STILL);
      int tint = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor(fluid);
      float a = ((tint >> 24) & 0xFF) / 255f;
      float r = ((tint >> 16) & 0xFF) / 255f;
      float g = ((tint >> 8) & 0xFF) / 255f;
      float b = (tint & 0xFF) / 255f;
      if (a == 0f) {
        a = 1f;
      }
      RenderSystem.setShaderColor(r, g, b, a);
      graphics.blit(fx, fy, 0, 16, 16, sprite);
      RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
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
