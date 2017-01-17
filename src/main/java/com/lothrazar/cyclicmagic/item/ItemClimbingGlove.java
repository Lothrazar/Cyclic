package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemClimbingGlove extends BaseItem implements IHasRecipe {
  private static final double CLIMB_SPEED = 0.288D;
  private static final int ITEMSLOT_OFFHANDMAX = 8;//offhand is 0 , and hotbar is 0-8 (diff arrays)
  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often
  public ItemClimbingGlove() {
    this.setMaxStackSize(1);
  }
  @Override
  public String getTooltip() {
    return this.getUnlocalizedName() + ".tooltip";
  }
  /**
   * Called each tick as long the item is on a player inventory. Uses by maps to
   * check if is on a player hand and update it's contents.
   */
  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    if (!(entityIn instanceof EntityLivingBase)) { return; }
    if (itemSlot > ITEMSLOT_OFFHANDMAX) { return; }
    EntityLivingBase entity = (EntityLivingBase) entityIn;
    if (!entityIn.isCollidedHorizontally) { return; }
    if (entity.isSneaking()) {
      entity.motionY = 0.0D;
    }
    else if (entity.moveForward > 0.0F && entity.motionY < CLIMB_SPEED) {
      entity.motionY = CLIMB_SPEED;
    }
    if (worldIn.isRemote && //setting fall distance on clientside wont work
        entity instanceof EntityPlayer && entity.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
      ModCyclic.network.sendToServer(new PacketPlayerFalldamage());
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this, 1),
        "s  ",
        "ls ",
        "lls",
        's', Items.SLIME_BALL,
        'L', Items.LEATHER);
 
  }
}
