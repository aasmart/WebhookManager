package com.smart.manager.events;

import com.smart.manager.Webhook;
import com.smart.manager.guiComponents.popups.NoGuildsPopUp;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import com.smart.manager.WebhookGUI;

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
        } else
            WebhookGUI.GUI.MAIN_CONSOLE.webhookListPanels.keySet().forEach(WebhookGUI.GUI.MAIN_CONSOLE::populateList);
    }
}
