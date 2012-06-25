package logic;

import teampg.grid2d.chunkgrid.GlobalPos;
import teampg.grid2d.point.Pos2D;
import teampg.grid2d.point.RelPos;

public class Player extends Entity {
	private final Board map;

	public Player(Board board) {
		map = board;
	}

	public boolean move(RelPos dir) {
		GlobalPos myPos = map.getPos(this);
		GlobalPos target = Pos2D.offset(myPos, dir);

		Tile targetBackground = map.getBackground(target);
		if (targetBackground.walkable() == false) {
			return false;
		}

		Entity targetEntity = map.getEntity(target);
		if (targetEntity != null) {
			return false;
		}

		map.moveEntity(myPos, target);
		return true;
	}

	@Override
	public String display() {
		return "P";
	}

}
