package com.lothrazar.cyclic.render;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ClientRegistryCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import java.util.List;
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
  //  @Override
  //  public void onResourceManagerReload(ResourceManager rm) {
  //    this.shield = new ShieldModel(this.ems.bakeLayer(ModelLayers.SHIELD));
  //  }

  @Override
  public void renderByItem(ItemStack stackIn, ItemTransforms.TransformType type, PoseStack ps, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
    //copied from superclass
    ps.pushPose();
    ps.scale(1, -1, -1);
    boolean isBanner = (stackIn.getTagElement("BlockEntityTag") != null);
    Material rendermaterial = isBanner ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
    if (stackIn.is(ItemRegistry.SHIELD_WOOD.get())) {
      rendermaterial = isBanner ? ClientRegistryCyclic.SHIELD_BASE_WOOD : ClientRegistryCyclic.SHIELD_BASE_WOOD_NOPATTERN;
    }
    else if (stackIn.is(ItemRegistry.SHIELD_LEATHER.get())) {
      rendermaterial = isBanner ? ClientRegistryCyclic.SHIELD_BASE_LEATHER : ClientRegistryCyclic.SHIELD_BASE_LEATHER_NOPATTERN;
    }
    VertexConsumer vertex = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, shieldModel.renderType(rendermaterial.atlasLocation()), true, stackIn.hasFoil()));
    shieldModel.handle().render(ps, vertex, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    //how do i test banner support?> does it need recipes
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
