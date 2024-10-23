package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	public NewtonUniversalGravitationBuilder() {
		super("nlug", "NewtonUniversalGravitation");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		double g = 6.67e-11;
		if(data.has("g"))
			g = data.getDouble("g");
		return new NewtonUniversalGravitation(g);
	}
	
	@Override
	public JSONObject getData() {
		JSONObject obj = new JSONObject();
		
		obj.put("G",  "Gravitational constant, e.g, 6.67e-11");
		return obj;
	}

}
