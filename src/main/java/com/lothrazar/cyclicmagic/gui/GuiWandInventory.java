package com.lothrazar.cyclicmagic.gui;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.gui.button.*;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiWandInventory extends GuiContainer{

	private final InventoryWand inventory;
	private final ItemStack internalWand;
	//176x156
	private static final ResourceLocation BACKGROUND = new ResourceLocation(Const.MODID, "textures/gui/inventory_wand.png");

	// TODO: the swap type tooltop, if its on pattern, should show the current slot number, as i '3/9'
	int id = 777;
	final int padding = 4;
	

	GuiTextField buildSize;

	public GuiWandInventory(ContainerWand containerItem, ItemStack wand){

		super(containerItem);
		this.inventory = containerItem.inventory;
		this.internalWand = wand;
	}

	@Override
	public void initGui(){

		super.initGui();

		int y = this.guiTop + padding  ;
		int x = this.guiLeft + 5;

		int width = 20;
		this.buttonList.add(new ButtonSpellCircle(id, x, y, width));

		id++;
		x += width + padding;
		this.buttonList.add(new ButtonRecharge(id, x, y, width));
		
		//Next row
		//x = this.guiLeft + 5;
		//y += 20 + padding;
	
		id++;
		x += width + padding;
		width = 50;
		this.buttonList.add(new ButtonBuildToggle(inventory.getPlayer(), id, x, y, width));
		
		
		
		id++;
		x += width + padding;
		y += 10;

		int size = ItemCyclicWand.BuildType.getBuildSize(internalWand);
		if(size <= 0){
			size = 1;
		}
		System.out.println("set visible based on spell:size="+size); 
		buildSize = new GuiTextField(id,this.fontRendererObj,
				x,y,30,20);
		buildSize.setMaxStringLength(2);
		buildSize.setText(""+size);//TODO: save this in data
		buildSize.setVisible(true);
		
		
		
		//buildSize.setVisible(isVisible);
	//	buildSize.setFocused(true);
		
		
		
		
		/*
		id++;
		x += width + padding;
		width = 50;
		this.buttonList.add(new ButtonPlaceToggle(inventory.getPlayer(), id, x, y, width));
		*/
	}

	@Override
    public void onGuiClosed(){

		System.out.println("save on closed="); 

		int size = 1;
	
		try{
			size = Integer.parseInt(buildSize.getText());
		}catch(Exception e){
			
			return;//if its not an integer, then do notsave`
		}
		
		ItemCyclicWand.BuildType.setBuildSize(internalWand,size);
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){

		super.drawScreen(mouseX, mouseY, partialTicks);

		ITooltipButton btn;
		for(int i = 0; i < buttonList.size(); i++){
			if(buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton){
				btn = (ITooltipButton) buttonList.get(i);

				drawHoveringText(btn.getTooltips(), mouseX, mouseY, fontRendererObj);
				break;// cant hover on 2 at once
			}
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY){

		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
	}

	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3){

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);

		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}
