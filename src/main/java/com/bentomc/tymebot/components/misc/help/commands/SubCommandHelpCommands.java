package com.bentomc.tymebot.components.misc.help.commands;

import games.negative.framework.discord.command.SlashInfo;
import games.negative.framework.discord.command.SlashSubCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.reflections.Reflections;

import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

@SlashInfo(name = "commands", description = "Show the help info for commands.")
public class SubCommandHelpCommands extends SlashSubCommand {

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Tyme Commands Page 1");
        eb.setDescription("Use the command `/help commands <page>` to navigate throughout the different commands available with Tyme Bot.");
        eb.addField("Miscellaneous Commands"
                , """
                        • `/help [module] [page]` - Sends the commands and help information about Tyme.
                        • `/botsuggestion` - Send the developers of Tyme suggestions for the bot.
                        • `/discord` - Send the support discord link.
                        • `/invite` - Get an invite link to add Tyme to your server.
                        • `/info` - Get diagnostic information about Tyme.
                        • `/rules` - Get the rules for Tyme.
                        """, false);


        Reflections reflections = new Reflections("bot.tymebot.components.admin");
        Set<Class<?>> adminCommands = reflections.get(SubTypes.of(TypesAnnotated.with(SlashInfo.class)).asClass());
        System.out.println(adminCommands);

        for (Class<?> commands : adminCommands) {
            System.out.println(commands.getAnnotation(SlashInfo.class).name() + " - " + commands.getAnnotation(SlashInfo.class).description());
        }
    }
}
