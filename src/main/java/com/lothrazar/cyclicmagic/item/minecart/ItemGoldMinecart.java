package com.lothrazar.cyclicmagic.item.minecart;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecart;
import com.lothrazar.cyclicmagic.item.BaseItemMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemGoldMinecart extends BaseItemMinecart implements IHasRecipe {
  public ItemGoldMinecart() {
    super();
  } 
  @Override
  public IRecipe addRecipe() {
    return    GameRegistry.addShapedRecipe(new ItemStack(this),
        "   ",
        "gmg",
        "ggg",
        'g',Items.GOLD_INGOT,
        'm',Items.MINECART);
     
  }
  @Override
  public EntityMinecart summonMinecart(World world) {
    return  new EntityGoldMinecart(world);
 
  }
}
