package com.lothrazar.cyclic.event;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclic.config.ClientConfigCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.filesystem.CyclicFile;
import com.lothrazar.cyclic.item.LaserItem;
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
import com.lothrazar.library.core.BlockPosDim;
import com.lothrazar.library.data.RelativeShape;
import com.lothrazar.library.render.RenderEntityToBlockLaser;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.LevelWorldUtil;
import com.lothrazar.library.util.PlayerUtil;
import com.lothrazar.library.util.RenderBlockUtils;
import com.lothrazar.library.util.RenderUtil;
import com.lothrazar.library.util.SoundUtil;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventRender {

  @SubscribeEvent
  public static void onCustomizeDebugText(CustomizeGuiOverlayEvent.DebugText event) {
    //Build scepter feature : render selected blockstate in cross hair
    Player player = Minecraft.getInstance().player;
    var level = player.level();
    Minecraft mc = Minecraft.getInstance();
    // ModCyclic.LOGGER.info("TESTME : ElementType.CROSSHAIRS is gone deleted");
    //    if (event.getType() == ElementType.ALL) {
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
    int height = mc.getWindow().getGuiScaledHeight();
    CyclicFile datFile = PlayerDataEvents.getOrCreate(player);
    if (datFile.spectatorTicks > 0) {
      int sec = datFile.spectatorTicks / 20;
      RenderUtil.drawString(event.getGuiGraphics(), "noClip " + sec, 10, height - 10);
    }
  }

  @SubscribeEvent
  public void onRenderWorldLast(RenderLevelStageEvent event) {
    //    if (event.getStage() == Stage.AFTER_TRANSLUCENT_BLOCKS) { // was AFTER_SOLID_BLOCKS
    //      //      return; //send it
    //    }
    if (event.getStage() != Stage.AFTER_TRANSLUCENT_BLOCKS) { // was AFTER_SOLID_BLOCKS
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
      List<BlockPos> coords = RandomizerItem.getPlaces(lookingAt.getBlockPos(), lookingAt.getDirection());
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
      RenderBlockUtils.renderColourCubes(matrix, view, renderCubes, scale, alpha); // TODO: why do they wiggle
    }
    /****************** end rendering cubes. start laser beam render ********************/
    stack = LaserItem.getIfHeld(player);
    if (!stack.isEmpty() && player.isUsingItem()) {
      IEnergyStorage storage = stack.getCapability(ForgeCapabilities.ENERGY, null).orElse(null);
      if (storage == null || storage.getEnergyStored() < ConfigRegistry.LaserItemEnergy.get()) {
        return;
      }
      // RayTraceResult  became HitResult
      // objectMouseOver became hitResult
      if (mc.crosshairPickEntity != null) {
        //Render and Shoot. closerange version
        RenderEntityToBlockLaser.renderLaser(event, player, mc.getFrameTime(), stack, InteractionHand.MAIN_HAND, 18, -0.02F); // TODO
        if (world.getGameTime() % 4 == 0) {
          PacketRegistry.INSTANCE.sendToServer(new PacketEntityLaser(mc.crosshairPickEntity.getId(), true));
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
              RenderEntityToBlockLaser.renderLaser(event, player, mc.getFrameTime(), stack, InteractionHand.MAIN_HAND);
              if (world.getGameTime() % 4 == 0) {
                PacketRegistry.INSTANCE.sendToServer(new PacketEntityLaser(ehr.getEntity().getId(), false));
                SoundUtil.playSound(player, SoundRegistry.LASERBEANPEW.get(), 0.2F);
              }
            }
          }
        }
        else {
          //we missed. Do we render on a miss?
          if (ConfigRegistry.LaserRenderMisses.get()) {
            RenderEntityToBlockLaser.renderLaser(event, player, mc.getFrameTime(), stack, InteractionHand.MAIN_HAND);
          }
          else { //if we dont render the miss, show a message for better user experience
            ChatUtil.sendStatusMessage(player, "item.cyclic.laser_cannon.notarget");
          }
        }
      }
    }
  }
}
