package org.pisibp.demo.urlshortener.repository;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pisibp.demo.urlshortener.model.ShortURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShortURLRepositoryTest extends DatabaseSetup {

    private static final Random RANDOM = new Random();

    @Autowired
    public ShortURLRepository shortUrlRepository;

    @AfterEach
    public void cleanDatabase() {
        shortUrlRepository.deleteAll();
    }

    @Test
    @DisplayName("Test saving of the short url.")
    public void testSave() {
        ShortURL shortUrl = ShortURL.builder()
                .id(RANDOM.nextLong())
                .longUrl(UUID.randomUUID().toString())
                .shortUrl(String.valueOf(RANDOM.nextLong(123456)))
                .build();

        ShortURL savedUrl = shortUrlRepository.save(shortUrl);

        AssertionsForClassTypes.assertThat(savedUrl.getId()).isEqualTo(shortUrl.getId());
        AssertionsForClassTypes.assertThat(savedUrl.getShortUrl()).isEqualTo(shortUrl.getShortUrl());
        AssertionsForClassTypes.assertThat(savedUrl.getLongUrl()).isEqualTo(shortUrl.getLongUrl());
    }

    @Test
    @DisplayName("Test find by long URL.")
    public void testFindByLongUrl() {
        final String longUrl = UUID.randomUUID().toString();

        ShortURL shortUrl = ShortURL.builder()
                .id(RANDOM.nextLong())
                .longUrl(longUrl)
                .shortUrl(String.valueOf(RANDOM.nextLong(1111111L)))
                .build();
        shortUrl = shortUrlRepository.save(shortUrl);

        ShortURL foundUrl = shortUrlRepository.findByLongUrl(longUrl);
        AssertionsForClassTypes.assertThat(foundUrl).isEqualTo(shortUrl);
    }

    @Test
    @DisplayName("Test find by short URL.")
    public void testFindByShortUrl() {
        final String shortenedUrl = String.valueOf(RANDOM.nextLong(1111111L));

        ShortURL shortUrl = ShortURL.builder()
                .id(RANDOM.nextLong())
                .longUrl(UUID.randomUUID().toString())
                .shortUrl(shortenedUrl)
                .build();
        shortUrl = shortUrlRepository.save(shortUrl);

        Optional<ShortURL> foundUrlOptional = shortUrlRepository.findByShortUrl(shortenedUrl);

        AssertionsForClassTypes.assertThat(foundUrlOptional.isPresent()).isTrue();
        AssertionsForClassTypes.assertThat(foundUrlOptional.get()).isEqualTo(shortUrl);
    }

    @Test
    @DisplayName("Test find by short URL when the URL is not present.")
    public void testFindByShortUrlWhenUrlNotFound() {
        Optional<ShortURL> foundUrlOptional = shortUrlRepository.findByShortUrl(UUID.randomUUID().toString());

        AssertionsForClassTypes.assertThat(foundUrlOptional.isPresent()).isFalse();
    }
}