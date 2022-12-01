package faultZone;
import model.Testcase;

import mutant.Bessj.Bessj_AODU_1;
import original.*;

import java.io.File;

public class FaultZone_Bessj extends FaultZone{

    Bessj correct = new Bessj();

    public FaultZone_Bessj(){

    }

    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException{
        boolean jutice = false;
        double x = testcase.getValue(0);
        double y = testcase.getValue(1);


        try {
            double a = correct.bessj((int) x,y);
            double b = new Bessj_AODU_1().bessj((int)x,y);
            if(a == b) jutice = true;
        }catch (Exception ignored){

        }
        return jutice;
    }
}
