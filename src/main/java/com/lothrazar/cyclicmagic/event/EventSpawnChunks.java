package com.lothrazar.cyclicmagic.event;
import java.text.DecimalFormat;
import java.util.List;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSearchWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventSpawnChunks {
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
    detectSpawnableMobs(event,player.getPosition());
    if(player.dimension != Const.Dimension.overworld){
      return;
    }
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
  @SideOnly(Side.CLIENT)
  private void detectSpawnableMobs(RenderGameOverlayEvent.Text  event,BlockPos pos) {
    World world = Minecraft.getMinecraft().theWorld;
//    Biome biome = world.getBiomeForCoordsBody(pos);
//    
//    List<Biome.SpawnListEntry> clist = biome.getSpawnableList(EnumCreatureType.CREATURE);
//    List<Biome.SpawnListEntry> alist = biome.getSpawnableList(EnumCreatureType.AMBIENT);
//    List<Biome.SpawnListEntry> mlist = biome.getSpawnableList(EnumCreatureType.MONSTER);
//    List<Biome.SpawnListEntry> wlist = biome.getSpawnableList(EnumCreatureType.WATER_CREATURE);
//     

    //the biome does not tell whole story
    //ex: nether fortresses / ocean special rules
    //map objects also have own lists :  MapGenNetherBridge extends MapGenStructure
    // ChunkProviderServer not allowed
//    ChunkProviderClient
    if(world.getChunkProvider() instanceof ChunkProviderServer){
      
    ChunkProviderServer s = (ChunkProviderServer)world.getChunkProvider();
    
    List<Biome.SpawnListEntry> list = s.getPossibleCreatures(EnumCreatureType.MONSTER, pos);
    
      
      for(Biome.SpawnListEntry entry : list){
        if(WorldEntitySpawner.canCreatureTypeSpawnAtLocation(
            EntitySpawnPlacementRegistry.getPlacementForEntity(entry.entityClass),  world,pos     )){
          event.getLeft().add(entry.entityClass.getName());
        
        }
      }
      //
    }
    else{
      event.getLeft().add("not a CPS");
    }
    
  //  WorldEntitySpawner.canCreatureTypeSpawnAtLocation(En, worldIn, pos)
    
  }
}
