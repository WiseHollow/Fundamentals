package com.WiseHollow.Fundamentals;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class PlayerUtil
{
    public static Player GetPlayer(String s)
    {
        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getName().toLowerCase().contains(s.toLowerCase()))
                return p;
        return null;
    }

    public static String GetPlayerPrefix(Player player)
    {
        if (Main.chat == null)
            return player.getName();

        String prefix = "";
        if (Main.chat != null)
        {
            prefix = ChatColor.translateAlternateColorCodes('&', Main.chat.getPlayerPrefix(player));
        }

        return prefix;
    }
}
