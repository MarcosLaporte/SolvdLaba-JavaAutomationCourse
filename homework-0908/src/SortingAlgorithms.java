import java.util.Arrays;
import java.util.Random;

public class SortingAlgorithms {
  public static void main(String[] args) {
    final Random rand = new Random();
    int[] arr = new int[20];
    for (int i = 0; i < arr.length; i++) {
      arr[i] = rand.nextInt(-100, 100);
    }
    int[] sortedArrAsc = insertionSort(arr, true);
    int[] sortedArrDesc = insertionSort(arr, false);

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
  public static int[] insertionSort(int[] array, boolean asc) {
    final long start = System.nanoTime();
    int[] arrCopy = Arrays.copyOf(array, array.length);

    for (int i = 1; i < arrCopy.length; i++) {
      int currEl = arrCopy[i];
      int j = i - 1;

      while (j >= 0 && (currEl < arrCopy[j]) == asc) {
        arrCopy[j + 1] = arrCopy[j];
        j--;
      }
      arrCopy[j + 1] = currEl;
      System.out.println("Element sorted: " + currEl);
    }

    final long finish = System.nanoTime();
    System.out.printf("Time spent: %dms\n", (finish - start)/1000000);
    return arrCopy;
  }

  /**
   * In place, comparison-based sorting. Looks for the small/biggest element in a partition of the array, and sets it
   * in the right position. Iteration repeated until there's no element left unsorted. Not good for large arrays.
   *
   * @param array Array to be sorted.
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted array.
   */
  public static int[] selectionSort(int[] array, boolean asc) {
    int[] arrCopy = Arrays.copyOf(array, array.length);

    for (int i = 0; i < arrCopy.length; i++) {
      int minIndex = i;

      for (int j = i + 1; j < arrCopy.length; j++) {
        if ((arrCopy[j] < arrCopy[minIndex]) == asc) {
          minIndex = j;
        }
      }

      int aux = arrCopy[i];
      arrCopy[i] = arrCopy[minIndex];
      arrCopy[minIndex] = aux;
    }

    return arrCopy;
  }

  /**
   * Simple comparison-based sorting. Compares two adjacent elements and re-arranges them in the right order.
   *
   * @param array Array to be sorted.
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted Array.
   */
  public static int[] bubbleSort(int[] array, boolean asc) {
    int[] arrCopy = Arrays.copyOf(array, array.length);

    for (int i = 0; i < arrCopy.length; i++) {
      for (int j = 0; j < arrCopy.length - 1; j++) {
        if ((arrCopy[j] > arrCopy[j + 1]) == asc) {
          int aux = arrCopy[j];
          arrCopy[j] = arrCopy[j + 1];
          arrCopy[j + 1] = aux;
        }
      }
    }

    return arrCopy;
  }


  /**
   * Splits an array into two halves, each of which is recursively sorted,
   * and then merged back together in the specified order.
   *
   * @param array Array to be sorted.
   * @param asc   Whether the array should be sorted in ascending order or not.
   * @return Sorted Array.
   */
  public static int[] mergeSort(int[] array, boolean asc) {
    if (array.length < 2) return array;

    int midIndex = array.length / 2;
    int[] leftHalf = Arrays.copyOf(array, midIndex);
    int[] rightHalf = Arrays.copyOfRange(array, midIndex, array.length);

    leftHalf = mergeSort(leftHalf, asc);
    rightHalf = mergeSort(rightHalf, asc);

    return merge(leftHalf, rightHalf, asc);
  }

  /**
   * Merges two sorted halves of an array into a single sorted array.
   * The merging is done in either ascending or descending order based on the specified boolean flag.
   *
   * @param arrLeftHalf  Left half of the array.
   * @param arrRightHalf Right half of the array.
   * @param asc          Whether the array should be sorted in ascending order or not.
   * @return Merged and sorted Array.
   */
  private static int[] merge(int[] arrLeftHalf, int[] arrRightHalf, boolean asc) {
    int indexL = 0, indexR = 0, indexF = 0;
    int[] finalArray = new int[arrLeftHalf.length + arrRightHalf.length];

    while (indexL < arrLeftHalf.length && indexR < arrRightHalf.length) {
      if ((arrLeftHalf[indexL] <= arrRightHalf[indexR]) == asc) {
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

    return finalArray;
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
