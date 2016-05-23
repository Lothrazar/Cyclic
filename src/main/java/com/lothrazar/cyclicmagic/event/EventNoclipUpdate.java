package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilTeleport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventNoclipUpdate implements IHasConfig{

	//revived from https://github.com/PrinceOfAmber/Cyclic/blob/d2f91d1f97b9cfba47786a30b427fbfdcd714212/src/main/java/com/lothrazar/cyclicmagic/spell/SpellGhost.java
	private static final String KEY_BOOLEAN = "ghost_on";
	private static final String KEY_TIMER = "ghost_timer";
	private static final String KEY_EATLOC = "ghost_location";
	private static final String KEY_EATDIM = "ghost_dim";
	private static final int GHOST_SECONDS = 10;
	private final static int POTION_SECONDS = 20;
	
	public static void setPlayerGhostMode(EntityPlayer player, World par2World) {
		if (par2World.isRemote == false){
			player.setGameType(GameType.SPECTATOR);

			ModMain.logger.warn("WARN: dont use entitydata here");
			UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, GHOST_SECONDS * Const.TICKS_PER_SEC);
			player.getEntityData().setBoolean(KEY_BOOLEAN, true);
			player.getEntityData().setString(KEY_EATLOC, UtilNBT.posToStringCSV(player.getPosition()));
			player.getEntityData().setInteger(KEY_EATDIM, player.dimension);
		}
	}


	@SubscribeEvent
	public void onPlayerUpdate(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer == false) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		World world = player.worldObj;

		if (player.getEntityData().getBoolean(KEY_BOOLEAN)) {
			//currently in ghost mode now
			ModMain.logger.warn("WARN: dont use entitydata here");
			int playerGhost = player.getEntityData().getInteger(KEY_TIMER);
			
			if (playerGhost > 0) {
				if(playerGhost % Const.TICKS_PER_SEC == 0){
					int secs = playerGhost / Const.TICKS_PER_SEC;
					UtilChat.addChatMessage(player, "" + secs);
				}
				
				UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, -1);
			}
			else {
				//times up!
				player.getEntityData().setBoolean(KEY_BOOLEAN, false);
				
				if (player.getEntityData().getInteger(KEY_EATDIM) != player.dimension) {
					// if the player changed dimension while a ghost, thats not
					// allowed. dont tp them back

					player.setGameType(GameType.SURVIVAL);
					player.attackEntityFrom(DamageSource.magic, 50);
				}
				else {
					// : teleport back to source
					String posCSV = player.getEntityData().getString(KEY_EATLOC);
					String[] p = posCSV.split(",");

					
					BlockPos currentPos = player.getPosition();
					
					BlockPos sourcePos = new BlockPos(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
					
					if(world.isAirBlock(currentPos) && world.isAirBlock(currentPos.up())){
						//then we can stay, but add nausea
						player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA,Const.TICKS_PER_SEC*POTION_SECONDS ));
						player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS,Const.TICKS_PER_SEC*POTION_SECONDS ));
					}
					else{
						//teleport back home	
						UtilTeleport.teleportWallSafe(player, world, sourcePos);
						//player.setPositionAndUpdate(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
						
					}
					
					player.fallDistance = 0.0F;
					player.setGameType(GameType.SURVIVAL);
				}
			}
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}
}
