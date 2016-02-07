package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.net.MessagePlaceBlock;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellFarReach extends BaseSpell {

	public SpellFarReach(int id, String n) {
		super(id, n);
		this.cost = 20;
	}
	
	int maxRange = 64;//TODO: config
	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side) {

        if(!p.capabilities.allowEdit) {
        	return false;
        }
	
		if(world.isRemote){
			//only client side can call this method. mouseover does not exist on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseover(maxRange);
			
			if(mouseover != null){

				int itemSlot = p.inventory.currentItem + 1;
				
				if(itemSlot < 9 && p.inventory.getStackInSlot(itemSlot) != null){
					
					//TODO: we could get block id and meta here, pass along with pos?
					ModMain.network.sendToServer(new MessagePlaceBlock(mouseover));
					
				}
			}
		}
		
		return false;
	}
	
	public void placeFromServerPacket(EntityPlayer p, BlockPos pos, IBlockState state, int itemSlot){
		//kind of workaround since server packet handles message, but we want actual processing done in the spell
		p.worldObj.setBlockState(pos, state);
		
		if(p.capabilities.isCreativeMode == false){
			p.inventory.decrStackSize(itemSlot, 1);
		}
		
		//this kind of should be in on success.........but it doesnt have the block/state access
		UtilSound.playSoundAt(p, state.getBlock().stepSound.getPlaceSound());
		
		//since it never really returns TRue up a bove, not to the server, we mimic what that would do. again, a hack since
		//this is hte only packet spell
		
		SpellRegistry.caster.castSuccess(this, p.worldObj, p, pos);
	}
}
