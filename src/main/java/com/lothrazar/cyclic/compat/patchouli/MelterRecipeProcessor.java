package com.lothrazar.cyclic.compat.patchouli;

import com.lothrazar.cyclic.block.melter.RecipeMelter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
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
    Identifier rl = Identifier.tryParse(recipeId);
    // Full Recipe objects are no longer synced to the client; reading the local integrated
    // server's RecipeManager is the only way to get a real instance here (dedicated-server
    // connections simply won't resolve this recipe).
    MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
    if (rl != null && server != null) {
      recipe = server.getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE, rl))
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
