package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.item.builder.BuildStyle;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.builder.PacketSwapBlock;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class EventRender {
  @SubscribeEvent
  public void onRenderOverlay(RenderGuiEvent.Post event) {}
  @SubscribeEvent
  public void onRenderWorldLast(RenderLevelStageEvent event) {}

  @SubscribeEvent
  public void onRenderOutline(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
      return;
    }

    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;
    if (player == null) {
      return;
    }

    Level world = player.level();
    ItemStack stack = BuilderItem.getIfHeld(player);
    List<BlockPos> outlines = new ArrayList<>();

    if (stack.getItem() instanceof BuilderItem) {
      HitResult hitResult = mc.hitResult;
      if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
        BlockHitResult lookingAt = (BlockHitResult) hitResult;
        BlockPos pos = lookingAt.getBlockPos();
        BuildStyle buildStyle = ((BuilderItem) stack.getItem()).style;
        if (buildStyle.isOffset()) {
          pos = pos.relative(lookingAt.getDirection());
        }

        List<BlockPos> coordinates = PacketSwapBlock.getSelectedBlocks(world, pos, BuilderItem.getActionType(stack), lookingAt.getDirection(), buildStyle);
        outlines.addAll(coordinates);

        for (BlockPos outline : outlines) {
          testRender(event.getPoseStack(), event.getCamera(), outline);
        }
      }
    }
  }

  // TODO : Decide where this code should be put, or how to possibly use FLib methods
  private void testRender(PoseStack poseStack, Camera camera, BlockPos target) {
    float[] color = RenderBlockUtils.getRandomColour();
    float red = color[0],  green = color[1], blue = color[2], alpha = 1.0F;

    MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance()
            .renderBuffers().bufferSource();

    // Adjustment for the position of the camera
    Vec3 camPos = camera.getPosition();

    poseStack.pushPose();
    poseStack.translate(
            target.getX() - camPos.x,
            target.getY() - camPos.y,
            target.getZ() - camPos.z
    );

    VertexConsumer lines = bufferSource.getBuffer(RenderType.lines());

    // Renders the outline of a single block
    LevelRenderer.renderLineBox(
            poseStack,
            lines,
            new AABB(0, 0, 0, 1, 1, 1), // Local AABB (origin = block corner)
            red, green, blue, alpha
    );

    bufferSource.endBatch(RenderType.lines());
    poseStack.popPose();
  }
}
