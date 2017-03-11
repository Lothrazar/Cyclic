package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityMagicNetFull extends EntityThrowableDispensable {
  public static Item renderSnowball;
  private ItemStack captured;
  public EntityMagicNetFull(World worldIn) {
    super(worldIn);
  }
  public EntityMagicNetFull(World worldIn, EntityLivingBase ent,ItemStack c) {
    super(worldIn, ent);
    this.captured = c;
  }
  public EntityMagicNetFull(World worldIn, double x, double y, double z,ItemStack c) {
    super(worldIn, x, y, z);
    this.captured = c;
  }
  @Override
  protected void onImpact(RayTraceResult mop) {
    if (this.isDead) { return; }
    if(captured == null || captured.hasTagCompound() == false){
//      System.out.println("ERROR null captured");
      //client desync maybe
      return;
    }

    Entity spawnEntity = EntityList.createEntityFromNBT(captured.getTagCompound(), this.getEntityWorld());
    if (spawnEntity != null) {

//      System.out.println("!!!!!!!!spawnEntityInWorld");
      
      
      
      spawnEntity.readFromNBT(captured.getTagCompound());
      spawnEntity.setLocationAndAngles(this.posX, this.posY + 1.1F, this.posZ, this.rotationYaw, 0.0F);
      this.getEntityWorld().spawnEntityInWorld(spawnEntity);
//todo; could drop an empty one i guess
    }
    
    
    
    this.setDead();
  }
}