package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList; 
import java.util.List;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound; 
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

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
		
		itemStackIn.stackSize = 0;
		
		playerIn.setPositionAndUpdate(to.getX(), to.getY(), to.getZ());
		
        return itemStackIn;
    }
}
 