/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 GameBoxx <http://gameboxx.info>
 *  Copyright (c) 2016 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package info.gameboxx.gameboxx.user;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;


public class UserManager {

    private final Map<UUID, User> usersById = Maps.newHashMap();
    private final Map<String, User> usersByName = Maps.newHashMap();

    /**
     * Register a new {@link User}
     * @param user {@link User} instance.
     */
    public void register(User user) {
        if (usersById.containsKey(user.getUuid()) || usersByName.containsKey(user.getName())) return;
        usersById.put(user.getUuid(), user);
        usersByName.put(user.getName(), user);
    }

    /**
     * Unregister a {@link User} by name.
     * @param name {@link User} name
     */
    public void unregister(String name) {
        name = name.trim();
        if (usersByName.containsKey(name)) {
            usersByName.remove(name);
        }
    }

    /**
     * Unregister a {@link User} by uuid.
     * @param uuid {@link User} id
     */
    public void unregister(UUID uuid) {
        if (usersById.containsKey(uuid)) {
            usersById.remove(uuid);
        }
    }

}
