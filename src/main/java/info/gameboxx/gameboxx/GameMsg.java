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

    HELP(Cat.COMMAND, "&8======== &4&l/gameapi help &8========\n&6/gameapi [help] &8- &7Display this help page.\n&6/gameapi info &8- &7Display plugin information.\n" +
            "&6/gameapi reload &8- &7Reload configuration files.\n&6/gameapi wand &8- &7Get the selection wand."),
    RELOADED(Cat.COMMAND, "&6All configurations reloaded!"),

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
        COMMAND,
        ITEM_PARSER,
        SELECTION,
        GAME,
        ;
    }
}
