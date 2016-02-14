package com.lothrazar.cyclicmagic.gui.button;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.PlayerPowerups;
import com.lothrazar.cyclicmagic.SpellRegistry;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.net.MessageToggleSpell;
import com.lothrazar.cyclicmagic.spell.ISpell;
import com.lothrazar.cyclicmagic.util.UtilString;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ButtonSpell extends GuiButton implements ITooltipButton{

	final EntityPlayer thePlayer;
	private ISpell spell;
	private static final int btnSize = 16;

	public ButtonSpell(EntityPlayer player,int x, int y, ISpell s){

		super(s.getID(), x, y, btnSize, btnSize, "");
		spell = s;
		thePlayer = player;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){

		boolean pressed = super.mousePressed(mc, mouseX, mouseY);

		if(pressed){
			// button id matches spell id
			ItemStack wand = Minecraft.getMinecraft().thePlayer.getHeldItem();

			if(wand == null || wand.getItem() instanceof ItemCyclicWand == false){
				return pressed;
			}

			if(this.id == ItemCyclicWand.Spells.getSpellCurrent(wand)){
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("spell.locked.current"));
				return pressed;// cannot toggle current spell
			}

			if(this.id == SpellRegistry.Spells.inventory.getID() && ItemCyclicWand.Spells.isSpellUnlocked(Minecraft.getMinecraft().thePlayer.getHeldItem(), this.id)){
				// spell IS unlocked already, do not let player disable it
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("spell.locked.inventory"));
			}
			else{
				ModMain.network.sendToServer(new MessageToggleSpell(this.id));
			}
		}

		return pressed;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY){

		// override this and draw the texture here, so the vanilla grey square
		// doesnt show up
		if(this.visible){
			// http://www.minecraftforge.net/forum/index.php?topic=19594.0

			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);

			UtilTextureRender.drawTextureSquare(spell.getIconDisplay(), this.xPosition, this.yPosition, btnSize);

			this.mouseDragged(mc, mouseX, mouseY);
		}
	}

	public List<String> getTooltips(){

		PlayerPowerups props = PlayerPowerups.get(thePlayer);
		List<String> tooltips = new ArrayList<String>();
		tooltips.add(EnumChatFormatting.LIGHT_PURPLE + spell.getName());
		tooltips.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("spell.meta.cost") + EnumChatFormatting.BLUE + spell.getCost());

		// tooltips.add(EnumChatFormatting.LIGHT_PURPLE + spell.getInfo());

		tooltips.addAll(UtilString.splitIntoLine(spell.getInfo(), 28));

		boolean unlocked = ItemCyclicWand.Spells.isSpellUnlocked(props.getPlayer().getHeldItem(), spell);

		String ed = unlocked ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("spell.meta.enabled") : EnumChatFormatting.RED + StatCollector.translateToLocal("spell.meta.disabled");
		tooltips.add(ed);
		return tooltips;
	}
}
