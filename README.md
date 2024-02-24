**You're tasked with creating a web application for Randy's Candies, a local candy store in Chicago. Randy, the owner, wants the application to do three things:**

1. Display items that are almost out of stock (at less than 25% capacity)

2. Allow him to input how many of each of those items he wants to re-order

3. Determine the lowest total cost of re-ordering those items

The web application consists of a Java backend and React frontend.

**Backend**

**Workbooks**

The backend contains two workbooks, **Inventory.xlsx** and **Distributors.xlsx**. 

**Inventory.xlsx** contains the items in the store: their SKU, name, amount in stock, and the capacity in the store for that item. 
**Distributors.xlsx** contains three worksheets, one for each distributor, which list the items the distributor sells: their SKU, item name, and price. Note that each distributor will not necessarily have all the items that Randy sells at his store.


**Endpoints**

The backend exposes two endpoints at **http://localhost:4567** using the **Java Spark library:** _/low-stock_ and _/restock-cost_. The /low-stock endpoint should return only the items that are at less than 25% capacity. The /restock-cost endpoint should return the lowest total cost for Randy to restock. The lowest total cost is determined by the lowest available price across different distributors. The items to restock and their amounts should be provided in the request body from the frontend.

**Frontend**

The frontend will be responsible for utilizing the Java endpoints to display the low-stock items and calculating the cost of restocking items. You will need to add the appropriate event listeners to the buttons for using these endpoints.

You will also need to create a component to display a table row showing the item data. This

component should also have an input element where Randy can type in the amount he wants to restock.
