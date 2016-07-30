package com.lothrazar.cyclicmagic.module;
import java.text.DecimalFormat;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSearchWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class F3InfoModule extends BaseEventModule{
  final static int SPAWN_RADIUS = 8 * Const.CHUNK_SIZE;// 128 is spawn size
  // 1 chunk is 16x16 blocks
  public static int blockToChunk(int blockVal) {
    return blockVal >> 4; // ">>4" == "/16"
  }
  public static int chunkToBlock(int chunkVal) {
    return chunkVal << 4; // "<<4" == "*16"
  }
  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public void addSpawnInfo(RenderGameOverlayEvent.Text event) {
    if (Minecraft.getMinecraft().gameSettings.showDebugInfo == false) { return;//if f3 is not pressed
    }
    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
    if (player.dimension != Const.Dimension.overworld) { return; }
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
    int xCenterOfChunk = chunkToBlock(chunkHere.xPosition) + Const.CHUNK_SIZE / 2;
    int zCenterOfChunk = chunkToBlock(chunkHere.zPosition) + Const.CHUNK_SIZE / 2;
    //end border
    //start spawnchunk
    //actually its the distance not from ME but from my current chunk.
    //is the center of my chunk within 128 of worldspawn
    int xFromSpawn = Math.abs(xCenterOfChunk - spawn.getX());
    int zFromSpawn = Math.abs(zCenterOfChunk - spawn.getZ());
    DecimalFormat df = new DecimalFormat("0.0");
    double dist = UtilSearchWorld.distanceBetweenHorizontal(here, spawn);
    event.getLeft().add(UtilChat.lang("debug.spawn.distance") + df.format(dist));
    if (xFromSpawn < SPAWN_RADIUS && zFromSpawn < SPAWN_RADIUS) {
      event.getLeft().add(TextFormatting.GREEN + UtilChat.lang("debug.spawn.chunks"));
    }
  }
}
