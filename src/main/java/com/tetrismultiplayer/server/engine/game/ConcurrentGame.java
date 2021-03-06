package main.java.com.tetrismultiplayer.server.engine.game;

import main.java.com.tetrismultiplayer.server.engine.user.RemoteUser;
import main.java.com.tetrismultiplayer.server.gui.panel.MainPanel;
import org.json.JSONObject;

/**
 * Class and thread for concurrent game.
 */
public class ConcurrentGame extends ParentGameEngine
{
    public ConcurrentGame(RemoteUser ownerUser, MainPanel mainPanel, GameSpeed gameSpeed, int playersNumber)
    {
        super(GameStatus.WAITING, ownerUser, mainPanel, gameSpeed, GameType.CONCURRENT, playersNumber);
    }

    /**
     * Main concurrent game thread for concurrent game. Maintains all users connected to game.
     * @return null
     * @throws InterruptedException
     */
    public Object doInBackground() throws InterruptedException
    {
        mainPanel.writeLineInTextArea("Nowa gra w trybie konkurencji utworzona przez uzytkownika "
                + usersList.getFirst().getNick());
        if (waitForUsers())
        {
            mainPanel.writeLineInTextArea("Gra w trybie konkurencji id: " + getIdentifier() + " zostala rozpoczeta");

            long startFrameTime = System.currentTimeMillis();
            usersList.forEach(user -> placeNewTetromino(user));
            while (true)
            {
                checkPlayersMove(true);
                for (int i = 0; i < usersList.size(); i++)
                {
                    clearLine(checkForLineToClear(i), i);
                }
                for (int i = 0; i < usersList.size(); i++)
                {
                    RemoteUser user = usersList.get(i);
                    if (checkForInactiveBlock(user))
                    {
                        if (!placeNewTetromino(user))
                        {
                            user.sendToUser(new JSONObject().put("cmd", "endGame"));
			    i--;
                            if (i <= 0) return null;
                        }
                    }
                }

                if (System.currentTimeMillis() - startFrameTime >= getFrameInterval())
                {
                    moveDownAllActiveBlocks();
                    startFrameTime = System.currentTimeMillis();
                }
            }
        }
        else
        {
            mainPanel.writeLineInTextArea("Czas oczekiwania na nowych graczy gry: "
                    + getIdentifier() + " zostal przekroczony");
        }
        return null;
    }
}
