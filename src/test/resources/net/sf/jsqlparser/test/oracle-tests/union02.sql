(select DISTINCT job_id from hr.jobs)
union all
(select DISTINCT job_id from hr.job_history)

