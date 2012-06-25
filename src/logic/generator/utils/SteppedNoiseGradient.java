package logic.generator.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import teampg.grid2d.point.Pos2D;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Given output of a libnoise module, lets us bind certain values to certain
 * ranges.
 * <br /><br />
 * libnoise modules output between -1D and 1D. We don't want to mess around with
 * this everywhere, so this class lets you pass in the noise module and say -1
 * -> 0.5 is MyEnum.GREEN, 0.5 -> 1 is MyEnum.PURPLE.
 * <br /> <br />
 * From that point on, just call myInstOfThisClass.getValue(somePoint), and get
 * either GREEN or PURPLE, depending on what the underlying noise module has for
 * those x&y points.
 *
 * @author Jackson Williams
 */
public class SteppedNoiseGradient<T> {

	// value range pairs in sorted order
	private final ValueRangePair<T>[] valueRanges;
	private final DoubleForPointGetter input;

	private SteppedNoiseGradient(DoubleForPointGetter input, ValueRangePair<T>[] valueRanges) {
		this.valueRanges = valueRanges;
		this.input = input;
	}

	public T getValue(Pos2D forPos) {
		double rand = input.getValue(forPos);

		int indexOfRangeOwner = Arrays.binarySearch(valueRanges, new ValueRangePair<T>(null, rand));
		if (indexOfRangeOwner < 0) {
			indexOfRangeOwner = (-indexOfRangeOwner) - 1;
		}

		T valueForRand = valueRanges[indexOfRangeOwner].value;

		return valueForRand;
	}

	/**
	 * Builds containing class. Because we don't want to have a
	 * SteppedNoiseGradient sitting around half-constructed, and passing in some
	 * kind of map between ranges and values would be a pain. This exposes a
	 * fluent syntax for building. <br /><br />
	 * Example: <code><pre>
	 * SteppedNoiseGradient<MyEnum> myGrad =
	 *     new Builder<MyEnum>(someRandModule, MyEnum.RED)
	 *     .add(0D, MyEnum.GREEN)    //-1.0 -> 0.0D is RED
	 *     .add(0.5D, MyEnum.VIOLET) //0.0D -> 0.5 is GREEN
	 *     .build()                  //0.5D -> 1.0 is VIOLET</pre></code>
	 *
	 * @author Jackson Williams
	 *
	 * @param <E>
	 */
	public static class Builder<E> {
		private final List<ValueRangePair<E>> buildingValueRanges;
		private final DoubleForPointGetter input;

		/**
		 * @param input
		 *            the RNG from which input values from -1 to 1 will come.
		 */
		public Builder(DoubleForPointGetter input) {
			this.input = input;

			buildingValueRanges = new ArrayList<>();
		}

		/**
		 * Range is from floor (exclusive) to ceiling (inclusive)
		 */
		public Builder<E> add(double rangeCeiling, E rangeValue) {
			double rangeFloor;
			{
				if (buildingValueRanges.isEmpty()) {
					rangeFloor = Double.NEGATIVE_INFINITY;
				} else {
					rangeFloor = buildingValueRanges.get(buildingValueRanges.size() - 1).rangeCeiling;
				}
			}

			checkArgument(Double.compare(rangeFloor, rangeCeiling) < 0, "New point: " + rangeCeiling
					+ " must be less than last added point: " + rangeFloor);

			buildingValueRanges.add(new ValueRangePair<E>(rangeValue, rangeCeiling));
			return this;
		}

		/**
		 * @param lastValue
		 *            value returned for lastAddedValue to... MAX
		 */
		public SteppedNoiseGradient<E> build(E lastValue) {
			buildingValueRanges.add(new ValueRangePair<E>(lastValue, Double.POSITIVE_INFINITY));

			@SuppressWarnings("unchecked")
			ValueRangePair<E>[] builtRanges = new ValueRangePair[buildingValueRanges.size()];
			buildingValueRanges.toArray(builtRanges);

			return new SteppedNoiseGradient<>(input, builtRanges);
		}
	}

	private static class ValueRangePair<Q> implements Comparable<ValueRangePair<Q>> {
		public final Q value;
		public final double rangeCeiling;

		public ValueRangePair(Q value, double rangeCeiling) {
			this.value = value;
			this.rangeCeiling = rangeCeiling;
		}

		@Override
		public int compareTo(ValueRangePair<Q> other) {
			return Double.compare(rangeCeiling, other.rangeCeiling);
		}

		@Override
		public String toString() {
			return "ValueRangePair [value=" + value + ", rangeCeiling=" + rangeCeiling + "]";
		}
	}
}
