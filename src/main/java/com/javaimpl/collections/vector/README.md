# Vector Implementation

> **Legacy Warning**: `Vector` is a legacy collection class (from JDK 1.0) and is generally **not recommended** for new code. It imposes synchronization overhead on every operation, which is unnecessary for single-threaded use cases.
>
> *   **Replacement (Single-threaded)**: Use `ArrayList`. It is faster because it is not synchronized.
> *   **Replacement (Thread-safe)**: Use `Collections.synchronizedList(new ArrayList<>())` or `CopyOnWriteArrayList` depending on your concurrency refuirements.


## 1. Function of Vector

A **Vector** is a growable array of objects. It is very similar to `ArrayList`, but with two key differences: it is **synchronized** and it has some legacy methods.

*   **Dynamic Array**: Like `ArrayList`, it resizes itself as elements are added.
*   **Thread Safety**: `Vector` is synchronized, meaning it is thread-safe for concurrent access without external synchronization.
*   **Legacy**: It is one of the original Java collections (since JDK 1.0).

## 2. Concerns when Developing a Vector

Developing a Vector is similar to an ArrayList but adds concurrency overhead:

| Concern Target | Issue Description | Proposed Solution |
| :--- | :--- | :--- |
| **Concurrency** | Multiple threads accessing the list simultaneously can corrupt data. | **Synchronization**: All public methods (add, get, remove, size, etc.) must be `synchronized`. |
| **Resizing** | The underlying array has a fixed size. | **Growth Policy**: When full, create a larger array (usually doubling or by `capacityIncrement`) and copy elements over. |
| **Performance** | Synchronization introduces locking overhead. | **Use Correctly**: Only use `Vector` when thread safety is explicitly required and `CopyOnWriteArrayList` or `Collections.synchronizedList` are not suitable. |

## 3. Concerns when Using a Vector

| Concern | Risk | Best Practice |
| :--- | :--- | :--- |
| **Performance** | Because methods are synchronized, it is slower than `ArrayList` in single-threaded scenarios. | Use `ArrayList` if thread safety is not needed. |
| **Iteration** | Iterating over a Vector while it is being modified by another thread can throw `ConcurrentModificationException`. | Even though methods are synchronized, iteration requires external synchronization on the vector object itself during iteration. |

## 4. Limitations of Vector

1.  **Obsolescence**: Generally considered obsolete. `ArrayList` is preferred for non-threaded use, and concurrent collections are preferred for threaded use.
2.  **Locking**: Uses coarse-grained locking (locking the whole object), which can be a bottleneck in high-concurrency environments.

## 5. Evolutionary Logic

**Step 1: The Requirement**
*   Need a list that can grow dynamically.
*   Need it to be safe to use in a multi-threaded environment (common in early Java applets).

**Step 2: The Array Wrapper with Locking**
*   Wrap an array.
*   Make every method `synchronized`.
*   **Result**: `Vector`.

**Step 3: Modern Alternatives**
*   `ArrayList` (Step 2 without locking).
*   `Collections.synchronizedList(new ArrayList<>())` (Wrapper).
*   `CopyOnWriteArrayList` (Lock-free reads).
