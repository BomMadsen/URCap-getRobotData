package com.ur.urcap.sample.getRobotData.impl;

// Used for reading from RealTime Client
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

// Used to map joint mode codes to a descriptive string
import java.util.HashMap;
import java.util.Map;

public class getRobotRealtimeData {
	
	public getRobotRealtimeData() { }
	
	// localhost IP
	private String TCP_IP = "127.0.0.1";
	// Port for real time client
	private int TCP_port = 30003;
	
	// Initialize a new array for storing received data from realtime interface
	private double[] RealtimeMessage;
	
	// Public method that reads a snapshot from real time client interface
	public void readNow(){
		readSocket();
	}
	
	// Internal method that actually reads the data
	private void readSocket(){
		try{
			// Create a new Socket Client
			Socket rt = new Socket(TCP_IP, TCP_port);
			if (rt.isConnected()){
				System.out.println("Connected to UR Realtime Client");
			}
			
			// Create stream for data
			DataInputStream in;
			in = new DataInputStream(rt.getInputStream());
			
			// Read the integer available in stream
			int length = in.readInt();
			System.out.println("Length is "+length);
			
			// Initialize size of RealtimeMessage be using received length
			RealtimeMessage = new double[length];
			// Add length integer to output array
			RealtimeMessage[0] = length;
			
			// Calculate how much data is available from the length
			int data_available = (length-4)/8;
			System.out.println("There are "+data_available+" doubles available");
			
			// Loop over reading the available data
			int i = 1;
			while (i <= data_available){
				RealtimeMessage[i] = in.readDouble();
				System.out.println("Index "+i+" is "+RealtimeMessage[i]);
				i++;
			}
			
			// Perform housekeeping 
			in.close();
			rt.close();
			System.out.println("Disconnected from UR Realtime Client");
		} 
		catch (IOException e){
			System.out.println(e);
		}
	}
	
	/*
	 * Creating enum to map start index and lengths of data entries
	 * According to specification of RealTimeClient in Excel sheet
	 */
	private enum RTinfo {
		// name			(index in plot, number of doubles)
		q_target		(2, 6),
		qd_target		(8, 6),
		qdd_target		(14, 6),
		q_actual		(32, 6),
		qd_actual		(38, 6),
		TCP_actual		(56, 6),
		TCPd_actual		(62, 6),
		TCP_force		(68, 6),
		TCP_target		(74, 6),
		TCPd_target		(80, 6),
		temp_joint		(87, 6),
		robotmode		(95, 1),
		jointmode		(96, 6),
		safetymode		(97, 1),
		tcp_accel		(109, 3),
		speedscaling	(118, 1),
		prgstate		(132, 1);
		// More data points could be added if desired, by following above example and according to specification.
		
		private final int index;
		private final int count;
		RTinfo(int index, int count){
			this.index = index;
			this.count = count;
		}
		private int index() {return index;}
		private int count() {return count;}
	}

	/*****************************************************************
	 * Methods for returning the relevant data to the calling classes
	 *****************************************************************/
	
	/*
	 * Example get function, to read actual joint positions as double[] 
	 */
	public double[] getActualJointPose(){
		double[] val = new double[RTinfo.q_actual.count()];
		int i = 0;
		while (i < RTinfo.q_actual.count()){
			val[i] = RealtimeMessage[RTinfo.q_actual.index()+i];
			++i;
		}
		return val;
	}
	
	/*
	 * Example get function, to read actual TCP position as double[]
	 */
	public double[] getActualTcpPose(){
		double[] val = new double[RTinfo.TCP_actual.count()];
		int i = 0;
		while (i < RTinfo.TCP_actual.count()){
			val[i] = RealtimeMessage[RTinfo.TCP_actual.index()+i];
			++i;
		}
		return val;
	}
	
	/*
	 * Example get function, to read joint temperatures as double[]
	 */
	public double[] getJointTemperatures(){
		double[] val = new double[RTinfo.temp_joint.count()];
		int i = 0;
		while (i < RTinfo.temp_joint.count()){
			val[i] = RealtimeMessage[RTinfo.temp_joint.index()+i];
			++i;
		}
		return val;
	}
	
	/*
	 * Example to read joint state as useful information
	 */
	public String[] getJointStatus(){
		// Create a map binding message code to state message
		// According to Excel sheet client interface specification
		Map<Double, String> jointStates = new HashMap<Double, String>();
		jointStates.put((double) 236, "SHUTTING_DOWN");
		jointStates.put((double) 237, "DUAL_CALIB_MODE");
		jointStates.put((double) 238, "BACKDRIVE");
		jointStates.put((double) 239, "POWER_OFF");
		jointStates.put((double) 245, "NOT_RESPONDING");
		jointStates.put((double) 246, "MOTOR_INIT");
		jointStates.put((double) 247, "BOOTING");
		jointStates.put((double) 248, "DUAL_CALIB_ERROR");
		jointStates.put((double) 249, "BOOTLOADER");
		jointStates.put((double) 250, "CALIBRATION_MODE");
		jointStates.put((double) 252, "FAULT");
		jointStates.put((double) 253, "RUNNING");
		jointStates.put((double) 255, "IDLE");
		
		String[] val = new String[RTinfo.jointmode.count()];
		int i = 0;
		while (i < RTinfo.jointmode.count()){
			// Read the code for given joint
			double code = RealtimeMessage[RTinfo.jointmode.index()+i];
			
			// Check if the key is known in the map
			if(jointStates.containsKey(code)){
				// Read corresponding message
				val[i] = jointStates.get(code);
			}
			else{
				// If unknown code, show "unknown"
				val[i] = "UNKNOWN_CODE";
			}
			++i;
		}
		return val;
	}
	
	/*
	 * Example to read the actual safety limited speed scaling in percent
	 */
	public double getSpeedScaling(){
		double scaling = RealtimeMessage[RTinfo.speedscaling.index()];
		return scaling * 100; // Converted to percent
	}
	
	/*
	 * TODO Add your own get functions here.
	 */
	
	
	
}