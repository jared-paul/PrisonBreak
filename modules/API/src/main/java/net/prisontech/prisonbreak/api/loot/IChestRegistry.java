package net.prisontech.prisonbreak.api.loot;

import net.prisontech.prisonbreak.api.config.IRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public interface IChestRegistry extends IRegistry
{
    Set<Location> getChestLocations();

    LinkedList<ItemWrapper> getChestContents();
}
