package com.lothrazar.cyclicmagic.proxy;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.cablebundled.CableBundleRenderer;
import com.lothrazar.cyclicmagic.component.cablebundled.TileEntityCableBundle;
import com.lothrazar.cyclicmagic.component.cablefluid.CableFluidRenderer;
import com.lothrazar.cyclicmagic.component.cablefluid.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.component.cableitem.CableItemRenderer;
import com.lothrazar.cyclicmagic.component.cableitem.TileEntityItemCable;
import com.lothrazar.cyclicmagic.component.wandblaze.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.component.wandblaze.EntityBlazeBolt.FactoryFire;
import com.lothrazar.cyclicmagic.component.wandfishing.EntityFishingBolt;
import com.lothrazar.cyclicmagic.component.wandfishing.EntityFishingBolt.FactoryFish;
import com.lothrazar.cyclicmagic.component.wandice.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.component.wandice.EntitySnowballBolt.FactorySnow;
import com.lothrazar.cyclicmagic.component.wandlightning.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.component.wandlightning.EntityLightningballBolt.FactoryLightning;
import com.lothrazar.cyclicmagic.component.wandmissile.EntityHomingProjectile;
import com.lothrazar.cyclicmagic.component.wandmissile.EntityHomingProjectile.FactoryMissile;
import com.lothrazar.cyclicmagic.component.wandshears.EntityShearingBolt;
import com.lothrazar.cyclicmagic.component.wandshears.EntityShearingBolt.FactoryShear;
import com.lothrazar.cyclicmagic.component.wandspawner.EntityDungeonEye;
import com.lothrazar.cyclicmagic.component.wandspawner.EntityDungeonEye.FactoryDungeon;
import com.lothrazar.cyclicmagic.component.wandtorch.EntityTorchBolt;
import com.lothrazar.cyclicmagic.component.wandtorch.EntityTorchBolt.FactoryTorch;
import com.lothrazar.cyclicmagic.entity.EntityEnderEyeUnbreakable;
import com.lothrazar.cyclicmagic.entity.EntityGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecart;
import com.lothrazar.cyclicmagic.entity.EntityMinecartTurret;
import com.lothrazar.cyclicmagic.entity.EntityStoneMinecart;
import com.lothrazar.cyclicmagic.entity.RenderCyclicMinecart;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite.FactoryDyn;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamiteBlockSafe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamiteMining;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetEmpty;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetEmpty.FactoryBallEmpty;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetFull;
import com.lothrazar.cyclicmagic.entity.projectile.EntityMagicNetFull.FactoryBall;
import com.lothrazar.cyclicmagic.entity.projectile.RenderProjectile.FactoryDynMining;
import com.lothrazar.cyclicmagic.entity.projectile.RenderProjectile.FactoryDynSafe;
import com.lothrazar.cyclicmagic.module.KeyInventoryShiftModule;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
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
    // TODO: refactor cable
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFluidCable.class, new CableFluidRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityItemCable.class, new CableItemRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCableBundle.class, new CableBundleRenderer());
  }
  @Override
  public void init() {
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
    RenderingRegistry.registerEntityRenderingHandler(EntityLightningballBolt.class, new FactoryLightning());
    RenderingRegistry.registerEntityRenderingHandler(EntitySnowballBolt.class, new FactorySnow());
    RenderingRegistry.registerEntityRenderingHandler(EntityTorchBolt.class, new FactoryTorch());
    RenderingRegistry.registerEntityRenderingHandler(EntityFishingBolt.class, new FactoryFish());
    RenderingRegistry.registerEntityRenderingHandler(EntityShearingBolt.class, new FactoryShear());
    RenderingRegistry.registerEntityRenderingHandler(EntityDungeonEye.class, new FactoryDungeon());
    RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new FactoryDyn());
    RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new FactoryFire());
    RenderingRegistry.registerEntityRenderingHandler(EntityDynamiteMining.class, new FactoryDynMining());
    RenderingRegistry.registerEntityRenderingHandler(EntityDynamiteBlockSafe.class, new FactoryDynSafe());
    RenderingRegistry.registerEntityRenderingHandler(EntityMagicNetFull.class, new FactoryBall());
    RenderingRegistry.registerEntityRenderingHandler(EntityMagicNetEmpty.class, new FactoryBallEmpty());
    RenderingRegistry.registerEntityRenderingHandler(EntityHomingProjectile.class, new FactoryMissile());
    RenderingRegistry.registerEntityRenderingHandler(EntityEnderEyeUnbreakable.class, new EntityEnderEyeUnbreakable.FactoryMissile());
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
    if (mouseOver == null) {
      return null;
    }
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
          && player.getEntityWorld().isAirBlock(blockPos) == false) {
        return blockPos.offset(mouseOver.sideHit);
      }
    }
    return null;
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void setClientPlayerData(MessageContext ctx, NBTTagCompound tags) {
    EntityPlayer player = this.getPlayerEntity(ctx);
    if (player != null) {
      IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(getClientPlayer());
      if (props != null) {
        props.setDataFromNBT(tags);
        if (props.getMaxHealth() != 0 && props.getMaxHealth() > player.getMaxHealth()) {//it doesnt always need to do this somehow. i get 24>20 sometimes then 24>24 other tiems
          UtilEntity.setMaxHealth(player, props.getMaxHealth());
        }
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
    if (current == null) {
      return;
    }
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
    if (stack == null) {
      return;
    }
    itemRender.renderItemAndEffectIntoGUI(stack, x, y);
    itemRender.renderItemOverlays(fontRendererObj, stack, x, y);
  }
  @Override
  public void closeSpectatorGui() {
    try {
      Minecraft.getMinecraft().ingameGUI.getSpectatorGui().onSpectatorMenuClosed(null);
    }
    catch (Exception e) {
      ModCyclic.logger.error("Error trying to lock out Spectator GUI: ");
      ModCyclic.logger.error(e.getMessage());
    }
  }
  public static final String[] NET_CLIENT_HANDLER = new String[] { "connection", "field_78774_b" };//was field_78774_b
  public static final String[] CURRENT_GAME_TYPE = new String[] { "currentGameType", "field_78779_k" };//was field_78779_k
  /**
   * INSPIRED by universallp
   * 
   * This function was is part of VanillaAutomation which is licenced under the MOZILLA PUBLIC LICENCE 2.0 - mozilla.org/en-US/MPL/2.0/ github.com/UniversalLP/VanillaAutomation
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
      ModCyclic.logger.error("Error setting reach : " + e.getMessage());
    }
  }
  /**
   * From the open source project:/github.com/UniversalLP/VanillaAutomation who in turn got it from from github.com/vazkii/Botania.
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
