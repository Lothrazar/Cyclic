package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilFurnace;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerAbilitiesModule extends BaseEventModule implements IHasConfig {
  private static final int LADDER_ROTATIONLIMIT = -78;
  private static final double LADDER_SPEED = 0.10;
  private boolean signSkullName;
  private boolean easyEnderChest;
  private boolean fastLadderClimb;
  private boolean editableSigns;
  private boolean nameVillagerTag;
  private boolean passThroughClick;
  private boolean armorStandSwap;
  private boolean stardewFurnace; // inspired by stardew valley
  @SubscribeEvent
  public void onInteract(PlayerInteractEvent.RightClickBlock event) {
    if (passThroughClick) {
      EntityPlayer entityPlayer = event.getEntityPlayer();
      BlockPos pos = event.getPos();
      World worldObj = event.getWorld();
      if (pos == null) { return; }
      if (entityPlayer.isSneaking()) { return; }
      //      ItemStack held = event.getItemStack();// entityPlayer.getHeldItem(event.getHand());
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
          stuffBehind.getBlock().onBlockActivated(worldObj, posBehind, stuffBehind, entityPlayer, event.getHand(), event.getFace(), 0, 0, 0);
          // stop the normal item thing happening
          event.setUseItem(net.minecraftforge.fml.common.eventhandler.Event.Result.DENY);
        }
      }
    }
  }
  @SubscribeEvent
  public void onEntityInteractEvent(EntityInteract event) {
    if (nameVillagerTag) {
      EntityPlayer entityPlayer = event.getEntityPlayer();
      ItemStack held = entityPlayer.getHeldItem(event.getHand());
      Entity target = event.getTarget();
      if (held != null && held.getItem() == Items.NAME_TAG && held.hasDisplayName() && target instanceof EntityVillager) {
        EntityVillager v = (EntityVillager) target;
        v.setCustomNameTag(held.getDisplayName());
        if (entityPlayer.capabilities.isCreativeMode == false) {
          entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
        }
        event.setCanceled(true);// stop the GUI inventory opening
      }
    }
    if (passThroughClick) {
      EntityPlayer entityPlayer = event.getEntityPlayer();
      //      ItemStack held = entityPlayer.getHeldItem(event.getHand());
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
          stuffBehind.getBlock().onBlockActivated(worldObj, posBehind, stuffBehind, entityPlayer, event.getHand(), face, 0, 0, 0);
          event.setCanceled(true);
        }
      }
    }
  }
  @SubscribeEvent
  public void onPlayerInteract(PlayerInteractEvent event) {
    if (signSkullName) {
      EntityPlayer entityPlayer = event.getEntityPlayer();
      BlockPos pos = event.getPos();
      World worldObj = event.getWorld();
      if (pos == null) { return; }
      // event has no hand??
      // and no item stack. and right click rarely works. known bug
      // http://www.minecraftforge.net/forum/index.php?topic=37416.0
      ItemStack held = entityPlayer.getHeldItemMainhand();
      if (held == null) {
        held = entityPlayer.getHeldItemOffhand();
      }
      TileEntity container = worldObj.getTileEntity(pos);
      if (held != null && held.getItem() == Items.SKULL && held.getItemDamage() == Const.skull_player && container != null && container instanceof TileEntitySign) {
        TileEntitySign sign = (TileEntitySign) container;
        String firstLine = sign.signText[0].getUnformattedText();
        if (firstLine == null) {
          firstLine = "";
        }
        if (firstLine.isEmpty() || firstLine.split(" ").length == 0) {
          held.setTagCompound(null);
        }
        else {
          firstLine = firstLine.split(" ")[0];
          NBTTagCompound nbt = UtilNBT.getItemStackNBT(held);
          nbt.setString(Const.SkullOwner, firstLine);
        }
      }
    }
  }
  @SubscribeEvent
  public void onPlayerTick(LivingUpdateEvent event) {
    if (fastLadderClimb) {
      if (event.getEntityLiving() instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (player.isOnLadder() && !player.isSneaking() && player.moveForward == 0) {
          //move up faster without 'w'
          if (player.rotationPitch < LADDER_ROTATIONLIMIT) {
            //even bigger to counter gravity
            player.addVelocity(0, LADDER_SPEED, 0);
          }
        }
      }
    }
  }
  @SubscribeEvent
  public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer entityPlayer = event.getEntityPlayer();
    BlockPos pos = event.getPos();
    World worldObj = event.getWorld();
    //    ItemStack held = entityPlayer.getHeldItem(event.getHand());
    ItemStack held = event.getItemStack();
    if (stardewFurnace) {
      // ignore in creative// left clicking just breaks it anyway
      if (entityPlayer.capabilities.isCreativeMode) { return; }
      if (pos == null) { return; }
      int playerSlot = 0;// entityPlayer.inventory.currentItem;
      boolean wasMain = event.getHand() == EnumHand.MAIN_HAND;
      if (wasMain) {
        playerSlot = entityPlayer.inventory.currentItem;
      }
      else {
        //just dont use offhand, ignore it for now. is easier
        playerSlot = 40;
      }
      TileEntity tile = worldObj.getTileEntity(pos);
      if (tile instanceof TileEntityFurnace) {
        TileEntityFurnace furnace = (TileEntityFurnace) tile;
        if (held.isEmpty()) {
          UtilFurnace.extractFurnaceOutput(furnace, entityPlayer);
        }
        else {
          //holding a non null stack for sure
          //ALSO tools are smeltable now in new 1.11, but we skip that eh
          if (UtilFurnace.canBeSmelted(held) && (held.getItem() instanceof ItemTool) == false) {
            UtilFurnace.tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, UtilFurnace.SLOT_INPUT);
          }
          else if (UtilFurnace.isFuel(held)) {
            UtilFurnace.tryMergeStackIntoSlot(furnace, entityPlayer, playerSlot, UtilFurnace.SLOT_FUEL);
          }
        }
      }
    }
    if (easyEnderChest) {
      if (!held.isEmpty() && held.getItem() == Item.getItemFromBlock(Blocks.ENDER_CHEST)) {
        entityPlayer.displayGUIChest(entityPlayer.getInventoryEnderChest());
      }
    }
    if (editableSigns) {
      if (pos == null) { return; }
      TileEntity tile = worldObj.getTileEntity(pos);
      if (held.isEmpty() && tile instanceof TileEntitySign) {
        TileEntitySign sign = (TileEntitySign) tile;
        if (worldObj.isRemote == true) {//this method has    @SideOnly(Side.CLIENT) flag
          sign.setEditable(true);
        }
        sign.setPlayer(entityPlayer);
        //entityPlayer.openEditSign(sign);//NOPE: this does not cause server sync, must go through network with mod instance
        event.getEntityPlayer().openGui(ModCyclic.instance, ForgeGuiHandler.VANILLA_SIGN, event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
        
      }
    }
  }
  private final static EntityEquipmentSlot[] armorStandEquipment = {
      EntityEquipmentSlot.OFFHAND, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
  @SubscribeEvent
  public void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
    if (armorStandSwap) {
      //added for https://www.twitch.tv/darkphan
      if (event.getWorld().isRemote) { return; } //server side only
      if (event.getTarget() == null || event.getTarget() instanceof EntityArmorStand == false) { return; }
      EntityArmorStand entityStand = (EntityArmorStand) event.getTarget();
      EntityPlayer player = event.getEntityPlayer();
      if (player.isSneaking() == false) { return; } //bc when not sneaking, we do the normal single item version
      event.setCanceled(true);//which means we need to now cancel that normal version and do our own
      for (EntityEquipmentSlot slot : armorStandEquipment) {
        ItemStack itemPlayer = player.getItemStackFromSlot(slot);
        ItemStack itemArmorstand = entityStand.getItemStackFromSlot(slot);
        player.setItemStackToSlot(slot, itemArmorstand);
        entityStand.setItemStackToSlot(slot, itemPlayer);
      }
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.player;
    nameVillagerTag = config.getBoolean("Villager Nametag", category, true,
        "Let players name villagers with nametags");
    stardewFurnace = config.getBoolean("Furnace Speed", category, true,
        "Stardew Furnaces: Quickly fill a furnace by hitting it with fuel or an item, or interact with an empty hand to pull out the results [Inspired by Stardew Valley.  Left click only]");
    passThroughClick = config.getBoolean("Pass-Through Click", category, true,
        "Open chests (and other containers) by passing right through the attached signs, banners, and item frames");
    easyEnderChest = config.getBoolean("Easy Enderchest", category, true,
        "Open ender chest without placing it down, just attack with it");
    fastLadderClimb = config.getBoolean("Faster Ladders", category, true,
        "Allows you to quickly climb ladders by looking up instead of moving forward");
    config.addCustomCategoryComment(category, "Player Abilities and interactions");
    editableSigns = config.getBoolean("Editable Signs", category, true, "Allow editing signs with an empty hand by punching it (left click)");
    signSkullName = config.getBoolean("Name Player Skulls with Sign", category, true,
        "Use a player skull on a sign to name the skull based on the top line");
    category = Const.ConfigCategory.blocks;
    armorStandSwap = config.getBoolean("ArmorStandSwap", category, true,
        "Swap armor with a stand whenever you interact while sneaking");
  }
}
