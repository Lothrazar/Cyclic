package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidWaxHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class FluidRegistry {
  public static final DeferredRegister<Fluid> FLUID = DeferredRegister.create(Registries.FLUID, ModCyclic.MODID);
  public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, ModCyclic.MODID);


  public static final FluidXpJuiceHolder XPJUICE = new FluidXpJuiceHolder();
  public static final FluidSlimeHolder SLIME = new FluidSlimeHolder();
  public static final FluidBiomassHolder BIOMASS = new FluidBiomassHolder();
  public static final FluidHoneyHolder HONEY = new FluidHoneyHolder();
  public static final FluidMagmaHolder MAGMA = new FluidMagmaHolder();
  public static final FluidWaxHolder WAX = new FluidWaxHolder();
}
