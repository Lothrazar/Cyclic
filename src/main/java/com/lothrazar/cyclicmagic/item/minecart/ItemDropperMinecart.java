package com.lothrazar.cyclicmagic.item.minecart;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.EntityMinecartDropper;
import com.lothrazar.cyclicmagic.item.BaseItemMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDropperMinecart extends BaseItemMinecart implements IHasRecipe {
  public ItemDropperMinecart() {
    super();
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        " d ",
        " m ",
        "   ",
        'm', Items.MINECART,
        'd', Blocks.DROPPER);
  }
  @Override
  public EntityMinecart summonMinecart(World world) {
    return new EntityMinecartDropper(world);
  }
}
