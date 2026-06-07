package com.lothrazar.cyclic.render;

import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.item.LaserItem;
import com.lothrazar.cyclic.util.CapabilityUtil;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.network.PacketDistributor;
import com.lothrazar.cyclic.item.OreProspector;
import com.lothrazar.cyclic.item.builder.BuildStyle;
import com.lothrazar.cyclic.item.builder.BuilderActionType;
import com.lothrazar.cyclic.item.builder.BuilderItem;
import com.lothrazar.cyclic.item.builder.PacketSwapBlock;
import com.lothrazar.cyclic.item.datacard.LocationGpsCard;
import com.lothrazar.cyclic.item.datacard.ShapeCard;
import com.lothrazar.cyclic.item.random.RandomizerItem;
import com.lothrazar.cyclic.net.PacketEntityLaser;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.data.BlockPosDim;
import com.lothrazar.library.data.RelativeShape;
import com.lothrazar.library.render.RenderEntityToBlockLaser;
import com.lothrazar.library.util.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// was event.EventRender in 1.20
public class OutlineRenderer {

  @SubscribeEvent
  public void onCustomizeDebugText(CustomizeGuiOverlayEvent.DebugText event) {
    //Build scepter feature : render selected blockstate in cross hair
    Player player = Minecraft.getInstance().player;
    var level = player.level();
    Minecraft mc = Minecraft.getInstance();
    ItemStack itemStackHeld = BuilderItem.getIfHeld(player);
    if (itemStackHeld.getItem() instanceof BuilderItem) {
      //
      BlockState targetState = BuilderActionType.getBlockState(level, itemStackHeld);
      if (targetState != null) {
        //ok still 
        RenderUtil.drawStack(event.getGuiGraphics(), new ItemStack(targetState.getBlock()));
        int slot = PlayerUtil.getFirstSlotWithBlock(player, targetState);
        if (slot < 0) {
          //nothing found
          int width = mc.getWindow().getGuiScaledWidth();
          int height = mc.getWindow().getGuiScaledHeight();
          RenderUtil.drawString(event.getGuiGraphics(), "" + 0, width / 2 + 16, height / 2 + 12);
        }
      }
    }
  }

  @SubscribeEvent
  public void onRenderWorldLast(RenderLevelStageEvent event) {
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) { // was AFTER_SOLID_BLOCKS / AFTER_TRANSLUCENT_BLOCKS / AFTER_PARTICLES - testing the very last stage
      return; //send it
    }
    Minecraft mc = Minecraft.getInstance();
    Player player = mc.player;
    if (player == null) {
      return;
    }
    Level world = player.level();
    ItemStack stack = ItemStack.EMPTY;
    List<BlockPos> putBoxHere = new ArrayList<>();
    /****************** rendering outline ********************/
    stack = OreProspector.getIfHeld(player);
    if (stack.getItem() instanceof OreProspector) {
      List<BlockPosDim> coords = OreProspector.getPosition(stack);
      for (BlockPosDim loc : coords) {
        if (loc != null) {
          if (loc.getDimension() == null ||
              loc.getDimension().equalsIgnoreCase(LevelWorldUtil.dimensionToString(world))) {
            RenderBlockUtils.createBox(event.getPoseStack(), loc.getPos());
            putBoxHere.add(loc.getPos());
          }
        }
      }
    }
    /****************** end rendering outline,. start cubes ********************/
    double range = 6F;
    float alpha = 1; //0.125F * 2;
    Map<BlockPos, Color> renderCubes = new HashMap<>();
    ///////////////////// BuilderItem
    stack = BuilderItem.getIfHeld(player);
    if (stack.getItem() instanceof BuilderItem) {
      BlockHitResult lookingAt = (BlockHitResult) player.pick(range, 0F, false);
      if (!world.isEmptyBlock(lookingAt.getBlockPos())) {
        BlockPos pos = lookingAt.getBlockPos();
        BuildStyle buildStyle = ((BuilderItem) stack.getItem()).style;
        if (buildStyle.isOffset() && lookingAt.getDirection() != null) {
          pos = pos.relative(lookingAt.getDirection());
        }
        alpha = .01f;
        //now the item has a build area
        List<BlockPos> coordinates = PacketSwapBlock.getSelectedBlocks(world, pos, BuilderItem.getActionType(stack), lookingAt.getDirection(), buildStyle);
        for (BlockPos coordinate : coordinates) {
          putBoxHere.add(coordinate);
        }
      }
    }
    ///////////////////// RandomizerItem
    stack = RandomizerItem.getIfHeld(player);
    if (stack.getItem() instanceof RandomizerItem) {
      BlockHitResult lookingAt = RenderBlockUtils.getLookingAt(player, (int) range);
      if (world.getBlockState(lookingAt.getBlockPos()).isAir()) {
        return;
      }
      List<BlockPos> coords = RandomizerItem.getPlaces(lookingAt.getBlockPos(), lookingAt.getDirection(), stack);
      for (BlockPos e : coords) {
        BlockState stHere = world.getBlockState(e);
        if (!RandomizerItem.canMove(stHere, world, e) && !stHere.isAir()) {
          renderCubes.put(e, Color.RED);
        }
        else if (!stHere.isAir()) {
          putBoxHere.add(e);
        }
      }
    }
    stack = player.getMainHandItem();
    //    if (EntityDataCard.hasEntity(stack)) {
    //      Entity etar = EntityDataCard.matchesEntity(etar, stack);
    //    }
    ///////////////////// LocationGpsItem
    if (stack.getItem() instanceof LocationGpsCard) {
      BlockPosDim loc = LocationGpsCard.getPosition(stack);
      if (loc != null) {
        if (loc.getDimension() == null ||
            loc.getDimension().equalsIgnoreCase(LevelWorldUtil.dimensionToString(world))) {
          renderCubes.put(loc.getPos(), ClientConfigCyclic.getColor(stack));
        }
      }
    }
    ///////////////////////////////////////ShapeCard
    if (stack.getItem() instanceof ShapeCard) {
      RelativeShape shape = RelativeShape.read(stack);
      if (shape != null) {
        BlockPos here = player.blockPosition();
        for (BlockPos s : shape.getShape()) {
          putBoxHere.add(here.offset(s));
        }
      }
    }
    //the block edge outline
    for (BlockPos coordinate : putBoxHere) {
      RenderBlockUtils.createBox(event.getPoseStack(), coordinate);
    }
    //render the pos->colour map 
    if (renderCubes.keySet().size() > 0) {
      float scale = 1;
      PoseStack matrix = event.getPoseStack();
      Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
      RenderBlockUtils.renderColourCubes(matrix, view, renderCubes, scale, alpha);
    }
    /****************** end rendering cubes. start laser beam render ********************/
    stack = LaserItem.getIfHeld(player);
    if (!stack.isEmpty() && player.isUsingItem()) {
      IEnergyStorage storage = CapabilityUtil.energy(stack);
      if (storage == null || storage.getEnergyStored() < ConfigRegistry.LaserItemEnergy.get()) {
        return;
      }
      // RayTraceResult  became HitResult
      // objectMouseOver became hitResult
      java.awt.Color laserColor;
      try {
        laserColor = java.awt.Color.decode(ClientConfigCyclic.LASER_COLOR.get());
      }
      catch (Exception e) {
        laserColor = new java.awt.Color(100, 0, 2);
      }
      float lr = laserColor.getRed() / 255f;
      float lg = laserColor.getGreen() / 255f;
      float lb = laserColor.getBlue() / 255f;
      if (mc.crosshairPickEntity != null) {
        //Render and Shoot. closerange version
        RenderEntityToBlockLaser.renderLaser(event, player, mc.getTimer().getGameTimeDeltaPartialTick(false), stack, InteractionHand.MAIN_HAND, 18, -0.02F, lr, lg, lb);
        if (world.getGameTime() % 4 == 0) {
          PacketDistributor.sendToServer(new PacketEntityLaser(mc.crosshairPickEntity.getId(), true));
          SoundUtil.playSound(player, SoundRegistry.LASERBEANPEW.get(), 0.2F);
        }
      }
      else {
        //out of range- do custom raytrace for longrange version
        final int laserRange = ConfigRegistry.LaserItemRange.get();
        double laserGamemodeRange = laserRange;// mc.gameMode.getPickRange() * LaserItem.RANGE_FACTOR;
        Entity camera = mc.getCameraEntity();
        Vec3 cameraViewVector = camera.getViewVector(1.0F);
        Vec3 cameraEyePosition = camera.getEyePosition(1.0F);
        Vec3 cameraEyeViewRay = cameraEyePosition.add(cameraViewVector.x * laserGamemodeRange, cameraViewVector.y * laserGamemodeRange, cameraViewVector.z * laserGamemodeRange);
        AABB aabb = camera.getBoundingBox().expandTowards(cameraViewVector.scale(laserGamemodeRange)).inflate(1.0D, 1.0D, 1.0D);
        //
        //
        EntityHitResult ehr = ProjectileUtil.getEntityHitResult(camera, cameraEyePosition, cameraEyeViewRay, aabb, (ent) -> {
          return ent.isAttackable() && ent.isAlive();
        }, 0);
        if (ehr != null) {
          Vec3 entityHitResultLocation = ehr.getLocation();
          double distance = Math.sqrt(cameraEyePosition.distanceToSqr(entityHitResultLocation));
          if (distance < laserRange) {
            //first vector is FROM, second is TO
            BlockHitResult miss = mc.level.clip(new ClipContext(cameraEyePosition, entityHitResultLocation, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, mc.player));
            if (miss.getType() != HitResult.Type.BLOCK) {
              //  dont shoot thru walls
              RenderEntityToBlockLaser.renderLaser(event, player, mc.getTimer().getGameTimeDeltaPartialTick(false), stack, InteractionHand.MAIN_HAND, lr, lg, lb);
              if (world.getGameTime() % 4 == 0) {
                PacketDistributor.sendToServer(new PacketEntityLaser(ehr.getEntity().getId(), false));
                SoundUtil.playSound(player, SoundRegistry.LASERBEANPEW.get(), 0.2F);
              }
            }
          }
        }
        else {
          //we missed. Do we render on a miss?
          if (ConfigRegistry.LaserRenderMisses.get()) {
            RenderEntityToBlockLaser.renderLaser(event, player, mc.getTimer().getGameTimeDeltaPartialTick(false), stack, InteractionHand.MAIN_HAND, lr, lg, lb);
          }
          else { //if we dont render the miss, show a message for better user experience
            ChatUtil.sendStatusMessage(player, "item.cyclic.laser_cannon.notarget");
          }
        }
      }
    }
  }
}
