package logic.generator.utils;

import libnoiseforjava.module.ModuleBase;
import teampg.grid2d.point.Pos2D;

public class NoiseModuleDoubleGetterWrapper implements DoubleForPointGetter {
	private final ModuleBase wrapped;

	public NoiseModuleDoubleGetterWrapper(ModuleBase toWrap) {
		wrapped = toWrap;
	}

	@Override
	public double getValue(Pos2D forPos) {
		return wrapped.getValue(forPos.x/1000D, forPos.y/1000D, 0);
	}
}
