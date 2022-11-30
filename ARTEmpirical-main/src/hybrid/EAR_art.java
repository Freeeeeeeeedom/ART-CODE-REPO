package hybrid;

import model.AbstractART;
import model.DomainBoundary;
import model.Testcase;

/**
 * 2009-A Novel Evolutionary Approach for Adaptive Random Testing
 * SBS & QRS实现
 * SBS : SA
 * QRS : Sobol    generate random testcases
 *
 * author: yhj
 */
public class EAR_art extends AbstractART {
    public DomainBoundary inputBoundary;

    public double p;

    public EAR_art(DomainBoundary inputBoundary, double p){
        this.inputBoundary = inputBoundary;
        this.p = p;
    }

    public EAR_art(){}
    @Override
    public Testcase Best_candidate() {
        return null;
    }

    @Override
    public void testEfficiency(int pointNum) {

    }
}
