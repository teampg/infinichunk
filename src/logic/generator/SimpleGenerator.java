package logic.generator;

import java.awt.Dimension;

import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.Perlin;
import logic.Cell;
import logic.Tile;
import logic.generator.utils.NoiseModuleDoubleGetterWrapper;
import logic.generator.utils.SteppedNoiseGradient;
import teampg.grid2d.GridInterface.Entry;
import teampg.grid2d.chunkgrid.Chunk;
import teampg.grid2d.chunkgrid.ChunkPos;
import teampg.grid2d.chunkgrid.GlobalPos;
import teampg.grid2d.chunkgrid.SimpleChunk;

public class SimpleGenerator implements ChunkGenerator {
	private final SteppedNoiseGradient<Tile> gen;
	private final Dimension chunkSize;

	public SimpleGenerator(Dimension chunkSize) {
		this.chunkSize = (Dimension) chunkSize.clone();

		Perlin randInput = new Perlin();
		randInput.setSeed(0);
		randInput.setFrequency(8.0);
		randInput.setPersistence(0.5);
		randInput.setLacunarity(2.18359375);
		try {
			randInput.setOctaveCount(8);
		} catch (ExceptionInvalidParam e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		randInput.setNoiseQuality(NoiseQuality.QUALITY_STD);

		//@formatter:off
		NoiseModuleDoubleGetterWrapper wrappedInput = new NoiseModuleDoubleGetterWrapper(randInput);
		gen = new SteppedNoiseGradient.Builder<Tile>(wrappedInput)
				.add(-0.3, Tile.WATER)
				.add(-0.2, Tile.GRASS1)
				.add(0.0, Tile.GRASS2)
				.add(0.45, Tile.DIRT)
				.build(Tile.WALL);
		//@formatter:on
	}

	@Override
	public Chunk<Cell> loadChunk(ChunkPos from) {
		Chunk<Cell> ret = new SimpleChunk<>(chunkSize);

		for (Entry<Cell> e : ret.getEntries()) {
			GlobalPos cellPos = GlobalPos.of(from, e.getPosition());
			Tile randomChoice = gen.getValue(cellPos);

			ret.set(e.getPosition(), new Cell(randomChoice));
		}

		return ret;
	}
}
