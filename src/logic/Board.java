package logic;


import java.awt.Dimension;
import java.awt.image.BufferedImage;

import logic.generator.ChunkGenerator;
import logic.generator.SimpleGenerator;
import logic.io.BoardExport;
import logic.io.SimpleCellPalette;
import teampg.grid2d.GridInterface.Entry;
import teampg.grid2d.chunkgrid.ChunkEntry;
import teampg.grid2d.chunkgrid.ChunkPos;
import teampg.grid2d.chunkgrid.ChunkedGrid;
import teampg.grid2d.chunkgrid.ChunkedGridImpl;
import teampg.grid2d.chunkgrid.GlobalPos;
import teampg.grid2d.point.Pos2D;

public class Board {
	private static final Dimension CHUNK_SIZE = new Dimension(16, 16);

	private final ChunkedGrid<Cell> map;
	private final ChunkGenerator chunkGen;

	public Board() {
		map = new ChunkedGridImpl<>(CHUNK_SIZE);
		chunkGen = new SimpleGenerator(CHUNK_SIZE);

		ChunkPos centreChunkPos = ChunkPos.of(0, 0);
		map.putChunk(centreChunkPos, chunkGen.loadChunk(centreChunkPos));

		// generate chunks near centre
		updateNear(GlobalPos.of(0, 0, CHUNK_SIZE));
	}

	public Tile getBackground(GlobalPos at) {
		return map.get(at).getBackground();
	}

	public Entity getEntity(GlobalPos at) {
		return map.get(at).getEnt();
	}

	public void addEntity(GlobalPos at, Entity toAdd) {
		assert map.get(at).getBackground().walkable();

		map.get(at).setEnt(toAdd);

		updateNear(at);
	}

	public void moveEntity(GlobalPos from, GlobalPos target) {
		Entity movingEntity = map.get(from).getEnt();

		map.get(target).setEnt(movingEntity);
		map.get(from).setEnt(null);

		updateNear(target);
	}

	/**
	 * If at is in a chunk adjacent to one on outside edge of map, generate a
	 * new chunk and stick it onto that edge.
	 */
	private void updateNear(GlobalPos at) {
		ChunkPos nearChunk = at.getChunkComponent();

		for (ChunkPos p : Pos2D.getRing(nearChunk, 1)) {
			if (map.isOnBorder(p)) {
				map.putChunk(p, chunkGen.loadChunk(p));
			}
		}

	}

	public GlobalPos getPos(Entity toFind) {
		// for each chunk
		for (ChunkEntry<Cell> ce : map) {
			// for each cell in chunk
			for (Entry<Cell> e : ce.getChunk().getEntries()) {
				Entity entAtCell = e.getContents().getEnt();
				if (entAtCell == null) {
					continue;
				}

				// if ent in cell matches toFind, return its position
				if (entAtCell.equals(toFind)) {
					return GlobalPos.of(ce.getPosition(), e.getPosition(), CHUNK_SIZE);
				}
			}
		}

		// couldn't find it
		return null;
	}

	/**
	 * Displays a piece of the board as text/html.
	 *
	 * @param pos
	 *            Where to center window
	 * @param displaySize
	 *            Dimension of window
	 * @return text/html representation of board piece
	 */
	public String display(GlobalPos pos, Dimension displaySize) {
		StringBuilder ret = new StringBuilder(displaySize.height
				* displaySize.width * 3);
		ret.append("<html>");

		int yStart = pos.y - (displaySize.height / 2);
		int yEnd = yStart + displaySize.height;

		int xStart = pos.x - (displaySize.width / 2);
		int xEnd = xStart + displaySize.width;

		for (int y = yStart; y < yEnd; y++) {
			for (int x = xStart; x < xEnd; x++) {
				ret.append(" ");

				GlobalPos posToDisplay = GlobalPos.of(x, y, CHUNK_SIZE);
				if (map.contains(posToDisplay) == false) {
					ret.append("?");
					continue;
				}

				Cell currCell = map.get(posToDisplay);

				// if an ent, show it
				Entity ent = currCell.getEnt();
				if (ent != null) {
					ret.append(ent.display());
					continue;
				}

				// show background
				Tile background = currCell.getBackground();
				ret.append(background.display());
			}
			ret.append("<br/>\n");
		}

		ret.append("</html>");
		return ret.toString();
	}

	public void init(Player player) {
		addEntity(GlobalPos.of(0, 0, CHUNK_SIZE), player);
	}

	public Dimension getChunkSize() {
		return CHUNK_SIZE;
	}

	public BufferedImage exportToPNG(SimpleCellPalette palette) {
		return BoardExport.exportMapToPNG(map, palette);
	}

}
