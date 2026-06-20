package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.fluid.FluidAmethystHolder;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidChocolateHolder;
import com.lothrazar.cyclic.fluid.FluidEnderHolder;
import com.lothrazar.cyclic.fluid.FluidGlowstoneHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidRedstoneHolder;
import com.lothrazar.cyclic.fluid.FluidSculkHolder;
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

  public static final int LIGHT_LEVELMAGMA = 8;
  public static final int TEMP_WATER = 300;
  public static final int DENSITY_WATER = 1000;
  public static final int VISCOSITY_WATER = 1000;
  public static final int TEMP_LAVA = 1300;
  public static final int DENSITY_LAVA = 3000;
  public static final int VISCOSITY_LAVA = 3000;

  public static final FluidXpJuiceHolder XPJUICE = new FluidXpJuiceHolder();
  public static final FluidSlimeHolder SLIME = new FluidSlimeHolder();
  public static final FluidBiomassHolder BIOMASS = new FluidBiomassHolder();
  public static final FluidHoneyHolder HONEY = new FluidHoneyHolder();
  public static final FluidMagmaHolder MAGMA = new FluidMagmaHolder();
  public static final FluidWaxHolder WAX = new FluidWaxHolder();
  public static final FluidChocolateHolder CHOCOLATE = new FluidChocolateHolder();
  public static final FluidRedstoneHolder REDSTONE = new FluidRedstoneHolder();
  public static final FluidEnderHolder ENDER = new FluidEnderHolder();
  public static final FluidGlowstoneHolder GLOWSTONE = new FluidGlowstoneHolder();
  public static final FluidAmethystHolder AMETHYST = new FluidAmethystHolder();
  public static final FluidSculkHolder SCULK = new FluidSculkHolder();
}
