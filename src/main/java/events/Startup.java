package events;

import guis.MainConsole;
import guis.NoGuildsPopUp;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import other.WebhookGUI;

public class Startup extends ListenerAdapter {
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        attemptStartup();
    }

    public static void attemptStartup() {
        if(WebhookGUI.GUI.BOT.getGuilds().size() == 0) {
            new NoGuildsPopUp();
            return;
        }
        WebhookGUI.GUI.MAIN_CONSOLE = new MainConsole();
    }

    //public void
}
