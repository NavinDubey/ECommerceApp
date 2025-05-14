# 🛒 ECommerce App

An Android application built using **Kotlin** and following the **MVVM** architecture. This app
fetches product data from the **FakeStore API** and allows users to add items to a cart, manage
quantities, and view an order summary. The app uses **Room Database** for data persistence, *
*Retrofit** for network operations, and **Hilt** (with **Dagger**) for dependency injection.

## 🚀 Features

1. **Product List**  
   Fetches a list of products from the FakeStore API and displays them using a `RecyclerView`. Each
   item shows:
    - Product Name
    - Product Image
    - Price
    - Add to Cart button

2. **Cart Management**  
   Users can:
    - Add products to the cart
    - View and update the cart
    - Increase/decrease item quantities
    - Remove items  
      Cart data is stored locally using Room Database to persist changes.

3. **Order Summary**  
   Displays the total number of items and the total price of the cart.

## 🛠 Getting Started

**Prerequisites**  
To run this app, ensure you have the following:

- Android Studio (latest version)
- Minimum SDK: 24
- Internet connection

**Setup Instructions**

1. Clone the repository:

```bash
git clone https://github.com/yourusername/product-cart-app.git
