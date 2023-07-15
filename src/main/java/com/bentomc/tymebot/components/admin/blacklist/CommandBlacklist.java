package com.bentomc.tymebot.components.admin.blacklist;

import com.bentomc.tymebot.Bot;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SlashInfo(name = "blacklist", description = "Blacklist a user or guild.")
public class CommandBlacklist extends SlashCommand {
    public CommandBlacklist(Bot bot) {
        addSubCommands(new SubCommandBlacklistGuild(bot), new SubCommandBlacklistUser(bot));
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
    }
}
