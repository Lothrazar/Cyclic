package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTrader extends BaseItem {
  public ItemTrader() {
    super();
    this.setMaxStackSize(1);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

    World world = entity.getEntityWorld();
    System.out.println("itemInteractionForEntity "+world.isRemote);
    if (entity instanceof EntityVillager) {
      
      if (!world.isRemote) {//!!!!
      EntityVillager villager = (EntityVillager) entity;
      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_VILLAGER, world,
          villager.getPosition().getX(),
          villager.getPosition().getY(),
          villager.getPosition().getZ());
      }
    }
    else{
      int r = 5;
      BlockPos center = player.getPosition();
      BlockPos start = center.add(-r, -r, -r);
      BlockPos end = center.add(r, r, r);
      List<EntityVillager> all = world.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(start,end));
      
      if(!all.isEmpty()){
        //make second gui that lists these, store entity id too. click on trade ->go to villager . maybe TP it to me even?
      }      
    }
    return true;//Returns true if the item can be used on the given entity, e.g. shears on sheep.
  }
}
