public class Transition {
    private String Array;

    public Transition(String array) {
        setArray(array);
    }

    private void setArray(String array) {
            Array = array;
    }

    public String getValue() {
        return Array;
    }

    public String toString() {
        String[] a = Array.split(" ");
        return "("+a[0]+","+a[1]+") "+a[2]+" "+a[3]+" ("+a[4]+","+a[5]+") "+a[6]+ "\n";
    }
}
