# Decorator Pattern Exercise: Refactoring Hardcoded DataSource

## Exercise Description
In this module, you will work with a file data storage system. The initial code contains a class that tightly couples logic for writing, encrypting, and compressing data. This approach makes the system hard to extend and maintain.

## Structure
- **src/main/java/org/codeus/design_patterns/decorator/HardcodedFileDataSource.java** — class with hardcoded logic for writing, encrypting, and compressing.
- **src/main/java/org/codeus/design_patterns/decorator/App.java** — demo of class usage.
- **test/java/org/codeus/design_patterns/decorator/HardcodedFileDataSourceTest.java** — tests for correctness.

## Problem
All functionality (writing, encryption, compression) is implemented in a single class. This violates clean architecture principles and makes it difficult to add new features or change existing ones.

## Task
1. Review the code in `HardcodedFileDataSource.java`.
2. Identify which parts of the code are responsible for different aspects of data processing.
3. Redesign the system so that each responsibility is isolated.
4. Make sure the business logic (write/read, encryption, compression) works correctly after refactoring.
5. Verify your solution with the provided tests.

## Expected Result
- The code should be flexible for extension and maintenance.
- All tests should pass.
- Writing, encryption, and compression logic should be separated.

## Hints
<details>
<summary>Show hints</summary>

- Consider using the Decorator pattern to dynamically add functionality to objects without changing their code.
- Each responsibility (writing, encryption, compression) can be implemented as a separate class.
- You can chain decorators to combine multiple behaviors.
- Make sure each decorator only handles its own concern and delegates the rest.
- Test each decorator independently to ensure correctness.

</details>

---

**Good luck with your refactoring!**
