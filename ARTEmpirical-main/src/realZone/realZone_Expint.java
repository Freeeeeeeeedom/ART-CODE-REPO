package realZone;

import dt.original.Encoder;
import dt.original.Expint;
import faultZone.FaultZone;
import model.Testcase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

public class realZone_Expint extends FaultZone {

    Expint correct = new Expint();
    Expint mutation;

    Method method;
    Class<?>[] paramters;
    int pcount = 0;

    public realZone_Expint(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Expint) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }
    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException {
        int x = (int) testcase.getValue(0);
        double y = testcase.getValue(1);
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        try {
            double a = correct.exe(x, y);
            double b = mutation.exe(x,y);
            if(a==b) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}
