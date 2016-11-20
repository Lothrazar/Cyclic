package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderBag extends BaseItem implements IHasRecipe {
  public ItemEnderBag() {
    this.setMaxStackSize(1);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("item.sack_ender.tooltip"));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer player, EnumHand hand) {
    player.displayGUIChest(player.getInventoryEnderChest());
    if (world.rand.nextDouble() > 0.5)
      UtilSound.playSound(player, SoundEvents.BLOCK_ENDERCHEST_OPEN);
    else
      UtilSound.playSound(player, SoundEvents.BLOCK_ENDERCHEST_CLOSE);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "l l",
        "lel",
        "lsl",
        'l', Items.LEATHER,
        's', Blocks.OBSIDIAN,
        'e', Items.ENDER_PEARL);
    GameRegistry.addRecipe(new ItemStack(this),
        "l l",
        "lel",
        "lsl",
        'l', Items.LEATHER,
        's', Blocks.OBSIDIAN,
        'e', Items.ENDER_EYE);
    GameRegistry.addRecipe(new ItemStack(this),
        "   ",
        "lsl",
        "   ",
        'l', Items.LEATHER,
        's', Blocks.ENDER_CHEST);
  }
}
