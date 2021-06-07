select *
from   (select product_code, quantity
        from   pivot_test)
pivot xml (sum(quantity) as sum_quantity for product_code in (select DISTINCT product_code
                                                                from   pivot_test
                                                                where  id < 10))