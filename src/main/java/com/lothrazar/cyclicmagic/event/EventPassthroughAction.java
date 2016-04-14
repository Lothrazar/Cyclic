package com.lothrazar.cyclicmagic.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventPassthroughAction {
	
	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteract event) {

		EntityPlayer entityPlayer = event.getEntityPlayer();
		ItemStack held = entityPlayer.getHeldItem(event.getHand());
		Entity target = event.getTarget();
		World worldObj = event.getWorld();

		
		if(target instanceof EntityItemFrame){
			 System.out.println("EntityItemFrame");
			BlockPos pos = target.getPosition();
			//TileEntity tile = worldObj.getTileEntity(target.getPosition());
			// IBlockState state = worldObj.getBlockState(pos);
			
		//	if(tile != null){
				
				
			 EntityItemFrame frame = (EntityItemFrame)target;
			 
			
				//EnumFacing.getFront(state.getBlock().getMetaFromState(state)
			 EnumFacing face = frame.getAdjustedHorizontalFacing();
				 BlockPos posBehind = pos.offset(  face.getOpposite() ).down();//; // 

				 System.out.println("posBehind = "+posBehind);
				 
				 IBlockState stuffBehind = worldObj.getBlockState(posBehind);
				 
				 
				 if(stuffBehind != null && stuffBehind.getBlock() != null){//  && worldObj.getTileEntity(posBehind) != null
					 
					 System.out.println("stuff is not null"+stuffBehind.getBlock().getUnlocalizedName());
					 
					 //then perform the action on that thing (chest/furnace/etc)      
					 
					 //a function in base class of block
					 // public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
					 // event.getFace()
					 stuffBehind.getBlock().onBlockActivated(worldObj, posBehind, stuffBehind, entityPlayer, event.getHand(), held, face , 0, 0, 0);

					 //stop the normal item thing happening
	         //event.setCanceled(true);
	         //.setUseItem(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
	     
				 }
			//}
		}
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.RightClickBlock event) {

		EntityPlayer entityPlayer = event.getEntityPlayer();
		BlockPos pos = event.getPos();
		World worldObj = event.getWorld();
		if (pos == null) { return; }
		//if (entityPlayer.isSneaking()) { return; }
		ItemStack held = event.getItemStack();//entityPlayer.getHeldItem(event.getHand());
		
		
		 IBlockState state = event.getWorld().getBlockState(pos);
		 if(state != null && entityPlayer.isSneaking() == false && 
				 (state.getBlock() == Blocks.wall_sign 
					 || state.getBlock() == Blocks.wall_banner
				   || Item.getItemFromBlock(state.getBlock()) == Items.item_frame )){
			 //but NOT standing sign or standing banner
			 //?? paintings maybe?
			 
			 BlockPos posBehind = pos.offset(EnumFacing.getFront(state.getBlock().getMetaFromState(state)).getOpposite());
			 
			 IBlockState stuffBehind = worldObj.getBlockState(posBehind);
			 
			 if(stuffBehind != null && stuffBehind.getBlock() != null && worldObj.getTileEntity(posBehind) != null){
				 //then perform the action on that thing (chest/furnace/etc)      
				 
				 //a function in base class of block
				 // public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
				 stuffBehind.getBlock().onBlockActivated(worldObj, posBehind, stuffBehind, entityPlayer, event.getHand(), held, event.getFace(), 0, 0, 0);

				 //stop the normal item thing happening
         event.setUseItem(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
     
			 }
		 }
		//move above event to own class if it works
		
	}
}
