package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.DataCollection.PlayerData;
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
public class CommandHome implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.Home"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        PlayerData pd = PlayerData.GetPlayerData(player);

        if (args.length == 0)
        {
            String homes = "";
            for(String s : pd.getHomes().keySet())
            {
                homes+=s+" ";
            }
            player.sendMessage(Language.PREFIX_COLOR + ChatColor.BOLD + "Homes: " + ChatColor.RESET + homes);
            return true;
        }

        String name = args[0];

        Location target = pd.getHome(name);

        if (target == null)
        {
            player.sendMessage(Language.PREFIX_WARNING + "Home does not exist!");
            return true;
        }

        TeleportTask task = new TeleportTask(player, target, Settings.TeleportDelay);
        task.Run();

        return true;
    }
}
