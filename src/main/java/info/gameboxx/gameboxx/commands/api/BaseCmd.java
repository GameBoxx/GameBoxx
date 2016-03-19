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

import info.gameboxx.gameboxx.commands.api.data.Argument;
import info.gameboxx.gameboxx.commands.api.data.Flag;
import info.gameboxx.gameboxx.commands.api.data.Modifier;
import info.gameboxx.gameboxx.commands.api.parse.CmdParser;
import info.gameboxx.gameboxx.commands.api.parse.SubCmdO;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.options.SingleOption;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
 * {@link #addArgument(String, String, String, Argument.Requirement, SingleOption)}
 * {@link #addModifier(String, String, String, SingleOption)}
 * {@link #addFlag(String, String, String)}
 */
public abstract class BaseCmd extends Cmd {

    private File configFile = null;
    private YamlConfiguration config = null;

    /**
     * Construct a new command with the given name.
     * <p/>
     * The command won't have a description, permission or aliases.
     *
     * @param name The name/label of the command.
     * @param configFile A .yml {@link File} where the command can be configured.
     *                   May be {@code null} to prevent the user from modifying the command.
     */
    public BaseCmd(String name, File configFile) {
        this(name, new String[0], "", "", configFile);
    }

    /**
     * Construct a new command with the given name and aliases.
     * <p/>
     * The command won't have a description and permission.
     *
     * @param name The name/label of the command.
     * @param aliases Aliases for the command.
     * @param configFile A .yml {@link File} where the command can be configured.
     *                   May be {@code null} to prevent the user from modifying the command.
     */
    public BaseCmd(String name, String[] aliases, File configFile) {
        this(name, aliases, "", "", configFile);
    }

    /**
     * Construct a new command with the given name and description.
     * <p/>
     * The command won't have a permission or aliases.
     *
     * @param name The name/label of the command.
     * @param description The description which is shown in the help map and the command help.
     * @param configFile A .yml {@link File} where the command can be configured.
     *                   May be {@code null} to prevent the user from modifying the command.
     */
    public BaseCmd(String name, String description, File configFile) {
        this(name, new String[0], description, "", configFile);
    }

    /**
     * Construct a new command with the given name, aliases and description.
     * <p/>
     * The command won't have a permission.
     *
     * @param name The name/label of the command.
     * @param aliases Aliases for the command.
     * @param description The description which is shown in the help map and the command help.
     * @param configFile A .yml {@link File} where the command can be configured.
     *                   May be {@code null} to prevent the user from modifying the command.
     */
    public BaseCmd(String name, String[] aliases, String description, File configFile) {
        this(name, aliases, description, "", configFile);
    }

    /**
     * Construct a new command with the given name, description and permission.
     * <p/>
     * The command won't have aliases.
     *
     * @param name The name/label of the command.
     * @param description The description which is shown in the help map and the command help.
     * @param permission The permission which the sender must have to execute this command.
     * @param configFile A .yml {@link File} where the command can be configured.
     *                   May be {@code null} to prevent the user from modifying the command.
     */
    public BaseCmd(String name, String description, String permission, File configFile) {
        this(name, new String[0], description, permission, configFile);
    }

    /**
     * Construct a new command with the given name, aliases, description and permission.
     *
     * @param name The name/label of the command.
     * @param aliases Aliases for the command.
     * @param description The description which is shown in the help map and the command help.
     * @param permission The permission which the sender must have to execute this command.
     * @param configFile A .yml {@link File} where the command can be configured.
     *                   May be {@code null} to prevent the user from modifying the command.
     */
    public BaseCmd(String name, String[] aliases, String description, String permission, File configFile) {
        super(name, aliases, description, permission);
        this.configFile = configFile;

        addFlag("?", "Display the full command help.");
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
            cfg.set(name + ".aliases", cmd.getAliases());
        } else {
            cmd.setAliases(cfg.getStringList(name + ".aliases"));
        }

        //Command - description/permission
        cmd.setDescription((String)loadValue(cfg, name + ".description", cmd.getDescription()));
        cmd.setPermission((String)loadValue(cfg, name + ".permission", cmd.getPermission()));

        //Flag - description/permission
        List<Flag> flags = new ArrayList<>(cmd.getFlags().values());
        for (Flag flag : flags) {
            flag.setDescription((String)loadValue(cfg, name + ".flags." + flag.getName() + ".description", flag.getDescription()));
            flag.setPermission((String)loadValue(cfg, name + ".flags." + flag.getName() + ".permission", flag.getPermission()));
            cmd.getFlags().put(flag.getName().toLowerCase(), flag);
        }

        //Modifier - description/permission
        List<Modifier> modifiers = new ArrayList<>(cmd.getModifiers().values());
        for (Modifier mod : modifiers) {
            mod.setDescription((String)loadValue(cfg, name + ".modifiers." + mod.getName() + ".description", mod.getDescription()));
            mod.setPermission((String)loadValue(cfg, name + ".modifiers." + mod.getName() + ".permission", mod.getPermission()));
            cmd.getModifiers().put(mod.getName().toLowerCase(), mod);
        }

        //Argument - description/permission and sub commands.
        List<Argument> arguments = new ArrayList<>(cmd.getArguments().values());
        for (Argument arg : arguments) {
            arg.setDescription((String)loadValue(cfg, name + ".arguments." + arg.getName() + ".description", arg.getDescription()));
            arg.setPermission((String)loadValue(cfg, name + ".arguments." + arg.getName() + ".permission", arg.getPermission()));
            cmd.getArguments().put(arg.getName().toLowerCase(), arg);

            //Load sub command data recursively
            if (arg.getOption() instanceof SubCmdO) {
                for (SubCmd sub : ((SubCmdO)arg.getOption()).getSubCmds()) {
                    if (!cfg.contains(name + ".arguments." + arg.getName() + ".sub-commands")) {
                        cfg.createSection(name + ".arguments." + arg.getName() + ".sub-commands");
                    }
                    loadData(cfg.getConfigurationSection(name + ".arguments." + arg.getName() + ".sub-commands"), sub);
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
        CmdParser parser = new CmdParser(this, sender, args);
        if (parser.success()) {
            if (parser.getData().hasFlag("?")) {
                parser.getExecutor().showHelp(sender, label);
            } else {
                parser.getExecutor().onCommand(parser.getData());
            }
        } else {
            if ((parser.getData() != null && parser.getData().hasFlag("?")) || Arrays.asList(args).contains("-?")) {
                parser.getExecutor().showHelp(sender, label);
            } else {
                Msg.fromString(parser.getError()).send(sender);
            }
        }
        return true;
    }
}