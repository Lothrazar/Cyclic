package com.lothrazar.cyclicmagic.item.projectile;
import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemProjectileWater extends BaseTool implements IHasRecipe {
  private static final int HEIGHT = 3;
  private static final int RADIUS = 3;
  public ItemProjectileWater() {
    super(500);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack stack = playerIn.getHeldItem(hand);
    List<BlockPos> shape = UtilShape.cubeFilled(playerIn.getPosition(), RADIUS, HEIGHT);
    List<BlockPos> queuedToUpdate = new ArrayList<BlockPos>();
    int success = 0;
    Material mat;
    for (BlockPos pos : shape) {
      mat = worldIn.getBlockState(pos).getMaterial();
      if (mat == Material.WATER || mat == Material.LAVA) {
        if (worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2))
          success++;
      }
      if (success > 0) {
        UtilItemStack.damageItem(playerIn, playerIn.getHeldItem(hand), success);
        UtilSound.playSound(playerIn, SoundRegistry.pschew_fire);
        playerIn.swingArm(hand);
        //mimic what BlockSponge does : set block with status 2 so dont notify, then later notify them all at once
        //this prevents insta-fillins from neighbours as remvoed
        for (BlockPos blockpos2 : queuedToUpdate) {
          worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.AIR, false);
        }
      }
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        "iii",
        " c ",
        "bcb",
        'b', Items.SUGAR,
        'c', Blocks.SPONGE,
        'i', "dyeBlue");
  }
}
