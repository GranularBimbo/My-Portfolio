package com.artificial_neural_network;

public class Neuron {
	public Neuron connections[];
	public double connectionWeight[];
	public double inputValue, outputValue;
	public double[] inputArrayValue;
	public double[] outputArrayValue;
	
	public Neuron() {
		//these contain connection information and connection weight
		connections = new Neuron[4];	//4 slots incase i add an extra layer for deeper learning
		connectionWeight = new double[4];
	}
	
	//creates a connection between neurons
	public void connect(double weight, Neuron startingNeuron, Neuron targetNeuron) {
		for(int i = 0; i < connections.length; i++) {
			if(startingNeuron.connections[i] == null) {
				startingNeuron.connections[i] = targetNeuron;
				startingNeuron.connectionWeight[i] = weight;
				
				if(targetNeuron.connections[i] == null) {
					connect(0.5,targetNeuron,startingNeuron);
				}
				
				break;
			}
		}
	}
	
	public void assignInputValue(double value) {	//only for input layer neurons
		this.inputValue = value;
	}
	
	public void assignInputArrayValue(double[] value) {
		this.inputArrayValue = value;
	}
}
