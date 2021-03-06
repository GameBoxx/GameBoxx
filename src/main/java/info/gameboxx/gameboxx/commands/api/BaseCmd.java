/*
 The MIT License (MIT)

 Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 Copyright (c) 2016 contributors

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package info.gameboxx.gameboxx.commands.api;

import info.gameboxx.gameboxx.commands.api.data.*;
import info.gameboxx.gameboxx.commands.api.parse.CmdParser;
import info.gameboxx.gameboxx.commands.api.parse.SubCmdO;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.IntO;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base custom command.
 * <p/>
 * Extend this class to create commands.
 * <p/>
 * Make sure to register your commands with {@link CmdRegistration#register(Plugin, BaseCmd)}
 * And also don't forget to unregister in onDisable with {@link CmdRegistration#unregister(Plugin)}
 * <p/>
 * Check out the wiki for full details about creating custom commands and to see some example commands.
 * <p/>
 * After calling super in your constructor you can add arguments and such.
 * {@link #addArgument(String, ArgRequirement, SingleOption)}
 * {@link #addModifier(String, SingleOption)}
 * {@link #addFlag(String)}
 */
public abstract class BaseCmd extends Cmd {

    private File configFile = null;
    private YamlConfiguration config = null;

    /**
     * Construct a new command with the given name and aliases.
     *
     * @param name The name/label of the command.
     * @param aliases Aliases for the command.
     */
    public BaseCmd(String name, String... aliases) {
        super(name, aliases);

        addFlag("?").desc("Display the full command help.");
        addFlag("l").desc("List all the sub commands.");
        addModifier("page", new IntO().min(1)).desc("List all sub commands on a specific page.");
    }

    /**
     * Get the config file where the command can be configured.
     *
     * @return The {@link File} set with {@link #file(File)} (May be {@code null} if not set)
     */
    public File file() {
        return configFile;
    }

    /**
     * Set the config file where the command can be configured.
     * <p/>
     * This file must have a .yml extension.
     * It will be filled with all descriptions, permissions, aliases, arguments, sub commands, flags, modifiers etc.
     * <p/>
     * It's totally fine to set the same file for multiple commands.
     * <p/>
     * If no file is set the user won't be able to configure the command.
     *
     * @param configFile the {@link File} where the command can be configured.
     */
    public void file(File configFile) {
        this.configFile = configFile;
    }



    /**
     * Load the command from the config file.
     * If no config file has been set in the constructor this will return false.
     *
     * @return whether or not the command was loaded. Will be false when there was an error or when there is no config file for this command.
     */
    public boolean load() {
        if (configFile == null) {
            return false;
        }

        if (config == null) {
            config = YamlConfiguration.loadConfiguration(configFile);
        } else {
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                return false;
            }
        }

        config.options().copyDefaults(true);
        loadData(config, this);

        try {
            config.save(configFile);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private void loadData(ConfigurationSection cfg, Cmd cmd) {
        //Command - name
        String name;
        if (cmd instanceof SubCmd) {
            name = ((SubCmd)cmd).getSubName();
            ((SubCmd)cmd).setSubName((String)loadValue(cfg, name + ".name", name));
        } else {
            name = cmd.getName();
            cmd.setName((String)loadValue(cfg, name + ".name", name));
        }

        //Command - aliases
        if (!cfg.contains(name + ".aliases")) {
            cfg.set(name + ".aliases", cmd.isSub() ? ((SubCmd)cmd).getSubAliases() : cmd.getAliases());
        } else {
            if (cmd.isSub()) {
                ((SubCmd)cmd).setSubAliases(cfg.getStringList(name + ".aliases"));
            } else {
                cmd.setAliases(cfg.getStringList(name + ".aliases"));
            }
        }

        //Command - description/permission
        cmd.setDescription((String)loadValue(cfg, name + ".description", cmd.getDescription()));
        cmd.setPermission((String)loadValue(cfg, name + ".permission", cmd.getPermission()));

        //Flag - description/permission
        List<Flag> flags = new ArrayList<>(cmd.getFlags().values());
        for (Flag flag : flags) {
            if (flag.name().equals("?") || flag.name().equals("l")) {
                continue;
            }
            flag.desc((String)loadValue(cfg, name + ".flags." + flag.name() + ".description", flag.desc()));
            flag.perm((String)loadValue(cfg, name + ".flags." + flag.name() + ".permission", flag.perm()));
            cmd.getFlags().put(flag.name().toLowerCase(), flag);
        }

        //Modifier - description/permission
        List<Modifier> modifiers = new ArrayList<>(cmd.getModifiers().values());
        for (Modifier mod : modifiers) {
            if (mod.name().equals("page")) {
                continue;
            }
            mod.desc((String)loadValue(cfg, name + ".modifiers." + mod.name() + ".description", mod.desc()));
            mod.perm((String)loadValue(cfg, name + ".modifiers." + mod.name() + ".permission", mod.perm()));
            cmd.getModifiers().put(mod.name().toLowerCase(), mod);
        }

        //Argument - description/permission and sub commands.
        List<Argument> arguments = new ArrayList<>(cmd.getArguments().values());
        for (Argument arg : arguments) {
            arg.desc((String)loadValue(cfg, name + ".arguments." + arg.name() + ".description", arg.desc()));
            arg.perm((String)loadValue(cfg, name + ".arguments." + arg.name() + ".permission", arg.perm()));
            cmd.getArguments().put(arg.name().toLowerCase(), arg);

            //Load sub command data recursively
            if (arg.option() instanceof SubCmdO) {
                for (SubCmd sub : ((SubCmdO)arg.option()).getSubCmds()) {
                    if (!cfg.contains(name + ".arguments." + arg.name() + ".sub-commands")) {
                        cfg.createSection(name + ".arguments." + arg.name() + ".sub-commands");
                    }
                    loadData(cfg.getConfigurationSection(name + ".arguments." + arg.name() + ".sub-commands"), sub);
                }
            }
        }
    }

    private Object loadValue(ConfigurationSection cfg, String key, Object defaultValue) {
        if (!cfg.contains(key)) {
            cfg.set(key, defaultValue);
            return defaultValue;
        } else {
            return cfg.get(key);
        }
    }


    /**
     * Called when the command is executed.
     * <p/>
     * Do not override this method!
     * Override the {@link #onCommand(CmdData)} and use that to implement the command.
     * <p/>
     * This will use the {@link CmdParser} to parse the user input arguments and such.
     * It will be put all the results in the {@link CmdData} which is passed on in {@link #onCommand(CmdData)}
     * <p/>
     * If there are errors parsing the input the error message will be sent to the sender.
     *
     * @param sender The {@link CommandSender} who executed the command.
     * @param label The command label/name.
     * @param args The input argument array.
     * @return true
     */
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        CmdParser parser = new CmdParser(this, sender, label, args);

        //Show help
        if (parser.getData().hasFlag("?")) {
            parser.getCmd().showHelp(sender, label);
            return true;
        }

        //Show sub commands
        if (parser.getData().hasFlag("l")) {
            parser.getCmd().showSubCmds(sender, label, parser.getData().hasMod("page") ? (int)parser.getData().getMod("page") : 1);
            return true;
        }

        //Execute command or show error.
        if (parser.success()) {
            parser.getCmd().onCommand(parser.getData());
        } else {
            Msg.fromString(parser.getError()).send(sender);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        //No arguments
        if (getAllArguments().size() == 0) {
            return new ArrayList<>();
        }

        TabCompleteData data = null;

        if (args.length == 0 || args.length == 1 && args[0].trim().isEmpty()) {
            //Get option from first argument if nothing is specified yet.
            Argument arg = new ArrayList<>(getArguments().values()).get(0);
            data = new TabCompleteData(arg.name(), arg.option(), "");
        } else {
            //Parse the input
            CmdParser parser = new CmdParser(getBaseCmd(), sender, alias, args, false);
            data = parser.getTabCompleteData();

            //Get the first unparsed argument for blank input
            if (data == null) {
                String lastArg = args[args.length - 1];
                if (lastArg.trim().isEmpty()) {
                    List<Argument> arguments = new ArrayList<>(parser.getCmd().getAllArguments().values());
                    for (Argument arg : arguments) {
                        String argName = arg.name().toLowerCase();
                        if (!parser.getData().hasArg(argName) || parser.getData().getArg(argName) == null) {
                            data = new TabCompleteData(arg.name().toLowerCase(), arg.option(), lastArg);
                            break;
                        }
                    }
                }
            }
        }

        if (data == null) {
            return new ArrayList<>();
        }
        return data.getOption().onComplete(sender, data.getInput());
    }
}