package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCharmAir extends BaseCharm implements IHasRecipe {
  private static final int TICKS_FALLDIST_SYNC = 22;//tick every so often
  private static final int durability = 512;
  public ItemCharmAir() {
    super(durability);
  }
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    if (!this.canTick(stack)) { return; }
    World world = player.getEntityWorld();
    BlockPos belowMe = player.getPosition().down();
    boolean isAirBorne = (world.isAirBlock(belowMe) //sneak on air, or a nonsolid block like a flower
        || world.isSideSolid(belowMe, EnumFacing.UP) == false);
    //do not use  player.isAirBorne, its only true on clientside, and that doesnt let us deal charm damage.. among possible other issues
    if (player.isSneaking() && isAirBorne && player.motionY < 0) {
      player.motionY = 0;
      player.isAirBorne = false;
      //if we set onGround->true all the time, it blocks fwd movement anywya
      player.onGround = (player.motionX == 0 && player.motionZ == 0); //allow jump only if not walking
      if (player.getEntityWorld().rand.nextDouble() < 0.1) {
        super.damageCharm(player, stack);
      }
      if (world.isRemote && //setting fall distance on clientside wont work
          player instanceof EntityPlayer && player.ticksExisted % TICKS_FALLDIST_SYNC == 0) {
        ModCyclic.network.sendToServer(new PacketPlayerFalldamage());
      }
    }
  }
  @Override
  public String getTooltip() {
    return "item.charm_air.tooltip";
  }
  @Override
  public void addRecipe() {
    super.addRecipeAndRepair(Items.GLASS_BOTTLE);
  }
}
