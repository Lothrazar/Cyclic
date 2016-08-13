package com.lothrazar.cyclicmagic.event;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventScreenTodoCommand {
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onRenderTextOverlay(RenderGameOverlayEvent.Text event) {
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(Minecraft.getMinecraft().thePlayer);
    if (props != null && props.getTODO() != null && props.getTODO().length() > 0) {
      event.getRight().add(props.getTODO());
    }
  }
}
