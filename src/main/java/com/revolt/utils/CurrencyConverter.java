package com.revolt.utils;

import com.revolt.enums.Currency;
import com.revolt.models.Account;

import javax.xml.transform.TransformerException;
import java.net.MalformedURLException;

public class CurrencyConverter {

    public static double convertCurrency(double transferSum, Account sndAcc, Account rcvAcc) throws TransformerException, MalformedURLException {

        double finalTransferSum = 0;

        if (sndAcc.getCurrency().equals(Currency.RUB) && rcvAcc.getCurrency().equals(Currency.USD)) {
            finalTransferSum = transferSum / RatesExtractor.getActualRates().get(Currency.USD);
        } else if (sndAcc.getCurrency().equals(Currency.RUB) && rcvAcc.getCurrency().equals(Currency.GBP)) {
            finalTransferSum = transferSum / RatesExtractor.getActualRates().get(Currency.GBP);
        } else if (sndAcc.getCurrency().equals(Currency.USD) && rcvAcc.getCurrency().equals(Currency.RUB)) {
            finalTransferSum = transferSum * RatesExtractor.getActualRates().get(Currency.USD);
        } else if (sndAcc.getCurrency().equals(Currency.USD) && rcvAcc.getCurrency().equals(Currency.GBP)) {
            finalTransferSum = transferSum * RatesExtractor.getActualRates().get(Currency.USD) / RatesExtractor.getActualRates().get(Currency.GBP);
        } else if (sndAcc.getCurrency().equals(Currency.GBP) && rcvAcc.getCurrency().equals(Currency.RUB)) {
            finalTransferSum = transferSum * RatesExtractor.getActualRates().get(Currency.GBP);
        } else if (sndAcc.getCurrency().equals(Currency.GBP) && rcvAcc.getCurrency().equals(Currency.USD)) {
            finalTransferSum = transferSum * RatesExtractor.getActualRates().get(Currency.GBP) / RatesExtractor.getActualRates().get(Currency.USD);
        }

        return Double.parseDouble(String.format("%.2f", finalTransferSum));
    }

}
