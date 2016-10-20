package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import com.WiseHollow.Fundamentals.Settings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandListJail implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Jail"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        String jails = "";
        for(String s : Settings.jails.keySet())
        {
            jails+=s+" ";
        }

        sender.sendMessage(Language.PREFIX + "Jails: " + jails);
        return true;
    }
}
