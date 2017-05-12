package com.lothrazar.cyclicmagic.entity;
import javax.swing.text.html.parser.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IMinecartCollisionHandler;

public class EntityObsidianMinecart extends EntityGoldMinecart { 
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull
  public EntityObsidianMinecart(World worldIn) {
    super(worldIn); 
  }
  public EntityObsidianMinecart(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  } 
  @Override
  public ItemStack getCartItem() {
    return new ItemStack(dropItem);
  }
  
  
  
  
  public static class CollisionHandler implements IMinecartCollisionHandler
  {
 
    @Override
    public void onEntityCollision(EntityMinecart cart, net.minecraft.entity.Entity other) {
    System.out.println("onEntityCollision");
      if(other instanceof EntityPlayer ){
        return;
      }
      //TODO: check speed/direction, check entity is living, etc
      //check its not a minecart
      other.setDead();
      
    }

    @Override
    public AxisAlignedBB getCollisionBox(EntityMinecart cart, net.minecraft.entity.Entity other) {
      // TODO Auto-generated method stub
      return getBoundingBox(cart);
    }

    @Override
    public AxisAlignedBB getMinecartCollisionBox(EntityMinecart cart) {
      // TODO Auto-generated method stub
      return getBoundingBox(cart);
    }

    @Override
    public AxisAlignedBB getBoundingBox(EntityMinecart cart) {
      // TODO Auto-generated method stub
      return cart.getEntityBoundingBox();
    }

  }
}
