package org.pisibp.demo.urlshortener.repository;

import org.pisibp.demo.urlshortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    ShortUrl findByLongUrl(String longUrl);

    Optional<ShortUrl> findByShortUrl(String shortUrl);
}
