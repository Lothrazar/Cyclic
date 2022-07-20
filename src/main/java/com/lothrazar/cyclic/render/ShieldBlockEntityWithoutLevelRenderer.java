package com.lothrazar.cyclic.render;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MaterialShieldRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModCyclic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ShieldBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

  public static ShieldBlockEntityWithoutLevelRenderer instance;

  public ShieldBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher rd, EntityModelSet ems) {
    super(rd, ems);
  }

  @SubscribeEvent
  public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
    instance = new ShieldBlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    event.registerReloadListener(instance);
  }

  @Override
  public void renderByItem(ItemStack stackIn, ItemTransforms.TransformType type, PoseStack ps, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
    //copied from superclass
    ps.pushPose();
    ps.scale(1, -1, -1);
    boolean isBanner = (stackIn.getTagElement("BlockEntityTag") != null);
    Material rendermaterial = isBanner ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
    if (stackIn.is(ItemRegistry.SHIELD_WOOD.get())) {
      rendermaterial = isBanner ? MaterialShieldRegistry.SHIELD_BASE_WOOD : MaterialShieldRegistry.SHIELD_BASE_WOOD_NOPATTERN;
    }
    else if (stackIn.is(ItemRegistry.SHIELD_LEATHER.get())) {
      rendermaterial = isBanner ? MaterialShieldRegistry.SHIELD_BASE_LEATHER : MaterialShieldRegistry.SHIELD_BASE_LEATHER_NOPATTERN;
    }
    else if (stackIn.is(ItemRegistry.SHIELD_FLINT.get())) {
      rendermaterial = isBanner ? MaterialShieldRegistry.SHIELD_BASE_FLINT : MaterialShieldRegistry.SHIELD_BASE_FLINT_NOPATTERN;
    }
    else if (stackIn.is(ItemRegistry.SHIELD_BONE.get())) {
      rendermaterial = isBanner ? MaterialShieldRegistry.SHIELD_BASE_BONE : MaterialShieldRegistry.SHIELD_BASE_BONE_NOPATTERN;
    }
    else if (stackIn.is(ItemRegistry.SHIELD_OBSIDIAN.get())) {
      rendermaterial = isBanner ? MaterialShieldRegistry.SHIELD_BASE_OBSIDIAN : MaterialShieldRegistry.SHIELD_BASE_OBSIDIAN_NOPATTERN;
    }
    VertexConsumer vertex = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, shieldModel.renderType(rendermaterial.atlasLocation()), true, stackIn.hasFoil()));
    shieldModel.handle().render(ps, vertex, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    if (isBanner) {
      List<Pair<BannerPattern, DyeColor>> pattern = BannerBlockEntity.createPatterns(ShieldItem.getColor(stackIn), BannerBlockEntity.getItemPatterns(stackIn));
      BannerRenderer.renderPatterns(ps, buffer, combinedLight, combinedOverlay, shieldModel.plate(), rendermaterial, false, pattern, stackIn.hasFoil());
    }
    else {
      shieldModel.plate().render(ps, vertex, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    ps.popPose();
  }
}
