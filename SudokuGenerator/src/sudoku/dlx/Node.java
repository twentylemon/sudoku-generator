package sudoku.dlx;

/**
 * A quadruply linked Node for the DLX matrix. Each node also has a pointer
 * to the column header.
 *
 * @author Taras Mychaskiw
 */
class Node {

    Node left, right, up, down;
    ColumnHeader head;

    Node(){
    }

    Node(Node left, Node right, Node up, Node down, ColumnHeader head){
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
        this.head = head;
    }

    Node setLeft(Node left){
        this.left = left;
        return this;
    }

    Node setRight(Node right){
        this.right = right;
        return this;
    }

    Node setUp(Node up){
        this.up = up;
        return this;
    }

    Node setDown(Node down){
        this.down = down;
        return this;
    }

    Node setHead(ColumnHeader head){
        this.head = head;
        return this;
    }

    void coverLeft(){
        right.left = left;
        left.right = right;
    }
    void coverUp(){
        up.down = down;
        down.up = up;
    }

    void uncoverLeft(){
        right.left = this;
        left.right = this;
    }
    void uncoverUp(){
        up.down = this;
        down.up = this;
    }
}
