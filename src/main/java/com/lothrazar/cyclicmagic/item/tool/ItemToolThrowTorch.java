package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolThrowTorch extends BaseTool implements IHasRecipe {
  private static final float VELOCITY_DEFAULT = 1.5F;
  private static final float INACCURACY_DEFAULT = 1.0F;
  private static final float PITCHOFFSET = 0.0F;//copied from BaseItemProjectile
  private static final int COOLDOWN = 8;//ticks
  public ItemToolThrowTorch() {
    super(256);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
    if (world.isRemote == false) {
      EntityTorchBolt thing = new EntityTorchBolt(world, player);
      thing.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, PITCHOFFSET, VELOCITY_DEFAULT, INACCURACY_DEFAULT);
      world.spawnEntityInWorld(thing);
    }
    UtilSound.playSound(player, player.getPosition(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS);
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    super.onUse(stack, player, world, hand);
    return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " gi",
        " ig",
        "o  ",
        'i', Items.IRON_INGOT,
        'g', Items.GOLD_INGOT,
        'o', Blocks.OBSIDIAN);
  }
}
