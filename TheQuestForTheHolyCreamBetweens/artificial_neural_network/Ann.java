package com.artificial_neural_network;

import java.util.Arrays;
import java.util.Random;

import com.artificial_neural_network.Neuron;

public class Ann {
	public Neuron neuron1;
	public Neuron neuron2;
	public Neuron neuron3;
	public Neuron neuron4;
	public Neuron decisionNode;
	public double[] woodsLocations;
	public double[] locationConnections;
	public double sum;
	public int bias;
	public Neuron[] neurons;
	public double[] loc1Connections,loc2Connections,loc3Connections,loc4Connections,loc5Connections,loc6Connections,loc7Connections;
	
	public double e, f, z, c, x, d1, d2, d3, E;
	
	Random random;
	
	public Ann() {
		random = new Random();
		e = 2.17;
		E = 1;
		f = 1;
		z = 0;
		sum = 0;
		bias = 3;
		c = 0;
		d1 = 0;
		d2 = 0;
		d3 = 0;
		
		//first layer
		neuron1 = new Neuron();
		neuron2 = new Neuron();
		
		//second layer
		neuron3 = new Neuron();
		neuron4 = new Neuron();
		
		//decision
		decisionNode = new Neuron();
		
		//input values for first layer neurons
		neuron2.assignInputValue(3);
		//set this ^ to player's average hit rate
		
		//neuron connections
		neuron1.connect(0.5, neuron1, neuron3);
		neuron2.connect(0.5, neuron2, neuron4);
		neuron1.connect(0.5, neuron1, neuron4);
		neuron2.connect(0.5, neuron2, neuron3);
		neuron3.connect(0.5, neuron3, decisionNode);
		neuron4.connect(0.5, neuron4, decisionNode);
		
		woodsLocations = new double[] {1,2,3,4,5,6,7};	//array has 6 slots (arrays start at 0)
		locationConnections = new double[5];	//4 possible connections (arrays start at 0) the extra slot is for your current location
		//second slot is to the left of, third slot is above, fourth slot is to the right of, fifth is below
		
		neurons = new Neuron[] {neuron1,neuron2,neuron3,neuron4,decisionNode};	//array of neurons for back prop recursion
		
		loc1Connections = new double[4];
		loc2Connections = new double[4];
		loc3Connections = new double[4];
		loc4Connections = new double[4];
		loc5Connections = new double[4];
		loc6Connections = new double[4];
		loc7Connections = new double[4];
		neuron2.outputArrayValue = new double[4];
		
		adjustLocationConnections(false,0,0,locationConnections);
	}
	
	public static boolean valueCheck(String[] arr, String targetValue) {
		return Arrays.asList(arr).contains(targetValue);
	}
	
	public void assignInputs(int location) {
		neuron1.assignInputValue(location);
		
		for(int i = 0; i < woodsLocations.length; i++) {
			if(location == woodsLocations[i]) {
				woodsLocations[i] = 0;
				neuron2.assignInputArrayValue(woodsLocations);
			}
		}
		
		neuron3.outputArrayValue = new double[4];
		neuron4.outputArrayValue = new double[4];
		decisionNode.inputArrayValue = new double[4];
		decisionNode.outputArrayValue = new double[4];
	}
	
	public void adjustLocationConnections(boolean yesOrNo,int direction,double location,double[] connectionList) {
		if(yesOrNo) {
			connectionList[direction] = location; 
		}
		else {
			connectionList[0] = 2;
			connectionList[1] = 3;
			connectionList[2] = 4;
			connectionList[3] = 5;
		}
	}
	
	public void findSum() {
		for(int i = 0; i < decisionNode.outputArrayValue.length; i++) {
			sum += decisionNode.outputArrayValue[i];
		}
	}
	
	//maybe change the input for the second layer neurons to the sum instead of individual array values
	public void backProp(double targetValue) {
		//finding the cost each neuron
		for(int r = 2; r < (neurons.length-1); r++) {
			for(int i = 0; i < neurons[r].outputArrayValue.length; i++) {
				if(r == 3) {
					c = Math.pow((neurons[r+1].outputValue-targetValue), 2);	//cost
					d1 = 2*(neurons[r+1].outputValue-targetValue);	//derivative of second neuron with respect to first neuron
					x = (neurons[r+1].outputValue*(1-neurons[r+1].outputValue));	//sigmoid derivative
				}
				else {
					if(r == 2) {
						c = Math.pow((neurons[r+2].outputValue-targetValue), 2);	//cost
						d1 = 2*(neurons[r+2].outputValue-targetValue);	//derivative of second neuron with respect to first neuron
						x = (neurons[r+2].outputValue*(1-neurons[r+2].outputValue));	//sigmoid derivative
					}
				}
				
				z = (neurons[r].connectionWeight[1]*neurons[r].outputArrayValue[i]);	//weighted sum of neuron
				
				f = (1/(1+Math.pow(Math.E, -neurons[r].outputArrayValue[i])));	//sigmoid of neuron output
				
				d2 = x*f*d1;	//derivative of the cost with respect to the weight
				d3 = 1*f*d1;	//derivative of the cost with respect to the bias
				
				/*
				System.out.println("neuron" + (r+1) + " cost: " + c);
				System.out.println("Partial Gradient: " + d2);
				*/
				neuron4.connectionWeight[1] += d2;
				neuron4.outputArrayValue[i] -= d3;
				
				System.out.println("neuron4 output: " + neuron4.outputArrayValue[2]);
				System.out.println("output: " + decisionNode.outputValue);
				System.out.println("weight: " + neuron4.connectionWeight[1]);
			}
		}
	}
	

	public void calculateOutput(double targetValue) {	//the higher the number the more connections it has
		for(int i = 0; i < neuron3.outputArrayValue.length; i++) {
			neuron3.outputArrayValue[i] = ((neuron1.inputValue*neuron1.connectionWeight[0]) + (neuron2.inputArrayValue[i]*neuron2.connectionWeight[1]));	// <- this is sigma
			E = (1/(1+Math.pow(Math.E, -neuron3.outputArrayValue[i])));
			
			neuron4.outputArrayValue[i] = (neuron2.inputArrayValue[i]*neuron2.connectionWeight[0]); //+ (neuron1.inputValue*neuron1.connectionWeight[1]));
			f = (1/(1+Math.pow(Math.E, -neuron4.outputArrayValue[i])));
			
			//use cost instead of percent error
			decisionNode.inputArrayValue[i] = (E*neuron3.connectionWeight[2] ) + (f*neuron4.connectionWeight[2]);
			decisionNode.outputArrayValue[i] = (1/(1+Math.pow(Math.E, -decisionNode.inputArrayValue[i])));
		}
		
		sum = 0;
		findSum();
		//decisionNode.outputValue = (e*neuron3.connectionWeight[2] ) + (f*neuron4.connectionWeight[2]);
		decisionNode.outputValue = sum - bias;
		
		backProp(targetValue);
	}
}
