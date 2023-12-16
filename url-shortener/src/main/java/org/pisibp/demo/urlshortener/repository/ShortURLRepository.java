package org.pisibp.demo.urlshortener.repository;

import org.pisibp.demo.urlshortener.model.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortURLRepository extends JpaRepository<ShortURL, Long> {

    ShortURL findByLongUrl(String longUrl);

    Optional<ShortURL> findByShortUrl(String shortUrl);
}
