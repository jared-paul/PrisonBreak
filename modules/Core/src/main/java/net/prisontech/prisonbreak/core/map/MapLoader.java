package net.prisontech.prisonbreak.core.map;

import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.map.IMapLoader;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MapLoader implements IMapLoader
{
    private World world;
    private String mapName;
    private File mapFolder;

    public MapLoader() throws IOException
    {
        this.mapName = "map_" + UUID.randomUUID();
        this.mapFolder = new File(PrisonBreak.getPlugin().getServer().getWorldContainer() + "\\" + mapName);

        loadMap();
    }

    public World getWorld()
    {
        return world;
    }

    public void unLoadMap() throws IOException
    {
        Bukkit.getScheduler().cancelAllTasks();
        PrisonBreak.getPlugin().getServer().unloadWorld(PrisonBreak.getPlugin().getServer().getWorld(mapName), false);
        FileUtils.forceDeleteOnExit(mapFolder);
    }

    public void loadMap() throws IOException
    {
        File map = new File(PrisonBreak.getPlugin().getDataFolder() + "\\map");
        FileUtils.copyDirectory(map, this.mapFolder);

        PrisonBreak.LOG.warning(mapFolder.getName());

        this.world = PrisonBreak.getPlugin().getServer().createWorld(new WorldCreator(this.mapName));
    }
}