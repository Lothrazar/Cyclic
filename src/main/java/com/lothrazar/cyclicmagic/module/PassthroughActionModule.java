package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PassthroughActionModule extends BaseEventModule implements IHasConfig {
  private static boolean passThroughClick;

  @Override
  public void onInit() {
    ModMain.instance.events.addEvent(this);//for SubcribeEvent hooks
  }
  //TODO: why does the chest not animate? or only sometimes?
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    EntityPlayer entityPlayer = event.getEntityPlayer();
    ItemStack held = entityPlayer.getHeldItem(event.getHand());
    Entity target = event.getTarget();
    World worldObj = event.getWorld();
    //dont do this if player is sneaking, let them use the item frame
    if (target instanceof EntityItemFrame && entityPlayer.isSneaking() == false) {
      BlockPos pos = target.getPosition();
      EntityItemFrame frame = (EntityItemFrame) target;
      EnumFacing face = frame.getAdjustedHorizontalFacing();
      // why does it need to go down. not sure exactly. but it works
      BlockPos posBehind = pos.offset(face.getOpposite()).down();
      IBlockState stuffBehind = worldObj.getBlockState(posBehind);
      if (stuffBehind != null && stuffBehind.getBlock() != null && worldObj.getTileEntity(posBehind) != null) {
        stuffBehind.getBlock().onBlockActivated(worldObj, posBehind, stuffBehind, entityPlayer, event.getHand(), held, face, 0, 0, 0);
        event.setCanceled(true);
      }
    }
  }
  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (passThroughClick == false) { return; }
    EntityPlayer entityPlayer = event.getEntityPlayer();
    BlockPos pos = event.getPos();
    World worldObj = event.getWorld();
    if (pos == null) { return; }
    if (entityPlayer.isSneaking()) { return; }
    ItemStack held = event.getItemStack();// entityPlayer.getHeldItem(event.getHand());
    IBlockState state = event.getWorld().getBlockState(pos);
    //removed  && entityPlayer.isSneaking() == false
    if (state != null && (state.getBlock() == Blocks.WALL_SIGN || state.getBlock() == Blocks.WALL_BANNER)) {
      // but NOT standing sign or standing banner
      EnumFacing face = EnumFacing.getFront(state.getBlock().getMetaFromState(state));
      BlockPos posBehind = pos.offset(face.getOpposite());
      IBlockState stuffBehind = worldObj.getBlockState(posBehind);
      if (stuffBehind != null && stuffBehind.getBlock() != null && worldObj.getTileEntity(posBehind) != null) {
        // then perform the action on that thing (chest/furnace/etc)
        // a function in base class of block
        // public boolean onBlockActivated(World worldIn, BlockPos pos,
        // IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack
        // heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
        stuffBehind.getBlock().onBlockActivated(worldObj, posBehind, stuffBehind, entityPlayer, event.getHand(), held, event.getFace(), 0, 0, 0);
        // stop the normal item thing happening
        event.setUseItem(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.player;
    passThroughClick = config.getBoolean("Pass-Through Click", category, true,
        "Open chests (and other containers) by passing right through the attached signs, banners, and item frames");
  }
}
