package simulator.model;

import simulator.misc.Vector2D;

public class MovingBody extends Body {

	// constructor
	public MovingBody(String id, String gid, Vector2D pos, Vector2D vel, double mass) {
		super(id, gid, vel, pos, mass);
	}

	// actualiza la posicion y velocidad de un cuerpo a partir de la segunda ley de
	// Newton
	@Override
	void advance(double dt) {
		Vector2D acc = new Vector2D();

		if (this.mass <= 0.0d)
			acc = new Vector2D();
		else
			acc = this.force.scale(1 / this.mass);

		this.pos = this.pos.plus(this.vel.scale(dt).plus(acc.scale(0.5 * dt * dt)));
		this.vel = acc.scale(dt).plus(this.vel);
	}
}
