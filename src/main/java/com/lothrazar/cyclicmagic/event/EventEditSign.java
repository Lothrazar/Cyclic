package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;


public class EventEditSign{

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event){

		EntityPlayer entityPlayer = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();
		if(pos == null){
			return;
		}
		
		ItemStack held = entityPlayer.getHeldItemMainhand();

		if(held == null){
			held = entityPlayer.getHeldItemOffhand();
		}

		TileEntity tile = worldObj.getTileEntity(pos);
		
		//test
		if(held == null && tile instanceof TileEntitySign){

			TileEntitySign sign = (TileEntitySign)tile;
			//sign.setEditable(true);
			ReflectionHelper.setPrivateValue(TileEntitySign.class, sign, true, "isEditable", "field_145916_j");
			sign.setPlayer(entityPlayer);
			
			entityPlayer.openEditSign(  sign);
		}
	}
}
