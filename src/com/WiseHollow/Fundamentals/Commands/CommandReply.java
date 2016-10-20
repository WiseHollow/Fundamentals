package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.CustomEvents.SendPrivateMessageEvent;
import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Main;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by John on 10/13/2016.
 */
public class CommandReply implements CommandExecutor
{
    public static HashMap<CommandSender, CommandSender> senderAndReceivers = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Message"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length < 1)
            return false;

        CommandSender target = senderAndReceivers.get(sender);
        if (target == null)
        {
            sender.sendMessage(Language.PREFIX + "There is nobody to reply to.");
            return true;
        }

        String msg = "";
        for(int i = 0; i < args.length; i++)
            msg+=args[i]+" ";

        SendPrivateMessageEvent event = new SendPrivateMessageEvent(sender, target, msg);
        Bukkit.getServer().getPluginManager().callEvent(event);
        event.run();

        return true;
    }
}
