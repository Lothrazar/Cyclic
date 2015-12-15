package com.lothrazar.cyclicmagic.proxy;

import org.lwjgl.input.Keyboard;   
import  net.minecraft.item.Item;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.projectile.*;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;

public class ClientProxy extends CommonProxy 
{   
	public static KeyBinding keySpellCast;
	public static KeyBinding keySpellUp;
	public static KeyBinding keySpellDown;
	public static KeyBinding keySpellToggle;

	//public static final String keyTransformName = "key.spell.transform";
	public static final String keySpellCastName = "key.spell.cast";
	public static final String keySpellUpName = "key.spell.up";
	public static final String keySpellDownName = "key.spell.down";
	public static final String keySpellToggleName = "key.spell.toggle";
	
    @Override
    public void register() 
    {  
    	registerKeyBindings(); 

        registerModels(); 
         
        registerEntities();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void registerEntities(){
    	RenderManager rm = Minecraft.getMinecraft().getRenderManager();
    	RenderItem ri = Minecraft.getMinecraft().getRenderItem();

    	//RenderingRegistry.registerEntityRenderingHandler(EntityFishingBolt.class, new RenderSnowball(rm, EntityFishingBolt.item, ri));
    //	RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new RenderSnowball(rm, EntityBlazeBolt.item, ri));
    	RenderingRegistry.registerEntityRenderingHandler(EntityTorchBolt.class, new RenderSnowball(rm, EntityTorchBolt.item, ri));
    }
    
	private void registerModels() 
	{
		//More info on proxy rendering
        //http://www.minecraftforge.net/forum/index.php?topic=27684.0
       //http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod
   
        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        String name;
 
        for(Item i : ItemRegistry.items)
        {  
        	name = Const.TEXTURE_LOCATION + i.getUnlocalizedName().replaceAll("item.", "");

   			mesher.register(i, 0, new ModelResourceLocation( name , "inventory"));	 
        }
        /*
	    if(ModMain.cfg.respawn_egg)
	    {
	    	for(Object key : EntityList.entityEggs.keySet())
	        {
	        	mesher.register(ItemRegistry.respawn_egg, (Integer)key, new ModelResourceLocation(Const.TEXTURE_LOCATION + "respawn_egg" , "inventory"));	 
	        }
	    }
     */
	}

	public static final String keyCategorySpell = "key.categories.spell";
	private void registerKeyBindings() 
	{
        keySpellCast = new KeyBinding(keySpellCastName, Keyboard.KEY_X, keyCategorySpell); 
        ClientRegistry.registerKeyBinding(ClientProxy.keySpellCast);

        keySpellUp = new KeyBinding(keySpellUpName, Keyboard.KEY_Z, keyCategorySpell); 
        ClientRegistry.registerKeyBinding(ClientProxy.keySpellUp);

        keySpellDown = new KeyBinding(keySpellDownName, Keyboard.KEY_C,  keyCategorySpell); 
        ClientRegistry.registerKeyBinding(ClientProxy.keySpellDown);
        
        keySpellToggle = new KeyBinding(keySpellToggleName, Keyboard.KEY_SEMICOLON,  keyCategorySpell); 
        ClientRegistry.registerKeyBinding(ClientProxy.keySpellToggle);
	} 
 
}
