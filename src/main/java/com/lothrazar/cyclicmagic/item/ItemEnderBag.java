package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEnderBag extends BaseItem implements IHasRecipe {
  public ItemEnderBag() {
    this.setMaxStackSize(1);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick( World world, EntityPlayer player, EnumHand hand) {
    player.displayGUIChest(player.getInventoryEnderChest());
    if (world.rand.nextDouble() > 0.5)
      UtilSound.playSound(player, SoundEvents.BLOCK_ENDERCHEST_OPEN);
    else
      UtilSound.playSound(player, SoundEvents.BLOCK_ENDERCHEST_CLOSE);
    return super.onItemRightClick(world, player, hand);///return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }
  @Override
  public void addRecipe() {
//    GameRegistry.addRecipe(new ItemStack(this),
//        "l l",
//        "lel",
//        "lsl",
//        'l', Items.LEATHER,
//        's', Blocks.OBSIDIAN,
//        'e', Items.ENDER_PEARL);
//    GameRegistry.addRecipe(new ItemStack(this),
//        "l l",
//        "lel",
//        "lsl",
//        'l', Items.LEATHER,
//        's', Blocks.OBSIDIAN,
//        'e', Items.ENDER_EYE);
    GameRegistry.addRecipe(new ItemStack(this),
        " l ",
        "lsl",
        " l ",
        'l', Items.LEATHER,
        's', Blocks.ENDER_CHEST);
  }
}
