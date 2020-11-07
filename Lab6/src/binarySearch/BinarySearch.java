public class BinarySearch {
    public static int search (int[] arr, int element, int start, int end) {
        while (start <= end) {
            int middle = start + (end - start) / 2;
            if (arr[middle] == element) {
                return middle;
            } else if (arr[middle] > element) {
                end = middle - 1;
            } else if (arr[middle] < element) {
                start = middle + 1;
            }
        }
        return -1;
    }
}