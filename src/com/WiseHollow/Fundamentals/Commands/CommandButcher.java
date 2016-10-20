package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

/**
 * Created by John on 10/13/2016.
 */
public class CommandButcher implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.Butcher"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        boolean all = false;
        if (args.length > 0 && args[0].equalsIgnoreCase("ALL"))
            all = true;

        int kill = 0;
        for(LivingEntity e : player.getWorld().getLivingEntities())
        {
            if (e instanceof Monster || (all && e instanceof Animals))
            {
                e.remove();
                kill++;
            }
        }
        player.sendMessage(Language.PREFIX + "Killed " + kill + " entities.");

        return true;
    }
}
