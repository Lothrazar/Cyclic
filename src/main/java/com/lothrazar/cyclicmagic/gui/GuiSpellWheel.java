package com.lothrazar.cyclicmagic.gui;
 
import com.lothrazar.cyclicmagic.gui.button.ButtonClose;
import com.lothrazar.cyclicmagic.gui.button.ButtonSpellToggle;
import com.lothrazar.cyclicmagic.gui.button.ITooltipButton;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilSpellCaster;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSpellWheel extends GuiScreen{

	private final EntityPlayer thePlayer;
	private final static ResourceLocation background = new ResourceLocation(Const.MODID, "textures/gui/spellbook.png");
	private final static ResourceLocation ptr = new ResourceLocation(Const.MODID, "textures/spells/mouseptr.png");

	// https://github.com/LothrazarMinecraftMods/EnderBook/blob/66363b544fe103d6abf9bcf73f7a4051745ee982/src/main/java/com/lothrazar/enderbook/GuiEnderBook.java
	private int xCenter;
	private int yCenter;
	private int radius;
	private double arc;
	final int textureWidth = 200;
	final int textureHeight = 180;
	final int h = 20;// all btn height

	public GuiSpellWheel(EntityPlayer p){

		super();
		this.thePlayer = p;
	}

	@Override
	public void initGui(){

		super.initGui();	

		ItemStack wand = UtilSpellCaster.getPlayerWandIfHeld(thePlayer);
		
		
		//List<Integer> spellbook = ;// ItemCyclicWand.Variant.getSpellsFromVariant(ItemCyclicWand.Variant.getVariantFromMeta(wand));
		

		xCenter = this.width / 2;
		yCenter = this.height / 2;
		radius = xCenter / 3 + 8;//was 26

		arc = (2 * Math.PI) / SpellRegistry.getSpellbook(wand).size();
		int btnCenter = yCenter - h / 2;
		int btnID = 999;
		this.buttonList.add(new ButtonClose(btnID++, xCenter - 15, btnCenter));

		double ang = 0;
		double cx, cy;

		ang = 0;
		ButtonSpellToggle b;
		//from here on, btnID++ is not used; the spell id is instead used as the id
		
		for(ISpell s : SpellRegistry.getSpellbook(wand)){
			/*
			boolean unlocked = ItemCyclicWand.Spells.isSpellUnlocked(props.getPlayer().getHeldItem(), s);
			if(!unlocked){
				continue;
			}*/
			cx = xCenter + radius * Math.cos(ang) - 2;
			cy = yCenter + radius * Math.sin(ang) - 2;
			
			b = new ButtonSpellToggle(thePlayer, (int) cx, (int) cy, s);
			this.buttonList.add(b);

			ang += arc;
		}
	}

	@Override
	public void drawBackground(int tint){

		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

		int screenWidth = res.getScaledWidth();
		int screenHeight = res.getScaledHeight();

		int guiLeft = screenWidth / 2 - textureWidth / 2;
		int guiTop = screenHeight / 2 - textureHeight / 2;

		UtilTextureRender.drawTextureSimple(background, guiLeft, guiTop, 200, 200);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){

		// this.drawBackground(1); //turn this on if we want 'background' on
		super.drawScreen(mouseX, mouseY, partialTicks);
		int spellSize = 16;
		UtilTextureRender.drawTextureSquare(ptr, mouseX - 8, mouseY - 8, spellSize);
		
		/*
		List<Integer> spellbook = ItemCyclicWand.Variant.getSpellsFromVariant(ItemCyclicWand.Variant.getVariantFromMeta(thePlayer.getHeldItem()));
		
		
		double ang = 0;
		double cx, cy;


		ISpell s;
		for(int sp : spellbook){
			s = SpellRegistry.getSpellFromID(sp); 

			cx = xCenter + radius * Math.cos(ang);
			cy = yCenter + radius * Math.sin(ang);

			boolean unlocked = ItemCyclicWand.Spells.isSpellUnlocked(props.getPlayer().getHeldItem(), s);
			if(!unlocked){
				continue;
			}

			ResourceLocation header;
			if(unlocked){
				header = s.getIconDisplayHeaderEnabled();
			}
			else{
				header = s.getIconDisplayHeaderDisabled();
			}

			// TODO: maybe a special header for this guy??
			// if(s.getID() == SpellRegistry.inventory.getID())
			UtilTextureRender.drawTextureSimple(header, (int) cx, (int) cy - 8, spellSize - 4, spellSize - 4);

			ang += arc;
		}
*/
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
	public boolean doesGuiPauseGame(){

		return false;
	}
}
