package org.pisibp.demo.urlshortener.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class URLBase62ConverterTest {

    @ParameterizedTest
    @MethodSource("provideSingleDigitNumbersForConvert")
    public void testConvert_withSingleDigitNumber(long input, String expected) {
        Assertions.assertThat(URLBase62Converter.convert(input)).isEqualTo(expected);
    }

    @Test
    public void testConvert_withLargerNumbers() {
        Assertions.assertThat(URLBase62Converter.convert(62)).isEqualTo("00000000010");
        Assertions.assertThat(URLBase62Converter.convert(7_912)).isEqualTo("0000000023C");
        Assertions.assertThat(URLBase62Converter.convert(Long.MAX_VALUE)).isEqualTo("aZl8N0y58M7");
    }

/*
    @Test
    public void testConvert_withLargerNumbers() {
        org.junit.jupiter.api.Assertions.assertAll(
                () -> Assertions.assertThat(UrlBase62Converter.convert(62)).isEqualTo("0000010"),
                () -> Assertions.assertThat(UrlBase62Converter.convert(7_912)).isEqualTo("000023B"),
                () -> Assertions.assertThat(UrlBase62Converter.convert(Long.MAX_VALUE)).isEqualTo("aZl8N0y58M7")
        );
    }
*/

    private static Stream<Arguments> provideSingleDigitNumbersForConvert() {
        return Stream.of(
                Arguments.of(0, "0000000"),
                Arguments.of(1, "0000001"),
                Arguments.of(2, "0000002"),
                Arguments.of(3, "0000003"),
                Arguments.of(4, "0000004"),
                Arguments.of(5, "0000005"),
                Arguments.of(6, "0000006"),
                Arguments.of(7, "0000007"),
                Arguments.of(8, "0000008"),
                Arguments.of(9, "0000009")
        );

    }

}