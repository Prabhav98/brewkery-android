# ☕ Brewkery — Android Coffee & Bakery App

A native Android ordering app for coffee & bakery items, built as a take-home assignment for Clickretina.

## 🏗️ Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **UI:** XML with ViewBinding
- **Navigation:** Jetpack Navigation Component (Single Activity)
- **Networking:** Retrofit + Coroutines + Gson
- **Local Storage:** Room Database (caches menu for offline support)
- **Image Loading:** Coil
- **Min SDK:** 24 (Android 7.0)

## 📱 Features
 
- **Screen 1 — Home:** Store banner, categories filter, live search, menu items with badges & ratings, sticky cart bar, active order tracking banner
- **Screen 2 — Item Detail:** Hero image, ingredients chips, dynamic customizations (sizes, milk, sugar) with real-time price recalculation and quantity selector
- **Screen 3 — Cart:** Line items with qty controls, Subtotal + $2.50 delivery + 8% tax calculation
- **Screen 4 — Order Status:** Random ticket ID (#BK-XXXXX) with PREPARING status
- **Bonus:** Room DB caches menu for offline viewing
- **Error & loading states** for all network calls
- **Unit tests** for price calculation logic

## 🏛️ Project Structure