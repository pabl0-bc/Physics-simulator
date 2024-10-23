package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws {

	private Vector2D c = new Vector2D();
	private double g;

	public MovingTowardsFixedPoint(Vector2D c, double g) {
		if (c == null || g <= 0.0)
			throw new IllegalArgumentException("c es null / g no es positivo");

		this.c = c;
		this.g = g;
	}

	// añade al cuerpo bi la fuerza provocado que este se mueva hasta un punto fijo "c"
	@Override
	public void apply(List<Body> bs) {
		for (Body b : bs) {
			Vector2D d = this.c.minus(b.getPosition());
			b.addForce(d.direction().scale(this.g * b.getMass()));
		}
	}
	
	public String toString() {
		return "Moving towards "+ c +" with constant acceleration " + g;
	}
}
