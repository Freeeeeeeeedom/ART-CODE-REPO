package realZone;

import dt.original.Gammq;
import dt.original.Median;
import faultZone.FaultZone;
import model.Testcase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class realZone_Median extends FaultZone {
    Median correct = new Median();
    Median mutation;

    Method method;
    Class<?>[] paramters;
    int pcount = 0;
    public realZone_Median(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Median) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }
    @Override
    public Boolean isCorrect(Testcase testcase) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        int x = (int) testcase.getValue(0);
        int y = (int) testcase.getValue(1);
        int[] m = new int[]{x,y};
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        try {
            double a = correct.exe(m);
            double b = mutation.exe(m);
            if(a==b) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}
