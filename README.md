# 🌱 KindnessHub App

KindnessHub is an Android application that connects donors with charitable organizations.
It enables users to make donations easily while allowing admins (charities) to manage and track contributions in real time using Firebase.

---

## 📱 Features

### 👤 User Module
- User registration & login (Email + Google Sign-In)
- Browse charities dynamically from Firebase
- Donate to trusted organizations
- Select charity, cause, payment method, and amount
- View personal donation history
- Track total contributions
- Edit profile details

---

### 🛡️ Admin Module
- Admin registration & login
- Firebase-based authentication
- View donations received
- Track donor name, amount, cause, and date
- See total donations received
- Real-time donation tracking

---

## 🔥 Firebase Integration
- Firebase Authentication (Email + Google)
- Firebase Realtime Database
- Dynamic data fetching

---

## 💸 Donation Data Stored
Each donation includes:
- User name
- User email
- Organization name
- Admin email
- Donation amount
- Charity cause
- Date & time
- Organization location

---

## 🏗️ Tech Stack
- Kotlin
- Android Studio
- Firebase Authentication
- Firebase Realtime Database
- RecyclerView
- ViewBinding

---

## 📊 Firebase Database Structure

### Users
user/
  uid/
    name
    email
    address
    phone

### Admins
admin/
  uid/
    orgName
    emailAdmin
    location

### Donations
donations/
  donationId/
    userName
    userEmail
    orgName
    emailAdmin
    charityCause
    amount
    date
    time
    location

---

## 🚀 Highlights
- Dual role login (User + Admin)
- Dynamic Firebase RecyclerViews
- Total donation tracking
- Clean modular structure

---

## 👨‍💻 Author
Hanson Vaz  
BCA Final Year Project  
KindnessHub – Spreading Kindness Through Technology ❤️
