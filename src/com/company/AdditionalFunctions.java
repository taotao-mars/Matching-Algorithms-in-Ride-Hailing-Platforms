package com.company;

import java.io.BufferedReader;
import java.io.*;
import java.util.*;

public class AdditionalFunctions {


    public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(byteOut);
    out.writeObject(src);
    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
    ObjectInputStream in = new ObjectInputStream(byteIn);
    @SuppressWarnings("unchecked")
    List<T> dest = (List<T>) in.readObject();
    return dest;
    }

    public static void Write_content(String fileName, String content) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(writer!= null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writeTxtFile(String filePath, String content) throws IOException {
        boolean flag = false;
        FileOutputStream fileOutputStream = null;
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
            flag = true;
        }
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(content.getBytes("UTF-8"));
            fileOutputStream.close();
            flag = true;
        } catch (Exception e) {
            System.out.println("文件写入失败！" + e);
        }
        return flag;
    }

    /**
     * sort the array and return the original index of sorted array
     * @param arr
     * @return
     */
    public static int[] sort_array(int[] arr) {
        int temp;
        int index;
        int k = arr.length;
        int[] Index = new int[k];
        for (int i = 0; i < k; i++) {
            Index[i] = i;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    index = Index[j];
                    Index[j] = Index[j + 1];
                    Index[j + 1] = index;
                }
            }
        }
        return Index;
    }
    public static int[] sort_array(double[] arr) {
        double temp;
        int index;
        int k = arr.length;
        int[] Index = new int[k];
        for (int i = 0; i < k; i++) {
            Index[i] = i;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    index = Index[j];
                    Index[j] = Index[j + 1];
                    Index[j + 1] = index;
                }
            }
        }
        return Index;
    }

    /**
     * return a random number in [0,999]
     * @return
     */
    public static int random_num(){
        int max=999;
        int min=0;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }


    /**
     * return a set that contains n random numbers from min to max
     * @return
     */
    public static Set<Integer> random_set(int min, int max, int n) {
        Set<Integer> set = new HashSet<Integer>();
        int[] array = new int[n];
        for (; true;) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            // 将不同的数存入HashSet中
            set.add(num);
            // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
            if (set.size() >= n) {
                break;
            }
        }
        return set;

    }
    /**
     * return an  n size set that contains numbers between [min,max)
     * @return
     */
    public static int[] randomArray(int min, int max, int n) {
        int[] array = new int[n];
        Set<Integer> set = random_set(min,max,n);
        int i = 0;
        for (int a : set) {
            array[i] = a;
            i++;
        }

        return array;
    }

    /**
     * return an  n size sorted List that contains numbers between [min,max),
     * @return
     */
    public static List<Integer> randomList(int min, int max, int n) {
        List<Integer> time_list = new ArrayList<Integer>();
        int[] array =  randomArray( min,  max,  n);
        for (Integer value : array) {
            time_list.add(value);
        }
        Collections.sort(time_list);
        return time_list;
    }

    /**
     * return an  n size sorted List that contains numbers between [min,max),
     * @return
     */

    public static List<Integer> randomList_can_repeat(int min, int max, int n) {
        List<Integer> time_list = new ArrayList<Integer>();
        int[] array =  new int[n];
        for(int i=0;i<n;i++){
            array[i]=(int)(Math.random()*(max-min+1))+min;
        }
        for (Integer value : array) {
            time_list.add(value);
        }
        Collections.sort(time_list);
        return time_list;
    }


}
