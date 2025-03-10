# TWG Product Search App

## Overview
This is a modern Android application following the best practices of Android development. It manages and displays product search data entirely in memory using **ViewModel + StateFlow** instead of a local database.

## Features
- Modern Android App Architecture
- **Dependency Injection** with **Hilt**
- **In-Memory Data Management** using **ViewModel + StateFlow**
- **Automatic Access Token Refresh Mechanism through OkHttp Interceptor**
- When users perform consecutive search actions, the previous job is canceled to optimize performance
- **Mockk** for Unit Testing
- **Coil** for Efficient Image Loading and Caching
- Jetpack Compose-based UI
- Unit and Instrumentation Test Coverage

## Tech Stack
- Kotlin – Primary language for development
- MVVM Architecture – Modern app architecture
- Hilt – Dependency Injection
- Retrofit – API calls to fetch product data
- Coroutines & Flow – Asynchronous programming
- Jetpack Compose – UI development
- Coil – Image loading and caching
- Mockk – Unit testing for mocking dependencies
- Material Design 3 – Modern UI components
- JUnit & Compose UI Test – Unit and Instrumentation testing

## How It Works
The app fetches product search data from the TWG back-end service.
Search results are stored only in memory using ViewModel + StateFlow instead of a local database.
Access Token refresh is automatically handled to maintain authenticated API requests.
Coil efficiently loads and caches product images for smooth performance.
When users perform consecutive search actions, the previous job is canceled to optimize performance.

## Build Environment
Android Studio Meerkat | 2024.3.1
Build #AI-243.22562.218.2431.13114758, built on February 25, 2025