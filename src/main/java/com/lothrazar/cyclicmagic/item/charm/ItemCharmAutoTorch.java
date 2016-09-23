package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCharmAutoTorch extends BaseCharm implements IHasRecipe {
  private static final int durability = 256;
  private static final int cooldown = 60;//ticks not seconds
  public ItemCharmAutoTorch() {
    super(durability);
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer && !world.isRemote) {
      EntityPlayer living = (EntityPlayer) entityIn;
      if (living.getCooldownTracker().hasCooldown(stack.getItem())) { return; } //cancel if on cooldown
      BlockPos pos = living.getPosition();
      //      System.out.println("blocklight "+world.getLight(pos, true));
      //      System.out.println("solid? "+ world.isSideSolid(pos,EnumFacing.UP));
      //      System.out.println("solidDOWN "+ world.isSideSolid(pos.down(),EnumFacing.UP));
      if (world.getLight(pos, true) < 7.0F && world.isSideSolid(pos.down(), EnumFacing.UP)) {
        if (UtilPlaceBlocks.placeStateSafe(world, living, pos, Blocks.TORCH.getDefaultState())) {
          super.damageCharm(living, stack, itemSlot);
          living.getCooldownTracker().setCooldown(this, cooldown);
        }
      }
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "ccc",
        " i ",
        " i ",
        'c', Blocks.COAL_BLOCK,
        'i', Blocks.IRON_BARS);
  }
  @Override
  public String getTooltip() {
    return "item.tool_auto_torch.tooltip";
  }
}
