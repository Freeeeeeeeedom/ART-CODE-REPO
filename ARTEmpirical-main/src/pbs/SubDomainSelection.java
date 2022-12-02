package pbs;

import model.Testcase;

import java.util.List;

public  interface SubDomainSelection {
     //已经测试的测试用例集合
     public List<Testcase> total = null;
     //子块选择算法
     List<Testcase> select(List<List<Testcase>> SubDomains);
     public void SetTotal(List<Testcase> total);
}
