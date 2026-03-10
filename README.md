# 🚀 GitHub Explorer
#### A high-performance Android application built with Jetpack Compose and Material 3. This project explores the GitHub Search API with a focus on fluid UI/UX, robust architecture, and comprehensive testing.

### 🛠 How to Build and Run
#### Clone the Repository: 
Ensure you are on the main branch.

#### Open in Android Studio: 
Use Android Studio Ladybug (2024.2.1) or newer.

#### Sync & Run: 
Click the "Run" button. The app supports both Phone and Tablet layouts (Landscape/Portrait).

### 🏗 Key Architectural Decisions
#### 1. Clean Architecture + MVI
The project follows Clean Architecture principles to ensure a highly maintainable and testable codebase, combined with a Unidirectional Data Flow (UDF) via the MVI pattern.

Data & Networking: Utilizes Retrofit for API communication. Specialized Mappers transform Network DTOs into clean Domain Models, ensuring the rest of the app is shielded from API schema changes.

Domain Layer: Contains UseCases that encapsulate granular business logic, such as the finalQuery logic. This provides a "Popular Repositories" fallback (stars > 10,000) when the search input is empty.

UI Layer (MVI):

Model (State): A single RepositoryUiState sealed interface provides a Single Source of Truth, preventing "impossible states" (e.g., loading and error showing simultaneously).

View: Stateless Jetpack Compose components that observe the state and emit Intents.

Intent: A sealed RepositoryListIntent contract that defines every possible user interaction (Search, Refresh, Clicks), ensuring all actions are funneled through a single entry point in the ViewModel.

#### 2. Reactive Search & Refresh Logic
The ViewModel leverages the combine operator to merge search queries (with a 1000ms debounce) and a refreshTrigger Flow.

The Problem: StateFlow ignores updates if the value hasn't changed.

The Solution: The refreshTrigger ensures that a "Pull-to-Refresh" gesture always forces a new network request, even if the search text remains identical.

#### 3. Adaptive & Responsive Design
The app uses a single codebase for multiple form factors. Using BoxWithConstraints, the Detail Screen dynamically switches between a Vertical Column (Phone-Portrait) and a Side-by-Side Row (Tablet/Phone-Landscape), ensuring optimal readability on all devices.

### ✨ Advanced UI/UX Features
#### 💎 Shared Element Transitions
Utilizing the latest SharedTransitionLayout (Compose 1.7.0+), the Owner Avatar "glides" and scales smoothly from its position in the grid to the header of the Detail Screen. This provides spatial context and a premium, high-end feel.

#### 🔄 Meaningful Motion
A synthetic 500ms delay is included in the refresh logic. This ensures that even on near-instantaneous network connections, the user receives clear visual feedback (the refresh spinner) that their request was processed.

### ⚖️ Trade-offs and Future Improvements
#### Persistence (Offline Mode): 
The current version is network-reliant. A logical next step is implementing Room as a local cache to allow the list to appear instantly without a network connection.

#### Pagination: 
To keep the initial scope lean, the app fetches the first page of results. Integrating the Paging 3 library would be the preferred method for handling infinite scroll.

#### Advanced Sorting: 
The UseCase and Repository layers are already designed with an optional sort parameter, making it trivial to add a "Sort by Stars/Forks" UI in the future.

### 🧪 Comprehensive Testing Strategy
The project maintains a rigorous testing standard across the entire stack:

#### Core & Common: 
Unit tests for utility logic like DateFormatter.

#### Networking & Data:
Verification of GithubApiService behavior and precision of Mappers.

#### Domain:
Business logic validation via UseCase tests.

#### UI (Atomic & Screen Level):

Component Tests: Individual verification of StatItem, InfoLabel, and RepositoryCard.

Screen Tests: Robolectric-backed tests for RepositoryListScreen and RepositoryDetailScreen covering Loading, Error, Success, and Adaptive Layout states.

ViewModel: State-machine testing using runTest and Turbine to verify Flow emissions and debounce timing.