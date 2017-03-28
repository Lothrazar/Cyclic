package com.lothrazar.cyclicmagic.item.tool;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolProspector extends BaseTool implements IHasRecipe, IHasConfig {
  private static final int DURABILITY = 2000;
  private static final int COOLDOWN = 12;
  private static int range = 16;
  public ItemToolProspector() {
    super(DURABILITY);
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (side == null || pos == null) { return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ); }
    Map<String, Integer> mapList = new HashMap<String, Integer>();
    String name;
    if (!worldObj.isRemote) {
      EnumFacing direction = side.getOpposite();
      BlockPos current = pos;
      IBlockState at = worldObj.getBlockState(current);
      Block blockAt = at.getBlock();
      ItemStack s;
      for (int i = 0; i < range; i++) {
        if (at != null && at.getBlock() != null) {
          
//          name = blockAt.getUnlocalizedName();
          s = new ItemStack(Item.getItemFromBlock(blockAt),1,blockAt.getMetaFromState(at));
          name = s.getDisplayName();
          if (name == "tile.air.name" || at.getBlock() == Blocks.AIR) {
//            name = "Air";//workaround for no lang entry
            
          }
          if (mapList.containsKey(name)) {
            mapList.put(name, mapList.get(name) + 1);
          }
          else {
            mapList.put(name, 1);
          }
        }
        current = current.offset(direction);
        at = worldObj.getBlockState(current);
      }
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    for (Map.Entry<String, Integer> entry : mapList.entrySet()) {
      UtilChat.addChatMessage(player, UtilChat.lang("tool_prospector.found") + entry.getKey() + " " + entry.getValue());
    }
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " sg",
        " bs",
        "b  ",
        'b', new ItemStack(Items.BLAZE_ROD),
        's', new ItemStack(Items.DIAMOND),
        'g', new ItemStack(Blocks.STAINED_GLASS, 1, EnumDyeColor.LIGHT_BLUE.getMetadata()));
  }
  @Override
  public void syncConfig(Configuration config) {
    ItemToolProspector.range = config.getInt("ProspectorRange", Const.ConfigCategory.modpackMisc, 32, 2, 256, "Block Range it will search onclick");
  }
}
