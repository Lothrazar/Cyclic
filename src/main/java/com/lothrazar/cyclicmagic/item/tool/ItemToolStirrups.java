package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolStirrups extends BaseTool implements IHasRecipe {
  public ItemToolStirrups() {
    super(100);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    World world = entity.getEntityWorld();
    if (world.isRemote) { return false; }
    player.startRiding(entity, true);
    super.onUse(stack, player, world, hand);
    return true;
  }
  @Override
  public String getTooltip() {
    return "item.tool_mount.tooltip";
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " ls",
        " sl",
        "ii ",
        'l', Items.LEAD,
        'i', Items.IRON_INGOT,
        's', Items.LEATHER);
  }
}
