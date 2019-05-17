package net.prisontech.prisonbreak.blackmarket;

import org.bukkit.Location;
import org.bukkit.entity.Villager;

public class BlackMarket
{
    Villager villager;
    Location location;

    public BlackMarket(Villager villager, Location location)
    {
        this.villager = villager;
        this.location = location;
    }

    public Villager getVillager()
    {
        return villager;
    }

    public Location getLocation()
    {
        return location;
    }
}
