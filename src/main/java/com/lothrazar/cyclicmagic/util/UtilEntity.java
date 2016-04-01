package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.EnumFacing;


public class UtilEntity{

	public static void setMaxHealth(EntityLivingBase living,int max)
	{	
		living.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(max);
	}
	
	public static EnumFacing getPlayerFacing(EntityLivingBase entity) 
	{
    	int yaw = (int)entity.rotationYaw;

        if (yaw<0)              //due to the yaw running a -360 to positive 360
           yaw+=360;    //not sure why it's that way

        yaw += 22;     //centers coordinates you may want to drop this line
        yaw %= 360;  //and this one if you want a strict interpretation of the zones

        int facing = yaw/45;   //  360degrees divided by 45 == 8 zones
       
		return EnumFacing.getHorizontal( facing/2 );
	}
}
