package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventEditSign  implements IHasConfig{

	private boolean editableSigns;
	
	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.LeftClickBlock event) {
		if(!editableSigns){return;}

		EntityPlayer entityPlayer = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();
		if (pos == null) { return; }
	
		ItemStack held = event.getItemStack();

		TileEntity tile = worldObj.getTileEntity(pos);

		// test
		if (held == null && tile instanceof TileEntitySign) {

			TileEntitySign sign = (TileEntitySign) tile;
			sign.setEditable(true);
			//dont need reflection anymore... it used to be a private value but now we can use the set command
//			try{
//				ReflectionHelper.setPrivateValue(TileEntitySign.class, sign, true, "isEditable", "field_145916_j");
//			}
//			catch(Exception e){
//				
//			}
			sign.setPlayer(entityPlayer);

			entityPlayer.openEditSign(sign);
		}
	}

	@Override
	public void syncConfig(Configuration config) {
		String category = Const.ConfigCategory.player;

		//TODO: put all custom categories in some central place?
		config.addCustomCategoryComment(category, "Player Abilities and interactions");
		
		editableSigns = config.getBoolean("Editable Signs", category, true, "Allow editing signs with an empty hand by punching it (left click)");
		
	}
}
