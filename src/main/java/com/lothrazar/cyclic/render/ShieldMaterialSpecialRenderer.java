package com.lothrazar.cyclic.render;

import com.lothrazar.cyclic.registry.MaterialShieldRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ShieldModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

/**
 * Cyclic's own shield material variants used the vanilla {@code BlockEntityWithoutLevelRenderer} + custom
 * {@code Material} approach pre-26.1. That whole system is gone; vanilla itself now renders shields via
 * {@code net.minecraft.client.renderer.special.ShieldSpecialRenderer}. This mirrors that class, but swaps
 * in one of Cyclic's own base/no-pattern {@link SpriteId} pairs instead of the vanilla shield texture.
 */
public class ShieldMaterialSpecialRenderer implements SpecialModelRenderer<DataComponentMap> {

  private final SpriteGetter sprites;
  private final ShieldModel model;
  private final SpriteId base;
  private final SpriteId baseNoPattern;

  public ShieldMaterialSpecialRenderer(SpriteGetter sprites, ShieldModel model, SpriteId base, SpriteId baseNoPattern) {
    this.sprites = sprites;
    this.model = model;
    this.base = base;
    this.baseNoPattern = baseNoPattern;
  }

  @Override
  public @Nullable DataComponentMap extractArgument(ItemStack stack) {
    return stack.immutableComponents();
  }

  @Override
  public void submit(@Nullable DataComponentMap components, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
      int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
    BannerPatternLayers patterns = components != null ? components.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY) : BannerPatternLayers.EMPTY;
    DyeColor baseColor = components != null ? components.get(DataComponents.BASE_COLOR) : null;
    boolean hasPatterns = !patterns.layers().isEmpty() || baseColor != null;
    SpriteId spriteToUse = hasPatterns ? this.base : this.baseNoPattern;
    submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, spriteToUse, this.sprites, outlineColor, null);
    if (hasPatterns) {
      BannerRenderer.submitPatterns(this.sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, this.model, Unit.INSTANCE, false,
          Objects.requireNonNullElse(baseColor, DyeColor.WHITE), patterns, null);
    }
    if (hasFoil) {
      submitNodeCollector.submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(), lightCoords, overlayCoords, -1, this.sprites.get(spriteToUse), 0, null);
    }
  }

  @Override
  public void getExtents(Consumer<Vector3fc> output) {
    PoseStack poseStack = new PoseStack();
    this.model.root().getExtentsForGui(poseStack, output);
  }

  private static ShieldMaterialSpecialRenderer bake(SpecialModelRenderer.BakingContext context, SpriteId base, SpriteId baseNoPattern) {
    return new ShieldMaterialSpecialRenderer(context.sprites(), new ShieldModel(context.entityModelSet().bakeLayer(ModelLayers.SHIELD)), base, baseNoPattern);
  }

  public record WoodUnbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
    public static final WoodUnbaked INSTANCE = new WoodUnbaked();
    public static final MapCodec<WoodUnbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public @Nullable ShieldMaterialSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
      return ShieldMaterialSpecialRenderer.bake(context, MaterialShieldRegistry.SHIELD_BASE_WOOD, MaterialShieldRegistry.SHIELD_BASE_WOOD_NOPATTERN);
    }

    @Override
    public MapCodec<WoodUnbaked> type() {
      return MAP_CODEC;
    }
  }

  public record LeatherUnbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
    public static final LeatherUnbaked INSTANCE = new LeatherUnbaked();
    public static final MapCodec<LeatherUnbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public @Nullable ShieldMaterialSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
      return ShieldMaterialSpecialRenderer.bake(context, MaterialShieldRegistry.SHIELD_BASE_LEATHER, MaterialShieldRegistry.SHIELD_BASE_LEATHER_NOPATTERN);
    }

    @Override
    public MapCodec<LeatherUnbaked> type() {
      return MAP_CODEC;
    }
  }

  public record FlintUnbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
    public static final FlintUnbaked INSTANCE = new FlintUnbaked();
    public static final MapCodec<FlintUnbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public @Nullable ShieldMaterialSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
      return ShieldMaterialSpecialRenderer.bake(context, MaterialShieldRegistry.SHIELD_BASE_FLINT, MaterialShieldRegistry.SHIELD_BASE_FLINT_NOPATTERN);
    }

    @Override
    public MapCodec<FlintUnbaked> type() {
      return MAP_CODEC;
    }
  }

  public record BoneUnbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
    public static final BoneUnbaked INSTANCE = new BoneUnbaked();
    public static final MapCodec<BoneUnbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public @Nullable ShieldMaterialSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
      return ShieldMaterialSpecialRenderer.bake(context, MaterialShieldRegistry.SHIELD_BASE_BONE, MaterialShieldRegistry.SHIELD_BASE_BONE_NOPATTERN);
    }

    @Override
    public MapCodec<BoneUnbaked> type() {
      return MAP_CODEC;
    }
  }

  public record ObsidianUnbaked() implements SpecialModelRenderer.Unbaked<DataComponentMap> {
    public static final ObsidianUnbaked INSTANCE = new ObsidianUnbaked();
    public static final MapCodec<ObsidianUnbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

    @Override
    public @Nullable ShieldMaterialSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
      return ShieldMaterialSpecialRenderer.bake(context, MaterialShieldRegistry.SHIELD_BASE_OBSIDIAN, MaterialShieldRegistry.SHIELD_BASE_OBSIDIAN_NOPATTERN);
    }

    @Override
    public MapCodec<ObsidianUnbaked> type() {
      return MAP_CODEC;
    }
  }
}
