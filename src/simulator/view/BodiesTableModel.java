package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
	String[] _header = { "Id", "gId", "Mass", "Velocity", "Position", "Force" };
	List<Body> _bodies;

	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
		
	}
	
	@Override
	public int getRowCount() {
		return _bodies == null ? 0 : _bodies.size();
	}

	@Override
	public int getColumnCount() {
		return _header.length;
	}

	@Override
	public String getColumnName(int col) {
		return _header[col];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String o = "";

		switch (columnIndex) {
		case 0:
			o = _bodies.get(rowIndex).getId();
			break;
		case 1:
			o = _bodies.get(rowIndex).getgId();
			break;
		case 2:
			o = Double.toString(_bodies.get(rowIndex).getMass());
			break;
		case 3:
			o = _bodies.get(rowIndex).getVelocity().toString();
			break;
		case 4:
			o = _bodies.get(rowIndex).getPosition().toString();
			break;
		case 5:
			o = _bodies.get(rowIndex).getForce().toString();
			break;
		}

		return o;
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		fireTableDataChanged();
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_bodies.clear();
		update(groups);
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		for(BodiesGroup g: groups.values()) {
			for(Body b: g) {
				_bodies.add(b);
			}
		}
		update(groups);
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		update(groups);
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		_bodies.add(b);
		update(groups);
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		fireTableDataChanged();
	}
	
	private void update(Map<String, BodiesGroup> group) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				fireTableStructureChanged();
			}
		});
	}
	
}