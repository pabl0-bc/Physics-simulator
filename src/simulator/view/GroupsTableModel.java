package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import org.json.JSONArray;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class GroupsTableModel extends AbstractTableModel implements SimulatorObserver{
	String[] _header = { "Id", "Force Laws", "Bodies" };
	List<BodiesGroup> _groups;
	JSONArray _body;
	 private int[] columnWidths;

	GroupsTableModel(Controller ctrl) {
		_groups = new ArrayList<>();
		_body = new JSONArray();
		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return _groups == null ? 0 : _groups.size();
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
			o = _groups.get(rowIndex).getId();
			break;
		case 1:
			o = _groups.get(rowIndex).getForceLawsInfo();
			break;
		case 2:
			_body = _groups.get(rowIndex).getState().getJSONArray("bodies");
			String bodyId = "";
			for(int i = 0; i < _body.length(); i++) {
				bodyId += " " + _body.getJSONObject(i).getString("id");
			}
			o = bodyId;
			break;
		}
		
		return o;
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {		
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_groups.clear();
		update();
		
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		for(BodiesGroup g: groups.values()) {
			_groups.add(g);
		}
		update();
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_groups.add(g);
		update();
		fireTableStructureChanged();
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		update();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		
	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		fireTableDataChanged();
		
	}
	
	public void setColumnWidth(int column, int width) {
        columnWidths[column] = width;
        fireTableStructureChanged();
    }
	
	public int getColumnWidth(int column) {
        return columnWidths[column];
    }

	
	private void update() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				fireTableStructureChanged();
			}
		});
	}

}
