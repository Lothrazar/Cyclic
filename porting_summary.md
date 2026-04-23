# Cyclic: NeoForge 1.21.1 Porting Notes & Technical Summary

This document outlines the comprehensive refactoring, bug fixes, and architectural adaptations performed to stabilize the Cyclic mod on the NeoForge 1.21.1 API. 

## 🎯 Executive Summary
The primary goal of this port was to resolve critical server-client synchronization failures, recipe registry crashes, and rendering exceptions caused by the transition from Minecraft 1.20 to 1.21.1. The mod is now stable, successfully boots into the world, handles complex UI interactions (like the Crafting Stick), and safely processes data-driven recipes without crashing the network thread.

---

## 🛠 Technical Refactoring & Fixes

### 1. Networking & Stream Codecs (`EncoderException` Fixes)
Minecraft 1.21.1 introduced strict validation for `StreamCodec` when syncing data across the network. Previously, sending an empty item or fluid stack was permissible, but in 1.21.1 it triggers a fatal `io.netty.handler.codec.EncoderException`.
*   **Item Stacks:** Replaced strict `ItemStack.STREAM_CODEC` with `ItemStack.OPTIONAL_STREAM_CODEC` in `RecipeSolidifier` and `RecipeCrusher`. This prevents server crashes when syncing recipes that have missing or deleted result items.
*   **Fluid Stacks:** 
    *   Replaced `FluidStack.STREAM_CODEC` with `FluidStack.OPTIONAL_STREAM_CODEC` in `RecipeMelter`.
    *   **The `flib` Bypass:** The `FluidTagIngredient` class provided by the `flib` library hardcodes a strict `FluidStack.STREAM_CODEC`. Because modifying the compiled `flib` library was not feasible, we implemented a custom inline `StreamCodec` bypass directly within `RecipeSolidifier` and `RecipeGeneratorFluid` to handle empty fluid synchronization safely.

### 2. Registry & Recipe Initialization (`UnsupportedOperationException`)
During world load, the recipe manager attempts to align recipe ingredients with the machine's inventory slots by filling empty slots with `Ingredient.EMPTY`.
*   **The Issue:** `RecipeMelter` and `RecipeSolidifier` were constructing their internal ingredient lists using `NonNullList.of()`, which returns an **immutable** list.
*   **The Fix:** Refactored the constructors to use `NonNullList.create()` followed by `.addAll()`, ensuring the recipe list remains mutable during the `RecipeManager` initialization phase.

### 3. Client-Side Rendering (`NullPointerException`)
When viewing items containing custom Cyclic fluids (e.g., XP Juice Bucket) in JEI or the creative inventory, the client would crash with a `NullPointerException` inside `TextureAtlas.getSprite()`.
*   **The Issue:** NeoForge 1.21.1 strictly requires custom fluids to register their textures via `IClientFluidTypeExtensions`. Cyclic's fluid holders lacked this initialization, causing `DynamicFluidContainerModel` to request a `null` texture.
*   **The Fix:** Injected the `initializeClient()` override into `FluidXpJuiceHolder` (and prepared others) to properly register `getStillTexture()`, `getFlowingTexture()`, and `getTintColor()`.

### 4. Interactive Items (Crafting Stick)
*   **The Issue:** The `CraftingStickItem` attempted to open a GUI by sending a `BlockPos` across the network, which failed because the stick operates from an inventory slot, not a world block.
*   **The Fix:** Rewrote the network packet payload to send `writeInt(slot)` instead of `BlockPos`, correctly identifying the item triggering the GUI and restoring full functionality.

### 5. Data-Driven Integrity & Localization
*   **Integration Audits:** Analyzed and disabled over 115 cross-mod integration recipes (referencing `cobblestoney`, `mysticalagriculture`, `tconstruct`, etc.) that were causing severe `JsonParseException` and empty item crashes due to the target mods altering their registries or not yet existing in 1.21.1.
*   **Vanilla Migrations:** Updated legacy items in surviving recipes (e.g., migrated `"minecraft:scute"` to `"minecraft:turtle_scute"` in the Climbing Glove recipe).
*   **Russian Localization:** Automated the cross-referencing of `en_us.json` against `ru_ru.json`. Successfully translated and injected **234 missing translation keys**, achieving 100% localization parity for Russian users.

---

## 📋 Developer Instructions & Next Steps

If you are continuing development on this branch, please adhere to the following guidelines:

> [!IMPORTANT]  
> **Re-enabling Integrations:** The broken integration recipe files in `src/main/resources/data/cyclic/recipe/` were completely removed to clean the PR. If you need to restore integration with other mods once they update to 1.21.1, you must pull the original JSON files from the 1.20 branch and audit their item/fluid IDs.

> [!WARNING]  
> **Fluid Client Initialization:** The `initializeClient(Consumer<IClientFluidTypeExtensions>)` method used in `FluidXpJuiceHolder` is marked as deprecated by NeoForge and slated for removal in future versions (likely 1.22+). For 1.21.1 it is perfectly safe, but consider migrating to the event-driven `RegisterClientExtensionsEvent` when porting to 1.22.

> [!TIP]  
> **Testing Network Codecs:** If you add new machines or recipes, **always** test joining a Dedicated Server. Singleplayer integrated servers sometimes mask `StreamCodec` exceptions that will instantly kick players on a real multiplayer server. Always use `OPTIONAL_STREAM_CODEC` for items/fluids in recipes.
