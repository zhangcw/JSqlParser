select * from v.e
where
	cid <> rid
	and  rid  not in
	(
		(select DISTINCT  rid  from  v.s )
		union
		(select DISTINCT  rid  from v.p )
	)
	and  `timestamp`  <= 1298505600000

