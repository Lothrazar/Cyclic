package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.BlockFluidMilk;
import com.lothrazar.cyclicmagic.fluid.FluidMilk;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidsRegistry {
  public static FluidMilk fluid_milk;
  public static BlockFluidMilk block_milk;
  private static List<Fluid> fluids = new ArrayList<Fluid>();
  public static void register(Fluid f) {
    fluids.add(f);
  }
  public static void onRegistryEvent() {
    fluid_milk = new FluidMilk();
    FluidRegistry.registerFluid(fluid_milk);
    block_milk = new BlockFluidMilk();
    fluid_milk.setBlock(block_milk);
    BlockRegistry.registerBlock(block_milk,"milk",null);
    FluidRegistry.addBucketForFluid(fluid_milk);
  }
  //eventually it may convert to event
  //  @SubscribeEvent
  //  public static void onRegistryEvent(RegistryEvent.Register<Fluid> event) {
  //    
  //  }
}
