package com.lothrazar.cyclicmagic.event;

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
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventSpawnChunks implements IFeatureEvent{

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
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo == false) {
			return;//if f3 is not pressed
		}
		/*
		 * The spawn chunks usually consist of an area of 16×16 chunks centered
		 * as close as possible to the world spawn point. Entities are only
		 * active if all chunks in an area of 5×5 chunks around them are loaded,
		 * limiting their activities to an area of 12×12 chunks. 
		 * 
		 * The exact rule
		 * includes chunks whose center is less than or equal to 128 blocks away
		 * from the world spawn along both axes. In the rare case where the
		 * world spawn is located at the exact center of a chunk, 17 chunks will
		 * be loaded along that axis, of which 13 activate entities.
		 */
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		BlockPos spawn = player.worldObj.getSpawnPoint();
		//Chunk chunkSpawn = player.worldObj.getChunkFromBlockCoords(spawn);
		
		//event.getLeft().add(UtilChat.lang("debug.spawn.pos") + " "+UtilChat.blockPosToString(spawn));
		

		//if(player.getBedLocation() != null)
		//	event.getLeft().add(UtilChat.lang("debug.bedspawn.pos") + " "+UtilChat.blockPosToString(player.getBedLocation()));
		
		BlockPos here = player.getPosition();
		Chunk chunkHere = player.worldObj.getChunkFromBlockCoords(here);
		int xCenterOfChunk = chunkToBlock(chunkHere.xPosition) + Const.CHUNK_SIZE/2;
		int zCenterOfChunk = chunkToBlock(chunkHere.zPosition) + Const.CHUNK_SIZE/2;
		/*
		//start center
		
		String f = "";
		
		int away = 2;
		if(	   Math.abs(xCenterOfChunk - here.getX()) <= away 
			&& Math.abs(zCenterOfChunk - here.getZ()) <= away){
			f = TextFormatting.GREEN + "";
		}
		event.getLeft().add(UtilChat.lang("debug.chunk.center")+" : " + f +xCenterOfChunk +","+zCenterOfChunk);
		//end center
		
		//start border
		int l = chunkToBlock(chunkHere.xPosition);
		int r = chunkToBlock(chunkHere.xPosition) + Const.CHUNK_SIZE;

		int t = chunkToBlock(chunkHere.zPosition);
		int b = chunkToBlock(chunkHere.zPosition) + Const.CHUNK_SIZE;

		f = "";
		if(here.getZ() % Const.CHUNK_SIZE <= away || here.getX() % Const.CHUNK_SIZE <= away){
			f = TextFormatting.GREEN + "";
		}
		event.getLeft().add(UtilChat.lang("debug.chunk.border")+ f +" ["+ l+","+r+" ["+ t +","+b+"]");
		*/
//end border
		
		//start spawnchunk
		
		//actually its the distance not from ME but from my current chunk.
		//is the center of my chunk within 128 of worldspawn
		int xFromSpawn = Math.abs(xCenterOfChunk - spawn.getX());

		int zFromSpawn = Math.abs(zCenterOfChunk - spawn.getZ());
		
		//int xDistance = Math.abs(here.getX() - spawn.getX());
		//int zDistance = Math.abs(here.getZ() - spawn.getZ());

		DecimalFormat df = new DecimalFormat("0.0");

		double dist = UtilSearchWorld.distanceBetweenHorizontal(here, spawn);

		event.getLeft().add(UtilChat.lang("debug.spawn.distance") +  df.format(dist));
		
		if (xFromSpawn < SPAWN_RADIUS && zFromSpawn < SPAWN_RADIUS) {

			//System.out.println("fromspawn ="+xFromSpawn+"__"+zFromSpawn);
			event.getLeft().add(TextFormatting.GREEN + UtilChat.lang("debug.spawn.chunks"));
			
		} 
		//end spawnchunk
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
