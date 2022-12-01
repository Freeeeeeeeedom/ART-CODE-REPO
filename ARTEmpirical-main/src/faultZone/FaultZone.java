package faultZone;

import model.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Simulation experiments-abstract class
 */
public abstract class FaultZone {
	public double fail_rate;
	public abstract Boolean isCorrect(Testcase testcase) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
	
}
