package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {
	
	
	public MovingTowardsFixedPointBuilder() {
		super("mtfp", "MovingTowardsFixedPoint");
	}

	@Override
	protected ForceLaws createInstance(JSONObject data) {
		Vector2D c = new Vector2D();
		JSONArray aux = new JSONArray();
		double g = 9.81;
		if(data.has("g"))
			g = data.getDouble("g");
		if(data.has("c")) {
			aux = data.getJSONArray("c");
			c = new Vector2D(aux.getDouble(0), aux.getDouble(1));
		}

		return new MovingTowardsFixedPoint(c, g);
	}
	
	@Override
	public JSONObject getData() {
		JSONObject obj = new JSONObject();
		
		obj.put("c", "Point towards which bodies move, e.g, [100.0,50.0],");
		obj.put("g", "Length of the acceleration vector, e.g, 9.8");
		return obj;
	}

}
