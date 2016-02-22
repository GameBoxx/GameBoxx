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

package info.gameboxx.gameboxx;

import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.config.messages.MessageCfg;
import info.gameboxx.gameboxx.config.messages.Param;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public enum GameMsg {
    NO_PERMISSION(Cat.GENERAL, "&cInsufficient permissions."),
    PLAYER_COMMAND(Cat.GENERAL, "&cThis is a player command only."),
    INVALID_USAGE(Cat.GENERAL, "&cInvalid usage! &7{usage}"),
    INVALID_ONLINE_PLAYER(Cat.GENERAL, "&cInvalid player name specified! &7(The player must be online!)"),

    INVALID_GAME(Cat.ERROR, "&cThere is no game registered with the name &4{name}&c! &8(&8Games: &7{games}&8)"),
    INVALID_ARENA(Cat.ERROR, "&cThere is no arena registered with the name &4{name}&c! &8(&8Arenas: &7{arenas}&8)"),
    NO_ARENA_SELECTION(Cat.ERROR, "&cYou don't have an arena selected! &7/select {game} {arena}"),
    ARENA_NAME_ALREADY_EXISTS(Cat.ERROR, "&cAn arena with the name &4{name} &calready exists! &8(&8Arenas: &7{arenas}&8)"),
    INVALID_ARENA_TYPE(Cat.ERROR, "&4{type} &cis not a valid arena type! &7Use either default, world or generateworld"),

    HELP(Cat.COMMAND, "&8======== &4&l/gameapi help &8========\n&6/gameapi [help] &8- &7Display this help page.\n&6/gameapi info &8- &7Display plugin information.\n" +
            "&6/gameapi reload &8- &7Reload configuration files.\n&6/gameapi wand &8- &7Get the selection wand."),
    RELOADED(Cat.COMMAND, "&6All configurations reloaded!"),
    ARENA_SELECTED(Cat.COMMAND, "&6You've selected the &a{arena} &6arena for &a{game}&6!"),

    NO_ITEM_SPECIFIED(Cat.ITEM_PARSER, "&cNo item specified!"),
    UNKNOWN_ITEM_NAME(Cat.ITEM_PARSER, "&cThe item &4{input} &cis not a valid item!"),
    MISSING_META_VALUE(Cat.ITEM_PARSER, "&cNo value specified for meta &4{meta}&c!"),
    NOT_A_NUMBER(Cat.ITEM_PARSER, "&4{input} &cis not a number!"),
    INVALID_COLOR(Cat.ITEM_PARSER, "&4{input} &cis not a valid color!"),
    INVALID_DYE_COLOR(Cat.ITEM_PARSER, "&4{input} &cis not a valid dye color!"),
    INVALID_FIREWORK_SHAPE(Cat.ITEM_PARSER, "&4{input} &cis not a valid firework shape!"),
    MISSING_FIREWORK_SHAPE(Cat.ITEM_PARSER, "&cTo create a firework effect, you need to specify the shape!"),
    MISSING_FIREWORK_COLOR(Cat.ITEM_PARSER, "&cTo create a firework effect, you need to set at least one color!"),
    INVALID_ENCHANT_VALUE(Cat.ITEM_PARSER, "&4{input} &cis not a valid enchantment level."),
    INVALID_POTION_VALUE(Cat.ITEM_PARSER, "&4{input} &cis not a valid potion effect value. It should be {duration}.{amplifier}&c."),

    WAND_NAME(Cat.SELECTION, "&6&lSelection Wand"),
    WAND_LORE(Cat.SELECTION, "&7&aLeft click &7a block to set the first position.\n&eRight click &7a block to set the second position."),
    SELECTED(Cat.SELECTION, "&a{type} &6selected! &8(&c{x}&8,&a{y}&8,&9{z} &8- &7{material}&8:&e{data}&8)"),
    POS_1(Cat.SELECTION, "Pos 1"),
    POS_2(Cat.SELECTION, "Pos 2"),

    COUNTDOWN(Cat.GAME, "&a&l{seconds} &6&lseconds till the game starts!"),

    OPT_ALWAYS_JOIN(Cat.OPTION_DESC, "When set to true players will be able able to join while the game is in progress."),
    OPT_COUNTDOWN(Cat.OPTION_DESC, "The amount of seconds to set on the countdown. (Can be set to -1 to disable)"),
    OPT_PERMISSION_BYPASS(Cat.OPTION_DESC, "When a permission node is set players with this permission will be able to bypass the player limit and join full games."),
    OPT_AUTO_SPECTATE(Cat.OPTION_DESC, "When set to true players will automatically join as spectator when trying to join a full game. (By default a message will be sent that the game is full)"),
    OPT_MAX_PLAYERS(Cat.OPTION_DESC, "The maximum amount of players allowed in the game."),
    OPT_MIN_PLAYERS(Cat.OPTION_DESC, "The minimum amount of players required to start the game."),
    OPT_LOBBY_SPAWN(Cat.OPTION_DESC, "The location where players will be teleported to when they join the lobby."),
    OPT_QUEUE_TIME(Cat.OPTION_DESC, "When this is not -1 it won't teleport players to the lobby till the countdown time has reached the set value. If it's set to 0 players won't get teleported to the lobby spawn at all."),
    OPT_LOBBY_FLY(Cat.OPTION_DESC, "Allows players to fly when inside the lobby. For example if the lobby spawn is within the arena players can fly around the arena before it starts."),
    OPT_COUNTDOWN_TIME(Cat.OPTION_DESC, "Time in seconds to count down from before the game starts."),
    OPT_COUNT_INTERVAL(Cat.OPTION_DESC, "Time in seconds between each count. (It will still tick down per second)"),
    OPT_COUNT_SECONDS(Cat.OPTION_DESC, "At which time in seconds change the interval to seconds."),
    OPT_COUNT_SOUND(Cat.OPTION_DESC, "Sound to play for each count. (Message can be set in message config)"),
    ;


    private Cat cat;
    private String msg;

    GameMsg(Cat cat, String msg) {
        this.cat = cat;
        this.msg = msg;
    }


    public String getDefault() {
        return msg;
    }

    public String getName() {
        return toString().toLowerCase().replace("_", "-");
    }

    public String getCategory() {
        return cat.toString().toLowerCase().replace("_", "-");
    }

    public String getMsg() {
        return MessageCfg.inst().getMessage(getCategory(), getName());
    }

    public String getMsg(Param... params) {
        return getMsg(false, false, params);
    }

    public String getMsg(boolean prefix, boolean color, Param... params) {
        String message = (prefix ? MessageCfg.inst().prefix : "") + getMsg();
        for (Param p : params) {
            message = message.replace(p.getParam(), p.toString());
        }
        if (color) {
            message = Str.color(message);
        }
        return message;
    }


    public void broadcast(Param... params) {
        broadcast(true, true, params);
    }

    public void broadcast(boolean prefix, boolean color, Param... params) {
        Bukkit.broadcastMessage(getMsg(prefix, color, params));
    }

    public void send(CommandSender sender, Param... params) {
        send(sender, true, true, params);
    }

    public void send(CommandSender sender, boolean prefix, boolean color, Param... params) {
        if (sender != null) {
            sender.sendMessage(getMsg(prefix, color, params));
        }
    }


    private enum Cat {
        GENERAL,
        ERROR,
        COMMAND,
        ITEM_PARSER,
        SELECTION,
        GAME,
        OPTION_DESC,
        ;
    }
}
