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
public class CommandSudo implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Sudo"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length <= 1)
            return false;

        Player target = PlayerUtil.GetPlayer(args[0]);
        if (target == null || ! target.isOnline())
        {
            sender.sendMessage(Language.PlayerMustBeLoggedIn);
            return true;
        }

        String c = "";
        for(int i = 1; i < args.length; i++)
        {
            c+=args[i]+" ";
        }

        target.performCommand(c);
        sender.sendMessage(Language.PREFIX_COLOR + "You have run the command \'/" + c + "\' for player " + target.getName() + " seconds");

        return true;
    }
}
