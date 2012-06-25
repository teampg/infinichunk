package logic.generator;

import logic.Cell;
import teampg.grid2d.chunkgrid.Chunk;
import teampg.grid2d.chunkgrid.ChunkPos;

public interface ChunkGenerator {

	public abstract Chunk<Cell> loadChunk(ChunkPos from);

}