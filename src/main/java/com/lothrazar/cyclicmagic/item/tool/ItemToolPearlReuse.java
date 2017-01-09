package com.lothrazar.cyclicmagic.item.tool;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.item.EntityEnderPearl;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolPearlReuse extends BaseTool implements IHasRecipe {
  private static final int durability = 2000;
  private static final int cooldown = 10;
  public static enum OrbType {
    NORMAL, MOUNTED;
  }
  private OrbType orbType;
  public ItemToolPearlReuse(OrbType o) {
    super(durability);
    orbType = o;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
    worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    playerIn.getCooldownTracker().setCooldown(this, cooldown);
    if (!worldIn.isRemote) {
      EntityEnderPearl entityenderpearl = new EntityEnderPearl(worldIn, playerIn); //func_184538_a
      entityenderpearl.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
      worldIn.spawnEntityInWorld(entityenderpearl);
      if (orbType == OrbType.MOUNTED) {
        playerIn.dismountRidingEntity();
        playerIn.startRiding(entityenderpearl);
      }
    }
    super.onUse(itemStackIn, playerIn, worldIn, hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }
  @Override
  public void addRecipe() {
    switch (orbType) {
      case MOUNTED:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "ere",
            "rsr",
            "ere",
            'e', new ItemStack(Items.ENDER_EYE),
            'r', new ItemStack(Blocks.LAPIS_BLOCK),
            's', new ItemStack(Blocks.EMERALD_BLOCK));
      break;
      case NORMAL:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "ere",
            "rsr",
            "ere",
            'e', new ItemStack(Items.ENDER_EYE),
            'r', new ItemStack(Blocks.REDSTONE_BLOCK),
            's', new ItemStack(Blocks.EMERALD_BLOCK));
      break;
      default:
      break;
    }
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    switch (orbType) {
      case MOUNTED:
        tooltip.add(UtilChat.lang("item.ender_pearl_mounted.tooltip"));
      break;
      case NORMAL:
        tooltip.add(UtilChat.lang("item.ender_pearl_reuse.tooltip"));
      break;
      default:
      break;
    }
  }
}
