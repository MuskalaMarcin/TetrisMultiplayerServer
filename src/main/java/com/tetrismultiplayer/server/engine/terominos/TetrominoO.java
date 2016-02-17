package main.java.com.tetrismultiplayer.server.engine.terominos;

import java.awt.*;

/**
 * Created by Marcin on 2016-02-16.
 */
public class TetrominoO extends Tetromino
{
    public TetrominoO(Color color, int x, int y)
    {
        super(x, y, color);
        type = TetrominoType.O;
        bricksList.add(new Brick(x - Brick.LENGTH, y - Brick.LENGTH));
        bricksList.add(new Brick(x, y - Brick.LENGTH));
        bricksList.add(new Brick(x - Brick.LENGTH, y));
        bricksList.add(new Brick(x, y));
    }
}