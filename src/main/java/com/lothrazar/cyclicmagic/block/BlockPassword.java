package com.lothrazar.cyclicmagic.block;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword.UsersAllowed;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPassword extends BlockBaseHasTile implements IHasRecipe {
  public static final PropertyBool POWERED = PropertyBool.create("powered");
  public BlockPassword() {
    super(Material.ROCK);
    this.setHardness(4F);
    this.setResistance(4F);
    this.setSoundType(SoundType.STONE);
    this.setHarvestLevel("pickaxe", 0);
    this.setGuiId(ModGuiHandler.GUI_INDEX_PASSWORD);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityPassword();
  }
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(POWERED, meta == 1 ? true : false);
  }
  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(POWERED)) ? 1 : 0;
  }
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, new IProperty[] { POWERED });
  }
  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }
  @Override
  public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
  }
  @Override
  public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
    return blockState.getValue(POWERED) ? 15 : 0;
  }
  @SubscribeEvent
  public void chatEvent(ServerChatEvent event) {
    World world = event.getPlayer().getEntityWorld();
    //for each loop hits a // oops : java.util.ConcurrentModificationException, so we need iterator
    Iterator<TileEntityPassword> iterator = TileEntityPassword.listeningBlocks.iterator();
    //    Map<BlockPos, Boolean> updates = new HashMap<BlockPos, Boolean>();
    List<TileEntityPassword> toRemove = new ArrayList<TileEntityPassword>();
    //TileEntityPassword current;
    int wasFound = 0;
    while (iterator.hasNext()) {
      TileEntityPassword current = iterator.next();
      if (current.isInvalid() == false) {
        if (current.getMyPassword() != null && current.getMyPassword().length() > 0 && event.getMessage().equals(current.getMyPassword())) {
          boolean isAllowed;
          if (current.getUserPerm() == UsersAllowed.ALL) {//user said everyones allowed
            isAllowed = true;
          }
          else {//it has no claimed user.. OR it is claimed
            isAllowed = !current.isClaimedBySomeone() || current.isClaimedBy(event.getPlayer());//nobody || me
          }
          if (isAllowed) {
            current.onCorrectPassword(world);
            wasFound++;
          }
          //          else {
          //            UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang(this.getUnlocalizedName() + ".notallowed"));
          //          }
        } //else password was wrong
      }
      else {
        toRemove.add(current);///is invalid
      }
    }
    //even with iterator we were getting ConcurrentModificationException on the iterator.next() line
    for (TileEntityPassword rm : toRemove) {
      TileEntityPassword.listeningBlocks.remove(rm);
    }
    if (wasFound > 0) {
      event.setCanceled(true);//If this event is canceled, the chat message is never distributed to all clients.
      if (wasFound == 1)
        UtilChat.addChatMessage(event.getPlayer(), UtilChat.lang(this.getUnlocalizedName() + ".triggered") + " : " + event.getMessage());
      else
        UtilChat.addChatMessage(event.getPlayer(), wasFound + " " + UtilChat.lang(this.getUnlocalizedName() + ".triggeredmany") + " : " + event.getMessage());
    }
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "sss",
        "trt",
        "sss",
        's', new ItemStack(Blocks.STONE_SLAB, 1, BlockStoneSlab.EnumType.STONE.getMetadata()),
        't', Blocks.TRIPWIRE_HOOK,
        'r', Items.COMPARATOR);
  }
}
