package com.lothrazar.cyclicmagic.gui.wand;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.PacketSpellBuildSize;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiWandInventory extends GuiContainer {

	private final InventoryWand						inventory;
	private final ItemStack								internalWand;
	// 176x156
	private static final ResourceLocation	BACKGROUND	= new ResourceLocation(Const.MODID, "textures/gui/inventory_wand.png");

	// TODO: the swap type tooltop, if its on pattern, should show the current
	// slot number, as i '3/9'
	int																		id					= 777;
	final int															padding			= 4;

	GuiTextField													buildSize;

	public GuiWandInventory(ContainerWand containerItem, ItemStack wand) {

		super(containerItem);
		this.inventory = containerItem.inventory;
		this.internalWand = wand;
	}

	@Override
	public void initGui() {

		super.initGui();

		int y = this.guiTop + padding;
		int x = this.guiLeft + 5;

		int width = 20;

		this.buttonList.add(new ButtonSpellCircle(id, x, y, width));


		id++;
		x += width + padding;
		width = 50;
		ButtonBuildToggle btn = new ButtonBuildToggle(inventory.getPlayer(), id, x, y, width);
		this.buttonList.add(btn);

		id++;
		x += width + padding + 8;
		// y += 10;

		int size = ItemCyclicWand.BuildType.getBuildSize(internalWand);
		if (size <= 0) {
			size = 1;
		}

		buildSize = new GuiTextField(id, this.fontRendererObj, x, y, 30, 20);
		buildSize.setMaxStringLength(2);
		buildSize.setText("" + size);
		 
		System.out.println("todo determine build size visible");
		//buildSize.setVisible(internalWand.getItem() != ItemRegistry.ModItems.cyclic_wand_range);
		
		buildSize.setFocused(true);

	}

	@Override
	public void onGuiClosed() {

		int size = 1;

		try {
			size = Integer.parseInt(buildSize.getText());
		} catch (Exception e) {

			return;// if its not an integer, then do notsave`
		}

		if (size > 16) {
			size = 16;
		}

		ModMain.network.sendToServer(new PacketSpellBuildSize(size));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		super.drawScreen(mouseX, mouseY, partialTicks);

		if (buildSize != null) {
			buildSize.drawTextBox();
		}

		ITooltipButton btn;
		for (int i = 0; i < buttonList.size(); i++) {
			if (buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton) {
				btn = (ITooltipButton) buttonList.get(i);

				drawHoveringText(btn.getTooltips(), mouseX, mouseY, fontRendererObj);
				break;// cant hover on 2 at once
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);

		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (buildSize != null) {
			buildSize.updateCursorCounter();
		}
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);
		if (buildSize != null) {
			buildSize.textboxKeyTyped(par1, par2);
		}
	}

	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		if (buildSize != null) {
			buildSize.mouseClicked(x, y, btn);
		}
	}
}
