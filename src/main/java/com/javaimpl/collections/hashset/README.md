# HashSet Implementation

## 1. Function of HashSet

A **HashSet** is a collection that contains no duplicate elements. It is backed by a `HashMap` instance.

*   **Unique Elements**: It does not allow duplicate elements.
*   **Unordered**: It makes no guarantees as to the iteration order of the set; in particular, it does not guarantee that the order will remain constant over time.
*   **Null Permitted**: It permits the null element.
*   **Constant Time**: Offers constant time performance for basic operations (`add`, `remove`, `contains`, and `size`), assuming the hash function disperses the elements properly among the buckets.

## 2. Concerns when Developing a HashSet

Developing a HashSet is largely about wrapping a HashMap efficiently.

| Concern Target | Issue Description | Proposed Solution |
| :--- | :--- | :--- |
| **Storage** | How to store the keys efficiently and check for duplicates. | **Backing Map**: Use a `HashMap` where the **keys** are the elements of the Set, and the **value** is a constant dummy object. |
| **Uniqueness** | Ensuring no duplicate elements are stored. | **Map Properties**: `HashMap` keys are unique by definition. If `map.put(key, value)` returns a previous value, we know the element was already present. |
| **Serialization** | Ensuring the state is saved correctly. | **Custom Serialization**: Implementation requires `readObject` and `writeObject` to handle the backing HashMap instance properly. |

## 3. Concerns when Using a HashSet

| Concern | Risk | Best Practice |
| :--- | :--- | :--- |
| **Iteration Order** | The order of elements is unpredictable and can change if the set is resized. | If you need insertion order, use `LinkedHashSet`. If you need sorted order, use `TreeSet`. |
| **Mutable Elements** | If the value of an object changes in a way that affects equality *after* it is added to the set, the behavior is not specified and can lead to bugs. | **Immutable Keys**: Prefer using immutable objects as elements in a HashSet. if mutable, ensure fields used in `hashCode`/`equals` are not modified. |

## 4. Limitations of HashSet

1.  **No Ordering**: As mentioned, order is not guaranteed.
2.  **Not Thread-Safe**: Like most standard collections, it is not synchronized.

## 5. Evolutionary Logic

**Step 1: The Requirement**
*   Need a collection of unique items.
*   Fast lookups are critical.

**Step 2: Reuse Existing Logic**
*   We already have `HashMap` which handles hashing, collision resolution, and uniqueness of keys.
*   **Action**: Create a class that holds a `HashMap`.

**Step 3: The Implementation**
*   **Add(E e)**: Call `map.put(e, DUMMY_OBJECT)`.
*   **Contains(E e)**: Call `map.containsKey(e)`.
*   **Remove(E e)**: Call `map.remove(e)`.
