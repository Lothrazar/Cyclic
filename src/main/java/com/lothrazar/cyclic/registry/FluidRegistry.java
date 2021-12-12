package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class FluidRegistry {

  public static FluidXpJuiceHolder xpjuice;
  public static FluidSlimeHolder slime;
  public static FluidBiomassHolder biomass;
  public static FluidHoneyHolder honey;
  public static FluidMagmaHolder magma;

  @SuppressWarnings("InstantiationOfUtilityClass")
  public static void setup() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    xpjuice = new FluidXpJuiceHolder(modEventBus);
    slime = new FluidSlimeHolder(modEventBus);
    biomass = new FluidBiomassHolder(modEventBus);
    honey = new FluidHoneyHolder(modEventBus);
    magma = new FluidMagmaHolder(modEventBus);
  }
}
