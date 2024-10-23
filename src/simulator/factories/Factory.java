package simulator.factories;

import java.util.List;

import org.json.JSONObject;

public interface Factory<T> {

	// recibe una estructura JSON que describe el objeto a crear, y devuelve una
	// instancia de la clase correspondiente
	public T createInstance(JSONObject info);

	// devuelve una lista de objetos JSON que describe qué puede ser creado por la
	// factoria
	public List<JSONObject> getInfo();
}
