package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.PlayerUtil;
import com.WiseHollow.Fundamentals.Tasks.JailTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandUnjail implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Jail"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length != 1)
            return false;

        Player player = PlayerUtil.GetPlayer(args[0]);
        if (player == null || !player.isOnline())
        {
            sender.sendMessage(Language.YouMustBeLoggedIn);
            return true;
        }

        JailTask task = JailTask.GetTask(player);
        if (task == null)
        {
            sender.sendMessage(Language.PREFIX_WARNING + "That player is not jailed.");
            return true;
        }

        task.Disable();

        return true;
    }
}
