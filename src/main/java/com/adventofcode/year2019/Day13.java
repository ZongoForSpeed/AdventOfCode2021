package com.adventofcode.year2019;

import com.adventofcode.Intcode;
import com.adventofcode.map.Map2D;
import com.adventofcode.map.Point2D;

import java.util.List;
import java.util.Map;

public final class Day13 {
    /**
     * --- Day 13: Care Package ---
     * As you ponder the solitude of space and the ever-increasing three-hour roundtrip for messages between you and
     * Earth, you notice that the Space Mail Indicator Light is blinking. To help keep you sane, the Elves have sent you
     * a care package.
     *
     * It's a new game for the ship's arcade cabinet! Unfortunately, the arcade is all the way on the other end of the
     * ship. Surely, it won't be hard to build your own - the care package even comes with schematics.
     *
     * The arcade cabinet runs Intcode software like the game the Elves sent (your puzzle input). It has a primitive
     * screen capable of drawing square tiles on a grid. The software draws tiles to the screen with output instructions:
     * every three output instructions specify the x position (distance from the left), y position (distance from the
     * top), and tile id. The tile id is interpreted as follows:
     *
     * 0 is an empty tile. No game object appears in this tile.
     * 1 is a wall tile. Walls are indestructible barriers.
     * 2 is a block tile. Blocks can be broken by the ball.
     * 3 is a horizontal paddle tile. The paddle is indestructible.
     * 4 is a ball tile. The ball moves diagonally and bounces off objects.
     * For example, a sequence of output values like 1,2,3,6,5,4 would draw a horizontal paddle tile (1 tile from the
     * left and 2 tiles from the top) and a ball tile (6 tiles from the left and 5 tiles from the top).
     *
     * Start the game. How many block tiles are on the screen when the game exits?
     */
    static long arkanoidBlockTiles(String line) {
        Arkanoid game = new Arkanoid();
        Intcode.intcode(line, () -> 0, game::gameOutput);

        List<String> print = game.print();
        return print.stream().flatMapToInt(String::chars).filter(c -> c == '░').count();
    }

    /**
     * --- Part Two ---
     * The game didn't run because you didn't put in any quarters. Unfortunately, you did not bring any quarters. Memory
     * address 0 represents the number of quarters that have been inserted; set it to 2 to play for free.
     *
     * The arcade cabinet has a joystick that can move left and right. The software reads the position of the joystick
     * with input instructions:
     *
     * If the joystick is in the neutral position, provide 0.
     * If the joystick is tilted to the left, provide -1.
     * If the joystick is tilted to the right, provide 1.
     * The arcade cabinet also has a segment display capable of showing a single number that represents the player's
     * current score. When three output instructions specify X=-1, Y=0, the third output instruction is not a tile; the
     * value instead specifies the new score to show in the segment display. For example, a sequence of output values
     * like -1,0,12345 would show 12345 as the player's current score.
     *
     * Beat the game by breaking all the blocks. What is your score after the last block is broken?
     */
    static Arkanoid gamePartTwo(String line) {
        line = '2' + line.substring(1);
        Arkanoid game = new Arkanoid();
        Intcode.intcode(line, game::gameInput, game::gameOutput);
        return game;
    }

    static class Arkanoid {
        private final Map2D squares = new Map2D();
        private int count = 0;
        private int x;
        private int y;
        private long score = 0;

        private static char print(long code) {
            return switch ((int) code) {
                case 0 -> ' ';
                case 1 -> '▓';
                case 2 -> '░';
                case 3 -> '═';
                case 4 -> 'Θ';
                default -> ' ';
            };
        }

        public void gameOutput(long output) {
            ++count;
            switch (count % 3) {
                case 1:
                    x = (int) output;
                    break;
                case 2:
                    y = (int) output;
                    break;
                case 0:
                    if (x == -1 && y == 0) {
                        score = output;
                    } else {
                        squares.put(Point2D.of(x, y), output);
                    }
                    break;
            }
        }

        public List<String> print() {
            return squares.print(Arkanoid::print);
        }

        public long getScore() {
            return score;
        }

        public long gameInput() {
            Point2D ballPosition = Point2D.of(0, 0);
            Point2D paddlePosition = Point2D.of(0, 0);
            for (Map.Entry<Point2D, Long> entry : squares.entrySet()) {
                if (entry.getValue() == 3) {
                    paddlePosition = entry.getKey();
                } else if (entry.getValue() == 4) {
                    ballPosition = entry.getKey();
                }
            }
            return Integer.compare(ballPosition.x(), paddlePosition.x());
        }
    }
}
