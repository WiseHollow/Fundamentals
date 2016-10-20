package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Kit;
import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Tasks.AFKTask;
import com.WiseHollow.Fundamentals.Tasks.KitTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandKit implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.Kit"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0)
        {
            String kits = "";
            for (Kit k : Kit.GetAvailableKits(player))
                kits+=k+" ";

            player.sendMessage("Available Kits: " + kits);

            return true;
        }

        String name = args[0];
        Kit kit = Kit.GetKit(name);
        if (kit == null)
        {
            player.sendMessage(Language.PREFIX_WARNING + "That kit does not exist!");
            return true;
        }

        KitTask task = KitTask.GetTask(player);
        if (task == null)
        {
            task = new KitTask(player, kit);
            task.Run();
        }
        else
        {
            player.sendMessage(Language.PREFIX_WARNING + "There is a cool-down in effect until the next use of this kit.");
            return true;
        }

        return true;
    }
}
