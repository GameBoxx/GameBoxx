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

package info.gameboxx.gameboxx.system.points.concurrent;


import info.gameboxx.gameboxx.GameBoxx;
import info.gameboxx.gameboxx.system.points.model.Currency;
import info.gameboxx.gameboxx.user.User;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Loader extends BukkitRunnable {

    private User user;
    private Currency currency;

    public Loader(User user, Currency currency) {
        this.user = user;
        this.currency = currency;
    }

    @Override
    public void run() {
        Connection connection = null;
        try {
            connection = GameBoxx.get().getHikariDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + currency.getName().toLowerCase() + " VALUES(?,?,?) ON DUPLICATE KEY UPDATE name=?");
            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setDouble(3, 0);
            preparedStatement.setString(4, user.getName());
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement("SELECT amount FROM " + currency.getName().toLowerCase() + " WHERE uuid=?");
            preparedStatement.setString(1, user.getUuid().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                GameBoxx.get().getCM().set(user, currency.getName(), resultSet.getInt("amount"));
            }
            preparedStatement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
