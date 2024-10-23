package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {

	public NoForceBuilder() {
		super("nf", "NoForce");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		return new NoForce();
	}
	
	@Override
	public JSONObject getData() {
		return new JSONObject();
	}

}
