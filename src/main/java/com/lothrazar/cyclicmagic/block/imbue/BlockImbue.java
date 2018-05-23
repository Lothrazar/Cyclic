package com.lothrazar.cyclicmagic.block.imbue;

import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.block.IBlockHasTESR;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockImbue extends BlockBaseHasTile implements IBlockHasTESR {

  private static final String NBT_IMBUE = "CYCLIC_IMBUE";

  enum ImbueFlavor {
    //TODO: maybe instead a potion type with potion meta
    NONE, LEVITATION;
  }

  public BlockImbue() {
    super(Material.ROCK);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityImbue();
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    boolean success = false;
    ItemStack held = player.getHeldItem(hand);
    TileEntityImbue te = (TileEntityImbue) world.getTileEntity(pos);
    if (held.isEmpty()) {
      //drop the things
      if (!te.getStackInSlot(TileEntityImbue.SLOT_BOW).isEmpty()) {
        player.setHeldItem(hand, te.getStackInSlot(TileEntityImbue.SLOT_BOW));
        te.setInventorySlotContents(TileEntityImbue.SLOT_BOW, ItemStack.EMPTY);
      }
    }
    if (held.getItem() instanceof ItemBow && te.getStackInSlot(TileEntityImbue.SLOT_BOW).isEmpty()) {
      //

      te.setInventorySlotContents(TileEntityImbue.SLOT_BOW, held);
      player.setHeldItem(hand, ItemStack.EMPTY);
      success = true;
    }
    return success || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImbue.class, new ImbueTESR(0));
  }
  @SubscribeEvent
  public void onProjectileImpactEvent(ProjectileImpactEvent.Arrow event) {

    System.out.println(event.getArrow().getEntityData());
    Entity trg = event.getRayTraceResult().entityHit;
    if (trg instanceof EntityLivingBase
        && event.getArrow().getEntityData().getInteger(NBT_IMBUE) == ImbueFlavor.LEVITATION.ordinal()) {

      EntityLivingBase target = (EntityLivingBase) trg;
      target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100, 1));
    }
  }

  @SubscribeEvent
  public void onJoinWorld(EntityJoinWorldEvent event) {
    if (event.getEntity() instanceof EntityArrow &&
        event.getEntity() != null &&
        event.getEntity().isDead == false) {
      EntityArrow arrow = (EntityArrow) event.getEntity();
      arrow.getEntityData().setInteger(NBT_IMBUE, ImbueFlavor.LEVITATION.ordinal());
    }
  }
}
