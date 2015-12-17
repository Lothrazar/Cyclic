package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.SpellRegistry;

/**
 * phasing out ISpell interface. now every spell must extend this base class for
 * now at least, until we make a spell book or some other system to replace this
 * linked list setup
 * 
 * @author Sam Bassett (Lothrazar)
 *
 */
public abstract class BaseSpell implements ISpell {

	private ResourceLocation icon;
	private int ID;

	public ISpell setSpellID(int id) {
		ID = id;
		return this;
	}

	@Override
	public int getSpellID() {
		return ID;
	}

	@Override
	public int getCastCooldown() {
		return 20;
	}

	@Override
	public boolean canPlayerCast(World world, EntityPlayer player, BlockPos pos){
		return true;//default to allow of course
	}
	
	@Override
	public ResourceLocation getIconDisplay() {
		return icon;
	}

	public ISpell setIconDisplay(ResourceLocation img) {
		icon = img;
		return this;
	}

	@Override
	public ISpell left() {
		int idx = SpellRegistry.spellbook.indexOf(this);// -1 for not found
		if (idx == -1) {
			return null;
		}

		if (idx == 0)
			idx = SpellRegistry.spellbook.size() - 1;
		else
			idx = idx - 1;

		return SpellRegistry.spellbook.get(idx);
	}

	@Override
	public ISpell right() {
		int idx = SpellRegistry.spellbook.indexOf(this);
		if (idx == -1) {
			return null;
		}

		if (idx == SpellRegistry.spellbook.size() - 1)
			idx = 0;
		else
			idx = idx + 1;

		return SpellRegistry.spellbook.get(idx);
	}
}
