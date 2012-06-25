package logic.io;

import logic.Cell;

public class SimpleCellPalette implements BoardColorPalette<Cell> {
	@Override
	public int getRGB(Cell forElement) {
		switch(forElement.getBackground()) {
		case DIRT:
			return 0x633D41;
		case WATER:
			return 0x1B63E0;
		case GRASS1:
			return 0x57733E;
		case GRASS2:
			return 0x718C42;
		case WALL:
			return 0x403F3F;
		default:
			throw new IllegalStateException("Unimplemented tile type");
		}
	}

}
