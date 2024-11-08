package ru.mirea.sevostyanov.stonks;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class StonksClient {
    public static void main(String[] args) {
        try {
            DatabaseService databaseService = new DatabaseServiceImpl();

            Retrofit client = new Retrofit.Builder()
                    .baseUrl("https://www.cbr.ru")
                    .addConverterFactory(JacksonConverterFactory.create(new XmlMapper()))
                    .build();

            LocalDate birthDate = LocalDate.of(2003, 4, 27);

            StonksService stonksService = client.create(StonksService.class);
            Response<DailyCurs> response = stonksService
                    .getDailyCurs(birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).execute();

            if (!response.isSuccessful() || response.body() == null) {
                System.out.println("Ошибка при получении данных с сервера.");
                return;
            }

            DailyCurs dailyCurs = response.body();

            Optional<Valute> maxValute = dailyCurs.getValutes().stream()
                    .filter(valute -> !valute.getName().equals("СДР (специальные права заимствования)"))
                    .max(Comparator.comparingDouble(Valute::getValue));

            if (maxValute.isPresent()) {
                Valute mv = maxValute.get();
                databaseService.saveMaxValuteOfDate("sevostyanov", mv, birthDate);
                System.out.println("Самая дорогая валюта на дату рождения: " + mv);
            } else {
                System.out.println("Не удалось найти данные о валюте на указанную дату.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
