package net.prisontech.prisonbreak.api.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.prisontech.prisonbreak.api.PrisonBreak;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BungeeUtil
{
    public static void sendPlayer(Player player, String serverName)
    {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);

        player.sendPluginMessage(PrisonBreak.getPlugin(), "BungeeCord", output.toByteArray());
    }

    public static boolean isServerOnline(int port)
    {
        try
        {
            Socket s = new Socket();
            s.connect(new InetSocketAddress("127.0.0.1", port), 10); //good timeout is 10-20
            s.close();

            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
