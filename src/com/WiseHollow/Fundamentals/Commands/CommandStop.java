package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Tasks.AFKTask;
import com.WiseHollow.Fundamentals.Tasks.StopTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandStop implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Stop"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0)
        {
            Bukkit.getServer().shutdown();
            return true;
        }

        int minutes = 0;
        if (args.length > 0)
        {
            if (args[0].equalsIgnoreCase("Cancel"))
            {
                if (StopTask.stopTask == null)
                    return true;
                StopTask.stopTask.Disable();
                return true;
            }

            try
            {
                minutes = Integer.valueOf(args[0]);
            }
            catch(Exception ex)
            {
                sender.sendMessage(Language.PREFIX_WARNING + ex.getMessage());
                return true;
            }
        }

        StopTask task = new StopTask(minutes);
        if (!task.Run())
        {
            sender.sendMessage(Language.PREFIX_WARNING + "There is already a shutdown task started.");
        }

        return true;
    }
}
