package com.lothrazar.cyclicmagic.block.imbue;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.core.util.UtilNBT;
import com.lothrazar.cyclicmagic.item.dynamite.ExplosionBlockSafe;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
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
  private static final String NBT_IMBUE_CHARGE = "CYCLIC_CHARGE";

  enum ImbueFlavor {
    //TODO: maybe instead a potion type with potion meta
    NONE, LEVITATE, EXPLOSION, FIRE, INVISIBILITY, POISON, SLOWNESS, GLOWING;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }

    public TextFormatting getColor() {
      switch (this) {
        case EXPLOSION:
          return TextFormatting.GRAY;
        case FIRE:
        case GLOWING:
          return TextFormatting.YELLOW;
        case INVISIBILITY:
          return TextFormatting.BLUE;
        case LEVITATE:
          return TextFormatting.AQUA;
        case POISON:
          return TextFormatting.DARK_GREEN;
        case SLOWNESS:
          return TextFormatting.LIGHT_PURPLE;
        default:
        case NONE:
          return TextFormatting.BLACK;
      }
    }
  }

  public BlockImbue() {
    super(Material.ROCK);
    this.setTranslucent();
    RecipeImbue.initAllRecipes();
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
            return true;
          }
        }
        //if we did not return,  block was empty so display recipes
        UtilChat.addChatMessage(player, "imbue.recipes");
        for (RecipeImbue irecipe : BlockImbue.recipes) {
          UtilChat.addChatMessage(player, irecipe.toString());
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

  public static ImbueFlavor getImbueType(ItemStack held) {
    if (UtilNBT.getItemStackNBT(held).hasKey(NBT_IMBUE) == false) {
      return null;
    }
    int val = UtilNBT.getItemStackNBT(held).getInteger(BlockImbue.NBT_IMBUE);
    return ImbueFlavor.values()[val];
  }

  public static void setImbue(ItemStack held, RecipeImbue found) {
    UtilNBT.getItemStackNBT(held).setInteger(BlockImbue.NBT_IMBUE, found.flavor.ordinal());
  }

  public static void setImbueCharge(ItemStack held, int found) {
    UtilNBT.getItemStackNBT(held).setInteger(BlockImbue.NBT_IMBUE_CHARGE, found);
  }

  public static int getImbueCharge(ItemStack held) {
    return UtilNBT.getItemStackNBT(held).getInteger(BlockImbue.NBT_IMBUE_CHARGE);
  }

  @SubscribeEvent
  public void onProjectileImpactEvent(ProjectileImpactEvent.Arrow event) {
    Entity trg = event.getRayTraceResult().entityHit;
    if (trg instanceof EntityLivingBase
        && event.getArrow().getEntityData().hasKey(NBT_IMBUE)) {
      EntityLivingBase target = (EntityLivingBase) trg;
      int imbue = event.getArrow().getEntityData().getInteger(NBT_IMBUE);
      ImbueFlavor flavor = ImbueFlavor.values()[imbue];
      World world = target.world;
      switch (flavor) {
        case GLOWING:
        break;
        case EXPLOSION:
          ExplosionBlockSafe explosion = new ExplosionBlockSafe(world,
              event.getArrow().shootingEntity,
              trg.posX, trg.posY, trg.posZ, MathHelper.getInt(world.rand, 1, 4), false, true);
          explosion.doExplosionA();
          explosion.doExplosionB(false);
        break;
        case FIRE:
          target.setFire(MathHelper.getInt(world.rand, 2, 6));
        break;
        case LEVITATE:
          target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100, 1));
        break;
        case NONE:
        break;
        case INVISIBILITY:
          target.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 100, 1));
        break;
        case POISON:
          target.addPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1));
        break;
        case SLOWNESS:
          target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
        break;
        default:
        break;
      }
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
        ItemStack bow = source.getHeldItemMainhand();
        ImbueFlavor flavor = getImbueType(bow);
        int charge = getImbueCharge(bow);
        if (charge > 0 && flavor != null) {
          //
          arrow.getEntityData().setInteger(NBT_IMBUE, flavor.ordinal());
          //reduce charge
          setImbueCharge(bow, charge - 1);
        }
        else {
          //remove
          bow.getTagCompound().removeTag(NBT_IMBUE);
          bow.getTagCompound().removeTag(NBT_IMBUE_CHARGE);
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onItemTooltipEvent(ItemTooltipEvent event) {
    ItemStack itemStack = event.getItemStack();
    ImbueFlavor type = getImbueType(itemStack);
    if (type == null) {
      return;
    }
    int charge = getImbueCharge(itemStack);
    if (charge <= 0) {
      itemStack.getTagCompound().removeTag(NBT_IMBUE);
      itemStack.getTagCompound().removeTag(NBT_IMBUE_CHARGE);
      return;
    }
    event.getToolTip().add(UtilChat.lang("imbue.prefix")
        + type.getColor() + UtilChat.lang("imbue.type." + type.toString())
        + TextFormatting.RESET + " [" + charge + "]");
  }

  static List<RecipeImbue> recipes = new ArrayList<RecipeImbue>();

  public static void addRecipe(RecipeImbue imb) {
    recipes.add(imb);
  }
}
