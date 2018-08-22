package me.glaremasters.buypermissions.utils;

import org.bukkit.ChatColor;

/**
 * Created by GlareMasters
 * Date: 8/21/2018
 * Time: 7:24 PM
 */
public class ColorUtil {

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
