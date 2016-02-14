package com.lothrazar.cyclicmagic.spell;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellGhost extends BaseSpell implements ISpell {
	private static final String KEY_BOOLEAN = "ghost_on";
	private static final String KEY_TIMER = "ghost_timer";
	private static final String KEY_EATLOC = "ghost_location";
	private static final String KEY_EATDIM = "ghost_dim";
	private static final int GHOST_SECONDS = 5;

	public SpellGhost(int id, String name) {
		super.init(id, name);
		this.cost = 25;
	}
	
	@Override
	public void spawnParticle(World world, EntityPlayer player, BlockPos pos) {

		UtilParticle.spawnParticle(world, EnumParticleTypes.CRIT_MAGIC, pos);
	}

	@Override
	public void playSound(World world, Block block, BlockPos pos) {
		
	}

	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos) {

		// if already in ghost mode, then disallow
		if (player.capabilities.isCreativeMode || player.getEntityData().getBoolean(KEY_BOOLEAN)) {
			return false;
		}

		return super.canPlayerCast(world, player, pos);
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side) {
		setPlayerGhostMode(player, player.worldObj);

		return true;
	}

	@Override
	public void payCost(World world, EntityPlayer player, BlockPos pos) {
		UtilSound.playSoundAt(player, UtilSound.drink);

		super.payCost(world, player, pos);
	}

	private void setPlayerGhostMode(EntityPlayer player, World par2World) {
		if (par2World.isRemote == false) // false means serverside
		{
			player.setGameType(GameType.SPECTATOR);

			UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, GHOST_SECONDS * Const.TICKS_PER_SEC);
			player.getEntityData().setBoolean(KEY_BOOLEAN, true);
			player.getEntityData().setString(KEY_EATLOC, UtilNBT.posToStringCSV(player.getPosition()));
			player.getEntityData().setInteger(KEY_EATDIM, player.dimension);
		}
	}

	public static void onPlayerUpdate(LivingUpdateEvent event) {
		if (event.entityLiving instanceof EntityPlayer == false) {
			return;
		}

		EntityPlayer player = (EntityPlayer) event.entityLiving;

		if (player.getEntityData().getBoolean(KEY_BOOLEAN)) {
			//currently in ghost mode now
			int playerGhost = player.getEntityData().getInteger(KEY_TIMER);

			if (playerGhost > 0) {
				UtilNBT.incrementPlayerIntegerNBT(player, KEY_TIMER, -1);
			}
			else {
				//times up!
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

					player.fallDistance = 0.0F;
					player.setPositionAndUpdate(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
					player.setGameType(GameType.SURVIVAL);
				}

				player.getEntityData().setBoolean(KEY_BOOLEAN, false);
			}
		}
	}
}
