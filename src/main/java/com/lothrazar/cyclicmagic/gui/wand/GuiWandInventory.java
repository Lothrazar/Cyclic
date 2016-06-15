package com.lothrazar.cyclicmagic.gui.wand;

import org.lwjgl.opengl.GL11;

import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiWandInventory extends GuiContainer {

	private final InventoryWand						inventory;
	
	@SuppressWarnings("unused")
	private final ItemStack							internalWand;
	// 176x156
	private static final ResourceLocation	BACKGROUND	= new ResourceLocation(Const.MODID, "textures/gui/inventory_wand.png");
 
	// TODO: the swap type tooltop, if its on pattern, should show the current
	// slot number, as i '3/9'
	int				id				= 777;
	final int		padding			= 4;
	ContainerWand container;

	public GuiWandInventory(ContainerWand containerItem, ItemStack wand) {

		super(containerItem);
		this.inventory = containerItem.inventory;
		this.container = containerItem;
		this.internalWand = wand;
	}

	@Override
	public void initGui() {

		super.initGui();

		int y = this.guiTop + padding;
		int x = this.guiLeft + 5;

		int width = 20;

//		id++;
//		x += width + padding;
		width = 50;
		ButtonBuildToggle btn = new ButtonBuildToggle(inventory.getPlayer(), id, x, y, width);
		this.buttonList.add(btn);

//		id++;
//		x += width + padding + 8;
		// y += 10;

//		int size = ItemCyclicWand.BuildType.getBuildSize(internalWand);
//		if (size <= 0) {
//			size = 1;
//		}
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
		
		

		int guiLeft = (this.width - 176) / 2;
		int guiTop = (this.height - 166) / 2;

		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		GlStateManager.disableLighting();
		int active = ItemCyclicWand.BuildType.getSlot(this.internalWand);
		for(Slot s : this.container.inventorySlots) {
//				ItemStack stack = s.getStack();
			
			if(active == s.getSlotIndex()){
				String test = (s==null||s.getStack()==null)?"null":s.getStack().getUnlocalizedName();
				
				
				
				System.out.println("wand inventory test "+test);
			 	Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Const.MODID,"textures/gui/slot_current.png"));
				this.drawTexturedModalRect(guiLeft + s.xDisplayPosition, guiTop + s.yDisplayPosition, 0, 0, 16, 16);
				 
				break;
			
			}
		}
		GlStateManager.popMatrix();

		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	@Override
	public void onGuiClosed() {
	   
		inventory.closeInventory(inventory.getPlayer());
		
		super.onGuiClosed();
    }
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);

		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}
