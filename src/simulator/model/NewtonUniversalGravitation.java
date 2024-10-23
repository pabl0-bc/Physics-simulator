package simulator.model;

import java.util.List;

public class NewtonUniversalGravitation implements ForceLaws {

	private double G;

	// constructor
	public NewtonUniversalGravitation(double G) {
		if (G <= 0.0d)
			throw new IllegalArgumentException("el valor de G no es positivo");

		this.G = G;
	}

	// calcula la fuerza total aplicada sobre Bi por el resto de cuerpos
	@Override
	public void apply(List<Body> bs) {

		for (int i = 0; i < bs.size(); i++) {
			double f = 0;
			double aux = 0;

			for (int j = 0; j < bs.size(); j++) {
				if (bs.get(j).getPosition().distanceTo(bs.get(i).getPosition()) != 0) {
					aux = bs.get(j).getPosition().distanceTo(bs.get(i).getPosition());
					f = (G * (bs.get(i).getMass() * bs.get(j).getMass())) / (aux * aux);
					bs.get(i).addForce(bs.get(j).getPosition().minus(bs.get(i).getPosition()).direction().scale(f));
				}
			}
		}
	}
	
	public String toString() {
		return "Newton’s Universal Gravitation with G="+ G;
	}

}
