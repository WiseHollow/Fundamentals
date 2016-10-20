package com.WiseHollow.Fundamentals;

import com.WiseHollow.Fundamentals.Tasks.AFKDetectTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
