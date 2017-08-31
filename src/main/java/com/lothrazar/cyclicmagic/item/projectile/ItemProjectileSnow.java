package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.item.base.BaseItemScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemProjectileSnow extends BaseItemScepter implements IHasRecipe {
  public ItemProjectileSnow() {
    super(1000);
  }
 
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 32),
        "cobblestone",
        new ItemStack(Blocks.ICE),
        new ItemStack(Items.SNOWBALL));
  }
  @Override
  public   void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand, float power ) {
    Vec3d vec = player.getLookVec().normalize();
    //use cross products vs Vertical to find left and right position offsets
    Vec3d vecCrossRight = player.getLookVec().normalize().crossProduct(new Vec3d(0,2,0));
    Vec3d vecCrossLeft = player.getLookVec().normalize().crossProduct(new Vec3d(0,-2,0));
//TODO: move cross product and multishot logic to Base class
    ModCyclic.logger.log("vecCross"+vecCrossRight.toString());
    
    EntitySnowballBolt proj = new EntitySnowballBolt(world, player);
    this.launchProjectile(world, player, hand, proj, VELOCITY_DEFAULT);
    if(power > 4){

      EntitySnowballBolt projRight = new EntitySnowballBolt(world, player);
 
      projRight.posX += vecCrossRight.x; 
      projRight.posZ += vecCrossRight.z;
      this.launchProjectile(world, player, hand, projRight, VELOCITY_DEFAULT);
      
      
      

      EntitySnowballBolt projLeft = new EntitySnowballBolt(world, player);
      projLeft.posX += vecCrossLeft.x; 
      projLeft.posZ += vecCrossLeft.z;
      this.launchProjectile(world, player, hand, projLeft, VELOCITY_DEFAULT);
    }
    if(power>5){

//      EntitySnowballBolt projDown = new EntitySnowballBolt(world, player);
// 
//
//      this.launchProjectile(world, player, hand, projDown, VELOCITY_DEFAULT);
    }
    UtilItemStack.damageItem(player, held);
    
//    
//    projDown.posX += vec.x;
//    projDown.posY += vec.y;
//    projDown.posZ += vec.z;
  }
}
