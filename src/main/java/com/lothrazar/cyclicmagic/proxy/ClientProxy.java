package com.lothrazar.cyclicmagic.proxy;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.entity.EntityGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecart;
import com.lothrazar.cyclicmagic.entity.EntityMinecartTurret;
import com.lothrazar.cyclicmagic.entity.EntityStoneMinecart;
import com.lothrazar.cyclicmagic.entity.RenderCyclicMinecart;
import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamiteBlockSafe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamiteMining;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetEmpty;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetFull;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;
import com.lothrazar.cyclicmagic.entity.projectile.RenderProjectile;
import com.lothrazar.cyclicmagic.module.KeyInventoryShiftModule;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
  public static KeyBinding keyShiftUp;
  public static KeyBinding keyShiftDown;
  public static KeyBinding keyBarUp;
  public static KeyBinding keyBarDown;
  public static KeyBinding keyExtraInvo;
  public static KeyBinding keyExtraCraftin;
  static final String keyCategoryInventory = "key.categories.inventorycontrol";
  @Override
  public void preInit() {
    //in 1.11 we need entities in preinit apparently..??http://www.minecraftforge.net/forum/topic/53954-1112-solved-renderingregistryregisterentityrenderinghandler-not-registering/
    registerEntities();
  }
  @Override
  public void init() {
    registerModels();
    registerKeys();
  }
  @Override
  public World getClientWorld() {
    return FMLClientHandler.instance().getClient().world;
  }
  @Override
  public EntityPlayer getClientPlayer() {
    return Minecraft.getMinecraft().player;
  }
  private void registerKeys() {
    if (KeyInventoryShiftModule.enableInvoKeys) {
      keyShiftUp = new KeyBinding("key.columnshiftup", Keyboard.KEY_Y, keyCategoryInventory);
      ClientRegistry.registerKeyBinding(ClientProxy.keyShiftUp);
      keyShiftDown = new KeyBinding("key.columnshiftdown", Keyboard.KEY_H, keyCategoryInventory);
      ClientRegistry.registerKeyBinding(ClientProxy.keyShiftDown);
      keyBarUp = new KeyBinding("key.columnbarup", Keyboard.KEY_LBRACKET, keyCategoryInventory);
      ClientRegistry.registerKeyBinding(ClientProxy.keyBarUp);
      keyBarDown = new KeyBinding("key.columnbardown", Keyboard.KEY_RBRACKET, keyCategoryInventory);
      ClientRegistry.registerKeyBinding(ClientProxy.keyBarDown);
      keyExtraInvo = new KeyBinding("key.keyExtraInvo", Keyboard.KEY_R, keyCategoryInventory);
      ClientRegistry.registerKeyBinding(ClientProxy.keyExtraInvo);
      keyExtraCraftin = new KeyBinding("key.keyExtraCraftin", Keyboard.KEY_O, keyCategoryInventory);
      ClientRegistry.registerKeyBinding(ClientProxy.keyExtraCraftin);
    }
  }
  private void registerEntities() {
    //minecarts
    //http://wiki.mcjty.eu/modding/index.php/Mobs-1.9
    RenderingRegistry.registerEntityRenderingHandler(EntityGoldMinecart.class, RenderCyclicMinecart.FACTORY_GOLD);
    RenderingRegistry.registerEntityRenderingHandler(EntityGoldFurnaceMinecart.class, RenderCyclicMinecart.FACTORY_GOLD_FURNACE);
    RenderingRegistry.registerEntityRenderingHandler(EntityStoneMinecart.class, RenderCyclicMinecart.FACTORY_STONE_FURNACE);
    RenderingRegistry.registerEntityRenderingHandler(EntityMinecartTurret.class, RenderCyclicMinecart.FACTORY_TURRET);
    //the projectiles too
    RenderingRegistry.registerEntityRenderingHandler(EntityLightningballBolt.class, RenderProjectile.FACTORY_LIGHTNING);
    RenderingRegistry.registerEntityRenderingHandler(EntityHarvestBolt.class, RenderProjectile.FACTORY_HARVEST);
    RenderingRegistry.registerEntityRenderingHandler(EntityWaterBolt.class, RenderProjectile.FACTORY_WATER);
    RenderingRegistry.registerEntityRenderingHandler(EntitySnowballBolt.class, RenderProjectile.FACTORY_SNOW);
    RenderingRegistry.registerEntityRenderingHandler(EntityTorchBolt.class, RenderProjectile.FACTORY_TORCH);
    RenderingRegistry.registerEntityRenderingHandler(EntityFishingBolt.class, RenderProjectile.FACTORY_FISH);
    RenderingRegistry.registerEntityRenderingHandler(EntityShearingBolt.class, RenderProjectile.FACTORY_SHEAR);
    RenderingRegistry.registerEntityRenderingHandler(EntityDungeonEye.class, RenderProjectile.FACTORY_DUNG);
    RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, RenderProjectile.FACTORY_DYN);
    RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, RenderProjectile.FACTORY_FIRE);
    RenderingRegistry.registerEntityRenderingHandler(EntityDynamiteMining.class, RenderProjectile.FACTORY_DYNMINING);
    RenderingRegistry.registerEntityRenderingHandler(EntityDynamiteBlockSafe.class, RenderProjectile.FACTORY_DYNSAVE);
    RenderingRegistry.registerEntityRenderingHandler(EntityMagicNetFull.class, RenderProjectile.FACTORY_BALL);
    RenderingRegistry.registerEntityRenderingHandler(EntityMagicNetEmpty.class, RenderProjectile.FACTORY_BALLEMPTY);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public EnumFacing getSideMouseover(int max) {
    RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
    // now get whatever block position we are mousing over if anything
    if (mouseOver != null) {
      // Get the block position and make sure it is a block
      return mouseOver.sideHit;
    }
    return null;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockPos getBlockMouseoverSingle() {
    RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
    if (mouseOver == null) { return null; }
    return mouseOver.getBlockPos();
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockPos getBlockMouseoverExact(int max) {
    // Get the player and their held item
    RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
    // now get whatever block position we are mousing over if anything
    if (mouseOver != null) {
      // Get the block position and make sure it is a block
      return mouseOver.getBlockPos();
    }
    return null;
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockPos getBlockMouseoverOffset(int max) {
    // Get the player and their held item
    EntityPlayerSP player = (EntityPlayerSP) getClientPlayer();
    // int max = 50;
    RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
    // now get whatever block position we are mousing over if anything
    if (mouseOver != null && mouseOver.sideHit != null) {
      // Get the block position and make sure it is a block
      // World world = player.worldObj;
      BlockPos blockPos = mouseOver.getBlockPos();
      if (blockPos != null && player != null && player.getEntityWorld().getBlockState(blockPos) != null
          && player.getEntityWorld().isAirBlock(blockPos) == false) { return blockPos.offset(mouseOver.sideHit); }
    }
    return null;
  }
  private void registerModels() {
    // with help from
    // http://www.minecraftforge.net/forum/index.php?topic=32492.0
    // https://github.com/TheOnlySilverClaw/Birdmod/blob/master/src/main/java/silverclaw/birds/client/ClientProxyBirds.java
    // More info on proxy rendering
    // http://www.minecraftforge.net/forum/index.php?topic=27684.0
    // http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod
    ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    String name;
    Item item;
    for (Block b : BlockRegistry.blocks) {
      item = Item.getItemFromBlock(b);
      name = Const.MODRES + b.getUnlocalizedName().replaceAll("tile.", "");
      mesher.register(item, 0, new ModelResourceLocation(name, "inventory"));
      if (b instanceof IBlockHasTESR) {
        ((IBlockHasTESR) b).initModel();
      }
    }
    for (String key : ItemRegistry.itemMap.keySet()) {
      item = ItemRegistry.itemMap.get(key);
      name = Const.MODRES + item.getUnlocalizedName().replaceAll("item.", "");
      mesher.register(item, 0, new ModelResourceLocation(name, "inventory"));
    }
  }
  @SideOnly(Side.CLIENT)
  public void setClientPlayerData(MessageContext ctx, NBTTagCompound tags) {
    EntityPlayer p = this.getPlayerEntity(ctx); //Minecraft.getMinecraft().thePlayer;
    if (p != null) {
      IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(getClientPlayer());
      if (props != null) {
        props.setDataFromNBT(tags);
      }
    }
  }
  //https://github.com/coolAlias/Tutorial-Demo/blob/e8fa9c94949e0b1659dc0a711674074f8752d80e/src/main/java/tutorial/ClientProxy.java
  @Override
  public IThreadListener getThreadFromContext(MessageContext ctx) {
    return (ctx.side.isClient() ? Minecraft.getMinecraft() : super.getThreadFromContext(ctx));
  }
  @Override
  public EntityPlayer getPlayerEntity(MessageContext ctx) {
    // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
    // your packets will not work as expected because you will be getting a
    // client player even when you are on the server!
    // Sounds absurd, but it's true.
    //https://github.com/coolAlias/Tutorial-Demo/blob/e8fa9c94949e0b1659dc0a711674074f8752d80e/src/main/java/tutorial/ClientProxy.java
    // Solution is to double-check side before returning the player:
    return (ctx.side.isClient() ? getClientPlayer() : super.getPlayerEntity(ctx));
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void renderItemOnScreen(ItemStack current, int x, int y) {
    if (current == null) { return; }
    RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    GlStateManager.color(1, 1, 1, 1);
    RenderHelper.enableStandardItemLighting();
    RenderHelper.enableGUIStandardItemLighting();
    itemRender.renderItemAndEffectIntoGUI(current, x, y);
    RenderHelper.disableStandardItemLighting();
  }
  /**
   * In a GUI we already have the context of the itemrender and font
   */
  public void renderItemOnGui(ItemStack stack, RenderItem itemRender, FontRenderer fontRendererObj, int x, int y) {
    if (stack == null) { return; }
    itemRender.renderItemAndEffectIntoGUI(stack, x, y);
    itemRender.renderItemOverlays(fontRendererObj, stack, x, y);
  }
  public static final String[] NET_CLIENT_HANDLER = new String[] { "connection", "field_78774_b" };//was field_78774_b
  public static final String[] CURRENT_GAME_TYPE = new String[] { "currentGameType", "field_78779_k" };//was field_78779_k
  /**
   * INSPIRED by universallp
   * 
   * This function was is part of VanillaAutomation which is licenced under the
   * MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/
   * github.com/UniversalLP/VanillaAutomation
   */
  public void setPlayerReach(EntityPlayer player, int currentReach) {
    super.setPlayerReach(player, currentReach);
    Minecraft mc = Minecraft.getMinecraft();
    try {
      if (player == mc.player && !(mc.playerController instanceof ReachPlayerController)) {
        GameType type = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, CURRENT_GAME_TYPE);
        NetHandlerPlayClient netHandler = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, NET_CLIENT_HANDLER);
        ReachPlayerController controller = new ReachPlayerController(mc, netHandler);
        boolean isFlying = player.capabilities.isFlying;
        boolean allowFlying = player.capabilities.allowFlying;
        controller.setGameType(type);
        player.capabilities.isFlying = isFlying;
        player.capabilities.allowFlying = allowFlying;
        mc.playerController = controller;
      }
      ((ReachPlayerController) mc.playerController).setReachDistance(currentReach);
    }
    catch (Exception e) {
      //sometimes it crashes just AS the world is loading, but then it works after everythings set up
      //does not affect functionality, its working before the player can ever make use of this.
      ModCyclic.logger.info("Error setting reach : " + e);
    }
  }
  /**
   * From the open source project:/github.com/UniversalLP/VanillaAutomation who
   * in turn got it from from github.com/vazkii/Botania.
   */
  @SideOnly(Side.CLIENT)
  public class ReachPlayerController extends PlayerControllerMP {
    //in vanilla code, it has a getBlockReachDistance but there is no variable to reflect over
    //instead it just returns 5 or 4.5 hardcoded. thanks mojang...
    private float distance = 0F;
    public ReachPlayerController(Minecraft mcIn, NetHandlerPlayClient netHandler) {
      super(mcIn, netHandler);
    }
    @Override
    public float getBlockReachDistance() {
      return distance;
    }
    public void setReachDistance(float f) {
      distance = f;
    }
  }
}
