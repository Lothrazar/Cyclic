package com.lothrazar.cyclic.datagen;

// 26.1: net.neoforged.neoforge.client.model.generators.BlockStateProvider (the whole datagen helper
// package this extended) doesn't exist in NeoForge 26.1 - confirmed gone, no replacement researched yet.
// This class was already empty/vestigial before the port (registerStatesAndModels() had no body) and is
// referenced nowhere else in the codebase. Flagged for the user in the port plan doc rather than deleted
// outright - kept commented per the "comment don't delete" policy for this port. Let us know if it's safe
// to delete, or if the datagen functionality should be rebuilt against whatever replaced this package.
//
// import net.minecraft.data.PackOutput;
// import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
// import net.neoforged.neoforge.common.data.ExistingFileHelper;
//
// public class CyclicDataGenerator {
//
//   public static class BlockStates extends BlockStateProvider {
//
//     public BlockStates(PackOutput gen, String modid, ExistingFileHelper exFileHelper) {
//       super(gen, modid, exFileHelper);
//     }
//
//     @Override
//     protected void registerStatesAndModels() {}
//   }
// }
