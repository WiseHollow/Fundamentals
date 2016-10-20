package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandWho implements CommandExecutor
{
    private String GetOnlinePlayers()
    {
        String list = "";
        for(Player p : Bukkit.getOnlinePlayers())
        {
            list+= PlayerUtil.GetPlayerPrefix(p) + p.getName() + " ";
        }
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Who"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        sender.sendMessage(Language.PREFIX + "Online players (" + Bukkit.getOnlinePlayers().size() + "): " + GetOnlinePlayers());
        return true;
    }
}
