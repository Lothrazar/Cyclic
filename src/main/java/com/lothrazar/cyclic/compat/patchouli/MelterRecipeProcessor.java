package com.lothrazar.cyclic.compat.patchouli;

import com.lothrazar.cyclic.block.melter.RecipeMelter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class MelterRecipeProcessor implements IComponentProcessor {

  private RecipeMelter recipe;
  private String recipeId;

  @Override
  public void setup(Level level, IVariableProvider variables) {
    recipeId = variables.get("recipe", level.registryAccess()).asString();
    ResourceLocation rl = ResourceLocation.tryParse(recipeId);
    if (rl != null) {
      recipe = level.getRecipeManager().byKey(rl)
          .filter(h -> h.value() instanceof RecipeMelter)
          .map(h -> (RecipeMelter) h.value())
          .orElse(null);
    }
  }

  @Override
  public IVariable process(Level level, String key) {
    if ("recipe".equals(key)) {
      return IVariable.wrap(recipeId, level.registryAccess());
    }
    return null;
  }
}
