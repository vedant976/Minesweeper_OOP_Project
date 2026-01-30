# Minesweeper OOP Project

A classic **Minesweeper game** implemented using **Object-Oriented Programming (OOP)** principles in **Java**.

This project demonstrates solid OOP design by modeling game components (like Board, Cell, Mine, etc.) as reusable classes, and applies game logic for revealing cells, placing flags, handling user input, and checking win/loss conditions.

---

## 🧠 Features

✨ Core gameplay
- Random mine placement on the board
- Reveal safe cells and recursive reveal for empty regions
- Flagging and unflagging suspected mines
- Game over on stepping on a mine
- Win condition when all non-mine cells are revealed

⚙️ OOP Structure
- Modular classes to represent game entities
- Clear separation of concerns
- Easily maintainable and extendable code

---

## 🧾 Table of Contents

- [Gameplay](#gameplay)
- [Installation](#installation)
- [How to Run](#how-to-run)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

---

## 🎮 Gameplay

Minesweeper is a puzzle game where the goal is to uncover all cells that **do not contain mines**. The board consists of hidden cells, some of which contain mines. Clicking a cell will either:
- Reveal a number for adjacent mines,
- Reveal an empty space and its neighbors,
- Trigger a mine and end the game.

Players can also **flag cells** to mark suspected mines.

---

## 🛠 Installation

1. Make sure you have **Java Development Kit (JDK)** installed (version 8 or above).
2. Clone this repository:

```bash
git clone https://github.com/vedant976/Minesweeper_OOP_Project.git
