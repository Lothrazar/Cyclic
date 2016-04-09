package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.net.DepositAllPacket;
import com.lothrazar.cyclicmagic.net.LootAllPacket;
import com.lothrazar.cyclicmagic.net.MessageBarMove;
import com.lothrazar.cyclicmagic.net.MessageKeyCast;
import com.lothrazar.cyclicmagic.net.MessageKeyLeft;
import com.lothrazar.cyclicmagic.net.MessageKeyRight;
import com.lothrazar.cyclicmagic.net.MessageOpenSpellbook;
import com.lothrazar.cyclicmagic.net.MessageParticle;
import com.lothrazar.cyclicmagic.net.MessageRecharge;
import com.lothrazar.cyclicmagic.net.MessageSlotMove;
import com.lothrazar.cyclicmagic.net.MessageSpellFromServer;
import com.lothrazar.cyclicmagic.net.MessageSpellPull;
import com.lothrazar.cyclicmagic.net.MessageSpellPush;
import com.lothrazar.cyclicmagic.net.MessageSpellReplacer;
import com.lothrazar.cyclicmagic.net.MessageSpellRotate;
import com.lothrazar.cyclicmagic.net.MessageToggleBuild;
import com.lothrazar.cyclicmagic.net.MessageToggleSpell;
import com.lothrazar.cyclicmagic.net.MessageUpgrade;
import com.lothrazar.cyclicmagic.net.OpenCraftingPacket;
import com.lothrazar.cyclicmagic.net.PacketDeleteButton;
import com.lothrazar.cyclicmagic.net.PacketNewButton;
import com.lothrazar.cyclicmagic.net.PacketWarpButton;
import com.lothrazar.cyclicmagic.net.QuickStackPacket;
import com.lothrazar.cyclicmagic.net.RestockPacket;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistry {

	public static void register(SimpleNetworkWrapper network){

		network.registerMessage(MessageKeyCast.class, MessageKeyCast.class, MessageKeyCast.ID, Side.SERVER);
		
		//merge into key shift packet?
		network.registerMessage(MessageKeyLeft.class, MessageKeyLeft.class, MessageKeyLeft.ID, Side.SERVER);
		network.registerMessage(MessageKeyRight.class, MessageKeyRight.class, MessageKeyRight.ID, Side.SERVER);
		
		network.registerMessage(MessageToggleSpell.class, MessageToggleSpell.class, MessageToggleSpell.ID, Side.SERVER);
		network.registerMessage(MessageParticle.class, MessageParticle.class, MessageParticle.ID, Side.CLIENT);
		network.registerMessage(MessageOpenSpellbook.class, MessageOpenSpellbook.class, MessageOpenSpellbook.ID, Side.CLIENT);
		network.registerMessage(MessageSpellFromServer.class, MessageSpellFromServer.class, MessageSpellFromServer.ID, Side.SERVER);
		network.registerMessage(MessageToggleBuild.class, MessageToggleBuild.class, MessageToggleBuild.ID, Side.SERVER);
		network.registerMessage(MessageSpellRotate.class, MessageSpellRotate.class, MessageSpellRotate.ID, Side.SERVER);
		network.registerMessage(MessageSpellPush.class, MessageSpellPush.class, MessageSpellPush.ID, Side.SERVER);
		network.registerMessage(MessageSpellPull.class, MessageSpellPull.class, MessageSpellPull.ID, Side.SERVER);
		network.registerMessage(MessageSpellReplacer.class, MessageSpellReplacer.class, MessageSpellReplacer.ID, Side.SERVER);
		network.registerMessage(MessageRecharge.class, MessageRecharge.class, MessageRecharge.ID, Side.SERVER);
		network.registerMessage(MessageUpgrade.class, MessageUpgrade.class, MessageUpgrade.ID, Side.SERVER);
	
    	network.registerMessage(MessageSlotMove.class, MessageSlotMove.class, MessageSlotMove.ID, Side.SERVER);
    	network.registerMessage(MessageBarMove.class, MessageBarMove.class, MessageBarMove.ID, Side.SERVER);
	
		network.registerMessage(PacketWarpButton.class, PacketWarpButton.class, PacketWarpButton.ID, Side.SERVER);
		network.registerMessage(PacketNewButton.class, PacketNewButton.class, PacketNewButton.ID, Side.SERVER);
		network.registerMessage(PacketDeleteButton.class, PacketDeleteButton.class, PacketDeleteButton.ID, Side.SERVER);
	
int packetID=30;
    	network.registerMessage(DepositAllPacket.class,  DepositAllPacket.class,  packetID++, Side.SERVER);
    	network.registerMessage(LootAllPacket.class,  LootAllPacket.class,  packetID++, Side.SERVER);
    	network.registerMessage(QuickStackPacket.class,  QuickStackPacket.class,  packetID++, Side.SERVER);
    	network.registerMessage(RestockPacket.class,  RestockPacket.class,  packetID++, Side.SERVER);
		
    	network.registerMessage(OpenCraftingPacket.class,  OpenCraftingPacket.class,  packetID++, Side.SERVER);
		
    	
		
	}
}
