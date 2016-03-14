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

package info.gameboxx.gameboxx.util.entity;

import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.aliases.EntityTypes;
import info.gameboxx.gameboxx.messages.Msg;
import info.gameboxx.gameboxx.messages.Param;
import info.gameboxx.gameboxx.options.SingleOption;
import info.gameboxx.gameboxx.options.single.*;
import info.gameboxx.gameboxx.util.Str;
import info.gameboxx.gameboxx.util.Utils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityParser {

    private static final int MAX_AMOUNT = 100;

    private String string = null;
    private EntityStack entities = new EntityStack();

    private String error = null;

    /**
     * Parses the given entity string in to an {@link EEntity}.
     * It uses {@link EntityTag}s for parsing the string.
     * <p/>
     * If ignoreErrors is set to true it will still set the errors but it will try to continue parsing the rest.
     * It would still fail in some cases for example if there is an invalid entity specified or if there is no input.
     *
     * @param string entity string with all entity data from {@link EntityTag}s.
     * @param sender optional sender used for parsing (may be {@code null}) will be used for options parsing like @ and such.
     * @param ignoreErrors If true it will continue parsing even when there is an error.
     */
    public EntityParser(String string, CommandSender sender, boolean ignoreErrors) {
        this(string, sender, ignoreErrors, MAX_AMOUNT, true);
    }

    /**
     * Parses the given entity string in to an {@link EEntity}.
     * It uses {@link EntityTag}s for parsing the string.
     * <p/>
     * If ignoreErrors is set to true it will still set the errors but it will try to continue parsing the rest.
     * It would still fail in some cases for example if there is an invalid entity specified or if there is no input.
     *
     * @param string entity string with all entity data from {@link EntityTag}s.
     * @param sender optional sender used for parsing (may be {@code null}) will be used for options parsing like @ and such.
     * @param ignoreErrors If true it will continue parsing even when there is an error.
     * @param maxAmount The maximum amount that can be set/spawned. (this does not count stacked entities) (set to null to use the default=100)
     * @param allowStacked If true entities will be stacked and if false entities can't be stacked.
     */
    public EntityParser(String string, CommandSender sender, boolean ignoreErrors, Integer maxAmount, boolean allowStacked) {
        this.string = string;
        if (string == null || string.isEmpty()) {
            error = Msg.getString("entityparser.no-input");
            return;
        }

        //Split the string by > for stacked entities. (> within quotes will be ignored)
        List<String> entitySections = Str.splitIgnoreQuoted(string, '>', true);
        if (entitySections.size() < 1) {
            error = Msg.getString("entityparser.no-input");
            return;
        }

        if (!allowStacked && entitySections.size() > 1) {
            error = Msg.getString("entityparser.cant-stack");
            return;
        }

        //Get location and amount
        Location location = null;
        int amount = 1;
        for (String entitySection : entitySections) {
            List<String> sections = Str.splitQuotes(string, ' ', false);
            for (String section : sections) {
                String[] split = section.split(":", 2);
                if (split.length < 2) {
                    continue;
                }
                if (split[0].equalsIgnoreCase("LOC") || split[0].equalsIgnoreCase("LOCATION")) {
                    LocationO locOpt = new LocationO();
                    if (locOpt.parse(sender, split[1])) {
                        location = locOpt.getValue();
                    } else {
                        error = locOpt.getError();
                        if (!ignoreErrors) {
                            return;
                        }
                    }
                } else if (split[0].equalsIgnoreCase("AMT") || split[0].equalsIgnoreCase("AMOUNT")) {
                    IntO amtOpt = new IntO().min(1).max(maxAmount == null ? MAX_AMOUNT : maxAmount);
                    if (amtOpt.parse(split[1])) {
                        amount = amtOpt.getValue();
                    } else {
                        error = amtOpt.getError();
                        if (!ignoreErrors) {
                            return;
                        }
                    }
                }
            }
        }

        if (location == null) {
            error = Msg.getString("entityparser.no-location");
            return;
        }

        EntityStack stack = new EntityStack();

        //Go through all the entity sections.
        for (String entitySection : entitySections) {
            //Split the string by spaces keeping quoted strings together to get all the sections for tags.
            List<String> sections = Str.splitQuotes(entitySection, ' ', false);

            EEntity entity = null;

            //Go through all the sections.
            for (int i = 0; i < sections.size(); i++) {
                String section = sections.get(i);

                //Get entity from first section
                if (i == 0) {
                    EntityType type = EntityTypes.get(section);
                    if (type == null) {
                        error = Msg.getString("entityparser.invalid-entity", Param.P("input", section), Param.P("entities", Utils.getAliasesString("entityparser.entities.entry", EntityTypes.getAliasMap())));
                        stack.killAll();
                        return;
                    }
                    entity = new EEntity(type, location);
                    if (entity == null || entity.bukkit() == null) {
                        error = Msg.getString("entityparser.cant-spawn");
                        stack.killAll();
                        return;
                    }
                    continue;
                }

                String[] split = section.split(":", 2);
                String key = split[0].toUpperCase().replace("_", "").replace(" ", "");
                String value = split.length > 1 ? split[1] : ""; //Allow key only

                if (key.equals("AMT") || key.equals("AMOUNT") || key.equals("LOC") || key.equals("LOCATION")) {
                    continue;
                }

                //Try to parse EntityTag
                EntityTag tag = EntityTag.fromString(key);
                if (tag == null) {
                    error = Msg.getString("parser.invalid-tag", Param.P("tag", key), Param.P("type", Msg.getString("entityparser.type")),
                            Param.P("tags", Utils.getAliasesString("parser.tag-entry", EntityTag.getTagsMap(entity.getType()))));
                    if (!ignoreErrors) {
                        entity.remove();
                        stack.killAll();
                        return;
                    } else {
                        continue;
                    }
                }

                //Make sure the entity tag can be used for the entity.
                if (!EntityTag.getTags(entity.getType()).contains(tag)) {
                    error = Msg.getString("parser.unusable-tag", Param.P("tag", key), Param.P("type", Msg.getString("entityparser.type")),
                            Param.P("tags", Utils.getAliasesString("parser.tag-entry", EntityTag.getTagsMap(entity.getType()))));
                    if (!ignoreErrors) {
                        entity.remove();
                        stack.killAll();
                        return;
                    } else {
                        continue;
                    }
                }

                //Parse the value for the tag
                SingleOption option = (SingleOption)tag.getOption().clone();
                if (option instanceof BoolO && value.isEmpty()) {
                    value = "true"; //Allow empty tags for booleans like 'baby' instead of 'baby:true'
                }
                if (!option.parse(sender, value)) {
                    error = option.getError();
                    if (!ignoreErrors) {
                        entity.remove();
                        stack.killAll();
                        return;
                    } else {
                        continue;
                    }
                }

                //Apply the tag to the entity
                if (tag.hasCallback()) {
                    if (!tag.getCallback().onSet(sender, entity, option)) {
                        error = Msg.getString("parser.tag-fail", Param.P("tag", key), Param.P("value", value));
                        if (!ignoreErrors) {
                            entity.remove();
                            stack.killAll();
                            return;
                        } else {
                            continue;
                        }
                    }
                } else {
                    boolean success = false;
                    try {
                        MethodUtils.invokeMethod(entity, tag.setMethod(), option.getValue());
                        success = true;
                    } catch (NoSuchMethodException e) {
                        error = Msg.getString("parser.missing-method", Param.P("tag", key), Param.P("value", value),
                                Param.P("method", tag.setMethod() + "(" + option.getValue().getClass().getSimpleName() + ")"));
                    } catch (IllegalAccessException e) {
                        error = Msg.getString("parser.inaccessible-method", Param.P("tag", key), Param.P("value", value),
                                Param.P("method", tag.setMethod() + "(" + option.getValue().getClass().getSimpleName() + ")"));
                    } catch (InvocationTargetException e) {
                        error = Msg.getString("parser.non-invokable-method", Param.P("tag", key), Param.P("value", value),
                                Param.P("method", tag.setMethod() + "(" + option.getValue().getClass().getSimpleName() + ")"));
                    }
                    if (!success) {
                        if (!ignoreErrors) {
                            entity.remove();
                            stack.killAll();
                            return;
                        } else {
                            continue;
                        }
                    }
                }

                //Done with section!
            }

            //Done with entity!
            stack.add(entity);
        }

        //Done with all entities!
        if (stack.getAmount() < 1) {
            error = "No entities..";
            return;
        }
        stack.stack();
        this.entities = stack;
    }

    public EntityParser(Entity entity) {
        this(new EEntity(entity));
    }

    public EntityParser(EEntity eentity) {
        if (eentity == null || eentity.bukkit() == null) {
            return;
        }
        this.entities = new EntityStack(eentity);
        if (entities.getAmount() < 1) {
            return;
        }

        List<String> entitySections = new ArrayList<>();
        for (EEntity entity : entities.getEntities()) {
            List<String> sections = new ArrayList<>();

            sections.add(EntityTypes.getName(entity.getType()));
            for (EntityTag tag : EntityTag.getTags(entity.getType())) {
                if (tag.hasCallback()) {
                    String result = tag.getCallback().onGet(entity);
                    if (result == null || result.isEmpty()) {
                        continue;
                    }
                    if (!result.toLowerCase().contains(tag.getTag().toLowerCase() + ":")) {
                        result = Str.escapeWords(Str.escapeQuotes(result));
                    } else {
                        result = Str.escapeQuotes(result);
                    }
                    sections.add(tag.getTag().toLowerCase() + ":" + result);
                } else {
                    if (tag.getMethod() == null || tag.getMethod().isEmpty() || tag.getMethod().equalsIgnoreCase("null")) {
                        continue;
                    }
                    try {
                        Object result = MethodUtils.invokeMethod(entity, tag.getMethod(), new Class[0]);
                        if (result == null) {
                            continue;
                        }
                        SingleOption option = (SingleOption)tag.getOption().clone();
                        if (!option.parse(result)) {
                            GameBoxx.get().warn("Failed to parse entity data! [tag=" + tag.getTag() + " value=" + result.toString() + " error='" + option.getError() + "']");
                            continue;
                        }
                        if (option.getValue().equals(option.getDefault())) {
                            continue;
                        }
                        String val = option.serialize();
                        if (option instanceof DoubleO) {
                            val = ((DoubleO)option).serialize(2);
                        }
                        val = Str.escapeWords(Str.escapeQuotes(val));
                        sections.add(tag.getTag().toLowerCase() + ":" + val);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            entitySections.add(Str.implode(sections, " "));
        }

        LocationO location = new LocationO();
        location.parse(entities.getBottom().getLocation());

        this.string = Str.implode(entitySections, " > ") + " loc:" + location.serialize(2);
    }

    /**
     * Checks if the parsing was successful or not.
     * Call getError() if it wasn't successful and display it to the user.
     * @return if it parsed successful.
     */
    public boolean isValid() {
        return entities != null && entities.getAmount() > 0 && string != null && error == null;
    }

    /**
     * If the validation was unsuccessful this will return the error text.
     * @return the text which contains the error. If it was successful the text will be null.
     */
    public String getError() {
        return error;
    }

    /**
     * Get the parsed string value.
     * @return string which can be used in configurations and commands.
     */
    public String getString() {
        return string;
    }

    public EntityStack getEntities() {
        return entities;
    }
}
