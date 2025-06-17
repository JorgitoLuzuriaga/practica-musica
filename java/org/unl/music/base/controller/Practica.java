package org.unl.music.base.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

import org.unl.music.base.controller.data_struct.list.LinkedList;

public class Practica {
    private Integer[] matriz;
    private LinkedList<Integer> lista;

    public void cargar() {
        // TODO
        lista = new LinkedList<>();
        try {
            BufferedReader fb = new BufferedReader(new FileReader("data.txt"));
            String line = fb.readLine();
            while (line != null) {
                lista.add(Integer.parseInt(line));
                line = fb.readLine();
            }
            fb.close();
            // System.out.println(fb.readLine());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

// PRACTICA ORDER
    private void quickSort(Integer arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private int partition(Integer arr[], int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    public void q_order() {
        cargar();
        if (!lista.isEmpty()) {
            Integer arr[] = lista.toArray();
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            quickSort(arr, 0, arr.length - 1);
           // quickSort(arr, 0, (arr.length - 1));
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado quicksort " + endTime );
            lista.toList(arr);
        }        
    }

    public void s_order() {
        cargar();
        if (!lista.isEmpty()) {
            Integer arr[] = lista.toArray();
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            shell_sort(arr);
           // quickSort(arr, 0, (arr.length - 1));
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado shell " + endTime );
            lista.toList(arr);
        }        
    }

    public void shell_sort(Integer arrayToSort[]) {
        int n = arrayToSort.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int key = arrayToSort[i];
                int j = i;
                while (j >= gap && arrayToSort[j - gap] > key) {
                    arrayToSort[j] = arrayToSort[j - gap];
                    j -= gap;
                }
                arrayToSort[j] = key;
            }
        }
    }

    public static void main(String[] args) {
        Practica p = new Practica();

        System.out.println("Quicksort");
        p.q_order();
        System.out.println("Shellsort");
        p.s_order();
    }
}
