package aoc;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Day01}.
 */
class Day01Test
{
    @ParameterizedTest
    @CsvSource({
            "01-control.txt, true, 11",
            "01-data.txt, true, 1882714",
            "01-control.txt, false, 31",
            "01-data.txt, false, 19437052",
    })
    void testExecute(String resourceName, boolean part1, long value) throws URISyntaxException, IOException
    {
        assertThat(new Day01().execute(resourceName, part1)).isEqualTo(value);
    }
}