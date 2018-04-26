public class TransitionenListe {
    private Transition head;

    public TransitionenListe() {
        head = null;
    }

    public boolean isEmpty() {
        if(head != null) {
            return false;
        }
        return true;
    }

    public void insert(Transition t) {
        head = insert(t, head);
    }

    private Transition insert(Transition in, Transition temp) {
        if(temp == null) {
            return in;
        } else{
            temp.setNext(insert(in, temp.getNext()));
            return temp;
        }
    }

    public Transition search(short x, short y, short dir) {
        Transition temp = head;

        while(temp != null) {
            if(temp.getNumber(x, y, dir) != 0){
                return temp;
            } else {
                temp = temp.getNext();
            }
        }
        return  null;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Transition temp = head;
        while(temp != null) {
            sb.append(temp.toString());
            sb.append("\n");
            temp = temp.getNext();
        }
        return sb.toString();
    }
}
