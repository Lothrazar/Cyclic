package com.lothrazar.cyclicmagic.projectile; 

import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
//import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World; 

public class EntitySnowballBolt extends EntityThrowable
{ 
	//public static int secondsFrozenOnHit;
	//public static int fireSeconds;
	public static boolean damageEntityOnHit;
	public static boolean damageBlazeImmune;
	//public static int damageToNormal = 0;//TODO CONFIG
	//public static int damageToBlaze = 2;//TODO CONFIG
	
    public EntitySnowballBolt(World worldIn)
    {
        super(worldIn);
    }

    public EntitySnowballBolt(World worldIn, EntityLivingBase ent)
    {
        super(worldIn, ent);
    }

    public EntitySnowballBolt(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public static final String name = "frostbolt";
    public static final String name_item = name+"_item";
    public static Item item = null;
    @Override
    protected void onImpact(MovingObjectPosition mop)
    {
        if (mop.entityHit != null)
        {
        	if(damageEntityOnHit)
        	{
	            float damage = 0;
	
	            if (mop.entityHit instanceof EntityBlaze)
	            {
	                damage = 1;
	            }
	            
	            //do the snowball damage, which should be none. put out the fire
	            mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
        	}
            
            if(mop.entityHit instanceof EntityLivingBase)
            {
            	EntityLivingBase e = (EntityLivingBase)mop.entityHit;

                if(e.isBurning())
                {
                	e.extinguish();
                }
            //TODO     System.out.println("TODO: potion");
            	//e.addPotionEffect(new PotionEffect(PotionRegistry.frost.id, secondsFrozenOnHit * 20,0));
            } 
        }
        
        BlockPos pos = mop.getBlockPos();
        if(pos == null){return;}//hasn't happened yet, but..
        
        UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.SNOWBALL, pos);
        UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.SNOW_SHOVEL, pos);
      
        if( mop.sideHit != null && this.getThrower() instanceof EntityPlayer)
        {
        	this.worldObj.extinguishFire((EntityPlayer)this.getThrower(), pos, mop.sideHit);
        }
        
    	if(this.isInWater() )
        { 
    		BlockPos posWater = this.getPosition();
    		
    		if(this.worldObj.getBlockState(posWater) != Blocks.water.getDefaultState() ) 
    		{
    			posWater = null;//look for the closest water source, sometimes it was air and we got ice right above the water if we dont do this check
    			 
    			if(this.worldObj.getBlockState(mop.getBlockPos()) == Blocks.water.getDefaultState())
    				posWater = mop.getBlockPos();
    			else if(this.worldObj.getBlockState(mop.getBlockPos().offset(mop.sideHit)) == Blocks.water.getDefaultState())
    				posWater = mop.getBlockPos().offset(mop.sideHit); 
    		}

        	if(posWater != null) //rarely happens but it does
        	{
        		this.worldObj.setBlockState(posWater, Blocks.ice.getDefaultState()); 
        	}
        }
    	else
    	{
    		//on land, so snow?
    		BlockPos hit = pos;
    		BlockPos hitDown = hit.down();
    		BlockPos hitUp = hit.up();
        	
    		IBlockState hitState = this.worldObj.getBlockState(hit);
    		if(hitState.getBlock() == Blocks.snow_layer)
    		{
    			setMoreSnow(this.worldObj,hit);

    		}//these other cases do not really fire, i think. unless the entity goes inside a block before despawning
    		else if(this.worldObj.getBlockState(hitDown).getBlock() == Blocks.snow_layer)
    		{
    			setMoreSnow(this.worldObj,hitDown);
    		}
    		else if(this.worldObj.getBlockState(hitUp).getBlock() == Blocks.snow_layer)
    		{
    			setMoreSnow(this.worldObj,hitUp);
    		}
    		else if(
    				this.worldObj.isAirBlock(hit) == false //one below us cannot be air
    				&& //and we landed at air or replaceable
    				this.worldObj.isAirBlock(hitUp) == true )
    				//this.worldObj.getBlockState(this.getPosition()).getBlock().isReplaceable(this.worldObj, this.getPosition()))
    		{

    			setNewSnow(this.worldObj,hitUp);
    		}  
    		else if(
    				this.worldObj.isAirBlock(hit) == false //one below us cannot be air
    				&& //and we landed at air or replaceable
    				this.worldObj.isAirBlock(hitUp) == true )
    				//this.worldObj.getBlockState(this.getPosition()).getBlock().isReplaceable(this.worldObj, this.getPosition()))
        		{
        			setNewSnow(this.worldObj,hitUp);
        		} 
        	}
        	 
            this.setDead();
 
    } 
    private static void setMoreSnow(World world, BlockPos pos)
    { 		
    	//so of the block hit, get metadata, and add +1 to it, unless its full like 8 or more
    	
    	IBlockState hitState = world.getBlockState(pos);
		int m = hitState.getBlock().getMetaFromState(hitState);
		//when it hits 7, same size as full block
		if(m+1 < 8)
			world.setBlockState(pos, Blocks.snow_layer.getStateFromMeta(m+1));

    	world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), getSnowSound(world), 1,1);
    }
    private static void setNewSnow(World world, BlockPos pos)
    {
    	world.setBlockState(pos, Blocks.snow_layer.getDefaultState());
    	
    	world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), getSnowSound(world), 1,1);
    }
    private static String getSnowSound(World world){return "dig.snow";
  //  +world.rand.nextInt(5);
    }
}