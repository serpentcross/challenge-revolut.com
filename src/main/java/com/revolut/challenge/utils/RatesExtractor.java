package com.revolut.challenge.utils;

import com.revolut.challenge.enums.Currency;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import javax.xml.transform.TransformerException;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;

public class RatesExtractor {

    public static Map<Currency, Double> getActualRates() throws TransformerException, MalformedURLException {

        Map<Currency, Double> rates = new HashMap<>();

        Currency currencySign;
        Double currencyPrice;

        boolean ratesWereExtracted = false;

        try {
            Document document = Jsoup.parse(new URL("http://www.cbr.ru/scripts/XML_daily.asp").openStream(), "UTF-8", "", Parser.xmlParser());
            Elements elements = document.getElementsByAttribute("id");
            for(Element e : elements) {
                for(Element child: e.children()) {
                    if(child.toString().contains(Currency.GBP.toString()) || child.toString().contains(Currency.USD.toString())) {
                        currencySign = Currency.valueOf(child.parent().getElementsByTag("charcode").get(0).text());
                        currencyPrice = Double.parseDouble(child.parent().getElementsByTag("value").get(0).text().replace(',','.'));
                        rates.put(currencySign, Double.parseDouble(String.format("%.2f", currencyPrice)));
                        ratesWereExtracted = true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!ratesWereExtracted) {          // let's use a dummy data if something goes wrong with remote rates;
            rates.put(Currency.GBP, 80.1);
            rates.put(Currency.USD, 57.7);
        }

        return rates;
    }

}
