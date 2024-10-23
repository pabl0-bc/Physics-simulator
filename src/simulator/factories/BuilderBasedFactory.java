package simulator.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private Map<String, Builder<T>> _builders;
	private List<JSONObject> _buildersInfo;

	public BuilderBasedFactory() {
		
		this._builders = new HashMap<>();
		this._buildersInfo = new ArrayList<>();
	}

	public BuilderBasedFactory(List<Builder<T>> builders) {
		
		this();
		
		for (Builder b : builders) {
			addBuilder(b);
		}
	}

	public void addBuilder(Builder<T> b) {
		_builders.put(b.getTypeTag(), b);
		_buildersInfo.add(b.getInfo());
	}

	@Override
	public T createInstance(JSONObject info) {
		if (info == null) {
			throw new IllegalArgumentException("Invalid value for createInstance: null");
		}
		if(info.has("type")) {
			if(_builders.get(info.getString("type")) == null)
				throw new IllegalArgumentException("Invalid value for createInstance: null");
			else
				if(info.has("data"))
					return _builders.get(info.get("type")).createInstance(info.getJSONObject("data"));
				else
					return _builders.get(info.get("type")).createInstance(new JSONObject());
		}	
		else
			throw new IllegalArgumentException("Invalid value for createInstance: " + info.toString());
	}

	@Override
	public List<JSONObject> getInfo() {
		return Collections.unmodifiableList(_buildersInfo);
	}

}
