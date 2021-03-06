package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import com.WiseHollow.Fundamentals.Tasks.AFKTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandAFK implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.AFK"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        AFKTask task = AFKTask.GetTask(player);
        if (task == null)
        {
            task = new AFKTask(player);
            task.Run();
        }
        else
        {
            task.Disable();
        }

        return true;
    }
}
