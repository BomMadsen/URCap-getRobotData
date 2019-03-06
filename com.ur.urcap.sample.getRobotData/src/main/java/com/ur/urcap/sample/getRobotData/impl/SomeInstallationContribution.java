package com.ur.urcap.sample.getRobotData.impl;

import java.awt.EventQueue;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.LabelComponent;

// Import the Real Time client reader class to this class
import com.ur.urcap.sample.getRobotData.impl.RobotRealtimeReader;

public class SomeInstallationContribution implements InstallationNodeContribution {

	private final DataModel model;
	private final URCapAPI api;

	public SomeInstallationContribution(URCapAPI api, DataModel model) {
		this.api = api;
		this.model = model;
	}
	
	// Creating handle to getRobotRealtimeData class
	RobotRealtimeReader realtimeReader = new RobotRealtimeReader();
	
	/*
	 * Methods to show data in UI
	 */
	
	private static final String lblActualTCP = "lblActualTCP";
	private static final String lblActualJoints = "lblActualJoints";
	private static final String lblJointTemps = "lblJointTemps";
	private static final String lblJoint0 = "lblJoint0";
	private static final String lblJoint1 = "lblJoint1";
	private static final String lblJoint2 = "lblJoint2";
	private static final String lblJoint3 = "lblJoint3";
	private static final String lblJoint4 = "lblJoint4";
	private static final String lblJoint5 = "lblJoint5";
	private static final String lblSpeedScaling = "lblSpeedScaling";
	private Timer uiTimer;
	
	@Label(id=lblActualTCP)
	private LabelComponent showActualTCP;
	
	@Label(id=lblActualJoints)
	private LabelComponent showActualJoints;
	
	@Label(id=lblJointTemps)
	private LabelComponent showJointTemps;
	
	@Label(id=lblJoint0)
	private LabelComponent showJoint0;
	
	@Label(id=lblJoint1)
	private LabelComponent showJoint1;
	
	@Label(id=lblJoint2)
	private LabelComponent showJoint2;
	
	@Label(id=lblJoint3)
	private LabelComponent showJoint3;
	
	@Label(id=lblJoint4)
	private LabelComponent showJoint4;
	
	@Label(id=lblJoint5)
	private LabelComponent showJoint5;
	
	@Label(id=lblSpeedScaling)
	private LabelComponent showSpeedScaling;
	
	private void updateUI() {
		// Used to read a snapshot from the Real Time Client
		// Should be called at least once before accessing data.
		realtimeReader.readNow();
		
		// Create a decimal formatter, to decide how many digits after decimal separator 
		DecimalFormat df = new DecimalFormat("#.####");
		
		// Read the actual TCP pose
		double[] tcp = realtimeReader.getActualTcpPose();
		String showTcp = "p["+
				df.format(tcp[0])+","+
				df.format(tcp[1])+","+
				df.format(tcp[2])+","+
				df.format(tcp[3])+","+
				df.format(tcp[4])+","+
				df.format(tcp[5])+"]";
		showActualTCP.setText(showTcp);
		
		// Read the actual joint positions
		double[] joints = realtimeReader.getActualJointPose();
		String showJoints = "["+
				df.format(joints[0])+","+
				df.format(joints[1])+","+
				df.format(joints[2])+","+
				df.format(joints[3])+","+
				df.format(joints[4])+","+
				df.format(joints[5])+"] rad";
		showActualJoints.setText(showJoints);
		
		// Read the joint temperatures
		double[] temps = realtimeReader.getJointTemperatures();
		String showTemps = "["+
				df.format(temps[0])+","+
				df.format(temps[1])+","+
				df.format(temps[2])+","+
				df.format(temps[3])+","+
				df.format(temps[4])+","+
				df.format(temps[5])+"] \u00b0C";
		showJointTemps.setText(showTemps);
		
		// Read the joint modes
		String[] jointmode = realtimeReader.getJointStatus();
		showJoint0.setText("Base: "+jointmode[0]);
		showJoint1.setText("Shoulder: "+jointmode[1]);
		showJoint2.setText("Elbow: "+jointmode[2]);
		showJoint3.setText("Wrist 1: "+jointmode[3]);
		showJoint4.setText("Wrist 2: "+jointmode[4]);
		showJoint5.setText("Wrist 3: "+jointmode[5]);

		double scale = realtimeReader.getSpeedScaling();
		showSpeedScaling.setText(df.format(scale)+" %");
	}
	
	@Override
	public void openView() {
		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateUI();
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void closeView() { 
		if (uiTimer != null) {
			uiTimer.cancel();
		}
	}

	public boolean isDefined() { return true; }

	@Override
	public void generateScript(ScriptWriter writer) { }
	
}
