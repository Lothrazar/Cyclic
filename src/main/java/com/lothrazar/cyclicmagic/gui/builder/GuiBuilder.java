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
	private static final int texture_width = 176;
	private static final int texture_height = 166;
	static final int padding = 4;
	private TileEntityBuilder tile;
	private ButtonBuilderType btn;
	private ButtonBuildSize btnSizeUp;
	private ButtonBuildSize btnSizeDown;
	private int xSizeTextbox;
	private int ySizeTxtbox;
	private int xHeightTextbox;
	private int yHeightTxtbox;
	boolean debugLabels = false;
	public GuiBuilder(InventoryPlayer inventoryPlayer, TileEntityBuilder tileEntity) {
		super(new ContainerBuilder(inventoryPlayer, tileEntity));
		tile = tileEntity;
	}
	public GuiBuilder(Container c) {
		super(c);
	}
	@Override
	public void initGui() {
		super.initGui();
		int width = 50;
		int id = 2;
		btn = new ButtonBuilderType(tile.getPos(), id++, this.guiLeft + padding, this.guiTop + padding, width);
		this.buttonList.add(btn);
		width = 15;
		xSizeTextbox = texture_width - 20;
		btnSizeUp = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xSizeTextbox, this.guiTop + padding, width, true,"size");
		this.buttonList.add(btnSizeUp);
		btnSizeDown = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xSizeTextbox, this.guiTop + padding + 20, width, false,"size");
		this.buttonList.add(btnSizeDown);
		xSizeTextbox += width / 2 - 2;
		ySizeTxtbox = padding + 12;
		
		

		xHeightTextbox = texture_width - 60;
		btnSizeUp = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xHeightTextbox, this.guiTop + padding, width, true,"height");
		this.buttonList.add(btnSizeUp);
		btnSizeDown = new ButtonBuildSize(tile.getPos(), id++, this.guiLeft + xHeightTextbox, this.guiTop + padding + 20, width, false,"height");
		this.buttonList.add(btnSizeDown);
		xHeightTextbox += width / 2 - 2;
		yHeightTxtbox = padding + 12;
		
		
	}
	@SideOnly(Side.CLIENT)
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.btn.displayString = UtilChat.lang("buildertype." + this.tile.getBuildTypeEnum().name().toLowerCase() + ".name");
		if (debugLabels) {
			this.fontRendererObj.drawString("t = " + this.tile.getTimer(), 32, this.ySize - 94, 4210752);
			this.fontRendererObj.drawString("b = " + this.tile.getBuildType(), 38, this.ySize - 104, 4210752);
			this.fontRendererObj.drawString("speed = " + this.tile.getSpeed(), 38, this.ySize - 114, 4210752);
			this.fontRendererObj.drawString("size = " + this.tile.getSize(), 38, this.ySize - 124, 4210752);
		}
		if (this.tile.getSize() > 0){
			String display = "" + this.tile.getSize();
			//move it over if more than 1 digit
			int x = (display.length() > 1) ? xSizeTextbox - 3 : xSizeTextbox;
			this.fontRendererObj.drawString(display, x, ySizeTxtbox, 4210752);
		}
		if (this.tile.getHeight() > 0){
			String display = "" + this.tile.getSize();
			//move it over if more than 1 digit
			int x = (display.length() > 1) ? xHeightTextbox - 3 : xHeightTextbox;
			this.fontRendererObj.drawString(display, x, yHeightTxtbox, 4210752);
		}
		 
		this.btnSizeDown.enabled = (this.tile.getSize() > 1);
		this.btnSizeUp.enabled = (this.tile.getSize() < TileEntityBuilder.maxSize);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(table);
		int thisX = (this.width - this.xSize) / 2;
		int thisY = (this.height - this.ySize) / 2;
		int u = 0, v = 0;
		Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, texture_width, texture_height);
		this.mc.getTextureManager().bindTexture(slot);
		for (int k = 0; k < this.tile.getSizeInventory(); k++) { // x had - 3 ??
			Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerBuilder.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerBuilder.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
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
