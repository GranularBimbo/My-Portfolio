package ratings;

import ratings.datastructures.BinaryTreeNode;
import ratings.datastructures.Comparator;
import ratings.datastructures.LinkedListNode;

public class Playlist {
    private Comparator<Song> comparator;
    private LinkedListNode<Song> head;
    private BinaryTreeNode<Song> root;

    public Playlist(Comparator<Song> comparator){
        this.comparator = comparator;
    }

    public void addSong(Song song){
        BinaryTreeNode<Song> node = new BinaryTreeNode<Song>(song, null, null);

        if(root == null)
            root = node;
        else
            addToBST(root, song);
    }

    public void addToBST(BinaryTreeNode<Song> node, Song value){
        BinaryTreeNode<Song> newNode = new BinaryTreeNode<Song>(value, null, null);

        if(comparator.compare(node.getValue(), value)){
            if(node.getRight() == null)
                node.setRight(newNode);
            else
                addToBST(node.getRight(), value);
        }
        else{
            if(node.getLeft() == null)
                node.setLeft(newNode);
            else
                addToBST(node.getLeft(), value);
        }
    }

    public BinaryTreeNode<Song> getSongTree(){
        return root;
    }

    public LinkedListNode<Song> getSongList(BinaryTreeNode<Song> node){
        traverseBST(node);
        return head;
    }

    public void traverseBST(BinaryTreeNode<Song> node){
        if(node != null){
            if(node.getLeft() != null){
                traverseBST(node.getLeft());
            }

            addSongToLL(node.getValue());

            if(node.getRight() != null){
                traverseBST(node.getRight());
            }
        }
    }

    public void addSongToLL(Song song){
        LinkedListNode<Song> node = new LinkedListNode<Song>(song, null);
        LinkedListNode<Song> current;

        if(head == null){
            head = node;
        }
        else{
            current = head;
            while(current != null){
                if(current.getNext() == null){
                    current.setNext(node);
                    break;
                }
                current = current.getNext();
            }
        }

    }

    public boolean inSongList(String title){
        boolean answer = false;
        LinkedListNode<Song> current;

        if(head != null){
            current = head;

            while(current != null){
                if(current.getValue().getTitle().equals(title))
                    answer = true;
                current = current.getNext();
            }
        }

        return answer;
    }

    public LinkedListNode<Song> getSongList(){
        return getSongList(root);
    }
}
