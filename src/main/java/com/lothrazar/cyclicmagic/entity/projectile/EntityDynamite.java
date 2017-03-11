package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.ExplosionBlockSafe;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityDynamite extends EntityThrowableDispensable {
  public static Item renderSnowball;
  public static final float EX_CREEPER = 1;
  public static final float EX_CHARGEDCREEPER = 2;
  public static final float EX_TNT = 4;
  public static final float EX_ENDCRYSTAL = 6;
  private float explosionLevel;
  private boolean isBlockSafe;
  public EntityDynamite(World worldIn) {
    super(worldIn);
    this.explosionLevel = EX_CREEPER;
  }
  public EntityDynamite(World worldIn, int explos) {
    super(worldIn);
    this.explosionLevel = explos;
  }
  public EntityDynamite(World worldIn, EntityLivingBase ent, int strength) {
    super(worldIn, ent);
    this.explosionLevel = strength;
  }
  public EntityDynamite(World worldIn, int strength, double x, double y, double z) {
    super(worldIn, x, y, z);
    this.explosionLevel = strength;
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    this.isBlockSafe=true;//TODO: pass in as constructor arg. make new items
    if(this.isBlockSafe){

    ExplosionBlockSafe explosion = new ExplosionBlockSafe(this.getEntityWorld(), this.getThrower(),  posX, posY, posZ, explosionLevel, false, true);
    explosion.doExplosionA();
    explosion.doExplosionB(false);
//TODO: packet to also call doExplosionB on client..?? if sounds stop working?
    
    System.out.println("explode side "+this.worldObj.isRemote);
    }
    
    else{//use vanilla splode
      this.getEntityWorld().createExplosion(this, this.posX, this.posY + (double) (this.height / 2.0F), this.posZ, explosionLevel, true);
      
    }
    
    this.setDead();
  }

}