package com.bentomc.tymebot.components.misc.help;

import com.bentomc.tymebot.Bot;
import com.bentomc.tymebot.components.misc.help.commands.SubCommandHelpCommands;
import com.bentomc.tymebot.core.util.Utils;
import games.negative.framework.discord.command.SlashCommand;
import games.negative.framework.discord.command.SlashInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.reflections.Reflections;

import java.util.*;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

@SlashInfo(name = "help", description = "Sends the commands and help information about Tyme.")
public class CommandHelp extends SlashCommand {

    private final Bot bot;

    public CommandHelp(Bot bot) {
        this.bot = bot;

        addSubCommand(new SubCommandHelpCommands());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {
        // TODO: create sub commands to go straight to other pages

        // Main help menu
        EmbedBuilder helpEmbed = new EmbedBuilder();
        helpEmbed.setTitle("Tyme Help Menu");
        helpEmbed.setDescription("Use this menu to navigate throughout the various help menus for Tyme.\n\n*Some commands may not be visible depending on your level and/or status.*");
        helpEmbed.addField("Basic Information"
                , """
                        __*Rules*__
                        In order to use Tyme, you must agree to our `/rules`.
                        Tyme is intended to be used to participate with members between servers and to do that, we need to ensure there is a safe environment for everyone.
                                                
                        __*Features*__
                        Tyme is a multi-functioning bot owned by Negative Games and created by `joee#1107`. It has features for server administration, communities and the main purpose: *a cross-server trading game*.
                        With lots of commands and modules built into Tyme, it is one of the most versatile bots out there.
                                                
                        __*Cross-server events*__
                        From time-to-time, the Admins of Tyme will host real-time events which anyone across Discord can participate in. Make sure you watch your DM's and configured bot-channels for these events.
                        """, false);
        helpEmbed.addField("Help Menu"
                , """
                        __*How to use the Help Menu*__
                        > You can use the `/help` command to view all the different menus and go to them directly.
                                                
                        We have tried to make Tyme as user friendly as possible. If you have any suggestions, please join the `/discord` or use the `/botsuggestion` command.
                        """, false);
        helpEmbed.setThumbnail(slashCommandInteractionEvent.getUser().getAvatarUrl());
        helpEmbed.setFooter("Tyme Bot v" + bot.getVersion(), bot.getJda().getSelfUser().getAvatarUrl());
        Reflections reflections = new Reflections("bot.tymebot.components.admin");
        Set<Class<?>> adminCommands = reflections.get(SubTypes.of(TypesAnnotated.with(SlashInfo.class)).asClass());
        System.out.println(adminCommands);
        for (Class<?> commands : adminCommands) {
            System.out.println(commands.getAnnotation(SlashInfo.class).name() + " - " + commands.getAnnotation(SlashInfo.class).description());
            if (commands.getName().startsWith("SubCommand")) adminCommands.remove(commands);
        }


        Collection<String> commandStrings = new ArrayList<>(Collections.emptyList());

        adminCommands.forEach(k -> {
            commandStrings.add(k.getAnnotation(SlashInfo.class).name() + " - " + k.getAnnotation(SlashInfo.class).description());
        });
        // pagination test
        int pageSize = 5;
        List<List<?>> pages = Utils.paginate(commandStrings, 5);
        int page = 0; // minus one because indexes start at 0
        pages.get(page).forEach(System.out::println);

        slashCommandInteractionEvent.replyEmbeds(helpEmbed.build()).setEphemeral(true).queue();
    }
}
