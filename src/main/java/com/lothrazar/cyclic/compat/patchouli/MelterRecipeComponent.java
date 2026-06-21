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
    // center of the 116px page content area
    int cx = x + 58;
    // row 1: item -> fluid centered
    int itemX = cx - 26;
    int fluidX = cx + 10;
    ctx.renderIngredient(graphics, itemX, y, mouseX, mouseY, resolvedRecipe.at(0));
    graphics.drawString(font, "->", itemX + 18, y + 4, 0xFF404040, false);
    FluidStack outFluid = resolvedRecipe.getRecipeFluid();
    renderFluidSlot(graphics, fluidX, y, outFluid);
    // row 2: fluid name centered
    // row 3: amount centered
    // row 4: energy centered
    if (!outFluid.isEmpty()) {
      String name = outFluid.getHoverName().getString();
      String amount = "x" + outFluid.getAmount() + "mB";
      graphics.drawString(font, name, cx - font.width(name) / 2, y + 22, 0xFF404040, false);
      graphics.drawString(font, amount, cx - font.width(amount) / 2, y + 32, 0xFF404040, false);
    }
    int rfpt = resolvedRecipe.getEnergy().getRfPertick();
    int total = resolvedRecipe.getEnergy().getEnergyTotal();
    String energy = rfpt + " RF/t  " + total + " RF";
    graphics.drawString(font, energy, cx - font.width(energy) / 2, y + 43, 0xFF404040, false);
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
