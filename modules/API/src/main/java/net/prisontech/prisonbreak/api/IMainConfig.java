package net.prisontech.prisonbreak.api;

import net.prisontech.prisonbreak.api.config.IRegistry;

public interface IMainConfig extends IRegistry
{
    int getDuration();

    int getLobbyPort();

    String getMainServerName();
}
