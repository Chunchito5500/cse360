# Section Outlines

### **Login**
![image](https://github.com/user-attachments/assets/8fba1c23-df4e-45ed-8593-dc71b299339b)


Log in is simple. If you have an account, use your username and password to sign in.  
If not, click on **Sign Up** to register.

---

### **Sign Up**
![image](https://github.com/user-attachments/assets/de29ed7e-c1ee-4c9c-bf82-657b09c4f175)


Create a new account by entering your name, desired username, password, and role.  
Admin accounts are pre-configured.

---

### **Buyer Dashboard**
![image](https://github.com/user-attachments/assets/d536fd36-0784-48bf-840e-9a84d2c676a1)


Buyers can browse listings, view details, and purchase books directly from sellers.

---

### **Seller Dashboard**
![image](https://github.com/user-attachments/assets/3b1407ba-f5d3-41cc-ad28-21792ab4a721)


Sellers can post books for sale. Just fill in the details and list them.

---

### **Admin Dashboard**  
Admins have special access to system logs and user/book management. Use:

```
Username: admin  
Password: 12345
```

| Transactions | Users | Listings |
|--------------|-------|----------|
| ![image](https://github.com/user-attachments/assets/def3a2b0-10c8-44c3-8b0f-e40ec25dad9e)
 | ![image](https://github.com/user-attachments/assets/2c4663ad-abe5-4a04-8eb7-eaf0410bc2af)
 | ![image](https://github.com/user-attachments/assets/1724cccb-7ce4-4218-90b6-0c757732847a)
 |

---


### Package Descriptions

1. **controllers**  
   Responds to user actions like button clicks and scene transitions.

2. **models**  
   Defines the core data types in the system: `User`, `Book`, `AdminLog`, etc.

3. **services**  
   Handles storage and retrieval logic via CSV. Acts as the app’s database layer.

4. **utils**  
   Provides reusable helpers for CSV parsing, password hashing, and constants.

5. **views**  
   Contains all FXML layouts, stylesheets, and image assets for the UI.
