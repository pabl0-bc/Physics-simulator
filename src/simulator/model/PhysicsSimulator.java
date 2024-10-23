package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator implements Observable<SimulatorObserver> {

	private double t;
	private ForceLaws fl;
	private double tAct;
	private Map<String, BodiesGroup> groups;
	private List<String> listId;
	private List<SimulatorObserver> observerList;
	private Map<String, BodiesGroup> _groupsRO;
	private double dt;

	// constructor
	public PhysicsSimulator(ForceLaws fl, double t) {
		if (t == 0 || fl == null)
			throw new IllegalArgumentException("El tiempo debe ser positivo / Parametro nulo");

		this.t = t;
		this.fl = fl;
		this.tAct = 0.0;
		this.groups = new HashMap<>();
		this.listId = new ArrayList<>();
		_groupsRO = Collections.unmodifiableMap(groups);
		this.observerList = new ArrayList<>();
	}

	// aplica un paso de simulación
	public void advance() {
		this.tAct += this.dt;
		for (BodiesGroup g : groups.values()) {
			g.advance(this.t);
		}
		
		for(SimulatorObserver so : observerList) {
			so.onAdvance(_groupsRO, tAct);
		}
		
	}

	// añade un nuevo grupo con identificador id al mapa de grupos
	public void addGroup(String id) {
		if (groups.containsKey(id))
			throw new IllegalArgumentException("Id ya existente");
		listId.add(id); // guarda los ids de los grupos por orden de creacion
		BodiesGroup bg = new BodiesGroup(id, fl);
		groups.put(id, bg);
		for(SimulatorObserver so : observerList) {
			so.onGroupAdded(_groupsRO, bg);
		}
	}

	// añade el cuerpo b al grupo con identificador b.getgId()
	public void addBody(Body b) {
		if (!groups.containsKey(b.getgId()))
			throw new IllegalArgumentException("Grupo con Id: " + b.getgId() + " no existe");
		groups.get(b.getgId()).addBody(b);
		for(SimulatorObserver so : observerList) {
			so.onBodyAdded(_groupsRO, b);
		}
	}

	// cambia las leyes de la fuerza del grupo con identificador id a f
	public void setForceLaws(String id, ForceLaws f) {
		BodiesGroup bg;
		if (groups.containsKey(id)) {
			bg = new BodiesGroup(id, fl);
			groups.get(id).setForceLaws(f);
		}	
		else
			throw new IllegalArgumentException("No existe grupo con Id " + id);
		
		for(SimulatorObserver so : observerList) {
			so.onForceLawsChanged(bg);
		}
	}

	// : devuelve el siguiente objeto JSON, que representa un estado del simulador
	public JSONObject getState() {

		JSONObject obj = new JSONObject();
		JSONArray g = new JSONArray();

		// se añaden los grupos al JSONObject que devolveremos por orden de creacion
		for (String id : listId) {
			g.put(groups.get(id).getState());
		}
		obj.put("groups", g);
		obj.put("time", tAct);

		return obj;
	}

	public String toString() {
		return getState().toString();
	}
	
	//resetea el mapa de grupos, las lista de ids y el tiempo actual
	public void reset() {
		groups.clear();
		listId.clear();
		tAct = 0.0;
		for(SimulatorObserver so : observerList) {
			so.onReset(_groupsRO, t, tAct);
		}
	}
	
	public void setDeltaTime(double dt) {
		if(dt < 0)
			throw new IllegalArgumentException("dt no es positivo");
		this.dt = dt;
		for(SimulatorObserver so : observerList) {
			so.onDeltaTimeChanged(dt);
		}
	}
	
	public void addObserver(SimulatorObserver o) {
		observerList.add(o);
		o.onRegister(_groupsRO, t, tAct);
	}
	
	public void removeObserver(SimulatorObserver o) {
		observerList.remove(o);
	}

}
