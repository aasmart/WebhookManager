package events;

import guiComponents.guis.MainConsole;
import guiComponents.popups.NoGuildsPopUp;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other.WebhookGUI;

/**
 * Called once the {@link WebhookGUI#BOT} is ready
 */
public class Startup extends ListenerAdapter {
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
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
        WebhookGUI.GUI.MAIN_CONSOLE = new MainConsole();
    }
}
