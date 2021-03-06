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
public class CommandWeather implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args)
    {
        if (!sender.hasPermission("Fundamentals.Weather"))
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

        if (args.length == 0)
        {
            String weather;
            if (world.hasStorm())
            {
                weather = "Raining";
                if (world.isThundering())
                    weather+="/Storming";
            }
            else
                weather = "Sunny";
            sender.sendMessage(Language.PREFIX + "It is currently " + weather + " (Opt: Sun/Rain/Storm).");
            return true;
        }

        if (args[0].equalsIgnoreCase("Sun") || args[0].equalsIgnoreCase("Clear"))
        {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(24000 * 3);
            sender.sendMessage(Language.PREFIX + "Weather set to sunny.");
        }
        else if (args[0].equalsIgnoreCase("Rain"))
        {
            world.setStorm(true);
            world.setThundering(false);
            world.setWeatherDuration(24000 * 3);
            world.setThunderDuration(24000 * 3);
            sender.sendMessage(Language.PREFIX + "Weather set to rain.");
        }
        else if (args[0].equalsIgnoreCase("Storm"))
        {
            world.setStorm(true);
            world.setThundering(true);
            world.setWeatherDuration(24000 * 3);
            world.setThunderDuration(24000 * 3);
            sender.sendMessage(Language.PREFIX + "Weather set to storm.");
        }
        else
        {
            return false;
        }

        return true;
    }
}
