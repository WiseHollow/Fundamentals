package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Settings;
import com.WiseHollow.Fundamentals.Tasks.TeleportTask;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandWarp implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.Warp"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0)
        {
            String warps = "";
            for(String s : Settings.warps.keySet())
            {
                if (player.hasPermission("Fundamentals.Warps." + s))
                    warps+=s+" ";
            }
            player.sendMessage(Language.PREFIX_COLOR + ChatColor.BOLD + "Warps: " + ChatColor.RESET + warps);
            return true;
        }

        String name = args[0].toLowerCase();
        if (!Settings.warps.containsKey(name))
        {
            player.sendMessage(Language.PREFIX_WARNING + "Warp does not exist!");
            return true;
        }

        Location target = Settings.warps.get(name);
        TeleportTask task = new TeleportTask(player, target, Settings.TeleportDelay);
        task.Run();

        return true;
    }
}
