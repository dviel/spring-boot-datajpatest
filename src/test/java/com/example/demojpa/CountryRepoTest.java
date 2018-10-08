package com.example.demojpa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RunWith(SpringRunner.class)
public class CountryRepoTest {

    @Autowired
    private CountryRepository countryRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createCountry_should_succeed() {
        Country country = countryRepository.save(new Country(null, "FR", "France"));
        assertThat(country.getId(), notNullValue());
    }

    @Test
    public void createCountry_should_failed_duplicate_abbreviation() {
        exception.expect(DataIntegrityViolationException.class);
        countryRepository.save(new Country(null, "FR", "France"));
        countryRepository.save(new Country(null, "FR", "France"));
    }

    @Test
    public void createCountry_should_failed_null_abbreviation() {
        exception.expect(TransactionSystemException.class);
        countryRepository.save(new Country(null, null, "France"));
    }

    @Test
    public void createCountry_should_failed_empty_abbreviation() {
        exception.expect(TransactionSystemException.class);
        countryRepository.save(new Country(null, "", "France"));

    }
}
