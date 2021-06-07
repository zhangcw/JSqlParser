select deptno
     ,      cast(
               collect(job)
                  as varchar2_ntt
               ) multiset union DISTINCT varchar2_ntt() as distinct_jobs
     from   emp
     group  by
            deptno