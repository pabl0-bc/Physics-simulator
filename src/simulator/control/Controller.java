package simulator.control;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.NoForce;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {

	PhysicsSimulator ps;
	Factory<ForceLaws> flFactory;
	Factory<Body> bFactory;

	public Controller(PhysicsSimulator ps, Factory<ForceLaws> flFactory, Factory<Body> bFactory) {
		this.ps = ps;
		this.flFactory = flFactory;
		this.bFactory = bFactory;
	}

	// ejecuta la simulacion completa y da formato al archivo JSON de salida
	public void run(int n, OutputStream out) {
		PrintStream p = new PrintStream(out);

		p.println("{");
		p.println("\"states\": [");
		p.println(ps.getState());
		// run the sumulation n steps, etc.
		for (int i = 0; i < n; i++) {
			ps.advance();
			p.println("," + ps.getState().toString());
		}
		p.println("]");
		p.println("}");
	}

	// carga los datos del archivo de entrada en el simulador
	public void loadData(InputStream in) {
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));

		JSONArray keys = jsonInupt.getJSONArray("groups");
		JSONArray laws = new JSONArray();
		JSONArray bodies = jsonInupt.getJSONArray("bodies");
		ForceLaws fl = new NoForce();

		for (int i = 0; i < keys.length(); i++) {
			ps.addGroup(keys.getString(i));
		}

		// en caso de que algun grupo en especifico tenga una force law especifica se le
		// asignara, en caso contrario se asignara la fl por defecto
		if (jsonInupt.has("laws")) {
			laws = jsonInupt.getJSONArray("laws");
			for (int i = 0; i < laws.length(); i++) {
				fl = flFactory.createInstance(laws.getJSONObject(i).getJSONObject("laws"));
				ps.setForceLaws(laws.getJSONObject(i).getString("id"), fl);
			}
		}

		for (int i = 0; i < bodies.length(); i++) {
			Body b = bFactory.createInstance(bodies.getJSONObject(i));
			ps.addBody(b);
		}
	}
	
	//llama al método reset del simulador
	public void reset() {
		ps.reset();
	}
	
	//ejecuta setDeltaTime del simulador.
	public void setDeltaTime(double dt) {
		ps.setDeltaTime(dt);
	}
	
	//llama al método addObserver del simulador.
	public void addObserver(SimulatorObserver o) {
		ps.addObserver(o);
	}
	
	// llama al método removeObserver del simulador
	public void removeObserver(SimulatorObserver o) {
		ps.removeObserver(o);
	}
	
	//devuelve la lista generada por el método getInfo() de la factoría de leyes de fuerza
	public List<JSONObject> getForceLawsInfo(){
		return flFactory.getInfo();
	}
	
	//usa la factoría de leyes de fuerza
	//actual para crear una nueva instancia de la ley de fuerza a partir de info
	public void setForcesLaws(String gId, JSONObject info) {
		ForceLaws fl = flFactory.createInstance(info);
		ps.setForceLaws(gId, fl);
	}
	
	public void run(int n) {
		for(int i=0; i<n; i++) ps.advance();
	}
}
