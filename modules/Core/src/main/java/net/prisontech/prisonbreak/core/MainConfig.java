package net.prisontech.prisonbreak.core;

import net.prisontech.prisonbreak.api.IMainConfig;
import net.prisontech.prisonbreak.api.PrisonBreak;
import net.prisontech.prisonbreak.api.config.Registry;
import org.apache.commons.io.FileUtils;

import java.io.IOException;

public class MainConfig extends Registry implements IMainConfig
{
    private final String path = "PrisonBreak";

    //defaults
    private String mainServer = "example";
    private int duration = -1;
    private int lobbyPort = -1;


    public MainConfig() throws IOException
    {
        super("Main");
    }

    @Override
    public String getMainServerName()
    {
        if (mainServer.equals("example"))
        {
            this.mainServer = this.config.getString(this.path + ".MainServer");
        }

        return mainServer;
    }

    @Override
    public int getDuration()
    {
        if (duration == -1)
        {
            this.duration = this.config.getInt(this.path + ".Duration");
        }

        return duration;
    }

    @Override
    public int getLobbyPort()
    {
        if (lobbyPort == -1)
        {
            this.lobbyPort = this.config.getInt(this.path + ".LobbyPort");
        }

        return lobbyPort;
    }
}
