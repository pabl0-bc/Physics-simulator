package simulator.model;

import simulator.misc.Vector2D;

public class StationaryBody extends Body {

	public StationaryBody(String id, String gid, Vector2D pos, double mass) {
		super(id, gid, new Vector2D(), pos, mass);
	}

	// no hace nada ya que los cuerpos estacionarios no tienen movimiento
	@Override
	void advance(double dt) {
	}

}
