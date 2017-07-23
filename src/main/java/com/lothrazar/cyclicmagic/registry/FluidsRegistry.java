package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.fluid.BlockFluidExp;
import com.lothrazar.cyclicmagic.fluid.BlockFluidMilk;
import com.lothrazar.cyclicmagic.fluid.FluidExp;
import com.lothrazar.cyclicmagic.fluid.FluidMilk;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidsRegistry {
  public static FluidMilk fluid_milk;
  public static BlockFluidMilk block_milk;
  public static FluidExp fluid_exp;
  public static BlockFluidExp block_exp;
  public static void onRegistryEvent() {
    registerMilk();
    registerExp();
  }
  //eventually it may convert to event
  //  @SubscribeEvent
  //  public static void onRegistryEvent(RegistryEvent.Register<Fluid> event) {
  //    
  //  }
  private static void registerMilk() {
    fluid_milk = new FluidMilk();
    FluidRegistry.registerFluid(fluid_milk);
    block_milk = new BlockFluidMilk();
    fluid_milk.setBlock(block_milk);
    BlockRegistry.registerBlock(block_milk,"milk",null);
    FluidRegistry.addBucketForFluid(fluid_milk);
  }
  private static void registerExp() {
    fluid_exp = new FluidExp();
    FluidRegistry.registerFluid(fluid_exp);
    block_exp = new BlockFluidExp();
    fluid_exp.setBlock(block_exp);
    BlockRegistry.registerBlock(block_exp,"xpjuice",null);
    FluidRegistry.addBucketForFluid(fluid_exp);
  }
}
