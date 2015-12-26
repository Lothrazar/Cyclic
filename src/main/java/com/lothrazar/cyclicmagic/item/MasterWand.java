package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MasterWand extends Item {

	public MasterWand() {
		this.setMaxStackSize(1);
	}

	@Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		
		PlayerPowerups props = PlayerPowerups.get(playerIn);
		ISpell spell = SpellRegistry.getSpellFromID(props.getSpellCurrent());

		tooltip.add(spell.getName());
		tooltip.add(StatCollector.translateToLocal("cost.cooldown") + spell.getCastCooldown());
		tooltip.add(StatCollector.translateToLocal("cost.exp") + spell.getCost());

		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{ 
		return EnumRarity.EPIC;  
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {

		//so this only happens IF either onItemUse did not fire at all, or it fired and casting failed
		SpellRegistry.caster.tryCastCurrent(worldIn, playerIn, null,null);
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}

 //TODO: do we even want a passive recharge at all?
	private double repairSpeed = -1;//higher is faster [0,1] // setting < zero will disable recharge fully
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// if held by something not a player? such as custom npc/zombie/etc
		if (entityIn instanceof EntityPlayer == false) {
			return;
		}

		// every second, make a roll. 1/10th of the time then do a repair
		if (worldIn.getWorldTime() % Const.TICKS_PER_SEC == 0 && worldIn.rand.nextDouble() < repairSpeed) {

			EntityPlayer p = (EntityPlayer) entityIn;
			PlayerPowerups props = PlayerPowerups.get(p);
			props.setMana(props.getMana() + 1);
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		//If onItemUse returns false onItemRightClick will be called.
		//http://www.minecraftforge.net/forum/index.php?topic=31966.0 
		//so if this casts and succeeds, the right click is cancelled
		return SpellRegistry.caster.tryCastCurrent(worldIn, playerIn, pos,side);
    }
}
