package com.lothrazar.samsmagic.util;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UtilTextureRender {
	@SideOnly(Side.CLIENT)
	public static void renderItemAt(ItemStack stack, int x, int y, int dim) {
		// first get texture from item stack
		IBakedModel iBakedModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(stack);
		TextureAtlasSprite textureAtlasSprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(iBakedModel.getTexture().getIconName());

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

		if (Minecraft.getMinecraft().currentScreen != null)
			Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x, y, textureAtlasSprite, dim, dim);
	}

	public static void drawTextureSimple(ResourceLocation res, int x, int y, int w, int h) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0F, 0F, w, h, w, h);
	}
	
	
	/*
	 //this shows how to draw items in a cycle on the screen
	  pattern is like
	  
	  
	  
	  
	      c
	    a   b
	  x       y



	@SideOnly(Side.CLIENT)
	private void drawSpell(RenderGameOverlayEvent.Text event)
	{ 
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer; 

		ISpell spell = SpellRegistry.getPlayerCurrentISpell(player);

		if(Minecraft.getMinecraft().gameSettings.showDebugInfo)
		{
			event.left.add(lang("key.spell."+spell.getSpellName()));
		}
		else
		{
			int ymain = 12;
			int dim = 12;
				
			int x = 12, y = 2;
			
			Item ptr = SpellRegistry.canPlayerCastAnything(player) ? ItemRegistry.exp_cost_dummy : ItemRegistry.exp_cost_empty_dummy;
			//spell.getIconDisplayHeader()
			renderItemAt(new ItemStack(ptr),x,y,dim);
				
			//int ysmall = ymain - 3;
			int xmain = 10;
			ymain = 14;
			if(spell.getIconDisplay() != null)
			{
				x = xmain; 
				y = ymain;
				dim = 16;
				renderItemAt(spell.getIconDisplay(),x,y,dim);
			}
			
			
			ISpell spellNext = spell.left();//SpellRegistry.getSpellFromType(spell.getSpellID().next());
			ISpell spellPrev = spell.right();//SpellRegistry.getSpellFromType(spell.getSpellID().prev());
			
			
			if(spellNext != null)// && spellNext.getIconDisplay() != null
			{
				x = xmain-3; 
				y = ymain + 16;
				dim = 16/2;
				renderItemAt(spellNext.getIconDisplay(),x,y,dim);
				
				ISpell sLeftLeft = spellNext.left();//SpellRegistry.getSpellFromType(spellNext.getSpellID().next());

				if(sLeftLeft != null && sLeftLeft.getIconDisplay() != null)
				{
					x = xmain-3 - 1; 
					y = ymain + 16+14;
					dim = 16/2 - 2;
					renderItemAt(sLeftLeft.getIconDisplay(),x,y,dim);
					
					ISpell another = sLeftLeft.left();
					if(another != null)
					{
						x = xmain-3 - 3; 
						y = ymain + 16+14+10;
						dim = 16/2 - 4;
						renderItemAt(another.getIconDisplay(),x,y,dim);
					}
				}
			}
			if(spellPrev != null)// && spellPrev.getIconDisplay() != null
			{
				x = xmain+6; 
				y = ymain + 16;
				dim = 16/2;
				renderItemAt(spellPrev.getIconDisplay(),x,y,dim);

				ISpell sRightRight = spellPrev.right();//SpellRegistry.getSpellFromType(spellPrev.getSpellID().prev());

				if(sRightRight != null && sRightRight.getIconDisplay() != null)
				{
					x = xmain+6 + 4; 
					y = ymain + 16+14;
					dim = 16/2 - 2;
					renderItemAt(sRightRight.getIconDisplay(),x,y,dim);
					
					ISpell another = sRightRight.right();
					if(another != null)
					{
						x = xmain+6 +7; 
						y = ymain + 16+14+10;
						dim = 16/2 - 4;
						renderItemAt(another.getIconDisplay(),x,y,dim);
					}
				}
			}
		}
	}
	*/
	
	
	
	
	
}