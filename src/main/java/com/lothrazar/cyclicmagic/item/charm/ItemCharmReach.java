package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCharmReach extends BaseCharm implements IHasRecipe {
 
  private static final int durability = 512;
  int REACH_VANILLA = 5;
  int REACH_BOOST = 10;
  public ItemCharmReach() {
    super(durability);
  }
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (entityIn instanceof EntityPlayer) {
      onTick(stack, (EntityPlayer) entityIn);
    }
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer player) {
    int currentReach = REACH_VANILLA;
    if (this.canTick(stack)) {
      currentReach = REACH_BOOST;
    }
   System.out.println("YEP reach "+currentReach);
   ModCyclic.proxy.setPlayerReach(player,currentReach);
    
  }
  @Override
  public void addRecipe() {
    super.addRecipeAndRepair(Items.GHAST_TEAR);
  }
}
