package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Imported from my
 * https://github.com/LothrazarMinecraftMods/CarbonPaper/blob/master
 * /src/main/java/com/lothrazar/samscarbonpaper/ItemPaperCarbon.java
 *
 */
public class ItemPaperCarbon extends Item {
	private static final int NOTE_EMPTY = -1;
	private static final String KEY_SIGN0 = "sign_0";
	private static final String KEY_SIGN1 = "sign_1";
	private static final String KEY_SIGN2 = "sign_2";
	private static final String KEY_SIGN3 = "sign_3";
	private static final String KEY_NOTE = "note";
	
	public ItemPaperCarbon(){
		super();
		this.setMaxDamage(5);
		this.setMaxStackSize(1);
	}

	public static void setItemStackNBT(ItemStack item, String prop, String value) {
		if (item.getTagCompound() == null) {
			item.setTagCompound(new NBTTagCompound());
		}
		item.getTagCompound().setString(prop, value);
	}

	public static String getItemStackNBT(ItemStack item, String prop) {
		if (item.getTagCompound() == null) {
			item.setTagCompound(new NBTTagCompound());
		}
		String s = item.getTagCompound().getString(prop);
		if (s == null) {
			s = "";
		}
		return s;
	}

	public static void copySignAndSpawnItem(World world,TileEntitySign sign,BlockPos pos) {

		ItemStack drop = new ItemStack(ItemRegistry.carbon_paper);
		setItemStackNBT(drop, KEY_SIGN0, sign.signText[0].getUnformattedText());
		setItemStackNBT(drop, KEY_SIGN1, sign.signText[1].getUnformattedText());
		setItemStackNBT(drop, KEY_SIGN2, sign.signText[2].getUnformattedText());
		setItemStackNBT(drop, KEY_SIGN3, sign.signText[3].getUnformattedText());

		drop.getTagCompound().setByte(KEY_NOTE, (byte) NOTE_EMPTY);
		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),drop));
		}
	}

	public static void copyNoteAndSpawnItem(World world, TileEntityNote noteblock,BlockPos pos) {
		ItemStack drop = new ItemStack(ItemRegistry.carbon_paper);
		if (drop.getTagCompound() == null) {
			drop.setTagCompound(new NBTTagCompound());
		}

		drop.getTagCompound().setByte(KEY_NOTE, noteblock.note);
		if(world.isRemote == false){
			world.spawnEntityInWorld(new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),drop));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack held, EntityPlayer player, List list, boolean par4) {
		boolean isEmpty = (held.getTagCompound() == null);
		if (isEmpty) {
			return;
		}

		String sign = getItemStackNBT(held, KEY_SIGN0) + getItemStackNBT(held, KEY_SIGN1) + getItemStackNBT(held, KEY_SIGN2) + getItemStackNBT(held, KEY_SIGN3);

		if (sign.length() > 0) {
			list.add(getItemStackNBT(held, KEY_SIGN0));
			list.add(getItemStackNBT(held, KEY_SIGN1));
			list.add(getItemStackNBT(held, KEY_SIGN2));
			list.add(getItemStackNBT(held, KEY_SIGN3));
		}

		String s = noteToString(held.getTagCompound().getByte(KEY_NOTE));

		if (s != null) {
			list.add(s);
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack held = playerIn.getCurrentEquippedItem();
		Block blockClicked = world.getBlockState(pos).getBlock();
		TileEntity container = world.getTileEntity(pos);

		boolean success = false;
		
		if ((blockClicked == Blocks.wall_sign || blockClicked == Blocks.standing_sign) && container instanceof TileEntitySign  && 
				held.getTagCompound() != null) {
			TileEntitySign sign = (TileEntitySign) container;

			//paste the sign
	 
			sign.signText[0] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN0));
			sign.signText[1] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN1));
			sign.signText[2] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN2));
			sign.signText[3] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN3));

			success = true;
		}
		else if (blockClicked == Blocks.noteblock && container instanceof TileEntityNote && 
				held.getTagCompound() != null) {
			TileEntityNote noteblock = (TileEntityNote) container;

			//paste the note
			if (held.getTagCompound().getByte(KEY_NOTE) != NOTE_EMPTY) {
			 
				noteblock.note = held.getTagCompound().getByte(KEY_NOTE);

				success = true;
			}
		}
		
		if(success){
			world.markBlockForUpdate(pos);

			held.damageItem(1, playerIn);
		}

		return super.onItemUse(stack, playerIn, world, pos, side, hitX, hitY, hitZ);
	}

	public static String noteToString(byte note) {
		String s = null;

		switch (note) {
		case 0:
			s = EnumChatFormatting.YELLOW + "F#";
			break;// yellow
		case 1:
			s = EnumChatFormatting.YELLOW + "G";
			break;
		case 2:
			s = EnumChatFormatting.YELLOW + "G#";
			break;
		case 3:
			s = EnumChatFormatting.YELLOW + "A";
			break;// or
		case 4:
			s = EnumChatFormatting.YELLOW + "A#";
			break;// or
		case 5:
			s = EnumChatFormatting.RED + "B";
			break;// red
		case 6:
			s = EnumChatFormatting.RED + "C";
			break;// red
		case 7:
			s = EnumChatFormatting.DARK_RED + "C#";
			break;
		case 8:
			s = EnumChatFormatting.DARK_RED + "D";
			break;
		case 9:
			s = EnumChatFormatting.LIGHT_PURPLE + "D#";
			break;// pink
		case 10:
			s = EnumChatFormatting.LIGHT_PURPLE + "E";
			break;
		case 11:
			s = EnumChatFormatting.DARK_PURPLE + "F";
			break;// purp
		case 12:
			s = EnumChatFormatting.DARK_PURPLE + "F#";
			break;
		case 13:
			s = EnumChatFormatting.DARK_PURPLE + "G";
			break;
		case 14:
			s = EnumChatFormatting.DARK_BLUE + "G#";
			break;
		case 15:
			s = EnumChatFormatting.DARK_BLUE + "A";
			break;// blue
		case 16:
			s = EnumChatFormatting.BLUE + "A#";
			break;
		case 17:
			s = EnumChatFormatting.BLUE + "B";
			break;
		case 18:
			s = EnumChatFormatting.DARK_AQUA + "C";
			break;// lt blue?
		case 19:
			s = EnumChatFormatting.AQUA + "C#";
			break;
		case 20:
			s = EnumChatFormatting.AQUA + "D";
			break;// EnumChatFormatting.GREEN
		case 21:
			s = EnumChatFormatting.GREEN + "D#";
			break;// there is no light green or dark green...
		case 22:
			s = EnumChatFormatting.GREEN + "E";
			break;
		case 23:
			s = EnumChatFormatting.AQUA + "F";
			break;
		case 24:
			s = EnumChatFormatting.AQUA + "F#";
			break;// EnumChatFormatting.GREEN
		}

		return s;
	}
}
