package logic;

public enum Tile {
	GRASS1 (true, "."),
	GRASS2 (true, ","),
	WALL (false, "#"),
	DIRT (true, "&nbsp"),
	WATER (false, "~");

	private final String display;
	private final boolean walkable;
	private Tile (final boolean walkable, final String display) {
		this.display = display;
		this.walkable = walkable;
	}
	public String display() {return display;}
	public boolean walkable() {return walkable;}
}
