# ♟️ The Drake — Strategic Board Game

A complete 2-player strategy board game built in **Java** with a **JavaFX** UI.

---

## 🎮 About the Game

The Drake is a turn-based strategy game where two players command armies on a board, moving troops and capturing the opponent's Drake (king piece) to win. The game enforces full rule validation on every action and tracks the game state from start to finish.

---

## ✨ Features

- ♟️ **Complete game-state machine** — board mechanics, troop movement, action validation, and win condition detection
- 🎨 **JavaFX UI** — clean, interactive board rendered with JavaFX
- 🔁 **Observer pattern** — fully decoupled game logic and UI; the view reacts to state changes automatically
- 💉 **Dependency injection** — loosely coupled architecture for easy testing and extension
- 💾 **JSON serialization** — save and load game state at any point
- 📋 **Full rule enforcement** — all moves and actions validated against the game rules before execution

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| UI | JavaFX |
| Architecture | Observer Pattern, Dependency Injection |
| Persistence | JSON serialization |
| IDE | IntelliJ IDEA |

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- IntelliJ IDEA (recommended)
- JavaFX SDK

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/blahel/the-drake.git
   cd the-drake
   ```

2. **Open in IntelliJ IDEA**
   - File → Open → select the project folder
   - IntelliJ will detect and import the project automatically

3. **Configure JavaFX** (if not already set up)
   - File → Project Structure → Libraries → Add JavaFX SDK

4. **Run the game**
   - Find `TheDrakeApp.java` inside `src/thedrake/ui/`
   - Right-click → Run

---

## 📁 Project Structure

```
the-drake/
├── src/
│   ├── assets/                   # Game images and troop sprites
│   └── thedrake/                 # Main game package
│       ├── ui/                   # JavaFX UI layer
│       │   ├── TheDrakeApp       # Application entry point
│       │   ├── BoardView         # Board rendering
│       │   ├── TileView          # Individual tile rendering
│       │   ├── TileViewContext   # Tile state context
│       │   ├── TileBackgrounds   # Tile background logic
│       │   ├── TroopImageSet     # Troop sprite management
│       │   ├── ValidMoves        # Move highlighting
│       │   ├── MainMenuController
│       │   ├── main-menu.fxml
│       │   └── style.css
│       ├── GameState             # Core game state machine
│       ├── Board                 # Board representation
│       ├── BoardTroops           # Troop placement on board
│       ├── Army                  # Player army management
│       ├── Troop                 # Troop definition
│       ├── TroopTile             # Troop tile state
│       ├── TroopAction           # Action interface
│       ├── TroopFace             # Troop facing (front / back)
│       ├── Move / BoardMove      # Move representation
│       ├── StepOnly              # Step-only action
│       ├── StepAndCapture        # Step and capture action
│       ├── CaptureOnly           # Capture-only action
│       ├── SlideAction           # Slide action
│       ├── ShiftAction           # Shift action
│       ├── StrikeAction          # Strike action
│       ├── PlaceFromStack        # Place troop from stack
│       ├── StandardDrakeSetup    # Default game setup
│       ├── BoardPos / TilePos    # Position types
│       ├── Offset2D              # 2D offset utility
│       ├── PlayingSide           # BLUE / ORANGE enum
│       ├── GameResult            # IN_PLAY / VICTORY / DRAW
│       ├── JSONSerializable      # JSON serialization interface
│       ├── PositionFactory       # Position creation helpers
│       └── module-info.java
└── README.md
```

---

## 🧠 Architecture Highlights

**Observer Pattern** — The UI registers as an observer on the game model. Every time the game state changes (a move is made, a piece is captured, the game ends), the view updates automatically without the model knowing anything about the UI.

**Dependency Injection** — Game components receive their dependencies from outside rather than creating them internally, keeping the architecture clean and each component independently testable.

**JSON Serialization** — The full game state (board layout, troop positions, whose turn it is, game result) is serializable to JSON via the `JSONSerializable` interface, allowing saves and loads mid-game.

**Action System** — Each troop action (`StepOnly`, `StepAndCapture`, `CaptureOnly`, `SlideAction`, `ShiftAction`, `StrikeAction`, `PlaceFromStack`) is a separate class implementing the `TroopAction` interface, making it easy to add new move types without touching existing logic.


## 👨‍💻 Author

**Emir Orhan**
Czech Technical University in Prague — Faculty of Information Technology
[linkedin.com/in/orhanemir](https://www.linkedin.com/in/orhanemir) · [github.com/blahel](https://github.com/blahel)
