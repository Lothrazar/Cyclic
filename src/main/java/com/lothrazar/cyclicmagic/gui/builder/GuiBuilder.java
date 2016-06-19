package com.lothrazar.cyclicmagic.gui.builder;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBuilder;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiBuilder extends GuiContainer {
	private static final String folder = "textures/gui/";
	private static final ResourceLocation table = new ResourceLocation(Const.MODID, folder + "table.png");
	private static final ResourceLocation slot = new ResourceLocation(Const.MODID, folder + "inventory_slot.png");
	private static final ResourceLocation progress = new ResourceLocation(Const.MODID, folder + "progress.png");
	private TileEntityBuilder tile;
	private ButtonBuilderType btn;
	public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityBuilder tileEntity) {
		super(new ContainerBuilder(inventoryPlayer, tileEntity));
		tile = tileEntity;
	}
	public GuiBuilder(Container c) {
		super(c);
	}
	static final int padding = 4;
	@Override
	public void initGui() {
		super.initGui();
		int y = this.guiTop + padding;
		int x = this.guiLeft + 5;
		int width = 20;
		width = 50;
		btn = new ButtonBuilderType(tile.getPos(), 2, x, y, width);
		this.buttonList.add(btn);
	}
	boolean debugLabels = true;
	@SideOnly(Side.CLIENT)
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.btn.displayString = UtilChat.lang("buildertype." + this.tile.getBuildTypeEnum().name().toLowerCase() + ".name");
		if (debugLabels) {
			this.fontRendererObj.drawString("t = " + this.tile.getTimer(), 32, this.ySize - 84, 4210752);
			this.fontRendererObj.drawString("b = " + this.tile.getBuildType(), 38, this.ySize - 74, 4210752);
			this.fontRendererObj.drawString("speed = " + this.tile.getSpeed(), 38, this.ySize - 64, 4210752);
			this.fontRendererObj.drawString("size = " + this.tile.getSize(), 38, this.ySize - 54, 4210752);
		}
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(table);
		int thisX = (this.width - this.xSize) / 2;
		int thisY = (this.height - this.ySize) / 2;
		int texture_width = 176;
		int texture_height = 166;
		int u = 0, v = 0;
		Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, texture_width, texture_height);
		this.mc.getTextureManager().bindTexture(slot);
		for (int k = 0; k < this.tile.getSizeInventory(); k++) {
			Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBuilder.SLOTX_START - 3 + k * Const.SQ, this.guiTop + ContainerBuilder.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
		}
		if (tile.getTimer() > 0 && tile.getStackInSlot(0) != null) {
			this.mc.getTextureManager().bindTexture(progress);
			float percent = ((float) tile.getTimer()) / ((float) TileEntityBuilder.TIMER_FULL);
			// maximum progress bar is 156, since the whole texture is 176 minus
			// 10 padding on each side
			int belowSlots = this.guiTop + 9 + 3 * Const.SQ;
			// Args: x, y, u, v, width, height, textureWidth, textureHeight
			Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + 10, belowSlots + 5, u, v, (int) (156 * percent), 7, 156, 7);
		}
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		ITooltipButton btn;
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton) {
				btn = (ITooltipButton) buttonList.get(i);
				drawHoveringText(btn.getTooltips(), mouseX, mouseY, fontRendererObj);
				break;// cant hover on 2 at once
			}
		}
	}
}
