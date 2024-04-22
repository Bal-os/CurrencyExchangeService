package os.balashov.currencyexchangeservice.infrastructure.provider.mock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import os.balashov.currencyexchangeservice.domain.builder.CurrencyRateBuilder;
import os.balashov.currencyexchangeservice.domain.datasource.CurrencyRateProvider;
import os.balashov.currencyexchangeservice.domain.entity.Currency;
import os.balashov.currencyexchangeservice.domain.entity.CurrencyRate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Profile("mock")
@Component
public class MockProvider implements CurrencyRateProvider {
    private final List<Currency> currencies = Arrays.asList(
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
    private final Random random = new Random();

    public List<CurrencyRate> getCurrentRates() {
        LocalDate currentDate = LocalDate.now();
        log.info("Getting mock currency rates for today ({})", currentDate);
        Map<Currency, CurrencyRate> currencyRates = new HashMap<>();
        for (int i = 0; i < random.nextInt(currencies.size()); i++) {
            Currency randomCurrency = currencies.get(random.nextInt(currencies.size()));
            CurrencyRate currencyRate = CurrencyRateBuilder.builder()
                    .currencyCode(randomCurrency.currencyCode())
                    .currencyName(randomCurrency.name())
                    .currencyNumberCode(randomCurrency.currencyCode())
                    .currencyRate(random.nextDouble() * 100)
                    .currencyDate(currentDate)
                    .receivingTime(LocalDateTime.now())
                    .build();
            currencyRates.put(randomCurrency, currencyRate);
        }
        return currencyRates.values().stream().toList();
    }
}
