package com.lothrazar.cyclicmagic.item;

import java.util.List;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse; 
import net.minecraft.entity.passive.HorseType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFoodHorse extends BaseItem implements IHasRecipe {

	public static int			HEARTS_MAX;
	public static int			SPEED_MAX;
	public static int			JUMP_MAX;
	private static double	JUMP_SCALE	= 1.02;	// %age
	private static double	SPEED_SCALE	= 1.05;	// %age

//	public static boolean						horseFoodEnabled;
	private ItemStack recipeItem;
	
	public ItemFoodHorse(ItemStack rec) {

		super();
		recipeItem = rec;
		// this.setMaxStackSize(64);
		// this.setCreativeTab(ItemRegistry.tabHorseFood);

	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, @SuppressWarnings("rawtypes") List tooltip, boolean advanced) {

		if (stack == null || stack.getItem() == null) { return; }// just being safe
		Item carrot = stack.getItem();

		tooltip.add(I18n.format(carrot.getUnlocalizedName(stack) + ".effect"));
	}

	public void addRecipe() {
 
		GameRegistry.addShapelessRecipe(new ItemStack(this)
				, Items.CARROT, recipeItem);
 
	}

	public static void onHorseInteract(EntityHorse horse, EntityPlayer player, ItemStack held) {

		boolean success = false;
		/*
		 * String ownerID = "..untamed.."; if(horse.isTame() &&
		 * horse.getEntityData().hasKey("OwnerUUID")) { ownerID =
		 * horse.getEntityData().getString("OwnerUUID"); }
		 * 
		 * 
		 * //or let it through if no owner exists ("owner = "+ownerID);
		 * ("player = "+player.getUniqueID().toString());
		 */
		
		//TODO: USE AN EnumType flag passed by constructor, if u dont want to do new classes
		if (held.getItem() == ItemRegistry.itemMap.get("emeraldCarrot")) {
			switch (horse.getType()) {
			case HORSE: 
				
				
				horse.setType(HorseType.ZOMBIE);
				success = true;
			break;
			case ZOMBIE:
				horse.setType(HorseType.SKELETON);
				success = true;
			break;
			case SKELETON:
				horse.setType(HorseType.HORSE);
				success = true;
			break;
			// donkey and mule ignored by design
			case DONKEY:
			break;
			case MULE:
			break;
			default:
			break;
			}
		}
		else if (held.getItem() == ItemRegistry.itemMap.get("lapisCarrot")) {
			int var = horse.getHorseVariant();
			int var_reduced = 0;
			int var_new = 0;
			while (var - 256 > 0) {
				var_reduced += 256;// this could be done with modulo % arithmetic too,
				                   // but meh
				// doesnt matter either way
				var -= 256;
			} // invalid numbers make horse invisible, but this is somehow safe. and
			  // easier than
			  // doing bitwise ops
			switch (var) {
			case Horse.variant_black:
				var_new = Horse.variant_brown;
			break;
			case Horse.variant_brown:
				var_new = Horse.variant_brown_dark;
			break;
			case Horse.variant_brown_dark:
				var_new = Horse.variant_chestnut;
			break;
			case Horse.variant_chestnut:
				var_new = Horse.variant_creamy;
			break;
			case Horse.variant_creamy:
				var_new = Horse.variant_gray;
			break;
			case Horse.variant_gray:
				var_new = Horse.variant_white;
			break;
			case Horse.variant_white:
				var_new = Horse.variant_black;
			break;
			}
			var_new += var_reduced;

			horse.setHorseVariant(var_new);

			success = true;
		}
		else if (held.getItem() == ItemRegistry.itemMap.get("diamondCarrot")) {
			float mh = (float) horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();

			if (mh < 2 * HEARTS_MAX) // 20 hearts == 40 health points
			{
				horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(mh + 2);

				success = true;
			}
		}
		else if (held.getItem() == ItemRegistry.itemMap.get("horse_upgrade_jump")) {
			if (ReflectionRegistry.horseJumpStrength != null)// only happpens if mod
			                                                 // installing preInit
			// method fails to find it
			{
				double jump = horse.getEntityAttribute(ReflectionRegistry.horseJumpStrength).getAttributeValue();// horse.getHorseJumpStrength()
				// ;

				double newjump = jump * JUMP_SCALE;

				// double jumpHeight = getJumpTranslated(horse.getHorseJumpStrength());
				if (UtilEntity.getJumpTranslated(newjump) < JUMP_MAX) {
					horse.getEntityAttribute(ReflectionRegistry.horseJumpStrength).setBaseValue(newjump);
					// System.out.println("newjump = "+newjump);
					success = true;
				}
			}
		}
		else if (held.getItem() == ItemRegistry.itemMap.get("horse_upgrade_speed")) {
			double speed = horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();

			double newSpeed = speed * SPEED_SCALE;
			// add ten percent
			if (UtilEntity.getSpeedTranslated(newSpeed) < SPEED_MAX) {
				horse.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(newSpeed);
				// System.out.println("speed = "+newSpeed);
				success = true;
			}
		}

		if (success) {
			if (player.capabilities.isCreativeMode == false) {
				player.inventory.decrStackSize(player.inventory.currentItem, 1);
			}

			double x = horse.getPosition().getX();
			double y = horse.getPosition().getY();
			double z = horse.getPosition().getZ();
			for (int countparticles = 0; countparticles <= 10; ++countparticles) {
				horse.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x + (horse.worldObj.rand.nextDouble() - 0.5D) * (double) 0.8, y + horse.worldObj.rand.nextDouble() * (double) 1.5 - (double) 0.1, z + (horse.worldObj.rand.nextDouble() - 0.5D) * (double) 0.8, 0.0D, 0.0D, 0.0D);
			}

			UtilSound.playSound(player, horse.getPosition(), SoundEvents.ENTITY_HORSE_EAT, SoundCategory.NEUTRAL);
		
			horse.setEatingHaystack(true); // makes horse animate and bend down to eat
		}
	}

	public static class Horse {

		public static final int	variant_white				= 0;
		public static final int	variant_creamy			= 1;
		public static final int	variant_chestnut		= 2;
		public static final int	variant_brown				= 3;
		public static final int	variant_black				= 4;
		public static final int	variant_gray				= 5;
		public static final int	variant_brown_dark	= 6;

		public static final int	type_standard				= 0;
		public static final int	type_donkey					= 1;
		public static final int	type_mule						= 2;
		public static final int	type_zombie					= 3;
		public static final int	type_skeleton				= 4;
	}

	public static void syncConfig(Configuration config) {

		String category = Const.ConfigCategory.itemsHorsefood; 

//		ItemFoodHorse.horseFoodEnabled = config.getBoolean("Enabled", category, true, "To disable all horse upgrade food");
		ItemFoodHorse.HEARTS_MAX = config.getInt("Max Hearts", category, 20, 1, 100, "Maximum number of upgraded hearts");
		ItemFoodHorse.JUMP_MAX = config.getInt("Max Jump", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
		ItemFoodHorse.SPEED_MAX = config.getInt("Max Speed", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");

	}
}
