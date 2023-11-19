package org.pisibp.demo.urlshortener.repository;

import jakarta.validation.ConstraintViolationException;
import org.pisibp.demo.urlshortener.model.ShortUrl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    ShortUrl findByLongUrl(String longUrl);

    Optional<ShortUrl> findByShortUrl(String shortUrl);

    default ShortUrl trySave(ShortUrl shortUrl) {
        try {
            return save(shortUrl);
        } catch (Exception e) {
            if (e instanceof ConstraintViolationException || e instanceof DataIntegrityViolationException) {
                throw new RuntimeException("Blbalb");
            }
            throw new RuntimeException("AHAH");
        }
    }
}
