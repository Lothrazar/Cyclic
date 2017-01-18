package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolSurface extends BaseTool implements IHasRecipe {
  public ItemToolSurface() {
    super(100);
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
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
    //    world.getTopSolidOrLiquidBlock(pos)
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
  public String getTooltip() {
    return this.getUnlocalizedName() + ".tooltip";
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "  e",
        " s ",
        "s  ",
        'e', Items.ENDER_EYE,
        's', Blocks.REDSTONE_LAMP);
  }
}
