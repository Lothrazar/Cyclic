package com.lothrazar.cyclicmagic.item;

import java.util.List;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.gui.GuiSpellbook;
import com.lothrazar.cyclicmagic.spell.ISpell;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MasterWand extends Item {

	private static final int MAXCHARGE = 5000;//10k

	public MasterWand() {
		this.setMaxStackSize(1);
		this.setMaxDamage(MAXCHARGE);
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		int charge = stack.getMaxDamage() - stack.getItemDamage();//invese of damge
		tooltip.add(charge + "/" + stack.getMaxDamage());
		PlayerPowerups props = PlayerPowerups.get(playerIn);
		ISpell spell = SpellRegistry.getSpellFromID(props.getSpellCurrent());

		tooltip.add(spell.getName());
		tooltip.add(StatCollector.translateToLocal("cost.cooldown") + spell.getCastCooldown());
		tooltip.add(StatCollector.translateToLocal("cost.durability") + spell.getCostDurability());
		tooltip.add(StatCollector.translateToLocal("cost.exp") + spell.getCostExp());
		
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	  /**
     * Called when a entity tries to play the 'swing' animation.
     *
     * @param entityLiving The entity swinging the item.
     * @param stack The Item stack
     * @return True to cancel any further processing by EntityLiving
     */
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack)
    {
    	if(entity instanceof EntityPlayer && entity.worldObj.isRemote){
    		//client side player swing
    		Minecraft.getMinecraft().displayGuiScreen(new GuiSpellbook( (EntityPlayer)entity));
    		return true;
    	}
        return false;
    }
    
    @Override
    public boolean doesSneakBypassUse(World world, BlockPos pos, EntityPlayer player)
    {
        return true;//default false
    }
    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
    	System.out.println("inside item right click");
    	return itemStackIn;
    }

	/**
	 * Called each tick as long the item is on a player inventory. Uses by maps
	 * to check if is on a player hand and update it's contents.
	 */
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// if held by something not a player? such as custom npc/zombie/etc
		if (entityIn instanceof EntityPlayer == false) {
			return;
		}
		EntityPlayer p = (EntityPlayer) entityIn;
		
		//every second, make a roll. 1/10th of the time then do a repair
		if (p.inventory.currentItem != itemSlot && worldIn.getWorldTime() % Const.TICKS_PER_SEC == 0 && 
				worldIn.rand.nextDouble() > 0.9) {
		
			int curr = stack.getItemDamage();
			if(curr > 0){
				stack.setItemDamage(curr - 1);
			}
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
		//starts out damaged increasing damage makes it repair
		stack.setItemDamage(MAXCHARGE - 5); 
		super.onCreated(stack, worldIn, playerIn);
	}
}
