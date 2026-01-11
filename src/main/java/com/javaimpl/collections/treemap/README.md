# TreeMap Implementation

## 1. Function of TreeMap

A **TreeMap** is a map implementation based on a **Red-Black Tree**. It stores keys in a sorted order, which allows for efficient range queries and predictable iteration order.

*   **Red-Black Tree Structure**: It uses a self-balancing binary search tree to store entries.
*   **Sorted Iteration**: Keys are ordered according to their **natural ordering** or by a **Comparator** provided at map creation time.
*   **Performance**: Guaranteed O(log n) time cost for the `containsKey`, `get`, `put`, and `remove` operations.

![TreeMap Structure](treemap_structure.png)

## 2. Concerns when Developing a TreeMap

Developing a TreeMap requires implementing a complex self-balancing tree algorithm:

| Concern Target | Issue Description | Proposed Solution |
| :--- | :--- | :--- |
| **Tree Balancing** | Maintaining the binary search tree balanced to ensure O(log n) operations. | **Red-Black Tree Rules**: Implement rotation (left/right) and color flipping logic to maintain balance after insertion and deletion. |
| **Ordering** | Keys must be comparable to determine their position in the tree. | **Comparator/Comparable**: Use the provided `Comparator` or expect keys to implement `Comparable`. |
| **Traversal** | Iterating over the map needs to follow the sorted order. | **In-Order Traversal**: Implement iterators that traverse the tree in-order (Left-Root-Right). |

### Deep Dive: Red-Black Tree Node
Each node in a `TreeMap` contains:
1.  `Key` and `Value` (Data)
2.  `Left` and `Right` child pointers
3.  `Parent` pointer
4.  `Color` (Red or Black)

```java
static final class Entry<K,V> implements Map.Entry<K,V> {
    K key;
    V value;
    Entry<K,V> left;
    Entry<K,V> right;
    Entry<K,V> parent;
    boolean color = BLACK;
    // ...
}
```

## 3. Concerns when Using a TreeMap

| Concern | Risk | Best Practice |
| :--- | :--- | :--- |
| **Performance Overhead** | Slower than `HashMap` for basic operations (O(log n) vs O(1)). | Use `TreeMap` only when you need sorted keys or range/navigation capabilities. |
| **Key Immutability** | Modifying a key that is already in the map can corrupt the tree structure. | Use immutable objects as keys. |
| **Null Keys** | Standard `TreeMap` does not allow null keys (depending on the Comparator), whereas `HashMap` does. | Avoid null keys or provide a special Comparator if strictly necessary. |

## 4. Limitations of TreeMap

1.  **Complexity**: more complex internal structure and logic than `HashMap`.
2.  **Concurrency**: Not thread-safe. Requires `Collections.synchronizedSortedMap` or `ConcurrentSkipListMap` for concurrent use.
3.  **Memory**: Overhead for storing parent/child pointers and color information.

## 5. Evolutionary Logic

**Step 1: The Requirement**
*   We need a Map where we can iterate over keys in a specific order (e.g., alphabetical, numerical).
*   We need to perform range searches (e.g., "give me all keys between 'A' and 'F'").

**Step 2: The Naive Solution (Sorted Array/List)**
*   Keep keys sorted in an array.
*   **Problem**: Lookup is O(log n) via binary search, but insertion/deletion is O(n) due to shifting elements.

**Step 3: The Binary Search Tree (BST)**
*   Use a tree structure. Lookup, Insert, Delete can be O(log n).
*   **Problem**: If strict sorting is applied on insertion (e.g., 1, 2, 3, 4), the tree becomes a linked list (unbalanced), degrading to O(n).

**Step 4: The Balanced Tree (Red-Black Tree)**
*   **Idea**: Enforce rules during insertion and deletion that ensure the tree effectively stays balanced.
*   **Mechanism**: Color nodes Red or Black. Use Rotations to fix violations of Red-Black properties.
*   **Result**: Guaranteed O(log n) height, ensuring fast operations even in worst-case insertion scenarios.
