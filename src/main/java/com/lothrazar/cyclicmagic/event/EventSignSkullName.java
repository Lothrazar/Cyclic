package com.lothrazar.cyclicmagic.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilNBT;

public class EventSignSkullName{
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event){

		EntityPlayer entityPlayer = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();

		if(pos == null){
			return;
		}
	
		// event has no hand??
		// and no item stack. and right click rarely works. known bug
		// http://www.minecraftforge.net/forum/index.php?topic=37416.0
		ItemStack held = entityPlayer.getHeldItemMainhand();
		
		if(held == null){
			held = entityPlayer.getHeldItemOffhand();
		}

		TileEntity container = worldObj.getTileEntity(pos);

		// event.getAction() == PlayerInteractEvent.Action.LEFT_CLICK_BLOCK &&
		// entityPlayer.isSneaking() &&
		if(held != null && held.getItem() == Items.skull && held.getItemDamage() == Const.skull_player && container != null && container instanceof TileEntitySign){
			TileEntitySign sign = (TileEntitySign) container;

			String firstLine = sign.signText[0].getUnformattedText();

			if(firstLine == null){
				firstLine = "";
			}
			if(firstLine.isEmpty() || firstLine.split(" ").length == 0){
				held.setTagCompound(null);
			}
			else{
				firstLine = firstLine.split(" ")[0];

				NBTTagCompound nbt = UtilNBT.getTagCompoundNotNull(held);
				
				nbt.setString(Const.SkullOwner, firstLine);
			}
		} // end of skullSignNames
	}
}
