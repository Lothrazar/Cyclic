package com.lothrazar.cyclicmagic.item.charm;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseCharm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCharmAntidote extends BaseCharm implements IHasRecipe {
  private static final int durability = 128;
  public ItemCharmAntidote() {
    super(durability);
  }
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if(entityIn instanceof EntityPlayer){
      EntityPlayer living = (EntityPlayer) entityIn;
     if(living.isPotionActive(MobEffects.POISON)){
       living.removeActivePotionEffect(MobEffects.POISON);
       super.damageCharm(living, stack, itemSlot);
     }
     if(living.isPotionActive(MobEffects.WITHER)){
       living.removeActivePotionEffect(MobEffects.WITHER);
       super.damageCharm(living, stack, itemSlot);
     }
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "r n",
        "in ",
        "iir",
        'n', Items.FERMENTED_SPIDER_EYE,
        'r', Items.DIAMOND,
        'i', Items.IRON_INGOT);
  }
  @Override
  public String getTooltip() {
    return "item.charm_antidote.tooltip";
  }
}
