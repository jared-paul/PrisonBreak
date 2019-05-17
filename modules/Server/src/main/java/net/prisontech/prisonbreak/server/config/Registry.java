package net.prisontech.prisonbreak.server.config;

import net.prisontech.prisonbreak.server.ServerMain;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Registry
{
    protected FileConfiguration config;
    protected File configFile;

    private String regionID;
    private String serverName;
    private String broadcastMessage = "The game will start in 1 minute";

    public Registry() throws IOException
    {
        this.configFile = new File(ServerMain.getPlugin().getDataFolder() + "\\config.yml");

        if (!this.configFile.exists())
        {
            ServerMain.getPlugin().getDataFolder().mkdirs();
            this.configFile.createNewFile();
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.config.options().copyDefaults(true);

        this.config.addDefault("PrisonBreak.Server", "server2");
        this.config.addDefault("PrisonBreak.RegionID", "prisonbreak");

        this.config.save(configFile);
    }

    public String getRegionID()
    {
        if (regionID.equals("exampleID"))
        {
            this.regionID = config.getString("PrisonBreak.RegionID");
        }

        return regionID;
    }

    public String getServerName()
    {
        if (serverName.equals("example"))
        {
            this.serverName = config.getString("PrisonBreak.Server");
        }

        return serverName;
    }

    public FileConfiguration getConfig()
    {
        return config;
    }

    public File getConfigFile()
    {
        return configFile;
    }
}
