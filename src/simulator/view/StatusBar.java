package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

class StatusBar extends JPanel implements SimulatorObserver {
	private JLabel _timeTag, _gropsTag;


	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(BorderFactory.createBevelBorder(1));
		_timeTag = new JLabel("0.0");
		this.add(new JLabel("Time: "));
		_timeTag.setPreferredSize(new Dimension(100, 20));
		this.add(_timeTag);

		_gropsTag = new JLabel("0");
		this.add(new JLabel("Groups: "));
		this.add(_gropsTag);

		JSeparator s2 = new JSeparator(JSeparator.VERTICAL);
		s2.setPreferredSize(new Dimension(20, 20));
		this.add(s2);
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		_timeTag.setText(Double.toString(time));
		_gropsTag.setText(Integer.toString(groups.size()));
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		_timeTag.setText(Double.toString(dt));
		_gropsTag.setText(Integer.toString(groups.size()));
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		_timeTag.setText(Double.toString(dt));
		_gropsTag.setText(Integer.toString(groups.size()));
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		_gropsTag.setText(Double.toString(groups.size()));
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {

	}

	@Override
	public void onDeltaTimeChanged(double dt) {

	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {

	}
}
