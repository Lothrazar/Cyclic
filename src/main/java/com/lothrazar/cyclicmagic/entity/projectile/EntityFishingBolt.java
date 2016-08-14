package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFishingBolt extends EntityThrowable {
  public static Item renderSnowball;
  public EntityFishingBolt(World worldIn) {
    super(worldIn);
  }
  public EntityFishingBolt(World worldIn, EntityLivingBase ent) {
    super(worldIn, ent);
  }
  public EntityFishingBolt(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  static final double plainChance = 60;
  static final double salmonChance = 25 + plainChance; // so it is between 60
  // and 85
  static final double clownfishChance = 2 + salmonChance; // so between 85 and
  // 87
  @Override
  protected void onImpact(RayTraceResult mop) {
    BlockPos pos = mop.getBlockPos();
    if (pos == null) { return; }
    if (this.isInWater()) {
      UtilParticle.spawnParticle(this.worldObj, EnumParticleTypes.WATER_BUBBLE, pos);
      EntityItem ei = new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), getRandomFish());
      if (worldObj.isRemote == false) {
        worldObj.spawnEntityInWorld(ei);
      }
      UtilSound.playSound(worldObj, pos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.BLOCKS);
      this.setDead();
    }
    else {
      if (worldObj.isRemote == false) {
        worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.itemMap.get("ender_fishing"))));
        this.setDead();
      }
    }
  }
  private ItemStack getRandomFish() {
    ItemStack fishSpawned = null;
    // below is from MY BlockFishing.java in the unreleased ./FarmingBlocks/
    double diceRoll = rand.nextDouble() * 100;
    if (diceRoll < plainChance) {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.COD.getMetadata()); // plain
    }
    else if (diceRoll < salmonChance) {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.SALMON.getMetadata());// salmon
    }
    else if (diceRoll < clownfishChance) {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata());// clown
    }
    else {
      fishSpawned = new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata());// puffer
    }
    return fishSpawned;
  }
}
