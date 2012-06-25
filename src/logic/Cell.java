package logic;

public class Cell {
	private Entity ent;
	private final Tile background;

	public Cell(Tile background) {
		this.background = background;
	}

	public void setEnt(Entity ent) {this.ent = ent;}
	public Entity getEnt() {return ent;}
	public Tile getBackground() {return background;}
}
