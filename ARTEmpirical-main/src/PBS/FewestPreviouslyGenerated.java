package pbs;

import model.Testcase;

import java.util.ArrayList;
import java.util.List;

public class FewestPreviouslyGenerated implements SubDomainSelection{
    public List<Testcase> total = null;
    @Override
    public List<Testcase> select(List<List<Testcase>> SubDomains) {
        List<Testcase> selectedDomain = SubDomains.get(0);
        List<Testcase> T = new ArrayList<>(total);
        T.retainAll(selectedDomain);
        int cur_size = T.size();
        for(int i=1;i<SubDomains.size();i++){
            List<Testcase> intersection = new ArrayList<>(total);
            intersection.retainAll(SubDomains.get(i));
            if(intersection.size() < cur_size){
                cur_size = intersection.size();
                selectedDomain = SubDomains.get(i);
            }
        }
        return selectedDomain;
    }

    @Override
    public void SetTotal(List<Testcase> total){
        this.total = total;
    }
}
