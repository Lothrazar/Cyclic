package com.lothrazar.cyclic.recipe.ingredient;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;

public class EnergyIngredient {

  private static final String KEY_ENERGY = "energy";
  private static final int TICKS_DEFAULT = 60;
  private static final int RFPT_DEFAULT = 80;
  private int rfPertick;
  private int ticks;
  //  @Deprecated
  //  public EnergyIngredient(int energyTotal) {
  //    setRf(energyTotal / TICKS_DEFAULT); //TileMelter etc had these previously
  //    setTicks(TICKS_DEFAULT);
  //  }

  public EnergyIngredient(int rf, int ticks) {
    setRf(rf);
    setTicks(ticks);
  }

  public EnergyIngredient(final JsonObject recipeJson) {
    parseData(recipeJson);
  }

  private void parseData(final JsonObject recipeJson) {
    if (!recipeJson.has(KEY_ENERGY)) {
      ModCyclic.LOGGER.error("  Missing JSON 'energy', setting default values" + recipeJson);
      setRf(RFPT_DEFAULT);
      setTicks(TICKS_DEFAULT);
    }
    else if (recipeJson.get(KEY_ENERGY).isJsonObject()) {
      JsonObject energyJson = recipeJson.get(KEY_ENERGY).getAsJsonObject();
      setRf(energyJson.get("rfpertick").getAsInt());
      setTicks(energyJson.get("ticks").getAsInt());
    }
    else {
      ModCyclic.LOGGER.error("  Deprecated JSON 'energy' should be energy:{rfpertick,ticks} not integer. defaulting to 60 ticks " + recipeJson);
      setRf(recipeJson.get(KEY_ENERGY).getAsInt() / TICKS_DEFAULT);
      setTicks(TICKS_DEFAULT);
    }
  }

  private void setTicks(int ticks) {
    this.ticks = Math.max(1, ticks); // at least 1 per operation
  }

  private void setRf(int rf) {
    this.rfPertick = Math.max(0, rf); // not negative, can be zero for free cost
  }

  public int getEnergyTotal() {
    return rfPertick * ticks;
  }

  public int getTicks() {
    return ticks;
  }

  public int getRfPertick() {
    return this.rfPertick;
  }
}
