package net.prisontech.prisonbreak.api.kits;

import net.prisontech.prisonbreak.api.config.IRegistry;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IKitRegistry extends IRegistry
{
    void saveKit(IKit kit, UUID playerUUID) throws IOException;

    List<IKit> getAvailableKits(UUID playerUUID);

    IKit getKit(String kitName);

    List<IKit> getKits();
}
