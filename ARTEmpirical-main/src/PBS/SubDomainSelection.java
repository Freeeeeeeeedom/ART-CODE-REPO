package pbs;

import model.Testcase;

import java.util.List;

public  interface SubDomainSelection {
     public List<Testcase> total = null;
     List<Testcase> select(List<List<Testcase>> SubDomains);
     public void SetTotal(List<Testcase> total);
}
