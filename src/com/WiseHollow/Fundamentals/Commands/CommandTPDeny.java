package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 10/13/2016.
 */
public class CommandTPDeny implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Language.YouMustBeLoggedIn);
            return true;
        }

        Player player = (Player) sender;
        if (!sender.hasPermission("Fundamentals.TPDeny"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (!CommandTPA.tpaHash.containsValue(player))
        {
            player.sendMessage(Language.PREFIX_COLOR + "No pending teleport requests.");
            return true;
        }

        List<Player> requested = new ArrayList<>();

        for (Player p : CommandTPA.tpaHash.keySet())
        {
            if (CommandTPA.tpaHash.get(p).equals(player))
            {
                requested.add(p);
            }
        }

        for (Player p : requested)
        {
            Bukkit.getServer().getScheduler().cancelTask(CommandTPA.tpaTaskIDs.get(p));
            CommandTPA.tpaHash.containsKey(p);
            CommandTPA.tpaTaskIDs.remove(p);
            p.sendMessage(Language.PREFIX_COLOR + "Teleport request denied.");
        }

        return true;
    }
}
