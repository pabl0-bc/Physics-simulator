package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body> {

	public StationaryBodyBuilder() {
		super("st_body", "StationaryBody");
	}

	@Override
	protected Body createInstance(JSONObject data) {
		if (!data.has("id") || !data.has("gid") || !data.has("m") || !data.has("p")
				|| data.getJSONArray("p").length() != 2) {
			
			throw new IllegalArgumentException("Faltan datos/No vector2D");
		}

		Vector2D pos = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1));

		return new StationaryBody(data.getString("id"), data.getString("gid"), pos, data.getDouble("m"));
	}

	@Override
	public JSONObject getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
