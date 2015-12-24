package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound; 
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemWaypointPortal extends Item
{ 
	private final static String KEY_LOC = "location"; 
	private final static String KEY_DIM = "dimension"; 
	private final static String KEY_BIOME = "biome"; 
  
	public ItemWaypointPortal( )
	{  
		super(); 
		this.setMaxStackSize(1);
	}

	public static void saveCurrentLocation(EntityPlayer player,ItemStack held) 
	{ 
		if (held.getTagCompound() == null) {held.setTagCompound(new NBTTagCompound());}
	 
		held.getTagCompound().setString(KEY_LOC , UtilNBT.posToStringCSV(player.getPosition().up()));	
		held.getTagCompound().setInteger(KEY_DIM , player.dimension);	
		held.getTagCompound().setString(KEY_BIOME , player.worldObj.getBiomeGenForCoords(player.getPosition()).biomeName);		
	} 

	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		tooltip.add(stack.getTagCompound().getString(KEY_BIOME ));
		tooltip.add(stack.getTagCompound().getString(KEY_LOC ));
    }

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
		
		BlockPos to = UtilNBT.stringCSVToBlockPos(itemStackIn.getTagCompound().getString(KEY_LOC ));
		
		if(itemStackIn.getTagCompound().getInteger(KEY_DIM ) != playerIn.dimension){
			
			//bad dimension
			
			UtilSound.playSoundAt(playerIn, UtilSound.fizz);
			playerIn.addChatMessage(new ChatComponentTranslation("teleport.dimension"));
			
	        return itemStackIn;
		}
		
		itemStackIn.stackSize = 0;
		//do the sounds/particles at both locations (before and after);
		UtilSound.playSoundAt(playerIn, UtilSound.portal);
		UtilParticle.spawnParticle(worldIn, EnumParticleTypes.PORTAL, playerIn);
		
		playerIn.setPositionAndUpdate(to.getX(), to.getY(), to.getZ());

		UtilSound.playSoundAt(playerIn, UtilSound.portal);
		UtilParticle.spawnParticle(worldIn, EnumParticleTypes.PORTAL, playerIn);
		
        return itemStackIn;
    }
}
 