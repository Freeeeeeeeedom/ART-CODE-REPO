package pbs;

import model.Testcase;

import java.util.ArrayList;
import java.util.List;

public class MaximumSize implements SubDomainSelection{
    public List<Testcase> total = null;
    @Override
    public List<Testcase> select(List<List<Testcase>> SubDomains) {
        int cur_size = -1;
        List<Testcase> selectedDomain = SubDomains.get(0);
        for (List<Testcase> subDomain : SubDomains) {
            List<Testcase> temp = new ArrayList<>(subDomain);
            if(total!=null) temp.retainAll(total);
            if (temp.size() == 0 && subDomain.size() > cur_size) {
                cur_size = subDomain.size();
                selectedDomain = subDomain;
            }
        }
        return selectedDomain;
    }

    public void SetTotal(List<Testcase> total){
        this.total = total;
    }
}
