package pl.jacci.entities;

import pl.jacci.graphics.Skin;
import pl.jacci.handlers.Movement;
import pl.jacci.logic.Monster;
import pl.jacci.logic.Player;
import pl.jacci.main.MainScreen;
import pl.jacci.resources.Assets;
import pl.jacci.resources.Vars;



public class EntityManager {
	
	public static Room currentRoom;
	
	public Entity playerEntity;
	public DynamicBody playerBody;
	
	private Skin playerSkin;
	private Skin[][] monsterSkins;
	
	public EntityManager () {
		currentRoom = MainScreen.rManager.rooms[0]; /////////////////////
		playerEntity = new Entity(new Player());
		playerBody = new DynamicBody();
		monsterSkins = new Skin[7][13];
	}
	
	public void update () {
		Movement.handleInput(playerEntity, playerBody);
		currentRoom.update();
	}
	
	public void setupScene () { //////////////////////////////////////////////
		currentRoom.createRoom();
		
		for (int i = 0; i < MainScreen.rManager.rooms.length; ++i) {
			MainScreen.rManager.rooms[i].update();
		}
		//currentRoom.update();
	}
	
	public void setupEntities () {
		/*for (int i = 0; i < MainScreen.roomManager.count; ++i) {
			DynamicBody.setCoords(MainScreen.roomManager.rooms[i])
		}*/
		DynamicBody.setCoords(currentRoom.X/2, currentRoom.Y);
		playerBody.createPlayer();
		playerSkin = new Skin(playerBody);
		
		int monsterID = 0;
		for (int i = 0; i < 7; ++i) {
			for (int j = 0; j < 13; ++j) {
				if (Assets.monsterMaps[RoomManager.current][i][j] == -1) {
					continue;
				}
				int monsterType = Assets.monsterMaps[RoomManager.current][i][j]-1;
				currentRoom.addMonster(monsterType, Vars.y(i)+45, Vars.x(j)+45, i, j);
				monsterSkins[i][j] = new Skin(currentRoom.monsters.get(monsterID).body, monsterType);
				++monsterID;
			}
		}
	}
	
	public void render () {
		// player
		playerSkin.drawPlayer();
		
		// fly
		for (Skin[] i : monsterSkins) {
			for (Skin j : i) {
				if (j != null) {
					j.drawMonster();
				}
			}
		}
	}
	
	public void damage (int id) {
		currentRoom.monsters.get(id).damageMonster(getPlayer().getDamage());
	}
	
	public void linear (Entity monster, int direction) {
		Movement.linear(monster);
	}
	
	public void buzz (Entity monster) {
		Movement.buzz(monster);
	}
	
	public void chase (Entity monster) {
		if (playerBody.body.getPosition().dst2(monster.body.getPosition()) > 20) {
			return;
		}
		Movement.chase(playerBody, monster);
	}
	
	public Player getPlayer () {
		return playerEntity.PLAYER;
	}
	
	public Monster getMonster (int id) {
		return currentRoom.monsters.get(id).MONSTER;
	}
	
	public Monster getMonster (String id) {
		return getMonster(Integer.parseInt(id));
	}
}
