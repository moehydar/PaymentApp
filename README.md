# Cashi Payment App - Technical Challenge

Hey there! 👋 This is a payment app I built as a technical challenge, showcasing what's possible with Kotlin Multiplatform.


https://github.com/user-attachments/assets/b422bd66-ad44-44b3-b783-e9e48732cf77


## What's This All About?

I created a fintech app where users can send money to each other and check their transaction history. Pretty straightforward, right? But here's where it gets interesting - I used some cutting-edge tech to make it happen:

- **Kotlin Multiplatform (KMP)** 
- **Jetpack Compose**
- **Firebase Firestore**
- **Ktor**
- **Koin**
- **BDD Testing**

## How I Built This Thing

### The Architecture

I went with a clean architecture approach. Here's how it breaks down:

```
CashiPaymentApp/
├── androidApp/          # Android-specific stuff
│   ├── MainActivity     # Just one activity - keeping it simple
│   ├── ui/             # Beautiful Compose screens
│   └── data/           # Firebase magic happens here
├── shared/             # The secret sauce - shared code!
│   ├── model/          # Data classes
│   ├── network/        # API client
│   ├── repository/     # Business logic
│   └── validation/     # Making sure emails are emails
├── backend/            # A real REST API (not mocked!)
└── jmeter/            # Performance tests because speed matters
```

### Some Design Decisions I'm Proud Of

**1. Platform-Specific Firebase**  
I could've tried to force Firebase into the shared module, but firebase KMP is still experimental.

**2. Dependency Inversion FTW**  
The repository doesn't know it's talking to Firebase - it just knows it has a `PaymentStore`. This means we could swap Firebase for something else tomorrow without touching the business logic.

**3. KMP Where It Matters**  
I shared the stuff that makes sense - models, validation, API calls. But UI and platform-specific integrations? Those stay native.

## What Can It Do?

### The Good Stuff ✅
- **Send Money**: Enter an email, amount, pick USD or EUR
- **See Your History**: All your transactions, updating in real-time
- **Smart Validation**: No sending $0 or using "not-an-email" as an email
- **Helpful Errors**: When something goes wrong, you'll know exactly what
- **Real Backend**: Not just a mock - there's an actual server running!

### About That Testing...

Okay, so here's an interesting story about the BDD testing requirement. I started with the textbook approach:

1. **Wrote beautiful Gherkin scenarios** in `payment.feature`:

2. **Tried integrating Cucumber** - because that's what you do for BDD, right?

3. **Hit a wall** - Turns out Cucumber and Compose UI tests don't play nice together. They have different ideas about how to run tests.

4. **Pivoted smartly** - Instead of fighting the framework, I implemented all the BDD scenarios using Compose UI tests with clear Given/When/Then structure. Same BDD principles, just a different (and more Android-friendly) implementation.

The result? All scenarios are covered, tests are maintainable, and they actually run! Sometimes the best solution isn't the most obvious one.

### Test Coverage Breakdown
- ✅ **Unit Tests**
- ✅ **Integration Tests**
- ✅ **UI Tests**
- ✅ **Performance Tests**

## Want to Run It?

### What You'll Need
- Android Studio
- JDK 17+
- A Firebase account (it's free!)
- JMeter

### Getting Started

1. **Clone this bad boy**
   ```bash
   git clone https://github.com/moehydar/PaymentApp.git   
   cd CashiPaymentApp
   ```

2. **Set up Firebase** (it's easier than you think)
    - Head to https://console.firebase.google.com
    - Create a new project (call it whatever you want)
    - Add an Android app with package: `com.cashi.payment.android`
    - Download that `google-services.json` file
    - Drop it in the `androidApp/` folder
    - Enable Firestore in test mode (we're not dealing with auth today)

3. **Fire up the backend**
   ```bash
   ./gradlew :backend:run
   ```
   You'll see it running at http://localhost:8080

4. **Launch the app**
   ```bash
   ./gradlew :androidApp:installDebug
   ```

## Running the Tests

Want to see those green checkmarks? Here's how:

**Unit Tests** (the fast ones):
```bash
./gradlew :shared:test
```

**UI Tests** (watch the app dance):
```bash
./gradlew :androidApp:connectedAndroidTest
```

**Performance Tests** (unleash the horde):
```bash
cd jmeter
./run-performance-test.sh
```

## How to Use the App

It's pretty intuitive, but here's the flow:

1. **Send Money**
    - Type in your friend's email
    - Enter how much you want to send
    - Pick USD or EUR (sorry, no Bitcoin yet)
    - Hit that send button!

2. **Check Your History**
    - Tap the history tab
    - See all your payments
    - Watch new ones appear in real-time (seriously, try it with two devices!)

## The API

For the curious developers, here's what's happening under the hood:

**POST /payments**
```json
// What you send:
{
  "recipientEmail": "buddy@example.com",
  "amount": 42.00,
  "currency": "USD"
}

// What you get back:
{
  "id": "some-uuid-here",
  "recipientEmail": "buddy@example.com",
  "amount": 42.00,
  "currency": "USD",
  "status": "success",
  "timestamp": 1234567890
}
```

Simple, clean, and it works!

## Performance Notes

I threw 50 concurrent requests at this thing, and guess what?
- **Zero errors** - All payments went through
- **~150ms response time** - Faster than you can say "payment sent"
- **4-5 requests/second** - Not breaking any records, but solid for a demo

Check out the fancy graphs in `jmeter/report/index.html` if you're into that sort of thing.
<img width="1506" height="830" alt="Screenshot 2025-07-13 at 1 58 43 PM" src="https://github.com/user-attachments/assets/fe0d6860-1404-4326-aceb-63ce9c99b712" />


## What About iOS?

Great question! The beauty of KMP is that all the hard work is done. To add iOS support, we just need:
- SwiftUI screens (the iOS equivalent of our Compose UI)
- iOS Firebase setup
- An iOS implementation of PaymentStore

The business logic? Already there, waiting to be used!

Built by Mohammad Haidar
