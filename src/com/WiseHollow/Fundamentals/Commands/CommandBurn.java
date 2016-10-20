package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.PlayerUtil;
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
public class CommandBurn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Burn"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0)
            return false;

        List<Player> targets = new ArrayList<>();

        Player target = PlayerUtil.GetPlayer(args[0]);
        if (args[0].equalsIgnoreCase("all"))
        {
            targets.addAll(Bukkit.getOnlinePlayers());
        }
        else if (target == null || ! target.isOnline())
        {
            sender.sendMessage(Language.PlayerMustBeLoggedIn);
            return true;
        }

        int seconds = 0;
        try
        {
            seconds = Integer.valueOf(args[1]);
        }
        catch(Exception ex)
        {
            sender.sendMessage(Language.PREFIX_WARNING + "Invalid time input.");
            return true;
        }

        targets.add(target);

        for (Player p : targets)
        {
            if (!p.equals(sender))
            {
                p.setFireTicks(20 * seconds);
            }
        }

        if (args[0].equalsIgnoreCase("all"))
            sender.sendMessage(Language.PREFIX_COLOR + "You have burned all players for " + seconds + " seconds");
        else if (sender != null)
            sender.sendMessage(Language.PREFIX_COLOR + "You have burned " + target.getName() + " for " + seconds + " seconds");

        return true;
    }
}
