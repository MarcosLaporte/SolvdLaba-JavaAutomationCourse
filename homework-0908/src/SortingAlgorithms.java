import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Random;

public class SortingAlgorithms {
  public static void main(String[] args) {
    final Random rand = new Random();
    Integer[] arr = new Integer[20];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = rand.nextInt(-100, 100);
    }
    Integer[] sortedArrAsc = selectionSort(arr, true);
    Integer[] sortedArrDesc = selectionSort(arr, false);

    System.out.println("Original Array: " + Arrays.toString(arr));
    System.out.println("Sorted Asc Array: " + Arrays.toString(sortedArrAsc));
    System.out.println("Sorted Desc Array: " + Arrays.toString(sortedArrDesc));
  }

  /**
   * Insertion sort is a sorting algorithm that builds the final sorted array one item at a time.
   * It starts with the second element of the array, then compares the current element with the elements
   * on its left.
   *
   * @param array Array to be sorted.
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted array.
   */
  public static <T extends Comparable<? super T>> void insertionSort(T[] array, boolean asc) {
    for (int i = 1; i < array.length; i++) {
      T currEl = array[i];
      int j = i - 1;

      while (j >= 0 && (currEl.compareTo(array[j]) < 0) == asc) {
        array[j + 1] = array[j];
        j--;
      }
      array[j + 1] = currEl;
    }
  }

  /**
   * In place, comparison-based sorting. Looks for the small/biggest element in a partition of the array, and sets it
   * in the right position. Iteration repeated until there's no element left unsorted. Not good for large arrays.
   *
   * @param array Array to be sorted.
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted array.
   */
public static <T extends Comparable<? super T>> void selectionSort(T[] array, boolean asc) {
  for (int i = 0; i < array.length; i++) {
    int minIndex = i;

    for (int j = i + 1; j < array.length; j++) {
      if ((array[j].compareTo(array[minIndex]) < 0) == asc) {
        minIndex = j;
      }
    }

    T aux = array[i];
    array[i] = array[minIndex];
    array[minIndex] = aux;
  }
}

  /**
   * Simple comparison-based sorting. Compares two adjacent elements and re-arranges them in the right order.
   *
   * @param array Array to be sorted.
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted Array.
   */
  public static <T extends Comparable<? super T>> void bubbleSort(T[] array, boolean asc) {
    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array.length - 1; j++) {
        if ((array[j].compareTo(array[j + 1]) < 0) == asc) {
          T aux = array[j];
          array[j] = array[j + 1];
          array[j + 1] = aux;
        }
      }
    }
  }


  /**
   * Splits an array into two halves, each of which is recursively sorted,
   * and then merged back together in the specified order.
   *
   * @param array Array to be sorted.
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted Array.
   */
public static <T extends Comparable<? super T>> void mergeSort(T[] array, boolean asc) {
  if (array.length < 2) return;

  int midIndex = array.length / 2;
  T[] leftHalf = Arrays.copyOf(array, midIndex);
  T[] rightHalf = Arrays.copyOfRange(array, midIndex, array.length);

  mergeSort(leftHalf, asc);
  mergeSort(rightHalf, asc);

  merge(leftHalf, rightHalf, array, asc);
}

/**
 * Merges two sorted halves of an array into a single sorted array.
 * The merging is done in either ascending or descending order based on the specified boolean flag.
 *
 * @param arrLeftHalf  Left half of the array.
 * @param arrRightHalf Right half of the array.
 * @param finalArray   The original array to be filled with merged values.
 * @param asc          Whether the array should be sorted in ascending order or not.
 * @return Merged and sorted Array.
 */
private static <T extends Comparable<? super T>> void merge(T[] arrLeftHalf, T[] arrRightHalf, T[] finalArray, boolean asc) {
  int indexL = 0, indexR = 0, indexF = 0;

  while (indexL < arrLeftHalf.length && indexR < arrRightHalf.length) {
    if ((arrLeftHalf[indexL].compareTo(arrRightHalf[indexR]) <= 0) == asc) {
      finalArray[indexF] = arrLeftHalf[indexL];
      indexL++;
    } else {
      finalArray[indexF] = arrRightHalf[indexR];
      indexR++;
    }
    indexF++;
  }

  while (indexL < arrLeftHalf.length) {
    finalArray[indexF] = arrLeftHalf[indexL];
    indexL++;
    indexF++;
  }

  while (indexR < arrRightHalf.length) {
    finalArray[indexF] = arrRightHalf[indexR];
    indexR++;
    indexF++;
  }
}

  /**
   * Choose a pivot element from the array.
   * Partition the array into two sub-arrays: elements less than the pivot and elements greater than the pivot.
   * Recursively sort the sub-arrays.
   * Combine the sub-arrays and the pivot to produce the sorted array.
   *
   * @param array Array to be sorted.
   * @param lowIndex
   * @param highIndex
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted array.
   */
  /*public static int[] quickSort(int[] array, int lowIndex, int highIndex boolean asc) {
    int[] arrCopy = Arrays.copyOf(array, array.length);
    int pivot = arrCopy[highIndex];

    *//*
     * arr[i] < pivot && i == j then i++ j++
     * arr[i] < pivot then swap(arr[i], arr[j]) &  i++ j++
     * arr[i] > pivot then j++
     * j == HIGH then swap(arr[i], arr[j]) & i=0 j=0
     *//*
    for (int i = lowIndex; i < highIndex; i++) {
      int currEl = arrCopy[i];
      if (currEl < pivot)
    }

    return arrCopy;
  }*/

}
