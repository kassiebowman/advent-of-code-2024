package aoc;

import com.google.common.base.Strings;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Day 5: Print Queue
 *
 * @see <a href="https://adventofcode.com/2024/day/5">AOC 2024 Day 5</a>
 */
public class Day05
{
    // Map of values to the list of update that must be after them
    private final Map<Integer, List<Integer>> afterKeyMap = new HashMap<>();

    // Map of values to the list of update that must be before them
    private final Map<Integer, List<Integer>> beforeKeyMap = new HashMap<>();

    // List of manual updates, where each update is a list of page numbers
    private final List<List<Integer>> updates = new ArrayList<>();

    long execute(String fileName, boolean part1) throws URISyntaxException, IOException
    {
        final List<String> lines = Utils.getInput(fileName);
        parseLines(lines);

        long result = 0;
        for (List<Integer> updateList : updates)
        {
            List<Integer> correctedList = null;
            boolean updateValid = true;
            for (int i = 0; i < updateList.size(); i++)
            {
                if (!isPageValid(updateList, i))
                {
                    if (!part1) correctedList = correctListOrder(updateList);
                    updateValid = false;
                    break;
                }
            }

            int medianIndex = updateList.size() / 2;
            if (updateValid && part1)
            {
                result += updateList.get(medianIndex);
            } else if (!updateValid && !part1)
            {
                result += correctedList.get(medianIndex);
            }
        }

        return result;
    }

    /**
     * Parses the input data into the ordering rules and update page lists.
     *
     * @param lines The lines of input data
     */
    void parseLines(List<String> lines)
    {
        int index = 0;
        String line = lines.get(index++);
        while (!Strings.isNullOrEmpty(line))
        {
            // Until a blank line is encountered, the lines are a mapping between pages that need to occur first and one
            // that needs to be after, in the format 47|53
            String[] pageStrings = line.split("\\|");
            int firstPage = Integer.parseInt(pageStrings[0]);
            int secondPage = Integer.parseInt(pageStrings[1]);

            afterKeyMap.computeIfAbsent(firstPage, k -> new ArrayList<>()).add(secondPage);
            beforeKeyMap.computeIfAbsent(secondPage, k -> new ArrayList<>()).add(firstPage);

            line = lines.get(index++);
        }

        for (int i = index; i < lines.size(); i++)
        {
            // In the second section, lines are a list of pages in a manual update, in the format 75,47,61,53,29
            line = lines.get(i);
            String[] pageStrings = line.split(",");
            updates.add(Arrays.stream(pageStrings).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
        }
    }

    /**
     * Determines if the page at the provided index in the update list is in the valid order based on the rules.
     *
     * @param updateList The list of pages updates
     * @param pageIndex  The index of the page to check
     * @return {@code true} if the page is valid.
     */
    boolean isPageValid(List<Integer> updateList, int pageIndex)
    {
        Integer page = updateList.get(pageIndex);

        // Make sure none of the after pages are before the page
        if (pageIndex > 0)
        {
            List<Integer> pagesThatMustBeAfter = afterKeyMap.get(page);
            List<Integer> pagesBeforeIndex = updateList.subList(0, pageIndex);

            if (pagesThatMustBeAfter != null && pagesBeforeIndex.stream().anyMatch(pagesThatMustBeAfter::contains))
            {
                return false;
            }
        }

        // Make sure none of the before pages are after the page
        int numPages = updateList.size();
        if (pageIndex < numPages - 1)
        {
            List<Integer> pagesThatMustBeBefore = beforeKeyMap.get(page);
            List<Integer> pagesAfterIndex = updateList.subList(pageIndex + 1, numPages);

            return pagesThatMustBeBefore == null || pagesAfterIndex.stream().noneMatch(pagesThatMustBeBefore::contains);
        }

        return true;
    }

    /**
     * Reorders the list so the pages are in the correct order according ot the ordering rules.
     *
     * @param updateList The list
     * @return The correctly ordered list.
     */
    List<Integer> correctListOrder(List<Integer> updateList)
    {
        List<Integer> newList = new ArrayList<>(updateList);

        newList.sort((page1, page2) -> {
            List<Integer> pagesThatMustBeAfter = afterKeyMap.get(page1);
            List<Integer> pagesThatMustBeBefore = beforeKeyMap.get(page1);

            if (pagesThatMustBeAfter != null && pagesThatMustBeAfter.contains(page2)) return -1;
            if (pagesThatMustBeBefore != null && pagesThatMustBeBefore.contains(page2)) return 1;

            return 0;
        });

        return newList;
    }
}