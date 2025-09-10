# Chain of Responsibility Exercise

## Task Overview
You are given a service that processes different types of banking requests.  
The current implementation is monolithic and works correctly, but it is difficult to maintain and extend.  
Unit tests are already provided and **must not be changed**.

Your task is to **refactor the implementation** using the **Chain of Responsibility pattern**, while keeping all existing tests green.

---

## Requirements
1. Apply the **Chain of Responsibility** design pattern.
2. Ensure different request types are handled by different pipelines (no branching inside handlers; handlers must not check request type):
    - **TRANSFER / BILL_PAYMENT**:
        - type validation
        - daily limit check
        - AML (anti-money laundering) check
        - commission calculation (1% unified for TRANSFER/BILL_PAYMENT)
        - logging
    - **CREDIT_APPLICATION**:
        - type validation
        - AML check
        - logging
   - Note: use the short pipeline only for `CREDIT_APPLICATION`; apply the long pipeline for all other types. Do not introduce branching inside handlers.
3. Each handler should only know how to execute its own responsibility and pass the request further down the chain.
4. The solution must be **extensible**: it should be easy to add new handlers or modify pipelines without changing existing ones.
5. You are **not allowed** to modify the provided test suite. All tests must remain green.

---

## Expectations
- The final implementation demonstrates clean separation of concerns.
- Pipelines for different request types are easy to configure and modify.
- The refactoring results in more maintainable and production-like code.

---

## Hints
- Start by defining a `Handler` interface with a method such as `process(BankRequest request)`.
- Implement individual handlers for each responsibility (validation, limit check, AML, commission, logging).
- Use a simple factory or builder to assemble the correct pipeline for each request type.
- Rely on the tests to ensure you did not break any existing behavior.

---
