package com.lothrazar.cyclicmagic.gui.builder;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.net.PacketTileBuildType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonBuilderType extends GuiButton implements ITooltipButton {
	private final BlockPos tilePos;
	public ButtonBuilderType(BlockPos current, int buttonId, int x, int y, int width) {
		super(buttonId, x, y, width, 20, I18n.format(""));
		tilePos = current;
	}
	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		boolean pressed = super.mousePressed(mc, mouseX, mouseY);
		if (pressed) {
			ModMain.network.sendToServer(new PacketTileBuildType(tilePos));
		}
		return pressed;
	}
	@Override
	public List<String> getTooltips() {
		List<String> tooltips = new ArrayList<String>();
		tooltips.add(TextFormatting.GRAY + I18n.format("button.builder.meta"));
		return tooltips;
	}
}
