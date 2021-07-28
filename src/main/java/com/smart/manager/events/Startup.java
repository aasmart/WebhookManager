package com.smart.manager.events;

import com.smart.manager.guiComponents.guis.MainConsole;
import com.smart.manager.guiComponents.popups.NoGuildsPopUp;
import com.smart.manager.guiComponents.settings.ManagerSettings;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import com.smart.manager.WebhookGUI;

/**
 * Called once the {@link WebhookGUI#BOT} is ready
 */
public class Startup extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        attemptStartup();
    }

    /**
     * Attempts to create the {@link MainConsole} and checks to see if the given bot is in any {@link net.dv8tion.jda.api.entities.Guild}s
     */
    public static void attemptStartup() {
        if(WebhookGUI.GUI.BOT.getGuilds().size() == 0) {
            new NoGuildsPopUp();
            return;
        }
        WebhookGUI.settings = ManagerSettings.compileSettings();
        WebhookGUI.GUI.MAIN_CONSOLE = new MainConsole();
    }
}
