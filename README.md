# Section Outlines

### **Login**
![image](https://github.com/user-attachments/assets/8fba1c23-df4e-45ed-8593-dc71b299339b)


Log in is simple. If you have an account, use your username and password to sign in.  
If not, click on **Sign Up** to register.

---

### **Sign Up**
![SignUp Screenshot](docs/img/signup.png)

Create a new account by entering your name, desired username, password, and role.  
Admin accounts are pre-configured.

---

### **Buyer Dashboard**
![Buyer Screenshot](docs/img/buyer.png)

Buyers can browse listings, view details, and purchase books directly from sellers.

---

### **Seller Dashboard**
![Seller Screenshot](docs/img/seller.png)

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
| ![Admin Screenshot 1](docs/img/admin.png) | ![Admin Screenshot 2](docs/img/admin2.png) | ![Admin Screenshot 3](docs/img/admin3.png) |

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
