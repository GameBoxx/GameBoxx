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

package info.gameboxx.gameboxx.system.points;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import info.gameboxx.gameboxx.system.points.model.Currency;
import info.gameboxx.gameboxx.system.points.model.CurrencyHolder;


/**
 * Manages currency transactions within the server.g
 */
public class CurrencyManager {

    private Table<CurrencyHolder, Currency, Double> currencyTable = HashBasedTable.create();

    /**
     * Adds a an certain amount of {@link Currency} to {@link CurrencyHolder} account.
     * If an account doesn't exist, a {@link CurrencyHolder} account will get generated with a predefined amount.
     *
     * @param currencyHolder Holder of currency
     * @param currency       Currency type
     * @param amount         Amount to give
     */
    public void give(CurrencyHolder currencyHolder, String currency, double amount) {
        if (currencyTable.get(currencyHolder, Currency.forName(currency)) != null) {
            currencyTable.put(currencyHolder, Currency.forName(currency), currencyTable.get(currencyHolder, Currency.forName(currency)) + amount);
        } else {
            currencyTable.put(currencyHolder, Currency.forName(currency), amount);
        }
    }

    /**
     * Returns the amount of specified {@link Currency}
     *
     * @param currencyHolder Holder of currency
     * @param currency       Currency type
     * @return Amount of currency
     */
    public double get(CurrencyHolder currencyHolder, String currency) {
        return currencyTable.get(currencyHolder, Currency.forName(currency));
    }

    /**
     * Subtracts a specified amount from a {@link CurrencyHolder} account.
     * If the {@link Currency} value after subtraction is less then 0, no amount will be taken from the {@link CurrencyHolder} account.
     *
     * @param currencyHolder Holder of currency
     * @param currency Currency type
     * @param amount Amount to take
     * @return {@link Boolean} value
     */
    public boolean take(CurrencyHolder currencyHolder, String currency, double amount) {
        if (currencyTable.get(currencyHolder, Currency.forName(currency)) - amount < 0) {
            return false;
        }
        currencyTable.put(currencyHolder, Currency.forName(currency), currencyTable.get(currencyHolder, Currency.forName(currency)) - amount);
        return true;
    }

    /**
     * Sets the value of the {@link CurrencyHolder} account to a specified amount.
     *
     * @param currencyHolder Holder of currency
     * @param currency Currency type
     * @param amount Amount to set
     */
    public void set(CurrencyHolder currencyHolder, String currency, double amount) {
        currencyTable.put(currencyHolder, Currency.forName(currency), amount);
    }

}
