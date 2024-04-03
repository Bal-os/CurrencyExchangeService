package os.balashov.currencyexchangeservice.utils;

import os.balashov.currencyexchangeservice.domain.builder.CurrencyRateBuilder;
import os.balashov.currencyexchangeservice.domain.entity.Currency;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public interface TestUtils {
    default List<Currency> currencies() {
        return Arrays.asList(
                new Currency("840", "USD", "Долар США"),
                new Currency("978", "EUR", "Євро"),
                new Currency("392", "JPY", "Японська ієна"),
                new Currency("826", "GBP", "Фунт стерлінгів"),
                new Currency("036", "AUD", "Австралійський долар"),
                new Currency("124", "CAD", "Канадський долар"),
                new Currency("756", "CHF", "Швейцарський франк"),
                new Currency("156", "CNY", "Китайський юань"),
                new Currency("752", "SEK", "Шведська крона"),
                new Currency("578", "NOK", "Норвезька крона"),
                new Currency("208", "DKK", "Данська крона"),
                new Currency("554", "NZD", "Новозеландський долар"),
                new Currency("702", "SGD", "Сінгапурський долар"),
                new Currency("344", "HKD", "Гонконгський долар"),
                new Currency("484", "MXN", "Мексиканський песо"),
                new Currency("986", "BRL", "Бразильський реал"),
                new Currency("710", "ZAR", "Південноафриканський ранд"),
                new Currency("356", "INR", "Індійська рупія"),
                new Currency("949", "TRY", "Турецька ліра"),
                new Currency("410", "KRW", "Південнокорейська вона"),
                new Currency("764", "THB", "Тайський бат"),
                new Currency("360", "IDR", "Індонезійська рупія"),
                new Currency("458", "MYR", "Малайзійський рингіт"),
                new Currency("608", "PHP", "Філіппінський песо"),
                new Currency("348", "VND", "В'єтнамський донг"),
                new Currency("784", "AED", "Дирхам ОАЕ"),
                new Currency("682", "SAR", "Саудівський ріал"),
                new Currency("818", "EGP", "Єгипетський фунт"),
                new Currency("376", "ILS", "Ізраїльський шекель"),
                new Currency("901", "TWD", "Новий тайванський долар"),
                new Currency("512", "PLN", "Польський злотий"));
    }

    default Map<String, Currency> codeCurrencyMap() {
        Map<String, Currency> codeCurrencyMap = new ConcurrentHashMap<>();
        List<Currency> res = currencies().parallelStream()
                .map(currency -> codeCurrencyMap.put(currency.currencyCode(), currency))
                .toList();
        return codeCurrencyMap;
    }

    default List<CurrencyRate> createMockCurrencyRates(LocalDate date) {
        LocalDateTime receivingTime = date.atStartOfDay().plusHours(12);
        return currencies().stream()
                .map(currency -> CurrencyRateBuilder.builder()
                        .currency(currency)
                        .currencyDate(date)
                        .currencyRate(new Random().nextDouble())
                        .receivingTime(receivingTime)
                        .build())
                .toList();
    }
}