public class SkipNode {
    private int data;
    private SkipNode right;
    private SkipNode left;
    private SkipNode up;
    private SkipNode down;

    /**
     * Constructs a new SkipNode with the given data and adjacent nodes.
     *
     * @param data  the data of the SkipNode
     * @param left  the left reference of the SkipNode
     * @param right the right reference of the SkipNode
     * @param up    the up reference of the SkipNode
     * @param down  the down reference of the SkipNode
     */
    public SkipNode(int data, SkipNode left, SkipNode right, SkipNode up, SkipNode down) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    /**
     * Constructor for adding SkipNodes to new levels.
     *
     * @param lowerLevelSkipNode the lower level SkipNode
     */
    public SkipNode(SkipNode lowerLevelSkipNode) {
        this(lowerLevelSkipNode.getData(), null, null, null, lowerLevelSkipNode);
    }

    /**
     * Constructs a SkipNode with the given data and no pointers.
     *
     * @param data the data of the SkipNode
     */
    public SkipNode(int data) {
        this(data, null, null, null, null);
    }

    /**
     * Gets the data of the SkipNode.
     *
     * @return the data of the SkipNode
     */
    public int getData() {
        return data;
    }

    /**
     * Sets the data of the SkipNode.
     *
     * @param data the data to store in the SkipNode
     */
    public void setData(int data) {
        this.data = data;
    }

    /**
     * Gets the reference above the SkipNode.
     *
     * @return the up reference of the SkipNode
     */
    public SkipNode getUp() {
        return up;
    }

    /**
     * Sets the above reference of the SkipNode.
     *
     * @param up the up reference to set
     */
    public void setUp(SkipNode up) {
        this.up = up;
    }

    /**
     * Gets the left reference of the SkipNode.
     *
     * @return the left reference of the SkipNode
     */
    public SkipNode getLeft() {
        return left;
    }

    /**
     * Sets the left reference of the SkipNode.
     *
     * @param left the left reference to set
     */
    public void setLeft(SkipNode left) {
        this.left = left;
    }

    /**
     * Gets the right reference of the SkipNode.
     *
     * @return the right reference of the SkipNode
     */
    public SkipNode getRight() {
        return right;
    }

    /**
     * Sets the right reference of the SkipNode.
     *
     * @param right the right reference to set
     */
    public void setRight(SkipNode right) {
        this.right = right;
    }

    /**
     * Gets the reference below the SkipNode.
     *
     * @return the down reference of the SkipNode.
     */
    public SkipNode getDown() {
        return down;
    }

    /**
     * Sets the below reference of the SkipNode.
     *
     * @param down the down reference to set
     */
    public void setDown(SkipNode down) {
        this.down = down;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SkipNode) {
            return this.data == ((SkipNode) other).getData()
                    && this.up == ((SkipNode) other).getUp()
                    && this.down == ((SkipNode) other).getDown()
                    && this.left == ((SkipNode) other).getLeft()
                    && this.right == ((SkipNode) other).getRight();
        }
        return false;
    }
}
