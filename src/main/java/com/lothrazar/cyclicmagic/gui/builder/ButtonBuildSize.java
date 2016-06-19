package com.lothrazar.cyclicmagic.gui.builder;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketTileBuildSize;
import com.lothrazar.cyclicmagic.net.PacketTileBuildType;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuildSize extends GuiButton implements ITooltipButton {
	private final BlockPos tilePos;
	private final List<String> tooltips = new ArrayList<String>();
	boolean goUp;
	public ButtonBuildSize(BlockPos current, int buttonId, int x, int y, int width,boolean up) {
		super(buttonId, x, y, width, 10, "");
		tilePos = current;
		tooltips.add(TextFormatting.GRAY + UtilChat.lang("button.speed.meta"));
		goUp = up;
	}
	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);
		if (pressed) {
			int size = (goUp) ? 1 : -1;
			ModMain.network.sendToServer(new PacketTileBuildSize(tilePos,size));
		}
		return pressed;
	}
	@Override
	public List<String> getTooltips() {
		return tooltips;
	}
}
