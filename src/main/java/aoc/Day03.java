package aoc;

import com.google.common.base.Strings;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Day 3: Mull It Over
 *
 * @see <a href="https://adventofcode.com/2024/day/3">AOC 2024 Day 3</a>
 */
public class Day03
{
    long execute(String fileName, boolean part1) throws URISyntaxException, IOException
    {
        final List<String> lines = Utils.getInput(fileName);

        long result = 0;
        boolean multiplicationEnabled = true;
        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|(do\\(\\))|(don't\\(\\))");
        for (String line : lines)
        {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find())
            {
                String doMul = matcher.group(3);
                String doNotMul = matcher.group(4);
                if (!Strings.isNullOrEmpty(doMul))
                {
                    multiplicationEnabled = true;
                } else if (!Strings.isNullOrEmpty(doNotMul))
                {
                    multiplicationEnabled = false;
                } else
                {
                    long leftValue = Integer.parseInt(matcher.group(1));
                    long rightValue = Integer.parseInt(matcher.group(2));
                    if (part1)
                    {
                        result += leftValue * rightValue;
                    } else if (multiplicationEnabled)
                    {
                        result += leftValue * rightValue;
                    }
                }
            }
        }

        return result;
    }
}