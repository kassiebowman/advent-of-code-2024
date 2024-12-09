package aoc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Day 4: Ceres Search
 *
 * @see <a href="https://adventofcode.com/2024/day/4">AOC 2024 Day 4</a>
 */
public class Day04
{
    long execute(String fileName, boolean part1) throws URISyntaxException, IOException
    {
        final List<String> rows = Utils.getInput(fileName);
        int numRows = rows.size();
        int numColumns = rows.get(0).length();

        long result = 0;
        if (part1)
        {
            for (int r = 0; r < numRows; r++)
            {
                String row = rows.get(r);
                for (int c = 0; c < numColumns; c++)
                {
                    char character = row.charAt(c);
                    if (character == 'X')
                    {
                        result += findXmasInstances(rows, numRows, numColumns, r, c);
                    }
                }
            }
        } else
        {
            for (int r = 1; r < numRows - 1; r++)
            {
                String row = rows.get(r);
                for (int c = 1; c < numColumns - 1; c++)
                {
                    char character = row.charAt(c);
                    if (character == 'A')
                    {
                        result += findCrossedMasInstances(rows, r, c);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Checks the provided rows for instances of "XMAS" in all directions starting at the specified row and column
     * indices.
     *
     * @param rows        The rows to search
     * @param numRows     The number of rows
     * @param numColumns  The number of columns
     * @param rowIndex    The row index to start the search
     * @param columnIndex The column index to start the search
     * @return The number of "XMAS" instances starting at the specified indices.
     */
    int findXmasInstances(List<String> rows, int numRows, int numColumns, int rowIndex, int columnIndex)
    {
        int instances = 0;

        // Check for horizontal, forward and back
        boolean enoughColumnsToTheRight = columnIndex < numColumns - 3;
        if (enoughColumnsToTheRight)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, 0, 1)) instances++;
        }

        boolean enoughColumnsToTheLeft = columnIndex >= 3;
        if (enoughColumnsToTheLeft)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, 0, -1)) instances++;
        }

        // Check for vertical, forward and back
        boolean enoughRowsBelow = rowIndex < numRows - 3;
        if (enoughRowsBelow)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, 1, 0)) instances++;
        }

        boolean enoughRowsAbove = rowIndex >= 3;
        if (enoughRowsAbove)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, -1, 0)) instances++;
        }

        // Check for diagonal in all four directions
        if (enoughColumnsToTheRight && enoughRowsBelow)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, 1, 1)) instances++;
        }

        if (enoughColumnsToTheRight && enoughRowsAbove)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, -1, 1)) instances++;
        }

        if (enoughColumnsToTheLeft && enoughRowsBelow)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, 1, -1)) instances++;
        }

        if (enoughColumnsToTheLeft && enoughRowsAbove)
        {
            if (isXmasInstance(rows, rowIndex, columnIndex, -1, -1)) instances++;
        }

        return instances;
    }

    /**
     * @return {@code true} if "XMAS" is present at the specified location
     */
    boolean isXmasInstance(List<String> rows, int rowIndex, int columnIndex, int rowIncrement, int columnIncrement)
    {
        return isInstance("XMAS", rows, rowIndex, columnIndex, rowIncrement, columnIncrement);
    }

    /**
     * @return {@code true} if "MAS" is present at the specified location
     */
    boolean isMasInstance(List<String> rows, int rowIndex, int columnIndex, int rowIncrement, int columnIncrement)
    {
        return isInstance("MAS", rows, rowIndex, columnIndex, rowIncrement, columnIncrement);
    }

    /**
     * Determines if there is an instance of the goal string
     *
     * @param goalString      The string for which to search
     * @param rows            The rows to search
     * @param rowIndex        The row index to start the search
     * @param columnIndex     The column index to start the search
     * @param rowIncrement    The increment/decrement value for navigating rows
     * @param columnIncrement The increment/decrement value for navigating columns
     * @return {@code true} if the goal string is present at the specified location
     */
    boolean isInstance(String goalString, List<String> rows, int rowIndex, int columnIndex, int rowIncrement, int columnIncrement)
    {
        int r = rowIndex;
        int c = columnIndex;
        for (int i = 0; i < goalString.length(); i++)
        {
            char goalChar = goalString.charAt(i);

            boolean isChar = rows.get(r).charAt(c) == goalChar;
            if (!isChar) return false;

            r += rowIncrement;
            c += columnIncrement;
        }

        return true;
    }

    /**
     * Checks the provided rows for instances of crossed "MAS" in all directions with an A at the specified row and column
     * indices.
     *
     * @param rows        The rows to search
     * @param rowIndex    The row index for the center of the crossed MAS
     * @param columnIndex The column index for the center of the crossed MAS
     * @return The number of crossed "MAS" instances centered at the specified indices.
     */
    int findCrossedMasInstances(List<String> rows, int rowIndex, int columnIndex)
    {
        int instances = 0;

        // Look for:
        // M..
        // .A.
        // ..S
        boolean isMasDownAndRight = isMasInstance(rows, rowIndex-1, columnIndex-1, 1, 1);

        // Look for:
        // ..M
        // .A.
        // S..
        boolean isMasDownAndLeft = isMasInstance(rows, rowIndex-1, columnIndex+1, 1, -1);

        // Look for:
        // S..
        // .A.
        // ..M
        boolean isMasUpAndLeft = isMasInstance(rows, rowIndex+1, columnIndex+1, -1, -1);

        // Look for:
        // ..S
        // .A.
        // M..
        boolean isMasUpAndRight = isMasInstance(rows, rowIndex+1, columnIndex-1, -1, 1);

        // Look for:
        // M.M
        // .A.
        // S.S
        if (isMasDownAndRight && isMasDownAndLeft) instances++;

        // Look for:
        // S.M
        // .A.
        // S.M
        if (isMasDownAndLeft && isMasUpAndLeft) instances++;

        // Look for:
        // S.S
        // .A.
        // M.M
        if (isMasUpAndLeft && isMasUpAndRight) instances++;

        // Look for:
        // M.S
        // .A.
        // M.S
        if (isMasUpAndRight && isMasDownAndRight) instances++;

        return instances;
    }
}