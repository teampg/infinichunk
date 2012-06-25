package logic.generator;

import java.util.Collection;
import java.util.HashSet;

import logic.Cell;
import teampg.grid2d.chunkgrid.Chunk;
import teampg.grid2d.chunkgrid.ChunkPos;
import teampg.grid2d.chunkgrid.ChunkedGrid;
import teampg.grid2d.chunkgrid.ChunkedGridImpl;

public class FeaturedGenerator implements ChunkGenerator {
	private final ChunkedGrid<Cell> map;

	private final ChunkedGrid<Cell> generatingMap;
	private final Collection<ChunkPos> finishedGenerating;

	private final TerrainGenerator grasslandGenerator;

	private enum ChunkState {
		BLANK, PARTIAL, COMPLETE, IN_MAP
	};

	public FeaturedGenerator(ChunkedGrid<Cell> map) {
		this.map = map;
		generatingMap = new ChunkedGridImpl<>(map.getChunkSize());
		finishedGenerating = new HashSet<>();

		grasslandGenerator = new GrasslandGenerator();
	}

	@Override
	public Chunk<Cell> loadChunk(ChunkPos from) {
		generateChunk(from);

		assert (getChunkState(from).equals(ChunkState.COMPLETE));

		finishedGenerating.remove(from);
		return generatingMap.removeChunk(from);
	}

	// TODO run in v. low priority background thread instead of on demand
	private void generateChunk(ChunkPos at) {
		/*
		 * So in the map generator... I'm not sure how regions/biomes would
		 * work. Would each cell in the world have exactly one biome, or would
		 * there be different weighted values (this cell is 7.8 forest, 4.2
		 * city)?
		 */

	}

	/**
	 * Finds generated state of a chunk.
	 * <ol>
	 * <li>BLANK : untouched, not in generating map.</li>
	 * <li>PARTIAL : has had some things added to it, not finalized.</li>
	 * <li>COMPLETE : done generating, ready to be added to map.</li>
	 * <li>IN_MAP : removed from generatingMap, added to real. Read only.</li>
	 * </ol>
	 */
	private ChunkState getChunkState(ChunkPos at) {
		if (generatingMap.contains(at)) {
			if (finishedGenerating.contains(at)) {
				return ChunkState.COMPLETE;
			}

			return ChunkState.PARTIAL;
		}

		if (map.contains(at)) {
			return ChunkState.IN_MAP;
		}

		return ChunkState.BLANK;
	}
}
