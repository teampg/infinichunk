package logic.generator.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import teampg.grid2d.point.Pos2D;

public class _SteppedNoiseGradientTest {
	private class NotVeryRandom implements DoubleForPointGetter {
		private double alwaysReturns = Double.NaN;

		public void setAlwaysReturns(double newValue) {
			alwaysReturns = newValue;
		}

		@Override
		public double getValue(Pos2D forPos) {
			return alwaysReturns;
		}
	};

	private enum Color {
		RED,
		ORANGE,
		YELLOW,
		GREEN,
		BLUE,
		INDIGO,
		VIOLET
	};


	private SteppedNoiseGradient<Color> rainbowGen;
	private NotVeryRandom tameInput;
	private static final Pos2D DUMMY_POINT = null;

	@Before
	public void setUp() throws Exception {
		tameInput = new NotVeryRandom();

		rainbowGen = new SteppedNoiseGradient.Builder<Color>(tameInput)
		.add(-0.8, Color.VIOLET)
		.add(-0.5, Color.INDIGO)
		.add(-0.1, Color.BLUE)
		.add(0.2, Color.GREEN)
		.add(0.4, Color.YELLOW)
		.add(0.8, Color.ORANGE)
		.build(Color.RED);
	}

	@Test
	public final void testGetValue() {
		tameInput.setAlwaysReturns(-0.9D);
		assertEquals(Color.VIOLET, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(-0.6D);
		assertEquals(Color.INDIGO, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(-0.2D);
		assertEquals(Color.BLUE, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(0.1D);
		assertEquals(Color.GREEN, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(0.3D);
		assertEquals(Color.YELLOW, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(0.6D);
		assertEquals(Color.ORANGE, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(0.9D);
		assertEquals(Color.RED, rainbowGen.getValue(DUMMY_POINT));
	}

	/**
	 * Range is floor (exclusive) to ceiling (inclusive). TODO swap the two, makes more sense
	 */
	@Test
	public final void testEdgeCases() {
		tameInput.setAlwaysReturns(-1.0D);
		assertEquals(Color.VIOLET, rainbowGen.getValue(DUMMY_POINT));
		tameInput.setAlwaysReturns(-0.8D);
		assertEquals(Color.VIOLET, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(-0.1D);
		assertEquals(Color.BLUE, rainbowGen.getValue(DUMMY_POINT));
		tameInput.setAlwaysReturns(0.2D);
		assertEquals(Color.GREEN, rainbowGen.getValue(DUMMY_POINT));

		tameInput.setAlwaysReturns(0.8D);
		assertEquals(Color.ORANGE, rainbowGen.getValue(DUMMY_POINT));
		tameInput.setAlwaysReturns(1.0D);
		assertEquals(Color.RED, rainbowGen.getValue(DUMMY_POINT));

	}

}
