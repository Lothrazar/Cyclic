package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderPearlReuse extends BaseTool implements IHasRecipe {
  private static final int durability = 2000;
  private static final int cooldown = 10;
  public static enum OrbType {
    NORMAL, MOUNTED;
  }
  private OrbType orbType;
  public ItemEnderPearlReuse(OrbType o) {
    super(durability);
    orbType = o;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    playerIn.getCooldownTracker().setCooldown(this, cooldown);
    if (!worldIn.isRemote) {
      EntityEnderPearl entityenderpearl = new EntityEnderPearl(worldIn, playerIn); //func_184538_a
      entityenderpearl.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
      worldIn.spawnEntity(entityenderpearl);
      if (orbType == OrbType.MOUNTED) {
        playerIn.dismountRidingEntity();
        playerIn.startRiding(entityenderpearl);
      }
    }
    super.onUse(itemStackIn, playerIn, worldIn, hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }
  @Override
  public IRecipe addRecipe() {
    switch (orbType) {
      case MOUNTED:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "ere",
            "rsr",
            "ere",
            'e', new ItemStack(Items.ENDER_EYE),
            'r', new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()),
            's', new ItemStack(Blocks.IRON_BLOCK));
      break;
      case NORMAL:
        GameRegistry.addShapedRecipe(new ItemStack(this),
            "ere",
            "rsr",
            "ere",
            'e', new ItemStack(Items.ENDER_EYE),
            'r', Items.REDSTONE,
            's', new ItemStack(Blocks.IRON_BLOCK));
      break;
      default:
      break;
    }
    return null;
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
