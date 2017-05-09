package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketChestSack;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemChestSackEmpty extends BaseItem implements IHasRecipe {
  public static final String name = "chest_sack_empty";
  public ItemChestSackEmpty() {
    super();
    // imported from my old mod
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/b02f6b4243993eb301f4aa2b39984838adf482c1/src/main/java/com/lothrazar/samscontent/item/ItemChestSack.java
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer entityPlayer, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (pos == null) { return EnumActionResult.FAIL; }
    //    ItemStack stack = entityPlayer.getHeldItem(hand);
    TileEntity tile = world.getTileEntity(pos);
    IBlockState state = world.getBlockState(pos);
    if (state == null || tile == null) {//so it works on EXU2 machines  || tile instanceof IInventory == false
      if (world.isRemote) {
        UtilChat.addChatMessage(entityPlayer, "item.chest_sack_empty.inventory");
      }
      return EnumActionResult.FAIL;
    }
    UtilSound.playSound(entityPlayer, pos, SoundRegistry.thunk);
    if (world.isRemote) {
      ModCyclic.network.sendToServer(new PacketChestSack(pos));// https://github.com/PrinceOfAmber/Cyclic/issues/131
    }
    return EnumActionResult.SUCCESS;
  }
  @Override
  public IRecipe addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "lbl",
        "lll",
        'l', new ItemStack(Items.LEATHER),
        'b', new ItemStack(Items.SLIME_BALL),
        's', new ItemStack(Items.STRING));
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        " s ",
        "lbl",
        "lll",
        'l', new ItemStack(Items.LEATHER),
        'b', new ItemStack(Items.APPLE),
        's', new ItemStack(Items.STRING));
  }
  private Item fullSack;
  public void setFullSack(Item item) {
    fullSack = item;
  }
  public Item getFullSack() {
    return fullSack;
  }
}