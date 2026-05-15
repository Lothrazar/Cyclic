package com.lothrazar.cyclic;
import net.neoforged.neoforge.common.Tags;
import java.lang.reflect.Field;
public class TagDump {
    public static void main(String[] args) {
        try {
            for (Field f : Tags.Items.class.getDeclaredFields()) {
                System.out.println(f.getName() + " = " + f.get(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
