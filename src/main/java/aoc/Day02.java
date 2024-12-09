package aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Day 2: Red-Nosed Reports
 *
 * @see <a href="https://adventofcode.com/2024/day/2">AOC 2024 Day 2</a>
 */
public class Day02
{
    long execute(String fileName, boolean part1) throws URISyntaxException, IOException
    {
        final List<String> reports = Utils.getInput(fileName);
        List<List<Long>> levelReports = new ArrayList<>();
        reports.forEach(r -> {
            String[] stringArray = r.split("\\s+");

            List<Long> levels = new ArrayList<>();
            for (String levelString : stringArray)
            {
                levels.add(Long.valueOf(levelString));
            }

            levelReports.add(levels);
        });

        long safeCount = 0;
        for (List<Long> levelReport : levelReports)
        {
            // First determine if the expected direction is increasing or decreasing
            Long firstLevel = levelReport.get(0);
            Long lastLevel = levelReport.get(levelReport.size() - 1);

            boolean shouldIncrease = firstLevel < lastLevel;
            if (!part1)
            {
                // For part 2, a single value can be excluded to make the report valid, and since that value might be
                // the first or last value, it is necessary to also check additional values to determine if the report
                // is increasing or decreasing overall
                Long level2 = levelReport.get(1);
                Long level3 = levelReport.get(2);
                int increasingCount = 0;
                if (shouldIncrease) increasingCount++;
                if (firstLevel < level2) increasingCount++;
                if (level2 < level3) increasingCount++;
                if (level3 < lastLevel) increasingCount++;

                shouldIncrease = increasingCount >= 3;
            }

            boolean safe = true;
            boolean dampenerUsed = false;
            int dampenedIndex = -1;
            Long previousLevel = firstLevel;
            for (int i = 1; i < levelReport.size(); i++)
            {
                Long currentLevel = levelReport.get(i);

                boolean levelsEqual = previousLevel.equals(currentLevel);
                boolean levelsIncrease = currentLevel > previousLevel;
                boolean levelsCloseEnough = Math.abs(previousLevel - currentLevel) <= 3;

                if (shouldIncrease != levelsIncrease || levelsEqual || !levelsCloseEnough)
                {
                    if (!part1 && !dampenerUsed)
                    {
                        dampenerUsed = true;
                        dampenedIndex = i - 1;

                        // Since they are equal, it doesn't matter which one we remove, so just remove the current
                        if (levelsEqual) {
                            dampenedIndex = i;
                            continue;
                        }

                        // Test which value we should try removing to resolve the issue
                        Long formerPreviousLevel = i >= 2 ? levelReport.get(i - 2) : null;
                        Long nextLevel = (i < levelReport.size() - 1) ? levelReport.get(i + 1) : null;

                        // If the current value is the last one anyway, just remove it
                        if (nextLevel == null) {
                            dampenedIndex = i;
                            continue;
                        }

                        if (formerPreviousLevel == null)
                        {
                            // If the values are in the right direction but too far apart, if the next value is in the
                            // right direction (increasing or decreasing), just drop the previous value. Otherwise,
                            // drop the current value.
                            if (shouldIncrease == levelsIncrease)
                            {
                                boolean nextLevelsIncrease = nextLevel > currentLevel;
                                if (shouldIncrease != nextLevelsIncrease) {
                                    dampenedIndex = i;
                                    continue;
                                }
                            } else
                            {
                                // If the values are in the wrong direction, we need to check the next value: if it is
                                // in the right direction from the previous value, drop the current value; otherwise,
                                // drop the previous value.
                                boolean nextLevelIncreasesFromPrevious = nextLevel > previousLevel;
                                if (shouldIncrease == nextLevelIncreasesFromPrevious) {
                                    dampenedIndex = i;
                                    continue;
                                }
                            }
                        } else if (!levelsCloseEnough)
                        {
                            // When too far apart with a former previous value present, always skip the current value
                            // (since there is no chance it will be any closer to older values.
                            dampenedIndex = i;
                            continue;
                        } else {
                            // When there are both former previous and next values, and the current value is the wrong
                            // direction from the previous, the adjacent values must be checked
                            boolean currentLevelIncreasesFromFormer = currentLevel > formerPreviousLevel;
                            boolean nextLevelIncreasesFromPrevious = nextLevel > previousLevel;
                            if (shouldIncrease == currentLevelIncreasesFromFormer) {
                                if (shouldIncrease == nextLevelIncreasesFromPrevious) {
                                    // If removing either value fixes the direction, it is necessary to check which one
                                    // steps down the most gradually
                                    boolean nextAndCurrentLevelsCloseEnough = Math.abs(nextLevel - currentLevel) <= 3;
                                    boolean nextAndPreviousLevelsCloseEnough = Math.abs(previousLevel - nextLevel) <= 3;
                                    if (nextAndPreviousLevelsCloseEnough)
                                    {
                                        dampenedIndex = i;
                                        continue;
                                    }
                                }

                            } else {
                                dampenedIndex = i;
                                continue;
                            }
                        }
                    } else
                    {
                        safe = false;
                        break;
                    }
                }

                previousLevel = currentLevel;
            }

            System.out.println((shouldIncrease ? "Inc" : "Dec") + " : " + (safe ? "S" : "U") + " : "
                    + (dampenerUsed ? dampenedIndex : "-") + " : " + levelReport);

            if (safe)
            {
                safeCount++;
            }
        }

        return safeCount;
    }
}