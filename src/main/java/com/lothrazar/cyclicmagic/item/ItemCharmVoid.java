package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCharmVoid extends BaseCharm implements IHasRecipe {
  private static final int durability = 16;
  private static final int yLowest = -30;
  private static final int yDest = 255;
  public ItemCharmVoid() {
    super(durability);
  }
 
  @Override
  public IRecipe addRecipe() {
    return super.addRecipeAndRepair(Items.ENDER_EYE);
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) { return; }
    World worldIn = living.getEntityWorld();
    if (living.getPosition().getY() < yLowest) {
      UtilEntity.teleportWallSafe(living, worldIn, new BlockPos(living.getPosition().getX(), yDest, living.getPosition().getZ()));
      super.damageCharm(living, stack);
      UtilSound.playSound(living, living.getPosition(), SoundEvents.ENTITY_ENDERMEN_TELEPORT, living.getSoundCategory());
      UtilParticle.spawnParticle(worldIn, EnumParticleTypes.PORTAL, living.getPosition());
    }
  }
}
