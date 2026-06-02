package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.item.LaserItem;
import com.lothrazar.cyclic.item.OreProspector;
import com.lothrazar.cyclic.item.builder.BuildStyle;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.builder.PacketSwapBlock;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.random.RandomizerItem;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.data.RelativeShape;
import com.lothrazar.library.util.LevelWorldUtil;
import com.lothrazar.library.util.RenderBlockUtils;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class OutlineRenderHandler {
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

    PoseStack poseStack = event.getPoseStack();
    Vec3 cameraPosition = event.getCamera().getPosition();
    Level world = player.level();
    ItemStack stack;

    // Normally, each condition should be mutually exclusive, so the overhead of render delay seems minimal
    /// Cubes: outlines and shadows
    stack = OreProspector.getIfHeld(player);
    if (stack.getItem() instanceof OreProspector) {
      handleOreProspector(poseStack, cameraPosition, world, stack);
    }

    stack = BuilderItem.getIfHeld(player);
    if (stack.getItem() instanceof BuilderItem) {
      HitResult hitResult = mc.hitResult;
      handleBuilderItem(poseStack, cameraPosition, world, hitResult, stack);
    }

    stack = RandomizerItem.getIfHeld(player);
    if (stack.getItem() instanceof RandomizerItem) {
      handleRandomizerItem(poseStack, cameraPosition, world, player);
    }

    stack = player.getMainHandItem();
    if (stack.getItem() instanceof LocationGpsCard) {
      handleLocationGpsCard(poseStack, cameraPosition, world, stack);
    }

    if (stack.getItem() instanceof ShapeCard) {
      handleShapeCard(poseStack, cameraPosition, player, stack);
    }

//    stack = LaserItem.getIfHeld(player);
//    if (!stack.isEmpty() && player.isUsingItem()) {
//      LaserBeamHandler.handle(event, player, stack);
//    }
  }

  private void handleOreProspector(PoseStack poseStack, Vec3 camPos, Level world, ItemStack stack) {
    List<BlockPosDim> coords = OreProspector.getPosition(stack);
    for (BlockPosDim loc : coords) {
      if (loc != null) {
        if (loc.getDimension() == null ||
                loc.getDimension().equalsIgnoreCase(LevelWorldUtil.dimensionToString(world))) {
          RenderBlockUtils.createBox(poseStack, loc.getPos(), camPos);
        }
      }
    }
  }

  private void handleBuilderItem(PoseStack poseStack, Vec3 camPos, Level world, HitResult hitResult, ItemStack stack) {
    if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
      BlockHitResult lookingAt = (BlockHitResult) hitResult;
      BlockPos pos = lookingAt.getBlockPos();
      BuildStyle buildStyle = ((BuilderItem) stack.getItem()).style;
      if (buildStyle.isOffset()) {
        pos = pos.relative(lookingAt.getDirection());
      }

      List<BlockPos> coordinates = PacketSwapBlock.getSelectedBlocks(world, pos, BuilderItem.getActionType(stack), lookingAt.getDirection(), buildStyle);

      for (BlockPos outline : coordinates) {
        RenderBlockUtils.createBox(poseStack, outline, camPos);
      }
    }
  }

  private void handleRandomizerItem(PoseStack poseStack, Vec3 camPos, Level world, Player player) {
    int range = 6;
    BlockHitResult lookingAt = RenderBlockUtils.getLookingAt(player, range);
    if (world.getBlockState(lookingAt.getBlockPos()).isAir()) {
      return;
    }

    List<BlockPos> coords = RandomizerItem.getPlaces(lookingAt.getBlockPos(), lookingAt.getDirection());
    Map<BlockPos, Color> colourMap = new HashMap<>();
    for (BlockPos e : coords) {
      BlockState stHere = world.getBlockState(e);
      if (!RandomizerItem.canMove(stHere, world, e) && !stHere.isAir()) {
        colourMap.put(e, Color.RED);
      }
      else if (!stHere.isAir()) {
        RenderBlockUtils.createBox(poseStack, e, camPos);
      }
    }

    if (!colourMap.isEmpty()) {
      final float scale = 1, alpha = 1;
      RenderBlockUtils.renderColourCubes(poseStack, camPos, colourMap, scale, alpha);
    }
  }

  private void handleLocationGpsCard(PoseStack poseStack, Vec3 camPos, Level world, ItemStack stack) {
    BlockPosDim loc = LocationGpsCard.getPosition(stack);
    if (loc != null) {
      if (loc.getDimension() == null ||
              loc.getDimension().equalsIgnoreCase(LevelWorldUtil.dimensionToString(world))) {
        final float scale = 1, alpha = 1;
        Map<BlockPos, Color> colourMap = new HashMap<>();
        colourMap.put(loc.getPos(), ClientConfigCyclic.getColor(stack));
        RenderBlockUtils.renderColourCubes(poseStack, camPos, colourMap, scale, alpha);
      }
    }
  }

  private void handleShapeCard(PoseStack poseStack, Vec3 camPos, Player player, ItemStack stack) {
    RelativeShape shape = RelativeShape.read(stack);
    if (shape != null) {
      BlockPos here = player.blockPosition();
      for (BlockPos s : shape.getShape()) {
        RenderBlockUtils.createBox(poseStack, here.offset(s), camPos);
      }
    }
  }
}
