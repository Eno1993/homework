package com.example.homework;

import java.io.*;

public class Main{

    public static void main(String[] args) throws IOException{
        //System.setIn(new FileInputStream("C:\\homework\\src\\main\\java\\com\\example\\homework\\input.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] str = br.readLine().split(" ");
        int len  = Integer.parseInt(str[0]);
        int answerSize = Integer.parseInt(str[1]);

        int[][] arr = new int[len+1][len+1];
        for(int i=1; i<=len; i++){
            String[] temp = br.readLine().split(" ");
            for(int j=1; j<=len; j++){
                arr[i][j] = arr[i-1][j]+arr[i][j-1]-arr[i-1][j-1]+Integer.parseInt(temp[j-1]);
            }
        }

        for(int i=0; i<answerSize; i++){
            String[] p = br.readLine().split(" ");
            int x1 = Integer.parseInt(p[0]);
            int y1 = Integer.parseInt(p[1]);
            int x2 = Integer.parseInt(p[2]);
            int y2 = Integer.parseInt(p[3]);
            System.out.println(arr[x2][y2]-arr[x1-1][y2]-arr[x2][y1-1]+arr[x1-1][y1-1]);
        }
    }
}