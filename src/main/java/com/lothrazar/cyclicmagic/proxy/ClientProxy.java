package com.lothrazar.cyclicmagic.proxy;

import org.lwjgl.input.Keyboard;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import com.lothrazar.cyclicmagic.gui.GuiSpellbook;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;

public class ClientProxy extends CommonProxy{
	public static KeyBinding keyShiftUp;
	public static KeyBinding keyShiftDown; 
	public static KeyBinding keyBarUp;
	public static KeyBinding keyBarDown;  
 
	static final String keyCategoryInventory = "key.categories.inventorycontrol";
	
	@Override
	public void register(){

		registerModels();
		
		keyShiftUp = new KeyBinding("key.columnshiftup", Keyboard.KEY_Y, keyCategoryInventory);
        ClientRegistry.registerKeyBinding(ClientProxy.keyShiftUp);
    
		keyShiftDown = new KeyBinding("key.columnshiftdown", Keyboard.KEY_H, keyCategoryInventory); 
        ClientRegistry.registerKeyBinding(ClientProxy.keyShiftDown); 

        keyBarUp = new KeyBinding("key.columnbarup", Keyboard.KEY_U, keyCategoryInventory);
        ClientRegistry.registerKeyBinding(ClientProxy.keyBarUp);
         
        keyBarDown = new KeyBinding("key.columnbardown", Keyboard.KEY_J, keyCategoryInventory); 
        ClientRegistry.registerKeyBinding(ClientProxy.keyBarDown);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayGuiSpellbook(){

		Minecraft.getMinecraft().displayGuiScreen(new GuiSpellbook(Minecraft.getMinecraft().thePlayer));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumFacing getSideMouseover(int max){

		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if(mouseOver != null){

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			return mouseOver.sideHit;

		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockPos getBlockMouseoverExact(int max){

		// Get the player and their held item

		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if(mouseOver != null){

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			return mouseOver.getBlockPos();

		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockPos getBlockMouseoverOffset(int max){

		// Get the player and their held item
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		// int max = 50;
		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if(mouseOver != null && mouseOver.sideHit != null){

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			BlockPos blockPos = mouseOver.getBlockPos();

			if(blockPos != null && player.worldObj.getBlockState(blockPos) != null){

				return blockPos.offset(mouseOver.sideHit);
			}
		}
		return null;
	}

	private void registerModels(){

		// with help from
		// http://www.minecraftforge.net/forum/index.php?topic=32492.0
		// https://github.com/TheOnlySilverClaw/Birdmod/blob/master/src/main/java/silverclaw/birds/client/ClientProxyBirds.java
		// More info on proxy rendering
		// http://www.minecraftforge.net/forum/index.php?topic=27684.0
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		String name;
		Item item;
		for(Block b : BlockRegistry.blocks){
			item = Item.getItemFromBlock(b);
			name = Const.MODRES + b.getUnlocalizedName().replaceAll("tile.", "");

			mesher.register(item, 0, new ModelResourceLocation(name, "inventory"));
		}
		
		for(Item i : ItemRegistry.items){
			name = Const.MODRES + i.getUnlocalizedName().replaceAll("item.", "");

			mesher.register(i, 0, new ModelResourceLocation(name, "inventory"));
		}
	}

	@SideOnly(Side.CLIENT)
	public static boolean isKeyDown(KeyBinding keybinding){

		//inside a GUI , we have to check the keyboard directly
		//thanks to Inventory tweaks, reminding me of alternate way to check keydown while in config
		// https://github.com/Inventory-Tweaks/inventory-tweaks/blob/develop/src/main/java/invtweaks/InvTweaks.java
		
		return keybinding.isPressed() || Keyboard.isKeyDown(keybinding.getKeyCode()) ;
	}
}
