package logic.generator;

import java.awt.Dimension;

import com.google.common.collect.Ranges;

import libnoiseforjava.NoiseGen.NoiseQuality;
import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.module.Perlin;
import logic.Cell;
import logic.Tile;
import teampg.datatypes.valuerange.RangePartition;
import teampg.datatypes.valuerange.ValueRangeMapper;
import teampg.datatypes.valuerange.ValueRangeMapper.Side;
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
		ValueRangeMapper<Double, Tile> inputSteps =
				new ValueRangeMapper.Builder<Double, Tile>(Ranges.closed(-1D, 1D), Tile.WATER)
				.add(RangePartition.of(-0.3D, Side.RIGHT), Tile.GRASS1)
				.add(RangePartition.of(-0.2D, Side.RIGHT), Tile.GRASS2)
				.add(RangePartition.of(0D, Side.RIGHT), Tile.DIRT)
				.add(RangePartition.of(0.45D, Side.RIGHT), Tile.WALL)
				.build();
		gen = new SteppedNoiseGradient<>(randInput, inputSteps);
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
