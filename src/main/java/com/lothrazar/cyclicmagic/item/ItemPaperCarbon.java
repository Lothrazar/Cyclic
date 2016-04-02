package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemPaperCarbon extends Item implements IHasRecipe{

	public static int NOTE_EMPTY = -1;
	private static final String KEY_SIGN0 = "sign_0";
	private static final String KEY_SIGN1 = "sign_1";
	private static final String KEY_SIGN2 = "sign_2";
	private static final String KEY_SIGN3 = "sign_3";
	private static final String KEY_NOTE = "note";

	public ItemPaperCarbon(){

		super();
	}

	private static void setItemStackNBT(ItemStack item, String prop, String value){

		item.getTagCompound().setString(prop, value);
	}

	private static String getItemStackNBT(ItemStack item, String prop){

		String s = item.getTagCompound().getString(prop);
		if(s == null){
			s = "";
		}
		return s;
	}

	private static void copySign(World world, EntityPlayer entityPlayer, TileEntitySign sign, ItemStack held){

		if(held.getTagCompound() == null){
			held.setTagCompound(new NBTTagCompound());
		}
		setItemStackNBT(held, KEY_SIGN0, sign.signText[0].getUnformattedText());
		setItemStackNBT(held, KEY_SIGN1, sign.signText[1].getUnformattedText());
		setItemStackNBT(held, KEY_SIGN2, sign.signText[2].getUnformattedText());
		setItemStackNBT(held, KEY_SIGN3, sign.signText[3].getUnformattedText());

		held.getTagCompound().setByte(KEY_NOTE, (byte) NOTE_EMPTY);

		// entityPlayer.swingItem();
	}

	private static void pasteSign(World world, EntityPlayer entityPlayer, TileEntitySign sign, ItemStack held){

		if(held.getTagCompound() == null){
			held.setTagCompound(new NBTTagCompound());
		}
		sign.signText[0] = new TextComponentTranslation(getItemStackNBT(held, KEY_SIGN0));
		sign.signText[1] = new TextComponentTranslation(getItemStackNBT(held, KEY_SIGN1));
		sign.signText[2] = new TextComponentTranslation(getItemStackNBT(held, KEY_SIGN2));
		sign.signText[3] = new TextComponentTranslation(getItemStackNBT(held, KEY_SIGN3));

		// world.markBlockForUpdate(sign.getPos());//so update is refreshed on client side

		// entityPlayer.swingItem();
	}

	private static void copyNote(World world, EntityPlayer entityPlayer, TileEntityNote noteblock, ItemStack held){

		if(held.getTagCompound() == null)
			held.setTagCompound(new NBTTagCompound());

		held.getTagCompound().setByte(KEY_NOTE, noteblock.note);
	}

	private static void pasteNote(World world, EntityPlayer entityPlayer, TileEntityNote noteblock, ItemStack held){

		if(held.getTagCompound() == null){
			return;
		}// nothing ot paste
		if(held.getTagCompound().getByte(KEY_NOTE) == NOTE_EMPTY){
			return;
		}

		noteblock.note = held.getTagCompound().getByte(KEY_NOTE);

		// world.markBlockForUpdate(noteblock.getPos());//so update is refreshed on client side

		// entityPlayer.swingItem();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack held, EntityPlayer player, List list, boolean par4){

		boolean isEmpty = (held.getTagCompound() == null);
		if(isEmpty){
			list.add("Click to copy a sign or noteblock");
			return;
		}

		String sign = getItemStackNBT(held, KEY_SIGN0) + getItemStackNBT(held, KEY_SIGN1) + getItemStackNBT(held, KEY_SIGN2) + getItemStackNBT(held, KEY_SIGN3);

		if(sign.length() > 0){
			list.add(getItemStackNBT(held, KEY_SIGN0));
			list.add(getItemStackNBT(held, KEY_SIGN1));
			list.add(getItemStackNBT(held, KEY_SIGN2));
			list.add(getItemStackNBT(held, KEY_SIGN3));
		}

		String s = noteToString(held.getTagCompound().getByte(KEY_NOTE));

		if(s != null){
			list.add("Note: " + s);
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack held, EntityPlayer entityPlayer, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){

		Block blockClicked = world.getBlockState(pos).getBlock();
		TileEntity container = world.getTileEntity(pos);
		boolean isValid = false;
		boolean wasCopy = false;

		boolean isEmpty = (held.getTagCompound() == null);

		if((blockClicked == Blocks.wall_sign || blockClicked == Blocks.standing_sign) && container instanceof TileEntitySign){
			TileEntitySign sign = (TileEntitySign) container;

			if(isEmpty){
				copySign(world, entityPlayer, sign, held);
				wasCopy = true;
			}
			else{
				pasteSign(world, entityPlayer, sign, held);
				wasCopy = false;
			}

			isValid = true;
		}
		if(blockClicked == Blocks.noteblock && container instanceof TileEntityNote){
			TileEntityNote noteblock = (TileEntityNote) container;

			if(isEmpty){
				copyNote(world, entityPlayer, noteblock, held);
				wasCopy = true;
			}
			else{
				pasteNote(world, entityPlayer, noteblock, held);
				wasCopy = false;
			}

			isValid = true;
		}

		if(isValid){

			UtilParticle.spawnParticle(world, EnumParticleTypes.PORTAL, pos.getX(), pos.getY(), pos.getZ());

			if(wasCopy == false)// on paste, we consume the item
			{
				if(entityPlayer.capabilities.isCreativeMode == false){
					entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
				}
			}

			UtilSound.playSound(entityPlayer, "random.fizz");
		}

		return EnumActionResult.PASS;
	}

	public static String noteToString(byte note){

		String s = null;

		switch(note){
		case 0:
			s = TextFormatting.YELLOW + "F#";
			break;// yellow
		case 1:
			s = TextFormatting.YELLOW + "G";
			break;
		case 2:
			s = TextFormatting.YELLOW + "G#";
			break;
		case 3:
			s = TextFormatting.YELLOW + "A";
			break;// or
		case 4:
			s = TextFormatting.YELLOW + "A#";
			break;// or
		case 5:
			s = TextFormatting.RED + "B";
			break;// red
		case 6:
			s = TextFormatting.RED + "C";
			break;// red
		case 7:
			s = TextFormatting.DARK_RED + "C#";
			break;
		case 8:
			s = TextFormatting.DARK_RED + "D";
			break;
		case 9:
			s = TextFormatting.LIGHT_PURPLE + "D#";
			break;// pink
		case 10:
			s = TextFormatting.LIGHT_PURPLE + "E";
			break;
		case 11:
			s = TextFormatting.DARK_PURPLE + "F";
			break;// purp
		case 12:
			s = TextFormatting.DARK_PURPLE + "F#";
			break;
		case 13:
			s = TextFormatting.DARK_PURPLE + "G";
			break;
		case 14:
			s = TextFormatting.DARK_BLUE + "G#";
			break;
		case 15:
			s = TextFormatting.DARK_BLUE + "A";
			break;// blue
		case 16:
			s = TextFormatting.BLUE + "A#";
			break;
		case 17:
			s = TextFormatting.BLUE + "B";
			break;
		case 18:
			s = TextFormatting.DARK_AQUA + "C";
			break;// lt blue?
		case 19:
			s = TextFormatting.AQUA + "C#";
			break;
		case 20:
			s = TextFormatting.AQUA + "D";
			break;// EnumChatFormatting.GREEN
		case 21:
			s = TextFormatting.GREEN + "D#";
			break;// there is no light green or dark green...
		case 22:
			s = TextFormatting.GREEN + "E";
			break;
		case 23:
			s = TextFormatting.AQUA + "F";
			break;
		case 24:
			s = TextFormatting.AQUA + "F#";
			break;// EnumChatFormatting.GREEN
		}

		return s;
	}

	@Override
	public void addRecipe(){
		
		GameRegistry.addRecipe(new ItemStack(this, 8),
				"ppp",
				"pcp",
				"ppp",
				'c',new ItemStack(Items.coal,1,1), //charcoal
				'p',Items.paper  );
	}
}