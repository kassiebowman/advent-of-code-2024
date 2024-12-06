package aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Day 1: Historian Hysteria
 *
 * @see <a href="https://adventofcode.com/2024/day/1">AOC 2024 Day 1</a>
 */
public class Day01
{
    long execute(String fileName, boolean part1) throws URISyntaxException, IOException
    {
        final List<String> lines = Utils.getInput(fileName);
        List<Long> left = new ArrayList<>();
        List<Long> right = new ArrayList<>();
        Map<Long, Integer> rightCounts = new HashMap<>();
        lines.forEach(l -> {
            String[] stringArray = l.split("\\s+");
            left.add(Long.valueOf(stringArray[0]));
            Long rightValue = Long.valueOf(stringArray[1]);
            right.add(rightValue);
            rightCounts.merge(rightValue, 1, Integer::sum);
        });

        left.sort(null);
        right.sort(null);

        long value = 0;
        for (int i = 0; i < left.size(); i++)
        {
            if (part1)
            {
                // For part 1, compute the "distance" between the two lists by getting the difference between the sorted
                // elements and summing them
                value += Math.abs(left.get(i) - right.get(i));
            } else
            {
                // For part 2, compute the "similarity" by multiplying each value on the left by the number of
                // occurrences in the right list.
                Long leftValue = left.get(i);
                value += leftValue * rightCounts.getOrDefault(leftValue, 0);
            }
        }

        return value;
    }
}