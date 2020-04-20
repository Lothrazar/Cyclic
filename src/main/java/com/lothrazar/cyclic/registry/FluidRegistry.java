package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.fluid.FluidAmberHolder;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidCrystalHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class FluidRegistry {

  public static FluidXpJuiceHolder xpjuice;
  public static FluidAmberHolder amber;
  public static FluidCrystalHolder crystal;
  public static FluidBiomassHolder biomass;

  public static void setup() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    xpjuice = new FluidXpJuiceHolder(modEventBus);
    amber = new FluidAmberHolder(modEventBus);
    crystal = new FluidCrystalHolder(modEventBus);
    biomass = new FluidBiomassHolder(modEventBus);
  }
}
