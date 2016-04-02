package com.lothrazar.cyclicmagic.registry;

import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.potion.PotionCustom;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Vector3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class PotionRegistry{

	public static float slowfallSpeed;
	public static boolean renderOnLeft;
	// tired;//http://www.minecraftforge.net/wiki/Potion_Tutorial
	public static Potion slowfall;
	public static Potion magnet;

	public final static int I = 0;
	public final static int II = 1;
	public final static int III = 2;
	public final static int IV = 3;
	public final static int V = 4;

	public static void register(){

		registerNewPotionEffects();
	}

	private static void registerNewPotionEffects(){

		// http://www.minecraftforge.net/forum/index.php?topic=11024.0
		// ??? http://www.minecraftforge.net/forum/index.php?topic=12358.0

		PotionRegistry.slowfall = new PotionCustom( new ResourceLocation(Const.MODID, "textures/potions/slowfall.png"), false, 0, "potion.slowfall");

		PotionRegistry.magnet = new PotionCustom( new ResourceLocation(Const.MODID, "textures/potions/magnet.png"), false, 0, "potion.magnet");

		// TODO: test out brewing api for these?
	}

	private final static int ITEM_HRADIUS = 20;
	private final static int ITEM_VRADIUS = 4;
	private final static float ITEMSPEED = 1.2F;

	public static void tickMagnet(EntityLivingBase entityLiving){

		if(entityLiving.isPotionActive(PotionRegistry.magnet)){
	
			World world = entityLiving.worldObj;

			BlockPos pos = entityLiving.getPosition();
			int x = pos.getX(), y = pos.getY(), z = pos.getZ();

			List<EntityItem> found = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));

			// int moved = 0;
			for(EntityItem eitem : found){
				Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
				// moved++;
			}

			List<EntityXPOrb> foundExp = world.getEntitiesWithinAABB(EntityXPOrb.class, new AxisAlignedBB(x - ITEM_HRADIUS, y - ITEM_VRADIUS, z - ITEM_HRADIUS, x + ITEM_HRADIUS, y + ITEM_VRADIUS, z + ITEM_HRADIUS));

			for(EntityXPOrb eitem : foundExp){
				Vector3.setEntityMotionFromVector(eitem, x, y, z, ITEMSPEED);
				// moved++;
			}
		}
	}

	public static void tickSlowfall(EntityLivingBase entityLiving){

		if(entityLiving.isPotionActive(PotionRegistry.slowfall)){

			if(entityLiving instanceof EntityPlayer){
				EntityPlayer p = (EntityPlayer) entityLiving;
				if(p.isSneaking()){
					return;// so fall normally for now
				}
			}

			// else: so we are either a non-sneaking player, or a non player
			// entity

			// a normal fall seems to go up to 0, -1.2, -1.4, -1.6, then
			// flattens out at -0.078
			if(entityLiving.motionY < 0){
				entityLiving.motionY *= slowfallSpeed;

				entityLiving.fallDistance = 0f; // for no fall damage
			}
		}
	}

	public static void addOrMergePotionEffect(EntityLivingBase player, PotionEffect newp){

		// this could be in a utilPotion class i guess...
		if(player.isPotionActive(newp.getPotion())){
			// do not use built in 'combine' function, just add up duration
			PotionEffect p = player.getActivePotionEffect(newp.getPotion());

			int ampMax = Math.max(p.getAmplifier(), newp.getAmplifier());

			player.addPotionEffect(new PotionEffect(newp.getPotion(), newp.getDuration() + p.getDuration(), ampMax));
		}
		else{
			player.addPotionEffect(newp);
		}
	}

	public static void syncConfig(){

		String category = "";
		category = Const.MODID;

		PotionRegistry.slowfallSpeed = ModMain.config.getFloat("slowfall_speed", category, 0.41F, 0.1F, 1F, "This factor affects how much the slowfall effect slows down the entity.");

	}
}