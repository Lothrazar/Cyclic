package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCharmAutoTorch extends BaseCharm implements IHasRecipe {
  private static final int durability = 128;
  private static final int cooldown = 60;//ticks not seconds
  public ItemCharmAutoTorch() {
    super(durability);
  }
  public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer && !world.isRemote) {
      EntityPlayer living = (EntityPlayer) entityIn;
      if (living.getCooldownTracker().hasCooldown(stack.getItem())) { return; } //cancel if on cooldown
      BlockPos pos = living.getPosition();
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
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return false;
  }
}
