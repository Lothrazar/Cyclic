package com.lothrazar.cyclicmagic.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Imported from my
 * https://github.com/LothrazarMinecraftMods/CarbonPaper/blob/master
 * /src/main/java/com/lothrazar/samscarbonpaper/ItemPaperCarbon.java
 * 
 *
 */
public class ItemPaperCarbon extends Item {
	public static int NOTE_EMPTY = -1;
	private static final String KEY_SIGN0 = "sign_0";
	private static final String KEY_SIGN1 = "sign_1";
	private static final String KEY_SIGN2 = "sign_2";
	private static final String KEY_SIGN3 = "sign_3";
	private static final String KEY_NOTE = "note";

	public ItemPaperCarbon() {
		super();
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	public void addRecipe() {
		GameRegistry.addRecipe(new ItemStack(this, 8), "ppp", "pcp", "ppp", 'c', new ItemStack(Items.coal, 1, 1), // charcoal
				'p', Items.paper);
	}

	public static void setItemStackNBT(ItemStack item, String prop, String value) {
		item.getTagCompound().setString(prop, value);
	}

	public static String getItemStackNBT(ItemStack item, String prop) {
		String s = item.getTagCompound().getString(prop);
		if (s == null) {
			s = "";
		}
		return s;
	}

	public static void copySign(World world, EntityPlayer entityPlayer, TileEntitySign sign, ItemStack held) {
		if (held.getTagCompound() == null) {
			held.setTagCompound(new NBTTagCompound());
		}
		setItemStackNBT(held, KEY_SIGN0, sign.signText[0].getUnformattedText());
		setItemStackNBT(held, KEY_SIGN1, sign.signText[1].getUnformattedText());
		setItemStackNBT(held, KEY_SIGN2, sign.signText[2].getUnformattedText());
		setItemStackNBT(held, KEY_SIGN3, sign.signText[3].getUnformattedText());

		held.getTagCompound().setByte(KEY_NOTE, (byte) NOTE_EMPTY);

		entityPlayer.swingItem();
	}

	public static void pasteSign(World world, EntityPlayer entityPlayer, TileEntitySign sign, ItemStack held) {
		if (held.getTagCompound() == null) {
			held.setTagCompound(new NBTTagCompound());
		}
		sign.signText[0] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN0));
		sign.signText[1] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN1));
		sign.signText[2] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN2));
		sign.signText[3] = new ChatComponentText(getItemStackNBT(held, KEY_SIGN3));

		world.markBlockForUpdate(sign.getPos());// so update is refreshed on
												// client side

		entityPlayer.swingItem();
	}

	public static void copyNote(World world, EntityPlayer entityPlayer, TileEntityNote noteblock, ItemStack held) {
		if (held.getTagCompound() == null)
			held.setTagCompound(new NBTTagCompound());

		held.getTagCompound().setByte(KEY_NOTE, noteblock.note);
	}

	public static void pasteNote(World world, EntityPlayer entityPlayer, TileEntityNote noteblock, ItemStack held) {
		if (held.getTagCompound() == null) {
			return;
		}// nothing ot paste
		if (held.getTagCompound().getByte(KEY_NOTE) == NOTE_EMPTY) {
			return;
		}

		noteblock.note = held.getTagCompound().getByte(KEY_NOTE);

		world.markBlockForUpdate(noteblock.getPos());// so update is refreshed
														// on client side

		entityPlayer.swingItem();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack held, EntityPlayer player, List list, boolean par4) {
		boolean isEmpty = (held.getTagCompound() == null);
		if (isEmpty) {
			list.add("Click to copy a sign or noteblock");
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
			list.add("Note: " + s);
		}
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack held = playerIn.getCurrentEquippedItem();
		Block blockClicked = world.getBlockState(pos).getBlock();
		TileEntity container = world.getTileEntity(pos);

		boolean isValid = false;
		boolean wasCopy = false;

		boolean isEmpty = (held.getTagCompound() == null);

		if ((blockClicked == Blocks.wall_sign || blockClicked == Blocks.standing_sign) && container instanceof TileEntitySign) {
			TileEntitySign sign = (TileEntitySign) container;

			if (isEmpty) {
				ItemPaperCarbon.copySign(world, playerIn, sign, held);
				wasCopy = true;
			} else {
				ItemPaperCarbon.pasteSign(world, playerIn, sign, held);
				wasCopy = false;
			}

			isValid = true;
		}
		if (blockClicked == Blocks.noteblock && container instanceof TileEntityNote) {
			TileEntityNote noteblock = (TileEntityNote) container;

			if (isEmpty) {
				ItemPaperCarbon.copyNote(world, playerIn, noteblock, held);
				wasCopy = true;
			} else {
				ItemPaperCarbon.pasteNote(world, playerIn, noteblock, held);
				wasCopy = false;
			}

			isValid = true;
		}

		if (isValid) {
			if (world.isRemote) {
				spawnParticle(world, EnumParticleTypes.PORTAL, pos.getX(), pos.getY(), pos.getZ());
			} else {
				if (wasCopy == false)// on paste, we consume the item
				{
					if (playerIn.capabilities.isCreativeMode == false) {
						// on success
						// playerIn.inventory.decrStackSize(playerIn.inventory.currentItem,
						// 1);
					}
				}
			}

			playSoundAt(playerIn, "random.fizz");
		}

		return super.onItemUse(stack, playerIn, world, pos, side, hitX, hitY, hitZ);
	}

	public static void spawnParticle(World world, EnumParticleTypes type, double x, double y, double z) {
		// http://www.minecraftforge.net/forum/index.php?topic=9744.0
		for (int countparticles = 0; countparticles <= 10; ++countparticles) {
			world.spawnParticle(type, x + (world.rand.nextDouble() - 0.5D) * (double) 0.8, y + world.rand.nextDouble() * (double) 1.5 - (double) 0.1, z + (world.rand.nextDouble() - 0.5D) * (double) 0.8, 0.0D, 0.0D, 0.0D);
		}
	}

	public static void playSoundAt(Entity player, String sound) {
		player.worldObj.playSoundAtEntity(player, sound, 1.0F, 1.0F);
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
