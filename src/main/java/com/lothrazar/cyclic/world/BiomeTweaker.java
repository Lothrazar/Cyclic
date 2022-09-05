package com.lothrazar.cyclic.world;

import java.util.Map;
import com.google.gson.JsonElement;
import com.lothrazar.cyclic.ModCyclic;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiomeTweaker {

  private static final String MODIFY_BIOMES = "modify_biomes";
  private static final ResourceLocation MODIFY_BIOMES_RL = new ResourceLocation(ModCyclic.MODID, MODIFY_BIOMES);

  public static void init(IEventBus bus) {
    final DeferredRegister<Codec<? extends BiomeModifier>> serializers = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ModCyclic.MODID);
    serializers.register(bus); // BP is hit
    serializers.register(MODIFY_BIOMES, TestModifier::makeCodec);
    bus.addListener(BiomeTweaker::onGatherData);
  }

  public record TestModifier(HolderSet<Biome> biomes, Precipitation precipitation, int waterColor) implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(MODIFY_BIOMES_RL, ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, ModCyclic.MODID);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, BiomeInfo.Builder builder) {
      if (phase == Phase.MODIFY && this.biomes.contains(biome)) {
        BiomeGenerationSettingsBuilder generation = builder.getGenerationSettings();
        GenerationStep.Decoration step = GenerationStep.Decoration.VEGETAL_DECORATION;
        //
        generation.addFeature(step, WorldGenPlacements.PF_FLOWER_CYAN);
        generation.addFeature(step, WorldGenPlacements.PF_FLOWER_LIME);
        generation.addFeature(step, WorldGenPlacements.PF_FLOWER_TULIP);
      }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
      return SERIALIZER.get();
    }

    private static Codec<TestModifier> makeCodec() {
      return RecordCodecBuilder.create(builder -> builder.group(
          Biome.LIST_CODEC.fieldOf("biomes").forGetter(TestModifier::biomes),
          Precipitation.CODEC.fieldOf("precipitation").forGetter(TestModifier::precipitation),
          Codec.INT.fieldOf("water_color").forGetter(TestModifier::waterColor)).apply(builder, TestModifier::new));
    }
  }

  private static void onGatherData(GatherDataEvent event) {
    final DataGenerator generator = event.getGenerator();
    final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
    final RegistryOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());
    //
    //
    generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
        generator, existingFileHelper, ModCyclic.MODID, ops, Registry.PLACED_FEATURE_REGISTRY, Map.of(
            new ResourceLocation(ModCyclic.MODID, "cyan"), WorldGenPlacements.PF_FLOWER_CYAN.get())));
    generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
        generator, existingFileHelper, ModCyclic.MODID, ops, Registry.PLACED_FEATURE_REGISTRY, Map.of(
            new ResourceLocation(ModCyclic.MODID, "lime"), WorldGenPlacements.PF_FLOWER_LIME.get())));
    generator.addProvider(event.includeServer(), JsonCodecProvider.forDatapackRegistry(
        generator, existingFileHelper, ModCyclic.MODID, ops, Registry.PLACED_FEATURE_REGISTRY, Map.of(
            new ResourceLocation(ModCyclic.MODID, "tulip"), WorldGenPlacements.PF_FLOWER_TULIP.get())));
  }
}
