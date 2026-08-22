package com.lothrazar.cyclic.fluid;

import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.FluidRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.library.fluid.GenericFluidBlock;
import com.lothrazar.library.item.BucketItemFlib;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;

public class FluidWaxHolder {

  private static final String ID = "wax";
  public static final Identifier FLUID_STILL = Identifier.fromNamespaceAndPath("minecraft", "block/water_still");
  public static final Identifier FLUID_FLOW = Identifier.fromNamespaceAndPath("minecraft", "block/water_flow");
  public static final int COLOR = 0xEEEEEE;

  public static final DeferredHolder<FluidType, FluidType> TYPE = FluidRegistry.FLUID_TYPES.register(ID,
      () -> new FluidType(
          FluidType.Properties.create().density(FluidRegistry.DENSITY_WATER + 200).viscosity(FluidRegistry.VISCOSITY_WATER + 500).temperature(FluidRegistry.TEMP_LAVA - 300)
              .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
              .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)));

  public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> STILL = FluidRegistry.FLUID.register(ID, () -> new BaseFlowingFluid.Source(makeProperties()));
  public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> FLOWING = FluidRegistry.FLUID.register(ID + "_flowing", () -> new BaseFlowingFluid.Flowing(makeProperties()));

  public static final DeferredBlock<GenericFluidBlock> BLOCK = BlockRegistry.BLOCKS.registerBlock(ID + "_block",
      props -> new GenericFluidBlock(STILL, props.liquid().replaceable().noCollision().strength(100.0F).noLootTable(),
          List.of(ent -> {
            if (!ent.isOnFire() && !ent.fireImmune()) {
              int lvl = EnchantUtil.getCurrentArmorLevel(EnchantUtil.holder(Enchantments.FIRE_PROTECTION, ent), ent);
              if (lvl < 4) {
                ent.igniteForSeconds(Mth.floor(ent.level().getRandom().nextDouble() * 10));
              }
            }
          })));

  public static final DeferredItem<Item> BUCKET = ItemRegistry.ITEMS.registerItem(ID + "_bucket",
      props -> new BucketItemFlib(STILL.get(), props));

  private static BaseFlowingFluid.Properties makeProperties() {
    return new BaseFlowingFluid.Properties(TYPE, STILL, FLOWING)
        .bucket(() -> BUCKET.get())
        .block(() -> BLOCK.get());
  }
}
