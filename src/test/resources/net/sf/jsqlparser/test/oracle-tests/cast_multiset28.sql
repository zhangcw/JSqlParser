select deptno
     ,      cast(
               collect(
                  DISTINCT empsal_ot(ename, sal)
                  ) as empsal_ntt) as empsals
     from   emp
     group  by
            deptno