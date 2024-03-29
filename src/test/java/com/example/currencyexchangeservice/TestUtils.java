package com.example.currencyexchangeservice;

import os.balashov.currencyexchangeservice.domain.entity.Currency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface TestUtils {
    default List<Currency> currencies() {
        return Arrays.asList(
                new Currency("840", "USD", "Долар США", "Долар Сполучених Штатів Америки"),
                new Currency("978", "EUR", "Євро", "Євро"),
                new Currency("392", "JPY", "Японська ієна", "Японська ієна"),
                new Currency("826", "GBP", "Фунт стерлінгів", "Фунт стерлінгів"),
                new Currency("036", "AUD", "Австралійський долар", "Австралійський долар"),
                new Currency("124", "CAD", "Канадський долар", "Канадський долар"),
                new Currency("756", "CHF", "Швейцарський франк", "Швейцарський франк"),
                new Currency("156", "CNY", "Китайський юань", "Китайський юань"),
                new Currency("752", "SEK", "Шведська крона", "Шведська крона"),
                new Currency("578", "NOK", "Норвезька крона", "Норвезька крона"),
                new Currency("208", "DKK", "Данська крона", "Данська крона"),
                new Currency("554", "NZD", "Новозеландський долар", "Новозеландський долар"),
                new Currency("702", "SGD", "Сінгапурський долар", "Сінгапурський долар"),
                new Currency("344", "HKD", "Гонконгський долар", "Гонконгський долар"),
                new Currency("484", "MXN", "Мексиканський песо", "Мексиканський песо"),
                new Currency("986", "BRL", "Бразильський реал", "Бразильський реал"),
                new Currency("710", "ZAR", "Південноафриканський ранд", "Південноафриканський ранд"),
                new Currency("356", "INR", "Індійська рупія", "Індійська рупія"),
                new Currency("949", "TRY", "Турецька ліра", "Турецька ліра"),
                new Currency("410", "KRW", "Південнокорейська вона", "Південнокорейська вона"),
                new Currency("764", "THB", "Тайський бат", "Тайський бат"),
                new Currency("360", "IDR", "Індонезійська рупія", "Індонезійська рупія"),
                new Currency("458", "MYR", "Малайзійський рингіт", "Малайзійський рингіт"),
                new Currency("608", "PHP", "Філіппінський песо", "Філіппінський песо"),
                new Currency("348", "VND", "В'єтнамський донг", "В'єтнамський донг"),
                new Currency("784", "AED", "Дирхам ОАЕ", "Дирхам Об'єднаних Арабських Еміратів"),
                new Currency("682", "SAR", "Саудівський ріал", "Саудівський ріал"),
                new Currency("818", "EGP", "Єгипетський фунт", "Єгипетський фунт"),
                new Currency("376", "ILS", "Ізраїльський шекель", "Ізраїльський шекель"),
                new Currency("624", "THB", "Тайський бат", "Тайський бат"),
                new Currency("901", "TWD", "Новий тайванський долар", "Новий тайванський долар"),
                new Currency("270", "MXN", "Мексиканський песо", "Мексиканський песо"),
                new Currency("512", "PLN", "Польський злотий", "Польський злотий"));
    }

    default Map<String, Currency> codeCurrencyMap() {
        Map<String, Currency> codeCurrencyMap = new ConcurrentHashMap<>();
        List<Currency> res = currencies().parallelStream()
                .map(currency -> codeCurrencyMap.put(currency.currencyCode(), currency))
                .toList();
        return codeCurrencyMap;
    }
}