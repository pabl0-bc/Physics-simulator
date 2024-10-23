package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public abstract class Body {

	protected String id;
	protected String gid;
	protected Vector2D vel;
	protected Vector2D force;
	protected Vector2D pos;
	protected double mass;

	// constructor de los cuerpos
	public Body(String id, String gid, Vector2D vel, Vector2D pos, double mass) {
		if (id == null || gid == null || vel == null || pos == null) {
			throw new IllegalArgumentException("Algun parametro es null");
		}
		if (id.trim().length() == 0 || gid.trim().length() == 0) {
			throw new IllegalArgumentException(
					"id o (gid) no incluye al menos un carćter que no sea espacio en blanco");
		}
		if (mass <= 0) {
			throw new IllegalArgumentException("La masa no es positiva");
		}

		this.id = id;
		this.gid = gid;
		this.vel = vel;
		this.pos = pos;
		this.mass = mass;
		this.force = new Vector2D();
	}

	// getters
	public String getId() {
		return id;
	}

	public String getgId() {
		return gid;
	}

	public Vector2D getVelocity() {
		return vel;
	}

	public Vector2D getForce() {
		return force;
	}

	public Vector2D getPosition() {
		return pos;
	}

	public double getMass() {
		return mass;
	}

	// añade la fuerza f al vector de fuerza del cuerpo
	void addForce(Vector2D f) {
		this.force = this.force.plus(f);
	}

	// pone el valor del vector de fuerza a (0, 0)
	void resetForce() {
		this.force = new Vector2D();
	}

	// mueve el cuerpo durante dt segundos
	abstract void advance(double dt);

	// devuelve la siguiente información del cuerpo en formato JSON
	public JSONObject getState() {
		JSONObject obj = new JSONObject();

		obj.put("id", id);
		obj.put("m", mass);
		obj.put("p", pos.asJSONArray());
		obj.put("v", vel.asJSONArray());
		obj.put("f", force.asJSONArray());

		return obj;
	}

	public String toString() {
		return getState().toString();
	}
}
