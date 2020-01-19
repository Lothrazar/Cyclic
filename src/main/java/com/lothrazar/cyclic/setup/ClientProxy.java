package com.lothrazar.cyclic.setup;

import com.lothrazar.cyclic.CyclicRegistry;
import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy {

  @Override
  public void init() {
    //TODO: clean this up ? maybe
    for (BlockBase b : CyclicRegistry.Blocks.blocks) {
      b.registerClient();
    }
  }

  @Override
  public World getClientWorld() {
    return Minecraft.getInstance().world;
  }

  @Override
  public PlayerEntity getClientPlayer() {
    return Minecraft.getInstance().player;
  }

  @Override
  public void setPlayerReach(PlayerEntity player, int currentReach) {
    player.getAttribute(PlayerEntity.REACH_DISTANCE).setBaseValue(currentReach);
    //    super.setPlayerReach(player, currentReach);
    //    Minecraft mc = Minecraft.getMinecraft();
    //    try {
    //      if (player == mc.player) {
    //        if (mc.playerController instanceof ReachPlayerController) {
    //          ((ReachPlayerController) mc.playerController).setReachDistance(currentReach);
    //        }
    //        else {
    //          NetHandlerPlayClient netHandler = ReflectionHelper.getPrivateValue(PlayerControllerMP.class, mc.playerController, NET_CLIENT_HANDLER);
    //          //copy values from existing controller to custom one. since there is no setReachDistance in vanilla
    //          ReachPlayerController controller = new ReachPlayerController(mc, netHandler);
    //          controller.setGameType(mc.playerController.getCurrentGameType());
    //          player.capabilities.isFlying = player.capabilities.isFlying;
    //          player.capabilities.allowFlying = player.capabilities.allowFlying;
    //          mc.playerController = controller;
    //          controller.setReachDistance(currentReach);
    //        }
    //      }
    //    }
    //    catch (Exception e) {
    //      //sometimes it crashes just AS the world is loading, but then it works after everythings set up
    //      //does not affect functionality, its working before the player can ever make use of this.
    //      ModCyclic.logger.error("Error setting reach : ", e);
    //    }
  }
}
