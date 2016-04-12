package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.ReflectionRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHorseFood extends Item {

	public static int			HEARTS_MAX;
	public static int			SPEED_MAX;
	public static int			JUMP_MAX;
	private static double	JUMP_SCALE	= 1.02;	// %age
	private static double	SPEED_SCALE	= 1.05;	// %age

	public ItemHorseFood() {

		super();
		// this.setMaxStackSize(64);
		// this.setCreativeTab(ItemRegistry.tabHorseFood);

	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, @SuppressWarnings("rawtypes") List tooltip, boolean advanced) {

		if (stack == null || stack.getItem() == null) { return; }// just being safe
		Item carrot = stack.getItem();

		tooltip.add(I18n.translateToLocal(carrot.getUnlocalizedName(stack) + ".effect"));
	}

	public static void addRecipes() {

		int dye_lapis = 4;

		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.emeraldCarrot), Items.carrot, Items.emerald);

		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.lapisCarrot), Items.carrot, new ItemStack(Items.dye, 1, dye_lapis));

		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.diamondCarrot), Items.carrot, Items.diamond);

		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.horse_upgrade_jump), Items.carrot, Items.ender_eye);

		GameRegistry.addShapelessRecipe(new ItemStack(ItemRegistry.horse_upgrade_speed), Items.carrot, Items.redstone);
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
		if (held.getItem() == ItemRegistry.emeraldCarrot) {
			switch (horse.getType()) {
			case HORSE:
				horse.setType(HorseArmorType.ZOMBIE);
				success = true;
			break;
			case ZOMBIE:
				horse.setType(HorseArmorType.SKELETON);
				success = true;
			break;
			case SKELETON:
				horse.setType(HorseArmorType.HORSE);
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
		else if (held.getItem() == ItemRegistry.lapisCarrot) {
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
		else if (held.getItem() == ItemRegistry.diamondCarrot) {
			float mh = (float) horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();

			if (mh < 2 * HEARTS_MAX) // 20 hearts == 40 health points
			{
				horse.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(mh + 2);

				success = true;
			}
		}
		else if (held.getItem() == ItemRegistry.horse_upgrade_jump) {
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
		else if (held.getItem() == ItemRegistry.horse_upgrade_speed) {
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

			UtilSound.playSound(player.worldObj, horse.getPosition(), SoundEvents.entity_horse_eat, SoundCategory.NEUTRAL);
		
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
}
