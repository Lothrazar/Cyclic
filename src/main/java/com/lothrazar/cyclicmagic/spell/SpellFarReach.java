package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessagePlaceBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellFarReach extends BaseSpell {

	public SpellFarReach(int id, String n) {
		super(id, n);
		this.cooldown = 3;
		this.cost = 20;
	}
	
	int maxRange = 64;//TODO: config
	@Override
	public boolean cast(World world, EntityPlayer p, BlockPos pos, EnumFacing side) {

        if(!p.capabilities.allowEdit) {
        	return false;
        }
	
		if(world.isRemote){
			
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
}
