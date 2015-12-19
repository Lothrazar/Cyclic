package com.lothrazar.cyclicmagic.projectile; 

import com.lothrazar.cyclicmagic.ItemRegistry;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World; 

public class EntityRespawnEgg extends EntityThrowable
{ 
    public EntityRespawnEgg(World worldIn)
    {
        super(worldIn);
    }

    public EntityRespawnEgg(World worldIn, EntityLivingBase ent)
    {
        super(worldIn, ent);
    }

    public EntityRespawnEgg(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static final String name = "eggbolt";
    public static Item item = null;
    @Override
    protected void onImpact(MovingObjectPosition mop)
    {
        if (mop.entityHit != null && mop.entityHit instanceof EntityLivingBase)
        {
        	mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0);
            //do the snowball damage, which should be none. put out the fire
        	int entity_id = EntityList.getEntityID(mop.entityHit);

    		if(entity_id > 0) 
    		{ 
    			System.out.println("egg hit");
    			this.worldObj.removeEntity(mop.entityHit); 
    			if(this.worldObj.isRemote == false){
    				ItemStack stack = new ItemStack(ItemRegistry.respawn_egg,1,entity_id);
    				
    				if(mop.entityHit.hasCustomName()){
    					stack.setStackDisplayName(mop.entityHit.getCustomNameTag());
    				}
    				
    				this.worldObj.spawnEntityInWorld( new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack));
    			}
    		} 
        }
        
        this.setDead();
    }  
}