package com.lothrazar.cyclic.render;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.MaterialShieldRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.packs.resources.ResourceManager;

@EventBusSubscriber(modid = ModCyclic.MODID, value = Dist.CLIENT)
public class ShieldBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {

  public static ShieldBlockEntityWithoutLevelRenderer instance;
  private ShieldModel shieldBody;
  private ShieldModel shieldNoPattern;

  public ShieldBlockEntityWithoutLevelRenderer(BlockEntityRenderDispatcher rd, EntityModelSet ems) {
    super(rd, ems);
  }

  @Override
  public void onResourceManagerReload(ResourceManager resourceManager) {
    EntityModelSet ems = Minecraft.getInstance().getEntityModels();
    this.shieldBody = new ShieldModel(ems.bakeLayer(ModelLayers.SHIELD));
    this.shieldNoPattern = new ShieldModel(ems.bakeLayer(ModelLayers.SHIELD));
  }

  @SubscribeEvent
  public static void onRegisterReloadListener(RegisterClientReloadListenersEvent event) {
    instance = new ShieldBlockEntityWithoutLevelRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    event.registerReloadListener(instance);
  }

  @Override
  public void renderByItem(ItemStack stackIn, ItemDisplayContext type, PoseStack ps, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
    ps.pushPose();
    ps.scale(1, -1, -1);
    boolean isBanner = stackIn.has(DataComponents.BANNER_PATTERNS);
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
    ShieldModel model = isBanner ? shieldBody : shieldNoPattern;
    var vertex = rendermaterial.sprite().wrap(ItemRenderer.getFoilBufferDirect(buffer, model.renderType(rendermaterial.atlasLocation()), true, stackIn.hasFoil()));
    model.renderToBuffer(ps, vertex, combinedLight, combinedOverlay);
    ps.popPose();
  }
}
