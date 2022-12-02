package realZone;

import dt.original.Gammq;
import faultZone.FaultZone;
import model.Testcase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class realZone_Gammq extends FaultZone {
    Gammq correct = new Gammq();
    Gammq mutation;

    Method method;
    Class<?>[] paramters;
    int pcount = 0;

    public realZone_Gammq(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Gammq) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }

    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException {
        double x =  testcase.getValue(0);
        double y =  testcase.getValue(1);
        double z = x * y / 100.0;
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        try {
            double a = correct.exe(x,y);
            double b = mutation.exe(x,y);
            if(a==b) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}