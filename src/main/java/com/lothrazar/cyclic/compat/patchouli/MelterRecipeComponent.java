package com.lothrazar.cyclic.compat.patchouli;

import java.util.function.UnaryOperator;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.library.render.FluidRenderMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class MelterRecipeComponent implements ICustomComponent {

  private String recipe;
  private transient int x;
  private transient int y;
  private transient RecipeMelter resolvedRecipe;

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
        .filter(h -> h.value() instanceof RecipeMelter)
        .map(h -> (RecipeMelter) h.value())
        .orElse(null);
  }

  @Override
  public void render(GuiGraphics graphics, IComponentRenderContext ctx, float partialTicks, int mouseX, int mouseY) {
    if (resolvedRecipe == null) {
      return;
    }
    Font font = Minecraft.getInstance().font;
    ctx.renderIngredient(graphics, x + 2, y + 10, mouseX, mouseY, resolvedRecipe.at(0));
    graphics.drawString(font, "->", x + 22, y + 14, 0xFF404040, false);
    FluidStack outFluid = resolvedRecipe.getRecipeFluid();
    renderFluidSlot(graphics, x + 38, y + 10, outFluid);
    if (!outFluid.isEmpty()) {
      graphics.drawString(font, outFluid.getHoverName().getString() + " x" + outFluid.getAmount() + "mB", x + 58, y + 14, 0xFF404040, false);
    }
    int rfpt = resolvedRecipe.getEnergy().getRfPertick();
    int total = resolvedRecipe.getEnergy().getEnergyTotal();
    graphics.drawString(font, rfpt + " RF/t  " + total + " RF", x + 2, y + 34, 0xFF404040, false);
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
}
