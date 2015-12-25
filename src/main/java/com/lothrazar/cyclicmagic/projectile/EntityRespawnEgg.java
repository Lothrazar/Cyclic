package com.lothrazar.cyclicmagic.projectile;

import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.item.ItemRespawnEggAnimal;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityRespawnEgg extends EntityThrowable {
	public EntityRespawnEgg(World worldIn) {
		super(worldIn);
	}

	public EntityRespawnEgg(World worldIn, EntityLivingBase ent) {
		super(worldIn, ent);
	}

	public EntityRespawnEgg(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public static final String name = "eggbolt";
	public static Item item = null;

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if (mop.entityHit != null && mop.entityHit instanceof EntityLivingBase) {
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
			// do the snowball damage, which should be none. put out the fire
			int entity_id = EntityList.getEntityID(mop.entityHit);

			if (entity_id > 0) {
				this.worldObj.removeEntity(mop.entityHit);
				if (this.worldObj.isRemote == false) {
						
					//ignore child mobs and tamed mobs
					
					boolean cancelDrop = false;
					
					
					if(mop.entityHit instanceof EntityAgeable){
						if(((EntityAgeable)mop.entityHit).isChild()){
							cancelDrop = true;
						}
					}
					if(mop.entityHit instanceof EntityTameable){
						if(((EntityTameable)mop.entityHit).isTamed()){
							//NOTE: Horse does not use this tameable base class, its different
							cancelDrop = true;
						}
					}
					if(mop.entityHit instanceof EntityHorse){
						if(((EntityHorse)mop.entityHit).isTame()){
							cancelDrop = true;
						}
					}
					if(mop.entityHit instanceof EntityVillager){
						//lots of complex NBT data, is not balanced/fair to reset trades and careers with this
						cancelDrop = true;
					}
					
					if(cancelDrop == false){

						ItemStack stack = new ItemStack(ItemRegistry.respawn_egg, 1, entity_id);
						NBTTagCompound nbt = null;
						
						if(mop.entityHit instanceof EntitySheep){
							EntitySheep sheep = ((EntitySheep)mop.entityHit);
						
							EnumDyeColor color = sheep.getFleeceColor();
	 
							nbt = new NBTTagCompound();
							nbt.setInteger(ItemRespawnEggAnimal.NBT_SHEEPCOLOR, color.getDyeDamage());
							nbt.setBoolean(ItemRespawnEggAnimal.NBT_SHEEPSHEARED, sheep.getSheared());
							stack.setTagCompound(nbt);
						}
						else if(mop.entityHit instanceof EntityRabbit){

							nbt = new NBTTagCompound();
							nbt.setInteger(ItemRespawnEggAnimal.NBT_RABBITTYPE,((EntityRabbit)mop.entityHit).getRabbitType());
							stack.setTagCompound(nbt);
						}
						else if(mop.entityHit instanceof EntityHorse){
							EntityHorse horse = ((EntityHorse)mop.entityHit);

							nbt = new NBTTagCompound();
							nbt.setInteger(ItemRespawnEggAnimal.NBT_HORSETEMPER, horse.getTemper());
							nbt.setInteger(ItemRespawnEggAnimal.NBT_HORSEVARIANT, horse.getHorseVariant());
							nbt.setInteger(ItemRespawnEggAnimal.NBT_HORSETYPE, horse.getHorseType());
							nbt.setBoolean(ItemRespawnEggAnimal.NBT_HORSEREPRO, horse.getHasReproduced());

							//not doing speed/jump/health by design. feel free to reroll these
							//only works on untamed anyway
						}
						else if(mop.entityHit instanceof EntityWolf){
							EntityWolf wolf = ((EntityWolf)mop.entityHit);

							nbt = new NBTTagCompound();
							nbt.setInteger(ItemRespawnEggAnimal.NBT_WOLFCOLOR, wolf.getCollarColor().getDyeDamage());
						}
						
						if (mop.entityHit.hasCustomName()) {
							stack.setStackDisplayName(mop.entityHit.getCustomNameTag());
						}
						
						if(nbt != null){
							stack.setTagCompound(nbt);
						}
						this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack));
					}
				}
			}
		}

		this.setDead();
	}
}