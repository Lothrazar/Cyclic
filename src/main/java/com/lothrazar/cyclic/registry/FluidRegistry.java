package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidRegistry {

  public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ModCyclic.MODID);
  public static FluidXpJuiceHolder xpjuice;
  public static FluidSlimeHolder slime;
  public static FluidBiomassHolder biomass;
  public static FluidHoneyHolder honey;
  public static FluidMagmaHolder magma;

  public static void setup(IEventBus modEventBus) {
    //    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    xpjuice = new FluidXpJuiceHolder();
    slime = new FluidSlimeHolder();
    biomass = new FluidBiomassHolder();
    honey = new FluidHoneyHolder();
    magma = new FluidMagmaHolder();
  }
}
