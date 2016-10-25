package com.gamedev.techtronic.lunargame.AI;

import com.gamedev.techtronic.lunargame.Level.Tile;
import com.gamedev.techtronic.lunargame.gage.util.Vector2;

import java.util.ArrayList;

public final class AIUtilities {

    /*
     * Generates a smoothed path by removing unnecessary nodes in the input path.
     * Useful for creating a more natural path after using a pathfinder to get an path.
     *
     * @param inputPath The path to be smoothed
     * @param levelTiles The tiles in the current level (required for raycasting a path between nodes)
     * @return The smoothed path
     *
     * Bugs:
     */
    public static ArrayList<Vector2> smoothPath(ArrayList<Vector2> inputPath, ArrayList<Tile> levelTiles) {
        // A path of 2 nodes cannot be smoothed, it's a line, just return the path
        if (inputPath.size() == 2 || inputPath.size() == 0) {
            return inputPath;
        }

        ArrayList<Vector2> outputPath = new ArrayList<>();
        outputPath.add(inputPath.get(0));

        // Start at index 2 because it is assumed there is already a clear line between position 0 and 1
        for (int i = 2; i < inputPath.size() - 1; i++) {

            // ray casting, if the ray path isn't clear, then add the last node that passed to output
            if (!isRayClear(outputPath.get(outputPath.size() - 1), inputPath.get(i), levelTiles)) {
                outputPath.add(inputPath.get(i - 1));
            }
        }

        outputPath.add(inputPath.get(inputPath.size() - 1));

        return outputPath;
    }

    public static boolean isRayClear(Vector2 position1, Vector2 position2, ArrayList<Tile> levelTiles) {
        // find the line between 2 points provided, basic geometry stuff
        // Y = mx + c
        // m is gradient
        // c is y axis intercept
        float y, x;
        float m = (position2.y - position1.y) / (position2.x - position1.x);
        float c = position1.y - (m * position1.x);


        if ((Math.abs(position2.x - position1.x)) > (Math.abs(position2.y - position1.y))) {
            if (position1.x < position2.x) {
                // calc position along line by incrementing x a little, and checking if the point is inside a tile bound
                for (x = position1.x; x < position2.x; x+=0.25) {
                    y = (m * x + c);
                    for (Tile tile : levelTiles) {
                        if (tile.getBound().x - tile.getBound().getWidth() < x &&
                                tile.getBound().x + tile.getBound().getWidth() > x &&
                                tile.getBound().y - tile.getBound().getHeight() < y &&
                                tile.getBound().y + tile.getBound().getHeight() > y) {
                            return false;
                        }
                    }
                }
            } else if (position1.x > position2.x) {
                // calc position along line by incrementing x a little, and checking if the point is inside a tile bound
                for (x = position1.x; x > position2.x; x-=0.25) {
                    y = (m * x + c);
                    for (Tile tile : levelTiles) {
                        if (tile.getBound().x - tile.getBound().getWidth() < x &&
                                tile.getBound().x + tile.getBound().getWidth() > x &&
                                tile.getBound().y - tile.getBound().getHeight() < y &&
                                tile.getBound().y + tile.getBound().getHeight() > y) {
                            return false;
                        }
                    }
                }
            }
        } else if ((Math.abs(position2.y - position1.y)) > (Math.abs(position2.x - position1.x))) {
            if (position1.y < position2.y) {
                // calc position along line by incrementing x a little, and checking if the point is inside a tile bound
                for (y = position1.y; y < position2.y; y+=0.25) {
                    x = (y - c) / m;
                    for (Tile tile : levelTiles) {
                        if (tile.getBound().x - tile.getBound().getWidth() < x &&
                                tile.getBound().x + tile.getBound().getWidth() > x &&
                                tile.getBound().y - tile.getBound().getHeight() < y &&
                                tile.getBound().y + tile.getBound().getHeight() > y) {
                            return false;
                        }
                    }
                }
            } else if (position1.x > position2.x) {
                // calc position along line by incrementing x a little, and checking if the point is inside a tile bound
                for (y = position1.y; y > position2.y; y-=0.25) {
                    x = (y - c) / m;
                    for (Tile tile : levelTiles) {
                        if (tile.getBound().x - tile.getBound().getWidth() < x &&
                                tile.getBound().x + tile.getBound().getWidth() > x &&
                                tile.getBound().y - tile.getBound().getHeight() < y &&
                                tile.getBound().y + tile.getBound().getHeight() > y) {
                            return false;
                        }
                    }
                }
            }
        }


// Traversed line from point 1 to point 2 without intercepting a tile, return isRayClear true
        return true;
    }

}
