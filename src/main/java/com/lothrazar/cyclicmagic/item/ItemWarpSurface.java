package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWarpSurface extends BaseTool implements IHasRecipe {
  public ItemWarpSurface() {
    super(32);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    World world = entity.getEntityWorld();
    BlockPos dest = UtilWorld.getFirstBlockAbove(world, entity.getPosition());
    if (dest != null) {//teleport the target entity
      UtilSound.playSound(player, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
      UtilEntity.teleportWallSafe(entity, world, dest);
      super.onUse(stack, player, world, hand);
      return true;
    }
    UtilSound.playSound(player, SoundEvents.BLOCK_FIRE_EXTINGUISH);
    return false;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    BlockPos dest = UtilWorld.getFirstBlockAbove(world, player.getPosition());
    if (dest != null) {
      UtilSound.playSound(player, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
      UtilEntity.teleportWallSafe(player, world, dest);
      super.onUse(stack, player, world, hand);
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    UtilSound.playSound(player, SoundEvents.BLOCK_FIRE_EXTINGUISH);
    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " de",
        " gd",
        "s  ",
        'e', Items.ENDER_EYE,
        'd', "gemDiamond",
        'g', Items.GHAST_TEAR,
        's', Blocks.REDSTONE_LAMP);
  }
}
