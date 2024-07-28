package com.semillero.ecosistemas.utils;

import com.semillero.ecosistemas.model.Country;
import com.semillero.ecosistemas.repository.ICountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CountryDataLoader implements CommandLineRunner {

    @Autowired
    private ICountryRepository countryRepository;

    @Override
    public void run(String... args) throws Exception {
        loadCountryData();
    }

    private void loadCountryData() {
        if (countryRepository.count() == 0) {
            Country argentina = new Country();
            argentina.setName("Argentina");

            Country bolivia = new Country();
            bolivia.setName("Bolivia");

            Country brazil = new Country();
            brazil.setName("Brazil");

            Country chile = new Country();
            chile.setName("Chile");

            Country colombia = new Country();
            colombia.setName("Colombia");

            Country ecuador = new Country();
            ecuador.setName("Ecuador");

            Country paraguay = new Country();
            paraguay.setName("Paraguay");

            Country peru = new Country();
            peru.setName("Peru");

            countryRepository.save(argentina);
            countryRepository.save(bolivia);
            countryRepository.save(brazil);
            countryRepository.save(chile);
            countryRepository.save(colombia);
            countryRepository.save(ecuador);
            countryRepository.save(paraguay);
            countryRepository.save(peru);
        }
    }
}