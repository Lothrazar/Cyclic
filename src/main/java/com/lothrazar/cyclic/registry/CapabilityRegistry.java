package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.fluid.FluidBiomassHolder;
import com.lothrazar.cyclic.fluid.FluidHoneyHolder;
import com.lothrazar.cyclic.fluid.FluidMagmaHolder;
import com.lothrazar.cyclic.fluid.FluidSlimeHolder;
import com.lothrazar.cyclic.fluid.FluidWaxHolder;
import com.lothrazar.cyclic.fluid.FluidXpJuiceHolder;
import com.lothrazar.cyclic.item.crafting.CraftingBagCapability;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardCapability;
import com.lothrazar.cyclic.item.enderbook.EnderBookCapability;
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
            (stack, ctx) -> new LunchboxCapability(stack),
            ItemRegistry.LUNCHBOX.get()
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
            FluidXpJuiceHolder.BUCKET.get()
        );
    }
}
