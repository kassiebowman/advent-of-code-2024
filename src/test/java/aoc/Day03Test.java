package aoc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Day03}.
 */
class Day03Test
{
    @ParameterizedTest
    @CsvSource({
//            "03-control.txt, true, 161",
//            "03-data.txt, true, 178538786",
//            "03-control2.txt, false, 48",
            "03-data.txt, false, 102467299",
    })
    void testExecute(String resourceName, boolean part1, long value) throws URISyntaxException, IOException
    {
        assertThat(new Day03().execute(resourceName, part1)).isEqualTo(value);
    }
}