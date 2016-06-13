package com.lothrazar.cyclicmagic.enchantment;

import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import com.lothrazar.cyclicmagic.util.UtilItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantHarvest extends Enchantment{

	private static final HarestCropsConfig conf1 = new HarestCropsConfig(); 
	private static final HarestCropsConfig conf2 = new HarestCropsConfig(); 
	private static final HarestCropsConfig conf3 = new HarestCropsConfig();
	public EnchantHarvest() {
		super(Rarity.COMMON, EnumEnchantmentType.ALL, new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setName("harvest");
        
        //level 1
		conf1.doesPumpkinBlocks = false;
		conf1.doesMelonBlocks = false;
		conf1.doesLeaves = false;
		conf1.doesCrops = true;
		conf1.doesFlowers = false;
		conf1.doesHarvestMushroom = false;
		conf1.doesHarvestSapling = false;
		conf1.doesHarvestTallgrass = false;
		
        //level 2
		conf2.doesPumpkinBlocks = false;
		conf2.doesMelonBlocks = false;
		conf2.doesLeaves = false;
		conf2.doesCrops = true;
		conf2.doesFlowers = false;
		conf2.doesHarvestMushroom = true;
		conf2.doesHarvestSapling = false;
		conf2.doesHarvestTallgrass = false;
		
        //level 3
		conf3.doesPumpkinBlocks = true;
		conf3.doesMelonBlocks = true;
		conf3.doesLeaves = false;
		conf3.doesCrops = true;
		conf3.doesFlowers = false;
		conf3.doesHarvestMushroom = false;
		conf3.doesHarvestSapling = false;
		conf3.doesHarvestTallgrass = false;
	}
	
	@Override
    public int getMaxLevel(){
        return 3;
    }
	 
	@Override
    public boolean canApply(ItemStack stack){
		return stack != null && stack.getItem() instanceof ItemHoe;    //return canApplyAtEnchantingTable(stack);
    }
	 
	@Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack); 	//this.type.canEnchantItem(stack.getItem());
    }

	@SubscribeEvent
	public void onPlayerFurnace(PlayerInteractEvent event) {
	
		EntityPlayer entityPlayer = event.getEntityPlayer();

		BlockPos pos = event.getPos();
		World world = event.getWorld();
		if (pos == null) { return; }
		ItemStack held = entityPlayer.getHeldItem(event.getHand());
		
		IBlockState clicked = world.getBlockState(pos);
		
		if(clicked != null && clicked.getBlock() != Blocks.DIRT 
				&& held != null && EnchantmentHelper.getEnchantments(held).containsKey(this)){
			
			int mainLevel = EnchantmentHelper.getEnchantments(held).get(this);
		 
			//level 1 or 2
			if(mainLevel > 0){
				int harvested = 0;
				
				if(mainLevel == 1){
					harvested = UtilHarvestCrops.harvestSingle(world,  pos, conf1) ? 1 : 0;
				}
				else if(mainLevel == 2){
					//radius means it goes both ways from senter
					harvested = UtilHarvestCrops.harvestArea(world,  pos, mainLevel ,conf2);
				}
				else if(mainLevel == 3){
					harvested = UtilHarvestCrops.harvestArea(world,  pos, mainLevel+1 ,conf3);
				}
				
				if(harvested > 0){
					UtilItem.damageItem(entityPlayer, held);
					//todo: harvest -> durability
					entityPlayer.swingArm(event.getHand());
				}
			}
		}
	}
}
