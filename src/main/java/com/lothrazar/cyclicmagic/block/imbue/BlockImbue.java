package com.lothrazar.cyclicmagic.block.imbue;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
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
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockImbue extends BlockBaseHasTile implements IBlockHasTESR {

  static final String NBT_IMBUE = "CYCLIC_IMBUE";

  enum ImbueFlavor {
    //TODO: maybe instead a potion type with potion meta
    NONE, POTION, EXPLOSION, ENTITY;
  }

  public BlockImbue() {
    super(Material.ROCK);
    this.setTranslucent();
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityImbue();
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (hand != EnumHand.MAIN_HAND) {
      return false;
      //    super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }
    boolean success = false;
    ItemStack held = player.getHeldItem(hand);
    TileEntityImbue te = (TileEntityImbue) world.getTileEntity(pos);
    if (held.isEmpty()) {
      //drop bow first if exists
      if (!te.getStackInSlot(TileEntityImbue.SLOT_TARGET).isEmpty()) {
        player.setHeldItem(hand, te.getStackInSlot(TileEntityImbue.SLOT_TARGET));
        te.setInventorySlotContents(TileEntityImbue.SLOT_TARGET, ItemStack.EMPTY);
        success = true;
      }
      else {//next hit, drop ingredients
        for (int i = TileEntityImbue.SLOT_TARGET + 1; i < te.getSizeInventory(); i++) {
          if (!te.getStackInSlot(i).isEmpty()) {
            UtilItemStack.dropItemStackInWorld(world, pos, te.getStackInSlot(i));
            te.setInventorySlotContents(i, ItemStack.EMPTY);
            break;
          }
        }
      }
    }
    else if (held.getItem() instanceof ItemBow && te.getStackInSlot(TileEntityImbue.SLOT_TARGET).isEmpty()) {
      te.setInventorySlotContents(TileEntityImbue.SLOT_TARGET, held);
      player.setHeldItem(hand, ItemStack.EMPTY);
      success = true;
    }
    else {//hand NOT empty and not a bow
      //ingredient
      for (int slot = TileEntityImbue.SLOT_TARGET + 1; slot < te.getSizeInventory(); slot++) {
        if (te.getStackInSlot(slot).isEmpty()) {
          te.setInventorySlotContents(slot, held);
          player.setHeldItem(hand, ItemStack.EMPTY);
          success = true;
          break;
        }
        //        te.setInventorySlotContents(slot, ItemStack.EMPTY);
        //        UtilItemStack.dropItemStackInWorld(world, pos, te.getStackInSlot(slot));
        //        break;
      }
      //      int slot = 1;
      //      if (te.getStackInSlot(slot).isEmpty()) {
      //        te.setInventorySlotContents(slot, held);
      //        player.setHeldItem(hand, ItemStack.EMPTY);
      //        success = true;
      //      }
    }
    return success || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImbue.class, new ImbueTESR());
  }

  @SubscribeEvent
  public void onProjectileImpactEvent(ProjectileImpactEvent.Arrow event) {
    System.out.println(event.getArrow().getEntityData());
    Entity trg = event.getRayTraceResult().entityHit;
    if (trg instanceof EntityLivingBase
        && event.getArrow().getEntityData().getInteger(NBT_IMBUE) == ImbueFlavor.POTION.ordinal()) {
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
      if (arrow.shootingEntity instanceof EntityPlayer) {
        EntityPlayer source = (EntityPlayer) arrow.shootingEntity;
        if (source.getHeldItemMainhand().isEmpty() == false
            && getImbueInt(source.getHeldItemMainhand()) != null) {
          switch (getImbueInt(source.getHeldItemMainhand())) {
            case POTION:
              arrow.getEntityData().setInteger(NBT_IMBUE, ImbueFlavor.POTION.ordinal());
            //REDUCE CHARGES 
            break;
            case NONE:
            break;
            default:
            break;
          }
        }
      }
    }
  }

  public static ImbueFlavor getImbueInt(ItemStack held) {
    if (UtilNBT.getItemStackNBT(held).hasKey(NBT_IMBUE) == false) {
      return null;
    }
    int val = UtilNBT.getItemStackNBT(held).getInteger(BlockImbue.NBT_IMBUE);
    return ImbueFlavor.values()[val];
  }

  public static void setImbueInt(ItemStack held, ImbueFlavor val) {
    UtilNBT.getItemStackNBT(held).setInteger(BlockImbue.NBT_IMBUE, val.ordinal());
  }


  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onItemTooltipEvent(ItemTooltipEvent event) {
    ItemStack itemStack = event.getItemStack();
    if (getImbueInt(itemStack) == null) {
      return;
    }
    event.getToolTip().add(UtilChat.lang("imbue.type.") + getImbueInt(itemStack).name().toLowerCase());
    //TODO CHARGES 
    //    switch (getImbueInt(itemStack)) {
    //      case LEVITATION:
    //        break;
    //      case NONE:
    //        break;
    //      default:
    //        break;
    //    }
  }

  static List<RecipeImbue> recipes = new ArrayList<RecipeImbue>();

  public static void addRecipe(RecipeImbue imb) {
    recipes.add(imb);
  }
}
