package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellChestTransport extends BaseSpellExp implements ISpell
{ 
	@Override
	public String getSpellName()
	{
		return "chest";
	}
	@Override
	public int getSpellID() 
	{
		return 2;
	}
	
	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos)
	{
		if(super.canPlayerCast(world, player, pos) == false) {return false;}
	
		TileEntity tile = world.getTileEntity(pos);
		return (tile != null && tile instanceof TileEntityChest);
	}
	
	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos)
	{
		TileEntityChest chestTarget = (TileEntityChest)world.getTileEntity(pos);
	
		if(chestTarget == null)//should never happen, assuming the canPlayerCast was run
		{
			onCastFailure(world,player,pos);
			return;
		}//wrong type of tile entity
		
		//TODO:  make it also work with trapped chests/dispensers/droppers/ etc. set extra flag to identify
		//means not just TileEntityChest
		//chestTarget.getInventoryStackLimit()//=64
		//chestContents.size internally is 27
		ItemStack chestItem;  
		//int chestMax;
		 
		int ROWS = 3;
		int COLS = 9;
		int START_CHEST = 0;
		//int START_INV = 9;//because we are ignoring the item hotbar, we skip the first row this way
		int END_CHEST =  START_CHEST + ROWS * COLS;
		//int END_INV = START_INV + ROWS * COLS;

		ItemStack drop = new ItemStack(ItemRegistry.itemChestSack ,1,0); 
		
		if(drop.getTagCompound() == null)  drop.setTagCompound(new NBTTagCompound());
 
		int stacks = 0;
		int count = 0;
		
		int[] itemids = new int[END_CHEST - START_CHEST];
		int[] itemqty = new int[END_CHEST - START_CHEST];		
		int[] itemdmg = new int[END_CHEST - START_CHEST];
		
		//inventory and chest has 9 rows by 3 columns, never changes. same as 64 max stack size
		for(int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++)
		{
			//zeroes to avoid nulls, and signify nothing goes there
			itemids[islotChest] = 0;
			itemqty[islotChest] = 0;
			itemids[islotChest] = 0;
			chestItem = chestTarget.getStackInSlot(islotChest);
		
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
			chestTarget.setInventorySlotContents(islotChest, null);	
		}
		 
		if(drop.getTagCompound() == null) drop.setTagCompound(new NBTTagCompound());
		
		drop.getTagCompound().setIntArray("itemids", itemids);
		drop.getTagCompound().setIntArray("itemdmg", itemdmg);
		drop.getTagCompound().setIntArray("itemqty", itemqty);
		 
		drop.getTagCompound().setString("count",""+count);
		drop.getTagCompound().setString("stacks",""+stacks);
	 	 
		player.entityDropItem(drop, 1); 
			 
		 //the 2 here is just a magic flag it passes to the world to propogate the event
	
		world.setBlockToAir(pos); 
 
	}

	@Override
	public ItemStack getIconDisplay()
	{
		return new ItemStack(ItemRegistry.itemChestSack);
	}

	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		UtilSound.playSoundAt(player, "random.chestclosed");

		super.onCastSuccess(world, player, pos);
	}

	@Override
	public int getExpCost()
	{
		return ModMain.cfg.chesttransp;
	}
}
