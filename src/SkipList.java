import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Your implementation of a SkipList.
 *
 * @author Sameer Kapoor
 * @userid skapoor68
 * @GTID 90XXXXXXX
 * @version 1.0
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */

public class SkipList {

    /*
     * The level cap of the SkipList.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    private static final int LEVEL_CAP = 5;

    /*
     * Do not add new instance variables.
     */
    private int size;
    private int levels;
    private SkipNode head;
    private SkipNode tail;

    /**
     * Constructs a new SkipList.
     *
     * The head and tail nodes should be properly initialized as new SkipNodes.
     *
     * The initial size of the SkipList is 0.
     *
     * The number of levels should be initialized to 1.
     */
    public SkipList() {
        head = new SkipNode(Integer.MIN_VALUE);
        tail = new SkipNode(Integer.MAX_VALUE);

        head.setRight(tail);
        tail.setLeft(head);

        size = 0;
        levels = 1;
    }

    /**
     * Creates a new level in the SkipList.
     *
     * Create a new head and tail node. Modify the existing head and tail variables to reflect a new level.
     * Finally, increment levels by 1.
     *
     * Must be O(1) in all cases.
     *
     * @throws IllegalStateException if calling this method will exceed the level cap
     */
    public void createNewLevel() {
        if (levels == LEVEL_CAP) {
            throw new IllegalStateException("Calling createNewLevel exceeds the levels cap");
        }

        SkipNode newHead = new SkipNode(Integer.MIN_VALUE);
        SkipNode newTail = new SkipNode(Integer.MAX_VALUE);

        newHead.setRight(newTail);
        newTail.setLeft(newHead);

        newHead.setDown(head);
        newTail.setDown(tail);

        head.setUp(newHead);
        tail.setUp(newTail);

        head = newHead;
        tail = newTail;

        levels++;
    }

    /**
     * Removes the top level of the SkipList.
     *
     * Reconfigure the head the tail pointers of the SkipList and decrement levels by 1.
     *
     * Must be O(1) in all cases.
     *
     * @throws IllegalStateException if calling this method deletes the base level
     */
    public void removeTopLevel() {
        if (levels == 1) {
            throw new IllegalStateException("Calling removeTopLevel removes the base level");
        }

        head = head.getDown();
        head.setUp(null);

        tail = tail.getDown();
        tail.setUp(null);

        levels--;
    }

    /**
     * Adds the data to the SkipList.
     *
     * Check for a resize if the number of heads exceeds or equals the number of current levels.
     *
     * If the data already exists in the SkipList, only add nodes if they are needed on higher levels.
     *
     * When traversing the SkipList, it will be helpful to store a list of nodes to update. This way
     * when you actually add the data, you can query the list of nodes to update to easily adjust their pointers.
     *
     * @param data the data to add
     * @param heads the number of heads for promoting the data
     * @throws java.lang.IllegalArgumentException if the number of heads is negative or
     *                                            is greater than or equal to LEVEL_CAP
     */
    public void add(int data, int heads) {
        if (heads < 0) {
            throw new IllegalArgumentException("The number of heads must be positive.");
        }
        if (heads >= LEVEL_CAP) {
            throw new IllegalArgumentException("The number of heads cannot not exceed or equal the level cap.");
        }

        // check for resize
        if (heads >= levels) {
            for (int i = levels; i <= heads; i++) {
                createNewLevel();
            }
        }

        List<SkipNode> nodesToUpdate = new ArrayList<>();
        SkipNode curr = head;
        int currLevel = levels;
        while (curr != null) {      // begin traversing the SkipList
            while (curr.getRight() != null && curr.getRight().getData() <= data) {
                curr = curr.getRight();
                if (curr.getData() == data) {   // check if the data already exists
                    // if higher level nodes need to be updated AND the data needs to be promoted to a higher level
                    while (nodesToUpdate.size() > 0 && heads >= currLevel) {
                        curr.setUp(new SkipNode(curr));     // insert the new node
                        curr = curr.getUp();
                        SkipNode nodeToUpdate = nodesToUpdate.remove(nodesToUpdate.size() - 1);
                        curr.setRight(nodeToUpdate.getRight());     // set the nodes pointers
                        curr.setLeft(nodeToUpdate);
                        nodeToUpdate.setRight(curr);
                        curr.getRight().setLeft(curr);
                    }
                    return;    // return early
                }
            }
            if (currLevel - 1 <= heads) {
                nodesToUpdate.add(curr);  // nodes we traverse down from are the ones that need to be updated when adding
            }
            curr = curr.getDown();
            currLevel--;
        }

        SkipNode newNode = null;
        currLevel = 0;
        while (currLevel == 0 || heads > 0) {      // if duplicates were not found
            if (currLevel == 0) {       // if the node is being added at the bottom level
                newNode = new SkipNode(data);   // then create a new node at the bottom level
            } else {
                newNode = newNode.getUp();  // otherwise move to the correct new node (line 171)
            }

            SkipNode nodeToUpdate = nodesToUpdate.remove(nodesToUpdate.size() - 1);

            // insert the new node
            newNode.setRight(nodeToUpdate.getRight());
            newNode.setLeft(nodeToUpdate);
            newNode.getRight().setLeft(newNode);
            nodeToUpdate.setRight(newNode);
            if (currLevel != 0) {
                heads--;
            }
            if (heads > 0 && currLevel < levels - 1) {    // if we still need to promote the data to another level
                newNode.setUp(new SkipNode(newNode));    // add the new node as the up reference of the current node
            }
            currLevel++;
        }
        size++;
    }

    /**
     * Returns whether data is stored in the SkipList.
     *
     * Must be O(log n) in the best and average case and O(n) in the worst case.
     *
     * @param data the data being searched for
     * @return true is the parameter is in the SkipList or false otherwise
     */
    public boolean contains(int data) {
        if (size == 0) {
            return false;
        }
        
        SkipNode curr = head;
        while (curr != null) {
            while (curr.getRight() != null && curr.getRight().getData() <= data) {
                curr = curr.getRight();
            }
            if (curr.getData() == data) {
                return true;
            }
            curr = curr.getDown();
        }
        return false;
    }

    /**
     * Removes all occurrences of the passed in data in the SkipList.
     *
     * Must be O(log n) in the best and average cases, and O(n) in the worst case.
     *
     * @param data the data to remove
     * @return the value of the removed data
     * @throws NoSuchElementException is the data is not in the SkipList
     */
    public int remove(int data) {
        if (size == 0) {
            throw new NoSuchElementException("Cannot remove from an empty SkipList");
        }

        List<SkipNode> nodesToUpdate = new ArrayList<>();
        SkipNode curr = head;
        while (curr != null) {      // begin traversing the SkipList
            while (curr.getRight() != null && curr.getRight().getData() < data) {
                curr = curr.getRight();
            }
            if (curr.getRight().getData() == data) {
                nodesToUpdate.add(curr);   // nodes to update are the ones whose right reference is the data to remove
            }
            curr = curr.getDown();
        }

        if (nodesToUpdate.size() == 0) {
            throw new NoSuchElementException("Unable to find node with data " + data);
        }

        int removedData = nodesToUpdate.get(0).getRight().getData();
        for (int i = nodesToUpdate.size() - 1; i >= 0; i--) {   // for each node to update
            // reconfigure pointers to remove the node
            SkipNode nodeToUpdate = nodesToUpdate.get(i);
            SkipNode nodeToRemove = nodeToUpdate.getRight();
            nodeToUpdate.setRight(nodeToRemove.getRight());
            nodeToRemove.getRight().setLeft(nodeToUpdate);
            nodeToRemove.setDown(null);
            nodeToRemove.setUp(null);
        }
        size--;

        while (head.getRight() == tail && levels > 1) {     // delete extra levels if necessary
            removeTopLevel();
        }

        return removedData;
    }

    /**
     * Clears the SkipList.
     *
     * Removes all the data in the SkipList and sets size to 0 and levels to 1.
     *
     * Must be O(1).
     */
    public void clear() {
        head.setRight(tail);
        head.setDown(null);

        tail.setLeft(head);
        tail.setDown(null);

        size = 0;
        levels = 1;
    }

    /**
     * Returns the size of the SkipList.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     * @return the size of the SkipList
     */
    public int getSize() {
        // DO NOT MODIFY THIS METHOD
        return size;
    }

    /**
     * Returns the head of the SkipList.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     * @return the head of the SkipList
     */
    public SkipNode getHead() {
        // DO NOT MODIFY THIS METHOD
        return head;
    }

    /**
     * Returns the tail of the SkipList.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     * @return the tail of the SkipList
     */
    public SkipNode getTail() {
        // DO NOT MODIFY THIS METHOD
        return tail;
    }

    /**
     * Returns the number of levels in the SkipList.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     * @return the number of levels in the SkipList
     */
    public int getLevels() {
        // DO NOT MODIFY THIS METHOD
        return levels;
    }
}
