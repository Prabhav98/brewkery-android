# ☕ Brewkery — Android Coffee & Bakery App

A native Android ordering app for coffee & bakery items.

## 🏗️ Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **UI:** XML with ViewBinding
- **Networking:** Retrofit + Coroutines + Gson
- **Local Storage:** Room Database (caches menu for offline support)
- **Min SDK:** 24 (Android 7.0)

## 📱 Features

- **Screen 1 — Home:** Store banner, categories filter, live search, menu items with badges & ratings, sticky cart bar, active order tracking banner.
- **Screen 2 — Item Detail:** Hero image, ingredients chips, dynamic customizations (sizes, milk, sugar) with real-time price recalculation and quantity selector.
- **Screen 3 — Cart:** Line items with qty controls, Subtotal + $2.50 delivery + 8% tax calculation.
- **Screen 4 — Order Status:** Random ticket ID (#BK-XXXXX) with PREPARING status.
- **Bonus:** Room DB caches menu for offline viewing.
- **Error & loading states** for all network calls.
- **Unit tests** for price calculation logic.

---

## 🤖 How I Worked with AI

I used AI tools as a pair-programming assistant throughout this build to speed up boilerplate setup and troubleshoot UI/layout edge cases.

### Tools Used
- **ChatGPT** — Used for architectural discussions, layout debugging, and writing unit tests for business logic.
- **Gemini** — Used for quick Kotlin boilerplate code and Retrofit data model generation.

---

### Actual Prompts I Used

1. *"Design a clean MVVM Android architecture in Kotlin using Retrofit, Room, and Navigation Component for a coffee ordering app. Keep the cart in memory while caching the menu in Room for offline access."*
2. *"Write a pure Kotlin unit test with JUnit 4 to test a `PriceCalculator` object covering base price, size/milk extras, 8.0% tax, and a $2.50 flat delivery fee."*
3. *"My RecyclerView inside a ScrollView is getting cut off and only showing 2 items on screen. How do I fix the height measurement issue?"*

---

### What AI Got Right ✅
- **TypeConverters for Room:** It accurately generated the Gson `TypeConverter` functions required to serialize nested Kotlin objects (`Customizations`) and lists into SQLite string columns without compilation errors.
- **Unit Test Setup:** It generated clean, isolated test cases for `PriceCalculatorTest` that executed in under 100ms without needing an emulator or Android Context.

---

### What AI Got Wrong & How I Fixed It ❌
- **The Issue:** AI originally generated the Home Screen layout using a standard `<ScrollView>` wrapping a `RecyclerView`. This caused a layout measurement bug where the `RecyclerView` couldn't calculate its full height, cutting off the menu list after just 2 items.
- **How I Fixed It:** I recognized the nested scrolling conflict, replaced `<ScrollView>` with `<androidx.core.widget.NestedScrollView>`, and set `isNestedScrollingEnabled = false` on the `RecyclerView`. This allowed the full list of 6 menu items to measure and render smoothly.