package com.lothrazar.cyclicmagic.item.tool;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasClickToggle;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolWarp extends BaseTool implements IHasRecipe, IHasClickToggle {
  private static final int cooldown = 600;//ticks not seconds
  private static final int durability = 16;
  public static enum WarpType {
    BED, SPAWN
  }
  private WarpType warpType;
  public ItemToolWarp(WarpType type) {
    super(durability);
    warpType = type;
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  @Override
  public void toggle(EntityPlayer player, ItemStack held) {
    tryActivate(player, held);
  }
  private boolean tryActivate(EntityPlayer player, ItemStack held) {
    if (player.getCooldownTracker().hasCooldown(this)) { return false; }
    World world = player.getEntityWorld();
    if (player.dimension != 0) {
      UtilChat.addChatMessage(player, "command.worldhome.dim");
      return false;
    }
    boolean success = false;
    switch (warpType) {
      case BED:
        success = UtilWorld.tryTpPlayerToBed(world, player);
      break;
      case SPAWN:
        UtilEntity.teleportWallSafe(player, world, world.getSpawnPoint());
        success = true;
      break;
      default:
      break;
    }
    if (success) {
      UtilItemStack.damageItem(player, held);
      player.getCooldownTracker().setCooldown(this, cooldown);
    }
    return success;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    ItemStack stack = player.getHeldItem(hand);
    if (tryActivate(player, stack)) {
      super.onUse(stack, player, world, hand);
      //      player.getCooldownTracker().setCooldown(this, cooldown);
      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }
    return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    switch (warpType) {
      case BED:
        tooltip.add(UtilChat.lang("item.tool_warp_home.tooltip"));
      break;
      case SPAWN:
        tooltip.add(UtilChat.lang("item.tool_warp_spawn.tooltip"));
      break;
      default:
      break;
    }
  }
  @Override
  public IRecipe addRecipe() {
    switch (warpType) {
      case BED:
        //goes to your BED (which can be anywhere)
        GameRegistry.addShapedRecipe(new ItemStack(this),
            " ft",
            "ggf",
            "dg ",
            't', new ItemStack(Items.GHAST_TEAR),
            'f', new ItemStack(Items.FEATHER),
            'g', new ItemStack(Items.GOLD_INGOT),
            'd', new ItemStack(Items.ENDER_EYE));
      break;
      case SPAWN:
        //this one needs diamond but is cheaper. goes to worldspawn
        GameRegistry.addShapedRecipe(new ItemStack(this),
            " ff",
            "ggf",
            "dg ",
            'f', new ItemStack(Items.FEATHER),
            'g', new ItemStack(Items.GOLD_NUGGET),
            'd', new ItemStack(Items.DIAMOND));
      break;
      default:
      break;
    }
    return null;
  }
  @Override
  public boolean isOn(ItemStack held) {
    return true;
  }
}
