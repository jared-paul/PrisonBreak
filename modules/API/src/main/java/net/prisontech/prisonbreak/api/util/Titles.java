package net.prisontech.prisonbreak.api.util;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Titles
{

    private PlayerConnection con;
    PacketPlayOutTitle title;
    PacketPlayOutTitle subtitle;
    PacketPlayOutTitle times;

    public Titles(Player p)
    {
        con = ((CraftPlayer) p).getHandle().playerConnection;
    }

    public Titles(Player p, String title)
    {
        con = ((CraftPlayer) p).getHandle().playerConnection;
        title(title);
    }

    public Titles title(String title)
    {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', title) + "\"}");
        this.title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, comp);
        return this;
    }

    public Titles subtitle(String subtitle)
    {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\"}");
        this.subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, comp);
        return this;
    }

    public Titles times(int fadeIn, int displayTime, int fadeOut)
    {
        times = new PacketPlayOutTitle(fadeIn, displayTime, fadeOut);
        return this;
    }

    public void send()
    {
        if (title != null)
        {
            con.sendPacket(title);
            if (subtitle != null) con.sendPacket(subtitle);
            if (times != null) con.sendPacket(subtitle);
        }
    }
}
