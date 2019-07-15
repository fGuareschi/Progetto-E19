package network.commands;

import flappyEntities.Entity;
import flappyEntities.logic.SerializableEntity;
import flappyEntities.logic.obstacles.ObstacleLogicComponent;
import game.multiplayer.OnlineLocalGame;
import game.multiplayer.OnlineRemoteGame;
import network.Command;

public class ObstacleGeneratedCommand extends Command {
    private static final long serialVersionUID = -539210512249000006L;

    private SerializableEntity obstacle;
    private int ID;

    public ObstacleGeneratedCommand(Entity obstacleEntity) {
        obstacle= ((ObstacleLogicComponent) obstacleEntity.getLogicComponent()).getSerializableVersion();
        ID= obstacleEntity.getID();
    }

    @Override
    public void execute(OnlineRemoteGame remoteGame, OnlineLocalGame localGame) {
        Entity obstacleEntity= obstacle.instantiate(remoteGame.getCanvas());
        obstacleEntity.setID(ID);
        remoteGame.addScrollingElement(obstacleEntity);
    }
}
