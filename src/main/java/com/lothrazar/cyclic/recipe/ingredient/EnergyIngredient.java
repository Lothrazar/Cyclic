package com.lothrazar.cyclic.recipe.ingredient;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;

public class EnergyIngredient {

  public static final int TICKS_DEFAULT = 60;
  public int rfpertick;
  public int ticks;

  public EnergyIngredient(int rf, int ticks) {
    setRf(rf);
    setTicks(ticks);
  }

  public EnergyIngredient(final JsonObject recipeJson) {
    parseData(recipeJson);
  }

  private void parseData(final JsonObject recipeJson) {
    if (!recipeJson.has("energy")) {
      setRf(0);
      setTicks(1);
    }
    else if (recipeJson.get("energy").isJsonObject()) {
      JsonObject energyJson = recipeJson.get("energy").getAsJsonObject();
      setRf(energyJson.get("rfpertick").getAsInt());
      setTicks(energyJson.get("ticks").getAsInt());
    }
    else {
      ModCyclic.LOGGER.error("Deprecated JSON 'energy' should be object not integer. defaulting to 60 ticks " + recipeJson);
      setRf(recipeJson.get("energy").getAsInt() / TICKS_DEFAULT);
      setTicks(TICKS_DEFAULT);
    }
  }

  private void setTicks(int ticks) {
    this.ticks = Math.max(1, ticks); // at least 1 per operation
  }

  private void setRf(int rf) {
    this.rfpertick = Math.max(0, rf); // not negative, can be zero for free cost
  }

  public int getEnergyTotal() {
    return rfpertick * ticks;
  }

  public int getTicks() {
    return ticks;
  }

  public int rfPt() {
    return this.rfpertick;
  }
}
