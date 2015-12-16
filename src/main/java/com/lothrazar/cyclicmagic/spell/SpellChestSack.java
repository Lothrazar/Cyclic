package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.util.UtilMoveBlock;
import com.lothrazar.cyclicmagic.ItemRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellChestSack extends BaseSpellExp {
	private final int cooldown = 5;// same cooldown for all potion spells
	@Override
	public int getCastCooldown() {
		return cooldown;
	}
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		BlockPos posChest = pos.offset(side);
		//imported from my old mod https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSackEmpty.java
	
		if(world.getTileEntity(posChest) == null || world.getTileEntity(posChest) instanceof IInventory == false){return;}
			
		TileEntity tile = world.getTileEntity(posChest);
		IInventory invo = (IInventory)tile;
		
		ItemStack chestItem;   
		int ROWS = 3;
		int COLS = 9;
		int START_CHEST = 0; 
		int END_CHEST =  START_CHEST + ROWS * COLS; 

		ItemStack drop = new ItemStack(ItemRegistry.chest_sack ,1,0); 
		
		if(drop.getTagCompound() == null)  drop.setTagCompound(new NBTTagCompound());
 
		int stacks = 0;
		int count = 0;
		
		int[] itemids = new int[END_CHEST - START_CHEST];
		int[] itemqty = new int[END_CHEST - START_CHEST];		
		int[] itemdmg = new int[END_CHEST - START_CHEST];
		
		//inventory and chest has 9 rows by 3 columns, never changes. same as 64 max stack size
		for(int islotChest = 0; islotChest < invo.getSizeInventory(); islotChest++)
		{
			//zeroes to avoid nulls, and signify nothing goes there
			itemids[islotChest] = 0;
			itemqty[islotChest] = 0;
			itemids[islotChest] = 0;
			chestItem = invo.getStackInSlot(islotChest);
		
			if(chestItem == null){continue;}//not an error; empty chest slot
			if(chestItem.getTagCompound() != null)
			{
				//probably has an enchantment
				player.dropPlayerItemWithRandomChoice(chestItem, false); 
			}
			else
			{
				stacks++; 
				count += chestItem.stackSize;
				
				itemids[islotChest] = Item.getIdFromItem(chestItem.getItem());
				itemdmg[islotChest] = chestItem.getItemDamage(); 
				itemqty[islotChest] = chestItem.stackSize;
				
			}
			//its either in the bag, or dropped on the player
			invo.setInventorySlotContents(islotChest, null);	
		}
		 
		if(drop.getTagCompound() == null) drop.setTagCompound(new NBTTagCompound());
		
		drop.getTagCompound().setIntArray("itemids", itemids);
		drop.getTagCompound().setIntArray("itemdmg", itemdmg);
		drop.getTagCompound().setIntArray("itemqty", itemqty);
		 
		drop.getTagCompound().setString("count",""+count);
		drop.getTagCompound().setString("stacks",""+stacks);
	 	 
		if(world.isRemote == false){
			player.entityDropItem(drop, 1); 
		}
		 //the 2 here is just a magic flag it passes to the world to propogate the event
	
		player.worldObj.setBlockToAir(posChest); 

		//playerIn.swingItem();
	
		//Util.spawnParticle(entityPlayer.worldObj, EnumParticleTypes.CRIT, pos);
		
		//Util.playSoundAt(entityPlayer, "random.wood_click");
		
 
		super.onCastSuccess(world, player, posChest);
    	//return super.onItemUse(stack, player, worldIn, pos, side, hitX, hitY, hitZ); 
	}
}
