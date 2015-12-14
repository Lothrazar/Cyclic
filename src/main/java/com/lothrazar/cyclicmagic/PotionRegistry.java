package com.lothrazar.cyclicmagic;

import com.lothrazar.cyclicmagic.potion.PotionCustom;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionRegistry 
{ 
	//public static Potion tired;//http://www.minecraftforge.net/wiki/Potion_Tutorial
	public static Potion waterwalk;
	public static Potion slowfall; 
	//public static Potion lavawalk;
	//public static Potion frost;
	
	public final static int I = 0; 
	public final static int II = 1;
	public final static int III = 2;
	public final static int IV = 3;
	public final static int V = 4;
	
	public static void register()
	{ 
		//initPotionTypesReflection();

		//NEW : the array is now [256]
		//old v's of the game only had 32 so NO room to add modded potions
		
	    registerNewPotionEffects(); 
	}
 
	private static void registerNewPotionEffects() 
	{  
		//http://www.minecraftforge.net/forum/index.php?topic=11024.0
		//???http://www.minecraftforge.net/forum/index.php?topic=12358.0
		
		PotionRegistry.waterwalk = new PotionCustom(ModMain.cfg.potionIdWaterwalk, new ResourceLocation(Const.MODID,"textures/potions/waterwalk.png"), false, 0,"potion.waterwalk");
		
		PotionRegistry.slowfall = new PotionCustom(ModMain.cfg.potionIdSlowfall,    new ResourceLocation(Const.MODID,"textures/potions/slowfall.png") , false, 0,"potion.slowfall");
	 
		//TODO: test out brewing api for these?
		
		//TODO: bring back frost pot
		//PotionRegistry.frost = (new PotionCustom(ModSpells.cfg.potionIdFrozen, new ResourceLocation("frost"), false, 0, new ItemStack(ItemRegistry.spell_frostbolt_dummy))).setPotionName("potion.frozen");	  
	}
/*
	private static void initPotionTypesReflection() 
	{
		//because it is final, and because forge does not natively do this, we must change it this way
		Potion[] potionTypes = null;  //  public static final Potion[] potionTypes = new Potion[32];
	    for (Field f : Potion.class.getDeclaredFields()) 
	    {
	        f.setAccessible(true);
	        try 
	        { 
	            if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) 
	            {
	                Field modfield = Field.class.getDeclaredField("modifiers");
	                modfield.setAccessible(true);
	                modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

	                potionTypes = (Potion[])f.get(null);
	                final Potion[] newPotionTypes = new Potion[256];
	               
	                System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
	                f.set(null, newPotionTypes);
	                
	            }
	        }
	        catch (Exception e) 
	        {
	            System.err.println("Severe error, please report this to the mod author:");
	            System.err.println(e);
	        }
	    }
	}
*/
	
	
	
	
	
	/*
	private static void doPotionParticle(World world, EntityLivingBase living, EnumParticleTypes particle)
	{
		if(world.getTotalWorldTime() % 2/2 == 0) // every half second
    	{
    		ModSpells.spawnParticlePacketByID(living.getPosition(), particle.getParticleID());
    	}
	}

	public static void tickFrost(LivingUpdateEvent event) 
	{ 
		if(event.entityLiving.isPotionActive(PotionRegistry.frost)) 
	    { 
			World world = event.entityLiving.worldObj;
			BlockPos pos = event.entityLiving.getPosition();

			if(world.rand.nextDouble() < 0.5)
				doPotionParticle(world,event.entityLiving,EnumParticleTypes.SNOWBALL);

			if(world.rand.nextDouble() < 0.3 && 
				//world.getBlockState(pos).getBlock().isReplaceable(world, pos) &&
				world.getBlockState(pos.down()).getBlock() != Blocks.snow_layer && 
				//world.getBlockState(pos).getBlock().isReplaceable(world, pos.down()) == false &&//dont place above torches/snow/grass
				
				world.isAirBlock(pos.down()) == false)//dont place above air
			{
				world.setBlockState( pos, Blocks.snow_layer.getDefaultState());
			}
	    } 
	}*/
	
/*
	public static void tickLavawalk(LivingUpdateEvent event) 
	{
		if(event.entityLiving.isPotionActive(PotionRegistry.lavawalk)) 
	    {
			tickLiquidWalk(event,Blocks.lava);
	    }
	}*/
	
	
	//TODO: refactor this to @override Potion. public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
//TODO: do potion particles 
	public static void tickWaterwalk(LivingUpdateEvent event) 
	{
		if(event.entityLiving.isPotionActive(PotionRegistry.waterwalk)) 
	    {
			tickLiquidWalk(event,Blocks.water);
	    }
	}
 
	private static void tickLiquidWalk(LivingUpdateEvent event, Block liquid)
	{
    	 World world = event.entityLiving.worldObj;
    	 
    	 if(world.getBlockState(event.entityLiving.getPosition().down()).getBlock() == liquid && 
    			 world.isAirBlock(event.entityLiving.getPosition()) && 
    			 event.entityLiving.motionY < 0)
    	 { 
    		 if(event.entityLiving instanceof EntityPlayer)  //now wait here, since if we are a sneaking player we cancel it
    		 {
    			 EntityPlayer p = (EntityPlayer)event.entityLiving;
    			 if(p.isSneaking())
    				 return;//let them slip down into it
    		 }
    		 
    		 event.entityLiving.motionY  = 0;//stop falling
    		 event.entityLiving.onGround = true; //act as if on solid ground
    		 event.entityLiving.setAIMoveSpeed(0.1F);//walking and not sprinting is this speed
    	 }  
	}
	
	public static void tickSlowfall(LivingUpdateEvent event) 
	{
		 if(event.entityLiving.isPotionActive(PotionRegistry.slowfall)) 
	     { 
			 if(event.entityLiving instanceof EntityPlayer)  //now wait here, since if we are a sneaking player we cancel
			 {
    			 EntityPlayer p = (EntityPlayer)event.entityLiving;
    			 if(p.isSneaking())
    			 {
    				 return;//so fall normally for now
    			 }
    		 }
			 //else: so we are either a non-sneaking player, or a non player entity
			  
	    	 //a normal fall seems to go up to 0, -1.2, -1.4, -1.6, then flattens out at -0.078 
	    	 if(event.entityLiving.motionY < 0)
	    	 { 
				event.entityLiving.motionY *= ModMain.cfg.slowfallSpeed;
				  
				event.entityLiving.fallDistance = 0f; //for no fall damage
	    	 } 
	     }
	}	
}
