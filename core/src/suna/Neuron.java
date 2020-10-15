package suna;

/** This class will represent the individual neurons in the SUNA algorithm
  * There will need to be two types of neurons; Control Neurons, and regular Neurons
  * Control Neurons will alter the output of other neurons by either delaying, or disabling them
  * Regular Neurons behave like the neurons in NEAT/HyperNEAT in just feeding forward the data
  * Neurons will now have an activation time and activation order
  * That order is: Input Neurons, Control Neurons that do not have a link towards them, Control Neurons that have a link
  * to them, then any other neurons that are above the activation threshold.
  * Neurons can have different activation function, exactly the same as in HyperNEAT.
  */
public class Neuron {
}