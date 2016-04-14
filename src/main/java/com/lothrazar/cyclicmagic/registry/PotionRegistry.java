package com.lothrazar.cyclicmagic.registry;

import java.util.List;
import com.lothrazar.cyclicmagic.potion.PotionCustom;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PotionRegistry {

	public static float					slowfallSpeed;
	public static boolean				renderOnLeft;
	// tired;//http://www.minecraftforge.net/wiki/Potion_Tutorial
	public static PotionCustom	slowfall;
	public static PotionCustom	magnet;
	public static PotionCustom	ender;
	public static PotionCustom	waterwalk;

	public final static int			I		= 0;
	public final static int			II	= 1;
	public final static int			III	= 2;
	public final static int			IV	= 3;
	public final static int			V		= 4;

	public static void register() {

		// http://www.minecraftforge.net/forum/index.php?topic=11024.0
		// ??? http://www.minecraftforge.net/forum/index.php?topic=12358.0
		PotionRegistry.ender = new PotionCustom(new ResourceLocation(Const.MODID, "textures/items/apple_ender.png"), false, 0, "potion.ender");

		PotionRegistry.waterwalk = new PotionCustom(new ResourceLocation("minecraft", "textures/items/prismarine_shard.png"), false, 0, "potion.waterwalk");

		PotionRegistry.slowfall = new PotionCustom(new ResourceLocation(Const.MODID, "textures/potions/slowfall.png"), false, 0, "potion.slowfall");

		PotionRegistry.magnet = new PotionCustom(new ResourceLocation(Const.MODID, "textures/potions/magnet.png"), false, 0, "potion.magnet");

		GameRegistry.register(ender,ender.getIcon());
		GameRegistry.register(waterwalk,waterwalk.getIcon());
		GameRegistry.register(slowfall,slowfall.getIcon());
		GameRegistry.register(magnet,magnet.getIcon());
		//this is bad
	// GameData.getPotionRegistry()
		
		// this is also bad:
	//	Potion.potionRegistry.putObject(ender.getIcon(), ender);

		// TODO: test out brewing api for these?
	}

	private final static int		ITEM_HRADIUS	= 20;
	private final static int		ITEM_VRADIUS	= 4;
	private final static float	ITEMSPEED			= 1.2F;

	public static void tickWaterwalk(EntityLivingBase entityLiving) {

		tickLiquidWalk(entityLiving, Blocks.water);

	}

	private static void tickLiquidWalk(EntityLivingBase entityLiving, Block liquid) {
		World world = entityLiving.worldObj;

		if (world.getBlockState(entityLiving.getPosition().down()).getBlock() == liquid && world.isAirBlock(entityLiving.getPosition()) && entityLiving.motionY < 0) {
			if (entityLiving instanceof EntityPlayer)  // now wait here, since if we
			                                           // are a sneaking player we
			                                           // cancel it
			{
				EntityPlayer p = (EntityPlayer) entityLiving;
				if (p.isSneaking())
					return;// let them slip down into it
			}

			entityLiving.motionY = 0;// stop falling
			entityLiving.onGround = true; // act as if on solid ground
			entityLiving.setAIMoveSpeed(0.1F);// walking and not sprinting is this
			                                  // speed
		}
	}

	public static void tickMagnet(EntityLivingBase entityLiving) {

		World world = entityLiving.worldObj;

		BlockPos pos = entityLiving.getPosition();
		int x = pos.getX(), y = pos.getY(), z = pos.getZ();

		List<EntityItem> found = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));

		// int moved = 0;
		for (EntityItem eitem : found) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
			// moved++;
		}

		List<EntityXPOrb> foundExp = world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));

		for (EntityXPOrb eitem : foundExp) {
			Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
			// moved++;
		}
	}

	public static void handle(EntityLivingBase entity) {

		if (entity.isPotionActive(PotionRegistry.slowfall)) {
			tickSlowfall(entity);
		}

		if (entity.isPotionActive(PotionRegistry.magnet)) {
			tickMagnet(entity);
		}

		if (entity.isPotionActive(PotionRegistry.waterwalk)) {
			tickWaterwalk(entity);
		}
		if (entity.isPotionActive(PotionRegistry.ender)) {

			// tick ender ?
			// tickWaterwalk(entity);
		}

	}

	public static void tickSlowfall(EntityLivingBase entityLiving) {

		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer) entityLiving;
			if (p.isSneaking()) { return;// so fall normally for now
			}
		}

		// else: so we are either a non-sneaking player, or a non player
		// entity

		// a normal fall seems to go up to 0, -1.2, -1.4, -1.6, then
		// flattens out at -0.078
		if (entityLiving.motionY < 0) {
			entityLiving.motionY *= slowfallSpeed;

			entityLiving.fallDistance = 0f; // for no fall damage
		}
	}

	public static void addOrMergePotionEffect(EntityLivingBase player, PotionEffect newp) {

		// this could be in a utilPotion class i guess...
		if (player.isPotionActive(newp.getPotion())) {
			// do not use built in 'combine' function, just add up duration
			PotionEffect p = player.getActivePotionEffect(newp.getPotion());

			int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());
			int dur = newp.getDuration() + p.getDuration();

			player.addPotionEffect(new PotionEffect(newp.getPotion(), dur, ampMax));
		}
		else {
			player.addPotionEffect(newp);
		}
	}

	public static void syncConfig(Configuration config) {

		String category = Const.MODCONF + "Misc";

		PotionRegistry.slowfallSpeed = config.getFloat("Slowfall Speed", category, 0.41F, 0.1F, 1F, "This factor affects how much the slowfall effect slows down the entity.");

	}
}