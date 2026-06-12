package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.battery.TileBattery;
import com.lothrazar.cyclic.block.batteryclay.TileClayBattery;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.block.expcollect.TileExpPylon;
import com.lothrazar.cyclic.block.tank.TileTank;
import com.lothrazar.cyclic.block.tankcask.TileCask;
import com.lothrazar.cyclic.capabilities.item.ItemEnergyCap;
import com.lothrazar.cyclic.capabilities.item.ItemFluidCap;
import com.lothrazar.cyclic.capabilities.item.ItemInventoryCap;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.item.WandHypnoItem;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.missile.WandMissileItem;
import com.lothrazar.cyclic.item.random.RandomizerItem;
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
import com.lothrazar.cyclic.item.crafting.CraftingBagCapability;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardCapability;
import com.lothrazar.cyclic.item.datacard.fluid.FluidFilterCardCapability;
import com.lothrazar.cyclic.item.enderbook.EnderBookCapability;
import com.lothrazar.cyclic.item.compass.GpsCompassCapability;
import com.lothrazar.cyclic.item.lunchbox.LunchboxCapability;
import com.lothrazar.cyclic.item.storagebag.StorageBagCapability;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import com.lothrazar.cyclic.ModCyclic;

@EventBusSubscriber(modid = ModCyclic.MODID)
public class CapabilityRegistry {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var type : TileRegistry.TILES.getEntries()) {
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type.get(), (be, side) -> {
                if (be instanceof TileBlockEntityCyclic cyclic) {
                    return cyclic.getItemHandler(side);
                }
                return null;
            });
            event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, type.get(), (be, side) -> {
                if (be instanceof TileBlockEntityCyclic cyclic) {
                    return cyclic.getFluidHandler(side);
                }
                return null;
            });
            event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type.get(), (be, side) -> {
                if (be instanceof TileBlockEntityCyclic cyclic) {
                    return cyclic.getEnergyHandler(side);
                }
                return null;
            });
        }
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new StorageBagCapability(stack),
            ItemRegistry.STORAGE_BAG.get()
        );
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new CraftingBagCapability(stack),
            ItemRegistry.CRAFTING_BAG.get()
        );
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new FilterCardCapability(stack),
            ItemRegistry.FILTER_DATA.get()
        );
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new FluidFilterCardCapability(stack),
            ItemRegistry.FILTER_FLUID.get()
        );
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new LunchboxCapability(stack),
            ItemRegistry.LUNCHBOX.get()
        );
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new GpsCompassCapability(stack),
            ItemRegistry.GPS_COMPASS.get()
        );
        event.registerItem(
            Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new EnderBookCapability(stack),
            ItemRegistry.ENDER_BOOK.get()
        );
        event.registerItem(Capabilities.FluidHandler.ITEM,
            (stack, ctx) -> new FluidBucketWrapper(stack),
            FluidBiomassHolder.BUCKET.get(),
            FluidHoneyHolder.BUCKET.get(),
            FluidMagmaHolder.BUCKET.get(),
            FluidSlimeHolder.BUCKET.get(),
            FluidWaxHolder.BUCKET.get(),
            FluidXpJuiceHolder.BUCKET.get(),
            FluidChocolateHolder.BUCKET.get(),
            FluidRedstoneHolder.BUCKET.get(),
            FluidEnderHolder.BUCKET.get(),
            FluidGlowstoneHolder.BUCKET.get(),
            FluidAmethystHolder.BUCKET.get(),
            FluidSculkHolder.BUCKET.get()
        );
        // Energy on BlockItem: persists into CUSTOM_DATA so place/break round-trips work
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new ItemEnergyCap(stack, TileBattery.MAX.get()),
            ItemRegistry.BATTERY.get());
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new ItemEnergyCap(stack, TileClayBattery.MAX.get()),
            ItemRegistry.BATTERY_CLAY.get());
        // Energy on handheld tools that consume RF per use
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new ItemEnergyCap(stack, WandMissileItem.MAX.get()),
            ItemRegistry.SCEPTER_MISSILE.get());
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new ItemEnergyCap(stack, WandHypnoItem.MAX.get()),
            ItemRegistry.SCEPTER_HYPNO.get());
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new ItemEnergyCap(stack, ConfigRegistry.LaserItemEnergyMax.get()),
            ItemRegistry.LASER_CANNON.get());
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new ItemEnergyCap(stack, BuilderItem.MAX.get()),
            ItemRegistry.SCEPTER_BUILD.get(),
            ItemRegistry.SCEPTER_REPLACE.get(),
            ItemRegistry.SCEPTER_OFFSET.get());
        event.registerItem(Capabilities.EnergyStorage.ITEM,
            (stack, ctx) -> new ItemEnergyCap(stack, RandomizerItem.MAX.get()),
            ItemRegistry.SCEPTER_RANDOMIZE.get());
        // Fluid on BlockItem
        event.registerItem(Capabilities.FluidHandler.ITEM,
            (stack, ctx) -> new ItemFluidCap(stack, TileTank.CAPACITY),
            ItemRegistry.TANK.get());
        event.registerItem(Capabilities.FluidHandler.ITEM,
            (stack, ctx) -> new ItemFluidCap(stack, TileCask.CAPACITY),
            ItemRegistry.CASK.get());
        event.registerItem(Capabilities.FluidHandler.ITEM,
            (stack, ctx) -> new ItemFluidCap(stack, TileExpPylon.CAPACITY),
            ItemRegistry.EXPERIENCE_PYLON.get());
        // Inventory on BlockItem
        // Note: ender shelf uses a bespoke handler with extra serialized state (book caches, etc)
        // and persists its full handler NBT directly into CUSTOM_DATA via the block's
        // setPlacedBy/playerDestroy, so it doesn't get an item-level cap.
        event.registerItem(Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new ItemInventoryCap(stack, 9 * 9),
            ItemRegistry.CRATE.get());
        event.registerItem(Capabilities.ItemHandler.ITEM,
            (stack, ctx) -> new ItemInventoryCap(stack, EnderShelfItemHandler.ROWS),
            ItemRegistry.SHELF.get());
    }
}
