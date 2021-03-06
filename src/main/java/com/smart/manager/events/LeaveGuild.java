package com.smart.manager.events;

import com.smart.manager.Webhook;
import com.smart.manager.guiComponents.popups.NoGuildsPopUp;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import com.smart.manager.WebhookGUI;

import java.util.stream.Collectors;

/**
 * Called when the bot leaves a {@link net.dv8tion.jda.api.entities.Guild}. This is for refreshing the list of active {@link Webhook}s
 */
public class LeaveGuild extends ListenerAdapter {
    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {
        if(WebhookGUI.GUI.MAIN_CONSOLE == null)
            return;

        if(WebhookGUI.GUI.BOT.getGuilds().size() == 0) {
            WebhookGUI.GUI.MAIN_CONSOLE.dispose();
            new NoGuildsPopUp();
        } else {
            WebhookGUI.GUI.MAIN_CONSOLE.webhookListPanels.remove(event.getGuild().getIdLong());
            WebhookGUI.GUI.MAIN_CONSOLE.tabGuildIDMap.remove(WebhookGUI.GUI.MAIN_CONSOLE.tabGuildIDMap.entrySet().stream()
                    .filter(e -> e.getValue() == event.getGuild().getIdLong())
                    .collect(Collectors.toList()).get(0).getKey());

            // This throws an exception I can't trace so...
            try {
                WebhookGUI.GUI.MAIN_CONSOLE.webhookList();
            } catch (Exception ignore) { }
        }
    }
}
