package com.lothrazar.cyclicmagic.module;
import java.text.DecimalFormat;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class F3InfoModule extends BaseEventModule implements IHasConfig {//TODO: no f3? eh?  
  private boolean spawnDistanceEnabled;
  private boolean horseInfoEnabled;
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void onTextOverlay(RenderGameOverlayEvent.Text event) {
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if (Minecraft.getMinecraft().gameSettings.showDebugInfo == false) { return; } //if f3 is not pressed
    if (spawnDistanceEnabled && player.dimension == Const.Dimension.overworld) {
      addSpawnInfo(event);
    }
    if (horseInfoEnabled) {
      addHorseInfo(event);
    }
  }
  private void addHorseInfo(RenderGameOverlayEvent.Text event) {
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if (player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityHorse) {
      EntityHorse horse = (EntityHorse) player.getRidingEntity();
      double speed = UtilEntity.getSpeedTranslated(horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
      // double jump = horse.getHorseJumpStrength() ;
      // convert from scale factor to blocks
      double jumpHeight = UtilEntity.getJumpTranslated(horse.getHorseJumpStrength());
      DecimalFormat df = new DecimalFormat("0.00");
      event.getLeft().add(UtilChat.lang("debug.horsespeed") + df.format(speed));
      df = new DecimalFormat("0.0");
      event.getLeft().add(UtilChat.lang("debug.horsejump") + df.format(jumpHeight));
    }
  }
  private void addSpawnInfo(RenderGameOverlayEvent.Text event) {
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    /*
     * The spawn chunks usually consist of an area of 16×16 chunks centered as
     * close as possible to the world spawn point. Entities are only active if
     * all chunks in an area of 5×5 chunks around them are loaded, limiting
     * their activities to an area of 12×12 chunks.
     * 
     * The exact rule includes chunks whose center is less than or equal to 128
     * blocks away from the world spawn along both axes. In the rare case where
     * the world spawn is located at the exact center of a chunk, 17 chunks will
     * be loaded along that axis, of which 13 activate entities.
     */
    BlockPos spawn = player.worldObj.getSpawnPoint();
    BlockPos here = player.getPosition();
    Chunk chunkHere = player.worldObj.getChunkFromBlockCoords(here);
    int xCenterOfChunk = UtilWorld.chunkToBlock(chunkHere.xPosition) + Const.CHUNK_SIZE / 2;
    int zCenterOfChunk = UtilWorld.chunkToBlock(chunkHere.zPosition) + Const.CHUNK_SIZE / 2;
    //end border
    //start spawnchunk
    //actually its the distance not from ME but from my current chunk.
    //is the center of my chunk within 128 of worldspawn
    int xFromSpawn = Math.abs(xCenterOfChunk - spawn.getX());
    int zFromSpawn = Math.abs(zCenterOfChunk - spawn.getZ());
    DecimalFormat df = new DecimalFormat("0.0");
    double dist = UtilWorld.distanceBetweenHorizontal(here, spawn);
    event.getLeft().add(UtilChat.lang("debug.spawn.distance") + df.format(dist));
    if (xFromSpawn < Const.SPAWN_RADIUS && zFromSpawn < Const.SPAWN_RADIUS) {
      event.getLeft().add(TextFormatting.GREEN + UtilChat.lang("debug.spawn.chunks"));
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    spawnDistanceEnabled = config.getBoolean("F3SpawnChunkInfo", Const.ConfigCategory.player, true, "Show Within Spawn Chunks or Distance from Spawn in F3 screen.");
    horseInfoEnabled = config.getBoolean("F3HorseInfo", Const.ConfigCategory.player, true, "Show Speed and jump height of any horse you are riding in F3.");
  }
}
