package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BodiesGroup  implements Iterable<Body>{

	private String gId;
	private ForceLaws fl;
	private List<Body> bs;
	private List<Body> _bodiesRO;

	// constructor
	BodiesGroup(String gId, ForceLaws fl){
		if (gId == null || fl == null)
			throw new IllegalArgumentException("Algun parametro es null");
		if (gId.trim().length() == 0)
			throw new IllegalArgumentException(
					"id o (gid) no incluye al menos un carćter que no sea espacio en blanco");

		this.gId = gId;
		this.fl = fl;
		this.bs = new ArrayList<>();
		_bodiesRO = Collections.unmodifiableList(bs);
	}

	// getters
	public String getId() {
		return gId;
	}

	// cambia las leyes de fuerza a fl.
	void setForceLaws(ForceLaws fl) {
		if (fl == null)
			throw new IllegalArgumentException("Algun parametro es null");
		else
			this.fl = fl;
	}

	// añade el cuerpo b a la lista de cuerpos
	void addBody(Body b) {
		if (b == null)
			throw new IllegalArgumentException("b es null");

		for (int i = 0; i < bs.size(); i++) {
			if (bs.get(i).getId() == b.getId())
				throw new IllegalArgumentException("Ya existe un body con el mismo id");
		}
		bs.add(b);
	}

	// aplica un paso de simulación en el grupo
	void advance(double dt) {
		if (dt <= 0)
			throw new IllegalArgumentException("el valor de dt no sea positivo");

		for (Body list : bs) {
			list.resetForce();
		}
		fl.apply(bs);
		for (Body list : bs) {
			list.advance(dt);
		}
	}

	// devuelve el siguiente objeto JSON, que representa un estado del grupo
	public JSONObject getState() {
		JSONObject obj = new JSONObject();

		JSONArray bodies = new JSONArray();
		for (Body b : bs) {
			bodies.put(b.getState());
		}
		obj.put("bodies", bodies);
		obj.put("id", gId);
		return obj;
	}

	public String toString() {
		return getState().toString();
	}
	
	public String getForceLawsInfo() {
		return fl.toString();
	}
	
	public Iterator<Body> iterator() {
		return _bodiesRO.iterator();
	}

}
