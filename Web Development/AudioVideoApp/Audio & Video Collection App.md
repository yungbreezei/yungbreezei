# 🎶 Project: Audio/Video Collection Web App
![AppLogo](https://github.com/user-attachments/assets/5e04318f-8299-46da-83cc-d4be96daeab4)

***

## Task
Front-end, processing, back-end, and general design diagrams illustrating your web app workflow process

***

````sql
SELECT 
  sales.customer_id, 
  SUM(menu.price) AS total_sales
FROM dannys_diner.sales
INNER JOIN dannys_diner.menu
  ON sales.product_id = menu.product_id
GROUP BY sales.customer_id
ORDER BY sales.customer_id ASC; 
````
