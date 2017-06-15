package com.lothrazar.cyclicmagic.component.merchant;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.item.base.BaseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemMerchantAlmanac extends BaseItem implements IHasRecipe {
  public static final int radius = 5;
  public ItemMerchantAlmanac() {
    super();
    this.setMaxStackSize(1);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    BlockPos p = player.getPosition();
    if (world.isRemote == false) {
      player.openGui(ModCyclic.instance, ForgeGuiHandler.GUI_INDEX_VILLAGER, world, p.getX(), p.getY(), p.getZ());
    }
    return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
  }
  @Override
  public IRecipe addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " b ", " q ",
        'e', Items.EMERALD,
        'b', Items.BOOK,
        'q', Blocks.BROWN_MUSHROOM);
    return GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " b ", " q ",
        'e', Items.EMERALD,
        'b', Items.BOOK,
        'q', Blocks.RED_MUSHROOM);
  }
}
