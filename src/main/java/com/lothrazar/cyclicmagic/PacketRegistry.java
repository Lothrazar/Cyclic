package com.lothrazar.cyclicmagic;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import com.lothrazar.cyclicmagic.net.MessageKeyCast;
import com.lothrazar.cyclicmagic.net.MessageKeyLeft;
import com.lothrazar.cyclicmagic.net.MessageKeyRight;
import com.lothrazar.cyclicmagic.net.MessageOpenSpellbook;
import com.lothrazar.cyclicmagic.net.MessageParticle;
import com.lothrazar.cyclicmagic.net.MessageRecharge;
import com.lothrazar.cyclicmagic.net.MessageSpellPull;
import com.lothrazar.cyclicmagic.net.MessageSpellPush;
import com.lothrazar.cyclicmagic.net.MessageSpellReach;
import com.lothrazar.cyclicmagic.net.MessageSpellReplacer;
import com.lothrazar.cyclicmagic.net.MessageSpellRotate;
import com.lothrazar.cyclicmagic.net.MessageToggleBuild;
import com.lothrazar.cyclicmagic.net.MessageTogglePassive;
import com.lothrazar.cyclicmagic.net.MessageToggleSpell;
import com.lothrazar.cyclicmagic.net.MessageToggleSpellGroup;


public class PacketRegistry{

	public static void register(){

		ModMain.network = NetworkRegistry.INSTANCE.newSimpleChannel(Const.MODID);
		
		int packetID = 11;
		ModMain.network.registerMessage(MessageKeyCast.class, MessageKeyCast.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageKeyLeft.class, MessageKeyLeft.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageKeyRight.class, MessageKeyRight.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageToggleSpell.class, MessageToggleSpell.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageParticle.class, MessageParticle.class, packetID++, Side.CLIENT);
		ModMain.network.registerMessage(MessageOpenSpellbook.class, MessageOpenSpellbook.class, packetID++, Side.CLIENT);
		ModMain.network.registerMessage(MessageSpellReach.class, MessageSpellReach.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageToggleBuild.class, MessageToggleBuild.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellRotate.class, MessageSpellRotate.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellPush.class, MessageSpellPush.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellPull.class, MessageSpellPull.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageSpellReplacer.class, MessageSpellReplacer.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageRecharge.class, MessageRecharge.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageTogglePassive.class, MessageTogglePassive.class, packetID++, Side.SERVER);
		ModMain.network.registerMessage(MessageToggleSpellGroup.class, MessageToggleSpellGroup.class, packetID++, Side.SERVER);
		
	}
}
