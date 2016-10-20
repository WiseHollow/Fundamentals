package com.WiseHollow.Fundamentals;

import org.bukkit.Material;

/**
 * Created by John on 10/20/2016.
 */
public class MaterialIndex
{
    @SuppressWarnings("deprecation")
    public static Material getMaterial(String input)
    {
        input = input.toUpperCase();
        Material mat = null;
        mat = Material.getMaterial(input);
        if (mat != null)
            return mat;

        for(Material m : Material.values())
            if (m.name().replaceAll("_", "").equalsIgnoreCase(input))
                return m;

        int id = Integer.parseInt(input);
        if (id > 0)
        {
            mat = Material.getMaterial(id);
            if (mat != null)
                return mat;
        }

        return null;
    }


}
