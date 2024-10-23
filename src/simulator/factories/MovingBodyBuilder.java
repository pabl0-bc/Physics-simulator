package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;

public class MovingBodyBuilder extends Builder<Body> {

	public MovingBodyBuilder() {
		super("mv_body", "MovingBody");
	}

	@Override
	protected Body createInstance(JSONObject data) {
		
		if(!data.has("id") || !data.has("gid") || !data.has("m") || !data.has("p") || !data.has("v") ||
				data.getJSONArray("v").length() != 2 || data.getJSONArray("p").length() != 2) {
			
			throw new IllegalArgumentException("Faltan datos/No vector2D");
		}
		
		Vector2D pos = new Vector2D(data.getJSONArray("p").getDouble(0), data.getJSONArray("p").getDouble(1));

		Vector2D vel = new Vector2D(data.getJSONArray("v").getDouble(0), data.getJSONArray("v").getDouble(1));
	
		return new MovingBody(data.getString("id"), data.getString("gid"), pos, vel, data.getDouble("m"));
	}

	@Override
	public JSONObject getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
