package logic.io;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Set;

import teampg.grid2d.GridInterface.Entry;
import teampg.grid2d.chunkgrid.ChunkEntry;
import teampg.grid2d.chunkgrid.ChunkPos;
import teampg.grid2d.chunkgrid.ChunkedGrid;
import teampg.grid2d.point.Pos2D;

public class BoardExport {
	public static <E> BufferedImage exportMapToPNG(ChunkedGrid<E> map, BoardColorPalette<E> palette) {
		final Dimension CHUNK_SIZE = map.getChunkSize();

		Set<ChunkPos> allChunkPos = map.pointSet();

		Dimension mapSize;
		ChunkPos topLeft;
		{
			int leftBoundary = Collections.min(
					allChunkPos, new Pos2D.AxisSizeComparator(Pos2D.Axis.X)).x;
			int rightBoundary = Collections.max(
					allChunkPos, new Pos2D.AxisSizeComparator(Pos2D.Axis.X)).x;


			int topBoundary = Collections.min(
					allChunkPos, new Pos2D.AxisSizeComparator(Pos2D.Axis.Y)).y;
			int bottomBoundary = Collections.max(
					allChunkPos, new Pos2D.AxisSizeComparator(Pos2D.Axis.Y)).y;


			mapSize = new Dimension(
					(Math.abs(leftBoundary - rightBoundary) + 1) * CHUNK_SIZE.width,
					(Math.abs(topBoundary - bottomBoundary) + 1) * CHUNK_SIZE.height);

			topLeft = ChunkPos.of(leftBoundary, topBoundary);
		}

		BufferedImage out = new BufferedImage(mapSize.width, mapSize.height,
				BufferedImage.TYPE_INT_RGB);

		for (ChunkEntry<E> chunkEntry : map) {
			ChunkPos fittedToImage = Pos2D.shiftCenterPosition(topLeft, chunkEntry.getPosition());

			for (Entry<E> cellEntry : chunkEntry.getChunk().getEntries()) {
				int imageX = fittedToImage.x * CHUNK_SIZE.width + cellEntry.getPosition().x;
				int imageY = fittedToImage.y * CHUNK_SIZE.height + cellEntry.getPosition().y;

				int colorToPaint = palette.getRGB(cellEntry.getContents());
				out.setRGB(imageX, imageY, colorToPaint);
			}
		}

		out.flush();

		return out;
	}

}
