# LinkedHashMap Implementation

## 1. Function of LinkedHashMap

A **LinkedHashMap** is a hybrid data structure that combines the fast lookups of a **HashMap** with the predictable iteration order of a **LinkedList**.

*   **Hash Table + Linked List**: It extends `HashMap` functionality by maintaining a doubly-linked list running through all of its entries.
*   **Ordered Iteration**: Unlike `HashMap`, which makes no guarantees about order, `LinkedHashMap` iterates elements in a predictable sequence:
    *   **Insertion-Order**: The order in which keys were inserted into the map (default).
    *   **Access-Order**: The order in which keys were last accessed (from least-recently accessed to most-recently accessed).
*   **Predictable Performance**: Like `HashMap`, it offers O(1) time complexity for basic operations (`add`, `contains`, `remove`), assuming the hash function disperses elements properly.

![LinkedHashMap Structure](linkedhashmap_structure.png)

## 2. Concerns when Developing a LinkedHashMap

Developing a LinkedHashMap requires managing two data structures simultaneously:

| Concern Target | Issue Description | Proposed Solution |
| :--- | :--- | :--- |
| **Data Structure Complexity** | Each entry belongs to two distinct structures: the Hash Table buckets (for lookup) and the Doubly Linked List (for iteration). | **Extended Entry Class**: Create a `LinkedHashMap.Entry` class that extends `HashMap.Entry` and adds `before` and `after` pointers. |
| **Insertion Overhead** | Adding an element requires updating bucket lists AND the global linked list. | **Hook Methods**: Override `HashMap`'s `newNode`, `replacementNode` methods or insert hooks like `afterNodeInsertion` to link the new node to the tail of the list. |
| **Deletion Overhead** | Removing an element requires unlinking it from the bucket AND the global linked list. | **Hook Methods**: Override/Use `afterNodeRemoval` to unlink the `before` and `after` pointers of the removed node. |
| **Access Order Support** | If `accessOrder` is true, simply querying a key (`get`) must move it to the end of the list. | **Move to Last**: In `get` or `afterNodeAccess`, unlink the node from its current position and append it to the tail. |

### Deep Dive: Two Lists in One Node
Each node in a `LinkedHashMap` is heavy. It contains:
1.  `Key` and `Value` (Data)
2.  `Next` pointer (for the Hash Table collision chain)
3.  `Before` and `After` pointers (for the Global Doubly Linked List)

```java
static class Entry<K,V> extends HashMap.Node<K,V> {
    Entry<K,V> before, after;
    Entry(int hash, K key, V value, Node<K,V> next) {
        super(hash, key, value, next);
    }
}
```

## 3. Concerns when Using a LinkedHashMap

| Concern | Risk | Best Practice |
| :--- | :--- | :--- |
| **Memory Consumption** | Significant overhead per entry (two extra 64-bit reference pointers) compared to `HashMap`. | Use only when iteration order matters. If order is irrelevant, stick to `HashMap`. |
| **Insertion Speed** | Slightly slower than `HashMap` due to the cost of maintaining the linked list. | Accept the minor trade-off for the benefit of ordering. |
| **LRU Cache Implementation** | `LinkedHashMap` can easily function as an LRU cache, but it doesn't evict old entries by default. | Override `removeEldestEntry(Map.Entry)` to return `true` when the map size exceeds a specific capacity. |

## 4. Limitations of LinkedHashMap

1.  **Memory Overhead**: It is heavier than both `HashMap` (due to list pointers) and `ArrayList` (due to Entry objects).
2.  **Not Thread-Safe**: Like `HashMap`, it requires external synchronization or usage of `Collections.synchronizedMap` in concurrent environments.
3.  **No Sorting**: While it maintains *insertion* or *access* order, it does not sort keys by value (like `TreeMap`).

## 5. Evolutionary Logic

**Step 1: The Requirement**
*   We like `HashMap` speed, but we hate that `keySet()` returns keys in random order.
*   We want to iterate over the map and see the items exactly as we put them in.

**Step 2: The Naive Solution**
*   Maintain a separate `ArrayList` of keys alongside the `HashMap`.
*   **Problem**: Removing a key from the map means finding and removing it from the List, which is O(N).

**Step 3: The Integrated Solution**
*   **Idea**: Embed the linked list **inside** the hash map entries.
*   **Mechanism**: Give every entry `before` and `after` pointers. Keep a reference to the `head` and `tail` of this list.
*   **Result**: Insertion is O(1). Removal is O(1) (since we have direct reference to the node being removed). Iteration is fast (just follow the `after` pointers).

**Step 4: The Pivot to LRU**
*   **Observation**: If we can move a node to the tail O(1), we can easy implement "Most Recently Used" logic.
*   **Feature**: Add a flag `accessOrder`. On every `get()`, move the accessed node to the `tail`.
*   **Outcome**: The `head` of the list becomes the "Least Recently Used" (LRU) item, perfect for building caches.

## 6. Building an LRU Cache with LinkedHashMap

One of the most powerful features of `LinkedHashMap` is its ability to serve as the foundation for a **Least Recently Used (LRU) Cache**.

### The Logic
An LRU cache discards the items that haven't been used for the longest time when the cache is full. `LinkedHashMap` supports this natively via the `accessOrder` property.

1.  **Access Order Mode**: When constructed with `accessOrder = true`, every time you call `get(key)` or `put(key, value)`, that entry is moved to the **end** (Tail) of the list.
    *   **Tail** = Most Recently Used (MRU)
    *   **Head** = Least Recently Used (LRU)

2.  **Automatic Eviction**: In a full implementation, `LinkedHashMap` calls a protected method `removeEldestEntry(entry)` after every insertion.
    *   If this method returns `true`, the map automatically deletes the **Head** (the oldest, least used item).
    *   *Note: In this project's simplified implementation, `removeEldestEntry` is not implemented to keep the code lightweight, but the `accessOrder` re-ordering logic is fully functional.*

### Why LinkedHashMap?
Implementing this with a standard `LinkedList` + `HashMap` manually is difficult:
*   **LinkedList**: Removing a node from the middle (when it is accessed) is O(N) because you have to search for it, unless you have a direct reference to the node.
*   **LinkedHashMap**: Since the HashMap Entry *is* the Linked List Node, we have direct O(1) access to unhook it from its current position and move it to the tail.
