package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.BlockRegistry;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class SpellScaffolding extends BaseSpell {
	public SpellScaffolding(int id,String name){
		super(id,name);
	}
 
	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side ) {

		BlockPos offset = (side == null)? pos : pos.offset(side);
		if(offset == null){
			//then start with player pos
			offset = player.getPosition().up();//up one for eye level
			
			int direction = MathHelper.floor_double((double)((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
	
			//-45 is  up
			//+45 is pitch down
			//first; is it up or down?

			boolean doHoriz = true;

			if(player.rotationPitch < -82){
				//really really up
				doHoriz = false;
				offset = offset.up().up();
			}
			else if(player.rotationPitch > 82){
				//really really down
				doHoriz = false;
				offset = offset.down();
			}
			else if(player.rotationPitch < -45){
				//angle is pretty high up. so offset up again
				offset = offset.up();
				doHoriz = true;
			}
			else if(player.rotationPitch > 45){
				//you are angled down, so bring down from eye level
				offset = offset.down();
				doHoriz = true;
			}
			//else doHoriz = true; stays
			
			//if not, go by dir
			if(doHoriz){
					
				switch(direction){
					case Const.DIR_EAST:
						offset = offset.east();
						break;
					case Const.DIR_WEST:
						offset = offset.west();//.offset(EnumFacing.WEST);
						break;
					case Const.DIR_SOUTH:
						offset = offset.south();
						break;
					case Const.DIR_NORTH:
						offset = offset.north();
						break;
				}
			}
			//1 : negative x : west
			//0 : south : +z
			//3 east +x
			//2 north -z
			
		}
		
		if(world.isAirBlock(offset)){
			world.setBlockState(offset, BlockRegistry.block_fragile.getDefaultState());
			
			UtilSound.playSoundAt(player, UtilSound.click);
			
			return true;
		}
		
		return false;
	}
}
