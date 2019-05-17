package net.prisontech.prisonbreak.api.util.serialization;

import net.prisontech.prisonbreak.api.loot.LocationValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

/**
 * Created by JPaul on 4/7/2016.
 */
public class LocationSerialization
{
    public static String serializeLocation(Location location)
    {
        return location.getX() + ";" + location.getY() + ";" + location.getZ();
    }

    public static Location deserializeLocation(String location, World world)
    {
        String[] parts = location.split(";");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        return new Location(world, x, y, z);
    }
}
