package net.prisontech.prisonbreak.api.map;

import org.bukkit.World;

import java.io.IOException;

public interface IMapLoader
{
    World getWorld();

    void unLoadMap() throws IOException;

    void loadMap() throws IOException;
}
