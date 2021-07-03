package com.smart.manager.events;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import com.smart.manager.WebhookGUI;

public class JoinGuild extends ListenerAdapter {
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        if(WebhookGUI.GUI.MAIN_CONSOLE == null)
            return;

        WebhookGUI.GUI.MAIN_CONSOLE.webhookListPanels.keySet().forEach(WebhookGUI.GUI.MAIN_CONSOLE::populateList);
    }
}
