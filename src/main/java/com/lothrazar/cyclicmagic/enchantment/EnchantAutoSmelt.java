package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnchantAutoSmelt extends EnchantBase {
  public EnchantAutoSmelt() {
    super("autosmelt",Rarity.RARE, EnumEnchantmentType.DIGGER, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND, EntityEquipmentSlot.OFFHAND });
    
  }
  @Override
  public int getMaxLevel() {
    return 1;
  }
  @Override
  public boolean canApplyTogether(Enchantment ench) {
    return ench != Enchantments.SILK_TOUCH && ench != Enchantments.FORTUNE && super.canApplyTogether(ench);
  }
  @SubscribeEvent()
  public void onHarvestDrops(HarvestDropsEvent event) {
    if (event.getHarvester() == null) { return; }
    int level = getCurrentLevelTool(event.getHarvester());
    if (level <= 0) { return; }
    if (event.isSilkTouching()) { return; } //it should be incompabile but check anyway ya
    List<ItemStack> drops = event.getDrops();
    List<ItemStack> dropsCopy = new ArrayList<ItemStack>();
    //  Collections.copy(dropsCopy, drops);//fails
    for (ItemStack drop : drops) {
      dropsCopy.add(drop.copy());//manual deep-clone
    }
    //erase list of drops and rebuild it
    drops.clear();//works since byref
    for (ItemStack drop : dropsCopy) {
      ItemStack fromSmelted = FurnaceRecipes.instance().getSmeltingResult(drop);
      if (fromSmelted != ItemStack.EMPTY) {
        if (fromSmelted.getCount() == 0) { //wtf!?!?! why how does this happen? idk whatever fixed
          fromSmelted.setCount(1);
        }
        drops.add(fromSmelted);//smelt it up yo!
      }
      else {
        drops.add(drop);//same as without enchant
      }
    }
  }
}
