# SkipList

## Implementation Notes

1. This uses a `tail` pointer that points to the top right $+\infty$ node. This is needed to create a new level in O(1).
1. This implementation assumes `size` to be the unique number of data and not the total number of nodes in the skip list
3. The `levels` variable is not 0-aligned. So in this case, an empty skip list has “levels” equal to 1 and not 0.
4. The `LEVEL_CAP` variable is set to 5 to reflect the csvistool depiction
5. The add and remove algorithms are also based on the csvistool’s traversal of the skip list.
6. `add(data)` uses a public utility method called `createNewLevel()` to handle resizing.
7. `remove(data)` uses a `removeTopLevel()` method to properly adjust the levels of the skip list if removing causes empty upper levels
