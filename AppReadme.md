```architecture
src
├── main
│   ├── java
│   │   └── com/yourcompany/pos
│   │       
│   │       ├── base ⬅️ Vaadin Layouts and Generic UI Components
│   │       │   └── ui
│   │       │       ├── component     // Reusable components (e.g., InfoTile, NavMenu)
│   │       │       │   └── ViewToolbar.java 
│   │       │       ├── contract      // The Decoupling Contract
│   │       │       │   └── PosViewContract.java 
│   │       │       └── presenter     // The UI-Agnostic Adapter
│   │       │           └── GenericPosPresenter.java
│   │       │
│   │       ├── **core** ⬅️ The Generic POS Engine (Always Loaded)
│   │       │   ├── data              // Entities and generic Repositories
│   │       │   │   ├── Sale.java
│   │       │   │   └── SaleRepository.java
│   │       │   ├── service           // The Central, Generic Business Logic
│   │       │   │   ├── PosService.java
│   │       │   │   └── extension     // The Extension Point Interface
│   │       │   │       └── PosExtensionHook.java 
│   │       │   └── Application.java  // Main Spring Boot Entry Point
│   │       │
│   │       ├── **extension** ⬅️ Conditional Feature Logic
│   │       │   ├── pharmacy          // PHARMACY DOMAIN (@Profile("pharmacy-domain"))
│   │       │   │   ├── data          // Pharmacy-specific entities (e.g., DrugInteraction)
│   │       │   │   ├── service       // Pharmacy-specific logic and hook
│   │       │   │   │   ├── DrugDataService.java 
│   │       │   │   │   └── PharmacyValidationHook.java // @Profile("pharmacy-domain")
│   │       │   │   └── ui            // Vaadin Views for the Pharmacy POS
│   │       │   │       └── PharmacyPosView.java      // @Route("pharmacy/pos")
│   │       │   │
│   │       │   └── grocery           // GROCERY DOMAIN (@Profile("grocery-domain"))
│   │       │       ├── data
│   │       │       ├── service
│   │       │       │   └── GroceryValidationHook.java  // @Profile("grocery-domain")
│   │       │       └── ui            // Vaadin Views for the Grocery POS
│   │       │           └── GroceryPosView.java         // @Route("grocery/pos")
│   │       │
│   │       └── **dashboard** ⬅️ Example of a Composite View (Uses Services from Core/Extensions)
│   │           ├── ui
│   │           │   ├── component
│   │           │   └── DashboardView.java
│   │           └── service
│   │               └── DashboardDataService.java
│   │
│   └── frontend
│       └── themes
│           └── default
│               ├── styles.css        // Shared styles (Grid, typography, etc.)
│               └── theme.json
│
└── test
    └── java
        └── com/yourcompany/pos
            ├── core
            │   └── service
            │       └── PosServiceTest.java // Test core logic without extensions
            └── extension
                └── pharmacy
                    └── service
                        └── PharmacyValidationHookTest.java // Test pharmacy logic in isolation
```