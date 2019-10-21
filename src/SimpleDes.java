import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Scanner;

public class SimpleDes {

    //生成一个字符的二进制
    public static int[] toBit(byte a){
        int e;
        int[] k=new int[8];
        for (int i=0;i<8;i++){
            e=a>>i;
            k[i]=e&1;
        }
        return k;
    }

    //输出二进制序列
    public static void BitPrint(int[] B){
        for (int i=B.length-1;i>=0;i--){
            System.out.print(B[i]);   //先输出高位
        }
    }

    //置换函数
    public static int[] p8(int[] k){
        int[] p2={6,3,7,4,8,5,10,9};  //置换位置
        int[] key=new int[8];
        for (int i=0;i<8;i++){
            key[i]=k[p2[i]-1];
        }
        return key;
    }

    //循环左移以为函数
    public static void lsl(int[] m,int i,int j){
        int temp=m[j-1];
        for (int k=j-1;k>i-1;k--){
            m[k]=m[k-1];
            m[i-1]=temp;
        }
    }

    //p10函数
    public static int[] p10(int[] key){
        int[] p1={3,5,2,7,4,10,10,1,8,6};
        int[] temp=new int[10];
        for (int i=0;i<10;i++){
            temp[i]=key[p1[i]-1];
        }
        return temp;
    }

    //ip函数
    public static int[] ip(int[] mw){
        int[] ips={2,6,3,1,4,8,5,7};
        int[] bmw=new int[8];
        for (int i=0;i<8;i++){
            bmw[i]=mw[ips[i]-1];
        }
        return bmw;
    }

    //ip逆函数
    public static int[] ip_1(int[] mw){
        int[] ips={4,1,3,5,7,2,8,6};
        int[] bmw=new int[8];
        for (int i=0;i<8;i++){
            bmw[i]=mw[ips[i]-1];
        }
        return bmw;
    }

    //f函数
    public static int[] f(int[] R,int[] SK){
        int[][] s0={
                {1,0,3,2},
                {3,2,1,0},
                {0,2,1,3},
                {3,1,3,2}
        };
        int[][] s1={
                {0,1,2,3},
                {2,0,1,3},
                {3,0,1,0},
                {2,1,0,3}
        };
        int[] temp={4,1,2,3,2,3,4,1};
        int[] t={2,4,3,1};
        int[] EP=new int[8];
        int[] ts1=new int[4];
        int[] ts2=new int[4];
        int r,c;
        for (int i=0;i<8;i++){
            EP[i]=R[temp[i]-1];
        }
        for (int i=0;i<8;i++){
            EP[i]=EP[i]^SK[i];  //异或
        }
        r=EP[0]*2+EP[3];
        c=EP[1]*2+EP[2];
        ts1[0]=s0[c][r]%2;
        ts1[1]=s0[c][r]/2;
        r=EP[4]*2+EP[7];
        c=EP[5]*2+EP[6];
        ts1[2]=s0[c][r]%2;
        ts1[3]=s0[c][r]/2;

        for(int i=0;i<4;i++){
            ts2[i]=ts1[t[i]-1];
        }
        return ts2;
    }

    //fk函数
    public static int[] fk(int[] mw,int[] key){
        int[] bmw=new int[8];
        int[] L=new int[4];
        int[] R=new int[4];
        int[] LF=new int[4];
        bmw=mw;
        for (int i=0;i<4;i++){
            L[i]=mw[i];
        }
        for (int i=4;i<8;i++){
            R[i-4]=mw[i];
        }
        LF=f(R,key);
        for (int i=0;i<4;i++){
            L[i]=L[i]^LF[i];
        }
        for (int i=0;i<4;i++){
            bmw[i]=L[i];
        }
        return bmw;
    }

    //sw函数，交换左右四位
    public static void sw(int[] mw){
        int temp;
        for (int i=0;i<4;i++){
            temp=mw[i];
            mw[i]=mw[i+4];
            mw[i+4]=temp;
        }
    }

    //讲二进制转换为byte
    public static byte tobyte(int[] mw){

        int b = 0;
        for (int i = mw.length-1; i >=0; i--) {
            b = b << 1;
            b += mw[i];
        }

       // bytes = (byte)(( b >> 8 * 0) & 0xff);

        //System.out.println(b);
        return (byte) b;
    }

    //加密函数
    public static byte[] encrypt(byte[] MW,String Key){

        byte[] buffer1=MW;
        byte[] buffer2=Key.getBytes();
        byte[] buffer3=new byte[buffer1.length];
        byte c2,c3;

        int[] mw=new int[8];
        int[] bmw=new int[8];
        int[] sw1=new int[8];
        int[] sw2=new int[8];
        int[] sw3=new int[10];
        int[] key1=new int[8];
        int[] key2=new int[8];

        c2=buffer2[0];
        c3=buffer2[1];
        sw1=toBit(c2);
        sw2=toBit(c3);
        for (int i=0;i<10;i++){
            if (i<5){
                sw3[i]=sw1[i];
            }else {
                sw3[i]=sw2[i-5];
            }
        }
        sw3=p10(sw3);
        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        key1=p8(sw3);                //第一次的密钥

        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        key2=p8(sw3);               //第二次的密钥

        for (int i=0;i<buffer1.length;i++){
            mw=toBit(buffer1[i]);
            bmw=ip(mw);
            bmw=fk(bmw, key1);
            sw(bmw);
            bmw=fk(bmw, key2);
            bmw=ip_1(bmw);
            buffer3[i]=tobyte(bmw);
        }
        return buffer3;
    }

    //解密函数
    public static byte[] decrypt(byte[] MW,String Key){
        byte[] buffer1=MW;
        byte[] buffer2=Key.getBytes();
        byte[] buffer3=new byte[MW.length];

        byte c2,c3;
        int[] mw=new int[8];
        int[] sw1=new int[8];
        int[] sw2=new int[8];
        int[] sw3=new int[10];
        int[] key1=new int[8];
        int[] key2=new int[8];

        c2=buffer2[0];
        c3=buffer2[1];
        sw1=toBit(c2);
        sw2=toBit(c3);
        for (int i=0;i<10;i++){
            if (i<5){
                sw3[i]=sw1[i];
            }else {
                sw3[i]=sw2[i-5];
            }
        }
        sw3=p10(sw3);
        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        key1=p8(sw3);                //第一次的密钥

        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        key2=p8(sw3);               //第二次密钥

        for (int i=0;i<buffer1.length;i++){
            mw=toBit(buffer1[i]);
            mw=ip(mw);
            mw=fk(mw, key2);
            sw(mw);
            mw=fk(mw, key1);
            mw=ip_1(mw);
            buffer3[i]=tobyte(mw);
        }
        return buffer3;
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);

        String key="lj";

        byte[] buffer1=new byte[1024];
        byte[] buffer2=new byte[1024];
        byte[] buffer3=new byte[1024];

        try {
            FileInputStream input =new FileInputStream("C:/Users/神仙鱼/IdeaProjects/S-DES/src/test.txt");
            FileOutputStream output =new FileOutputStream("encrypt.txt");

            FileOutputStream outputT =new FileOutputStream("decrypt.txt");

            while (true){
                int len=input.read(buffer1);
                if (len==-1) break;
                    buffer2=encrypt(buffer1,key);
                    output.write(buffer2);
                    buffer3=decrypt(buffer2, key);
                    outputT.write(buffer3);

            }
            input.close();
            output.close();

        }catch (Exception e){
            e.printStackTrace();
        }








//        System.out.println("明文：");
//        String mw=scanner.nextLine();
//        System.out.println("密钥:");
//        String key=scanner.nextLine();
//        byte[] bmw;
//        bmw=encrypt(mw, key);
//
//        // System.out.printf(mw);
//        System.out.println("密文");
//        System.out.println(new String(bmw));
//
//        System.out.println("解密：");
//        mw=decrypt(bmw,key);
//
//        System.out.println("明文：");
//        System.out.println(mw);

/*
        byte[] buffer1=new byte[512];
        byte[] buffer2=new byte[512];
        byte c1,c2,c3;
        int count=0;
        int[] mw=new int[8];
        int[] bmw=new int[8];
        int[] sw1=new int[8];
        int[] sw2=new int[8];
        int[] sw3=new int[10];
        int[] key1=new int[8];
        int[] key2=new int[8];
        System.out.println("请输入明文："); //只能处理前8个
        try {
            count=System.in.read(buffer1);
        }catch (IOException e){
            e.printStackTrace();
        }
        if (count>8) count=8;
        System.out.println("明文二进制为：");
        for (int i=0;i<count;i++){
            c1=buffer1[i];
            mw=toBit(c1);
            BitPrint(mw);
            System.out.print(" ");
        }
        System.out.println();
        System.out.println("请输入密钥："); //前二个有效

        try{
            count=System.in.read(buffer2);
        }catch (Exception e){
            e.printStackTrace();
        }

        c2=buffer1[0];
        c3=buffer2[0];

        sw1=toBit(c2);
        sw2=toBit(c3);

        for (int i=0;i<10;i++){
            if (i<5){
                sw3[i]=sw1[i];
            }else {
                sw3[i]=sw2[i-5];
            }
        }

        System.out.println();
        System.out.println("密钥的二进制为：");
        BitPrint(sw3);
        System.out.println();
        sw3=p10(sw3);
        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        key1=p8(sw3);
        System.out.println("第一个密钥的二进制：");
        BitPrint(key1);
        System.out.println();
        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        lsl(sw3, 1, 5);
        lsl(sw3, 6, 10);
        key2=p8(sw3);
        System.out.println("产生第二个密钥二进制为：");
        BitPrint(key2);
        System.out.println();
        System.out.println("S-DES加密开始：");
        System.out.println("明  文  列 -->密  文  列-->明  文  列");

        count=buffer1.length;
        if (buffer1.length>8)
            count=8;

        for (int i=0;i<count;i++){
            mw=toBit(buffer1[i]);
            BitPrint(mw);
            System.out.print(" ");
            System.out.print("-->");
            bmw=ip(mw);
            bmw=fk(bmw, key1);
            sw(bmw);
            bmw=fk(bmw, key2);
            bmw=ip_1(bmw);
            BitPrint(bmw);
            System.out.print(" -->");
            mw=ip(bmw);
            mw=fk(mw, key2);
            sw(mw);
            mw=fk(mw, key1);
            mw=ip_1(mw);
            BitPrint(mw);
            System.out.println();
        }
    */
    }

}


