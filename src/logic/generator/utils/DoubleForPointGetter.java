package logic.generator.utils;

import teampg.grid2d.point.Pos2D;

/**
 * Input for some {@link SteppedNoiseGradient}.
 * @author Jackson Williams
 */
public interface DoubleForPointGetter {
	public double getValue(Pos2D forPos);
	//TODO add expected range
}
