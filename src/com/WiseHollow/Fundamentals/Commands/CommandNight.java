package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandNight implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Time"))
        {
            sender.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        World world;

        if (!(sender instanceof Player))
        {
            //TODO: Handle as server console.

            sender.sendMessage(Language.YouMustBeLoggedIn);
            return true;
        }
        else
        {
            world = ((Player) sender).getWorld();
        }

        world.setTime(13000);
        sender.sendMessage(Language.PREFIX + "Time set to 7:00pm.");

        return true;
    }
}
