package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidRegistry {

  public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ModCyclic.MODID);
  public static final FluidXpJuiceHolder xpjuice = new FluidXpJuiceHolder();
  public static final FluidSlimeHolder slime = new FluidSlimeHolder();
  public static final FluidBiomassHolder biomass = new FluidBiomassHolder();
  public static final FluidHoneyHolder honey = new FluidHoneyHolder();
  public static final FluidMagmaHolder magma = new FluidMagmaHolder();
}
