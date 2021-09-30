package com.lothrazar.cyclic.datagen;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.conveyor.BlockConveyor;
import com.lothrazar.cyclic.block.conveyor.ConveyorSpeed;
import com.lothrazar.cyclic.block.conveyor.ConveyorType;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CyclicDataGenerator {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    if (event.includeClient()) {
      gen.addProvider(new BlockStates(gen, ModCyclic.MODID, event.getExistingFileHelper()));
    }
  }

  public static class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
      super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
      VariantBlockStateBuilder builder = getVariantBuilder(BlockRegistry.CONVEYOR);
      builder.forAllStates((state -> {
        ConveyorType type = state.get(BlockConveyor.TYPE);
        ConveyorSpeed speed = state.get(BlockConveyor.SPEED);
        Direction facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
        int y;
        switch (facing) {
          case EAST:
            y = 90;
          break;
          case SOUTH:
            y = 180;
          break;
          case WEST:
            y = 270;
          break;
          default:
            y = 0;
        }
        String location = String.format("block/conveyor/conveyor_%s_%s", speed.getString(), type.getString());
        ModelFile.UncheckedModelFile model = new ModelFile.UncheckedModelFile(new ResourceLocation(ModCyclic.MODID, location));
        return ConfiguredModel.builder()
            .rotationX(0)
            .rotationY(y)
            .modelFile(model)
            .build();
      }));
    }
  }
}
