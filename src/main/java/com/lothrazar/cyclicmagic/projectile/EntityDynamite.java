package com.lothrazar.cyclicmagic.projectile; 

import net.minecraft.entity.EntityLivingBase;  
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDynamite extends EntityThrowable
{ 
	public static final float LEVEL_CREEPER = 1F;
	public static final float LEVEL_CHARGED = 2F;
	public static final float LEVEL_TNT = 4F;
	public static final float LEVEL_ENDERCRYSTAL = 6F;
	private float explosionLevel=1.0F;
    public EntityDynamite(World worldIn)
    {
        super(worldIn); 
    }
    public EntityDynamite(World worldIn, int explos)
    {
        super(worldIn); 
        this.explosionLevel = explos;
    }

    public EntityDynamite(World worldIn, EntityLivingBase ent, float explos)
    {
        super(worldIn, ent);
        this.explosionLevel = explos;
    }

    public EntityDynamite(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static final String name = "tntbolt";
    public static Item item = null;
    @Override
    protected void onImpact(MovingObjectPosition mop)
    { 
        this.worldObj.createExplosion(this, this.posX, this.posY + (double)(this.height / 2.0F), this.posZ, explosionLevel, true);
        this.setDead(); 
    }
}