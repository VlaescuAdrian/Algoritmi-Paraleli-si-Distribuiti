public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 3, 5, 6, 7, 9};
        System.out.println(BinarySearch.search(arr, 5, 0, arr.length - 1));
        System.out.println(BinarySearch.search(arr, 1, 0, arr.length - 1));
        System.out.println(BinarySearch.search(arr, 2, 0, arr.length - 1));
    }
}