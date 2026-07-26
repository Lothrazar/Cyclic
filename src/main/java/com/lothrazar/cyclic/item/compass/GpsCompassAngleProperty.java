package com.lothrazar.cyclic.item.compass;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GpsCompassAngleProperty implements RangeSelectItemModelProperty {

  public static final GpsCompassAngleProperty INSTANCE = new GpsCompassAngleProperty();
  public static final MapCodec<GpsCompassAngleProperty> MAP_CODEC = MapCodec.unit(INSTANCE);

  @Override
  public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
    LivingEntity entity = owner == null ? null : owner.asLivingEntity();
    return GpsCompassItem.getAngle(itemStack, entity);
  }

  @Override
  public MapCodec<GpsCompassAngleProperty> type() {
    return MAP_CODEC;
  }
}
