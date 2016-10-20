package com.WiseHollow.Fundamentals.Commands;

import com.WiseHollow.Fundamentals.Language;
import com.WiseHollow.Fundamentals.Permissions.PermissionCheck;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by John on 10/13/2016.
 */
public class CommandSpawnEntity implements CommandExecutor
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
        if (!sender.hasPermission("Fundamentals.SpawnEntity"))
        {
            player.sendMessage(Language.DoesNotHavePermission);
            return true;
        }

        if (args.length == 0)
            return false;

        EntityType type = EntityType.valueOf(args[0].toUpperCase());
        if (type != null)
        {
            Set<Material> s = null;
            Location target = player.getTargetBlock(s, 100).getLocation().clone().add(0, 1, 0);
            target.getWorld().spawnEntity(target, type);
            sender.sendMessage(Language.PREFIX + type.name() + " spawned at location.");
        }

        return true;
    }
}
