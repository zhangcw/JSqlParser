select deptno
     ,      cast(
               collect(
                  DISTINCT job
                  order by job
                  ) as varchar2_ntt) as distinct_ordered_jobs
     from   emp
     group  by
            deptno