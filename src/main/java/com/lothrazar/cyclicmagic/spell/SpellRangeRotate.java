package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.MessageSpellRotate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStone.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellRangeRotate extends BaseSpellRange {

	public SpellRangeRotate(int id, String name) {

		super.init(id, name);
		this.cost = 35;
		this.cooldown = 2;
	}

	@Override
	public boolean cast(World world, EntityPlayer p, ItemStack wand, BlockPos pos, EnumFacing side) {

		if (world.isRemote) {
			// only client side can call this method. mouseover does not exist
			// on server
			BlockPos mouseover = ModMain.proxy.getBlockMouseoverExact(maxRange);

			if (mouseover != null) {
				ModMain.network.sendToServer(new MessageSpellRotate(mouseover, ModMain.proxy.getSideMouseover(maxRange)));
			}
		}

		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void castFromServer(BlockPos pos, EnumFacing side, EntityPlayer p) {

		// TODO Auto-generated method stub
		if (pos == null || p.worldObj.getBlockState(pos) == null || side == null) { return; }

		World worldObj = p.worldObj;
		IBlockState clicked = worldObj.getBlockState(pos);
		if (clicked.getBlock() == null) { return; }
		Block clickedBlock = clicked.getBlock();
		
		//avoiding using the integer values of properties
		//int clickedMeta = clickedBlock.getMetaFromState(clicked);

		//the built in function doues the properties: ("facing")|| ("rotation")
		// for example, BlockMushroom.rotateBlock uses this, and hay bales
		boolean isDone = clickedBlock.rotateBlock(worldObj, pos, side);

		
		if(!isDone){

			//first handle any special cases
			
			if(clickedBlock == Blocks.stone){

				EnumType variant = clicked.getValue(BlockStone.VARIANT);//.getProperties().get(BlockStone.VARIANT);
				//basically we want to toggle the "smooth" property on and off
				//but there is no property 'smooth' its just within the variant
				switch(variant){
				case ANDESITE:
					worldObj.setBlockState(pos, clicked.withProperty(BlockStone.VARIANT, EnumType.ANDESITE_SMOOTH));
					isDone = true;
					break;
				case ANDESITE_SMOOTH:
					worldObj.setBlockState(pos, clicked.withProperty(BlockStone.VARIANT, EnumType.ANDESITE));
					isDone = true;
					break;
				case DIORITE:
					worldObj.setBlockState(pos, clicked.withProperty(BlockStone.VARIANT, EnumType.DIORITE_SMOOTH));
					isDone = true;
					break;
				case DIORITE_SMOOTH:
					worldObj.setBlockState(pos, clicked.withProperty(BlockStone.VARIANT, EnumType.DIORITE));
					isDone = true;
					break;
				case GRANITE:
					worldObj.setBlockState(pos, clicked.withProperty(BlockStone.VARIANT, EnumType.GRANITE_SMOOTH));
					isDone = true;
					break;
				case GRANITE_SMOOTH:
					worldObj.setBlockState(pos, clicked.withProperty(BlockStone.VARIANT, EnumType.GRANITE));
					isDone = true;
					break;
				case STONE:
				default:
					break;
				}
			}
			
			//now try something else if not done

			if(!isDone)for (IProperty prop : (com.google.common.collect.ImmutableSet<IProperty<?>>) clicked.getProperties().keySet()) {
				
				if (prop.getName().equals("half")) {
					//also exists as object in BlockSlab.HALF
					worldObj.setBlockState(pos, clicked.cycleProperty(prop));

					isDone = true;
					break;
				}
				else if (prop.getName().equals("seamless")) {
					//http://minecraft.gamepedia.com/Slab#Block_state
					worldObj.setBlockState(pos, clicked.cycleProperty(prop));

					isDone = true;
					break;
				}
				else if (prop.getName().equals("axis")) {
					//i dont remember what blocks use this. rotateBlock might cover it in some cases
					worldObj.setBlockState(pos, clicked.cycleProperty(prop));

					isDone = true;
					break;
				}
			}
		}

		if (isDone) {
			this.playSound(worldObj, clickedBlock, pos);
			this.spawnParticle(worldObj, p, pos);
		}
		return;
	}
}
